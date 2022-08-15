/*
 * Copyright 2022 WangCai.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.xusc.trace;

import cn.xusc.trace.config.TraceRecorderConfig;
import cn.xusc.trace.constant.RecordLabel;
import cn.xusc.trace.enhance.*;
import cn.xusc.trace.exception.TraceClosedException;
import cn.xusc.trace.exception.TraceException;
import cn.xusc.trace.exception.TraceTimeoutException;
import cn.xusc.trace.filter.InfoFilter;
import cn.xusc.trace.filter.RecordLabelInfoFilter;
import cn.xusc.trace.handle.AsyncTraceHandler;
import cn.xusc.trace.handle.SyncTraceHandler;
import cn.xusc.trace.handle.TraceHandler;
import cn.xusc.trace.record.ConsoleInfoRecorder;
import cn.xusc.trace.record.InfoRecorder;
import cn.xusc.trace.util.*;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 跟踪记录仪
 *
 * <p>
 * Examples:
 * </p>
 * <pre>
 *     TraceRecorder recorder = new TraceRecorder();
 *     recorder.log("msg");
 *     recorder.nolog("msg");
 * </pre>
 *
 * @author WangCai
 * @since 1.0
 */
public class TraceRecorder {

    /**
     * 信息过滤器链
     */
    private final List<InfoFilter> INFO_FILTERS = new FastList<>(InfoFilter.class);
    /**
     * 信息增强器链
     */
    private final List<InfoEnhancer> INFO_ENHANCERS = new FastList<>(InfoEnhancer.class, 8);
    /**
     * 信息记录器链
     */
    private final List<InfoRecorder> INFO_RECORDERS = new FastList<>(InfoRecorder.class);

    /**
     * 跟踪记录仪备忘录
     *
     * @since 2.2
     */
    private Memo memo;

    /**
     * 基础记忆标签
     *
     * @since 2.2
     */
    private String baseLabel;

    /**
     * 脏的信息增强器链标识
     *
     * @since 2.4
     */
    private boolean dirtyInfoEnhancers;

    /**
     * 跟踪处理器
     *
     * @since 2.0
     */
    private final TraceHandler TRACE_HANDLER;

    /**
     * 记录标签
     */
    private RecordLabel label;

    /**
     * 启用堆栈信息
     */
    private boolean enableStack;

    /**
     * 启用短类名
     */
    private boolean enableShortClassName;

    /**
     * 启用线程名
     *
     * @since 2.0
     */
    private boolean enableThreadName;

    /**
     * 关闭标识
     *
     * @since 2.2.1
     */
    private boolean closed;

    {
        /*
          初始化基础跟踪记录仪
         */
        INFO_FILTERS.add(new RecordLabelInfoFilter());
        INFO_ENHANCERS.add(new LineInfoEnhancer());
        INFO_ENHANCERS.add(new StackInfoEnhancer());
        INFO_ENHANCERS.add(new ShortClassNameInfoEnhancer());
        INFO_ENHANCERS.add(new ThreadInfoEnhancer());
        INFO_RECORDERS.add(new ConsoleInfoRecorder());

        enableStack = true;
        enableThreadName = true;
    }

    /**
     * 基础构造
     *
     * @since 2.0
     */
    public TraceRecorder() {
        TRACE_HANDLER = new SyncTraceHandler(this);
    }

    /**
     * 异步标识构造
     *
     * @param enableAsync 异步标识
     * @since 2.0
     */
    public TraceRecorder(boolean enableAsync) {
        TRACE_HANDLER = enableAsync ? new AsyncTraceHandler(this) : new SyncTraceHandler(this);
    }

    /**
     * 异步处理器的跟踪记录仪构造
     *
     * @param taskHandlerSize 任务处理器数量
     * @throws TraceException if {@code taskHandlerSize} is less 1
     * @since 2.0
     */
    public TraceRecorder(int taskHandlerSize) {
        if (taskHandlerSize < 1) {
            throw new TraceException("taskHandlerSize < 1");
        }
        TRACE_HANDLER = new AsyncTraceHandler(this, taskHandlerSize);
    }

    /**
     * 配置构造
     *
     * @param config 配置
     * @since 2.0
     */
    public TraceRecorder(TraceRecorderConfig config) {
        if (!config.getInfoFilters().isEmpty()) {
            for (InfoFilter infoFilter : config.getInfoFilters()) {
                addInfoFilter(infoFilter);
            }
        }
        if (!config.getInfoEnhancers().isEmpty()) {
            for (InfoEnhancer infoEnhancer : config.getInfoEnhancers()) {
                addInfoEnhancer(infoEnhancer);
            }
        }
        if (!config.getInfoRecorders().isEmpty()) {
            for (InfoRecorder infoRecorder : config.getInfoRecorders()) {
                addInfoRecorder(infoRecorder);
            }
        }
        /*
          堆栈信息默认启用
          短类名默认禁用
          线程名默认启用
          记录所有默认禁用
         */
        if (!config.isEnableStack()) {
            disableStackInfo();
        }
        if (config.isEnableShortClassName()) {
            enableShortClassName();
        }
        if (!config.isEnableThreadName()) {
            disableThreadName();
        }
        if (config.isEnableRecordAll()) {
            recordAll();
        }
        /*
          异步
         */
        if (config.isEnableAsync()) {
            TRACE_HANDLER = new AsyncTraceHandler(this, config.getTaskHandlerSize());
            return;
        }
        TRACE_HANDLER = new SyncTraceHandler(this);
    }

    /**
     * 添加信息过滤器
     *
     * @param filter 信息过滤器
     * @return 添加结果
     */
    public boolean addInfoFilter(InfoFilter filter) {
        verifyClosed();
        memoryPoint();
        return INFO_FILTERS.add(filter);
    }

    /**
     * 添加信息增强器
     *
     * <p>
     * 通过脏的信息增强器链标识确保用户永远增强的是原信息
     * </p>
     *
     * @param enhancer 信息增强器
     * @return 添加结果
     */
    public boolean addInfoEnhancer(InfoEnhancer enhancer) {
        verifyClosed();
        memoryPoint();
        /*
          信息增强器变脏分两个阶段：
          - 第一阶段（首次变脏）：
               - 直接清空整个增强器链
          - 第二阶段（交替性变脏）
               - 清空尾部内设数量的增强器
         */
        if (!dirtyInfoEnhancers) {
            List<?> list = (List<?>) memo.read(baseLabel);
            list = Lists.statistic(list, component -> component instanceof InfoEnhancer);
            int size = list.size();
            if (size == INFO_ENHANCERS.size()) {
                INFO_ENHANCERS.clear();
            } else {
                for (int i = 0; i < size; i++) {
                    ((FastList) INFO_ENHANCERS).removeLast();
                }
            }
            dirtyInfoEnhancers = true;
        }
        return INFO_ENHANCERS.add(enhancer);
    }

    /**
     * 添加信息记录器
     *
     * @param recorder 信息记录器
     * @return 添加结果
     */
    public boolean addInfoRecorder(InfoRecorder recorder) {
        verifyClosed();
        memoryPoint();
        return INFO_RECORDERS.add(recorder);
    }

    /**
     * 移除信息过滤器
     *
     * @param filter 信息过滤器
     * @return 移除结果
     * @since 1.2.1
     */
    public boolean removeInfoFilter(InfoFilter filter) {
        verifyClosed();
        return INFO_FILTERS.remove(filter);
    }

    /**
     * 移除信息增强器
     *
     * @param enhancer 信息增强器
     * @return 移除结果
     * @since 1.2.1
     */
    public boolean removeInfoEnhancer(InfoEnhancer enhancer) {
        verifyClosed();
        return INFO_ENHANCERS.remove(enhancer);
    }

    /**
     * 移除信息记录器
     *
     * @param recorder 信息记录器
     * @return 移除结果
     * @since 1.2.1
     */
    public boolean removeInfoRecorder(InfoRecorder recorder) {
        verifyClosed();
        return INFO_RECORDERS.remove(recorder);
    }

    /**
     * 获取信息过滤器集
     *
     * @return 信息过滤器集
     * @since 2.0
     */
    public List<InfoFilter> getInfoFilters() {
        verifyClosed();
        return INFO_FILTERS;
    }

    /**
     * 获取信息增强器集
     *
     * @return 信息增强器集
     * @since 2.0
     */
    public List<InfoEnhancer> getInfoEnhancers() {
        verifyClosed();
        if (dirtyInfoEnhancers) {
            /*
              脏恢复
             */
            List<?> list = (List<?>) memo.read(baseLabel);
            Lists
                .statistic(list, component -> component instanceof InfoEnhancer)
                .forEach(component -> INFO_ENHANCERS.add((InfoEnhancer) component));
            dirtyInfoEnhancers = false;
        }
        return INFO_ENHANCERS;
    }

    /**
     * 获取信息记录器集
     *
     * @return 信息记录器集
     * @since 2.0
     */
    public List<InfoRecorder> getInfoRecorders() {
        verifyClosed();
        return INFO_RECORDERS;
    }

    /**
     * 记忆点
     *
     * @since 2.2
     */
    private void memoryPoint() {
        if (Objects.nonNull(baseLabel)) {
            return;
        }
        if (Objects.isNull(memo)) {
            memo = new Memo();
        }
        baseLabel = memo.storage(Lists.merge(getInfoFilters(), getInfoEnhancers(), getInfoRecorders()));
    }

    /**
     * 重置特殊的结构
     *
     * <p>
     * 特殊的结构 - {@link #memoryPoint()}记忆点数据
     * </p>
     *
     * @return 重置后详情
     * @since 2.2
     */
    public boolean resetSpecial() {
        verifyClosed();
        if (Objects.isNull(baseLabel)) {
            return true;
        }
        List<?> list = (List<?>) memo.read(baseLabel);
        INFO_FILTERS.clear();
        INFO_ENHANCERS.clear();
        INFO_RECORDERS.clear();
        for (Object component : list) {
            if (component instanceof InfoFilter) {
                INFO_FILTERS.add((InfoFilter) component);
            } else if (component instanceof InfoEnhancer) {
                INFO_ENHANCERS.add((InfoEnhancer) component);
            } else {
                INFO_RECORDERS.add((InfoRecorder) component);
            }
        }
        return true;
    }

    /**
     * 记录所有
     *
     * <p>启用记录所有标签</p>
     *
     * @return 配置结果
     */
    public boolean recordAll() {
        verifyClosed();
        label = RecordLabel.ALL;
        return true;
    }

    /**
     * 隐藏所有
     *
     * <p>启用隐藏所有标签</p>
     *
     * @return 配置结果
     */
    public boolean hideAll() {
        verifyClosed();
        label = RecordLabel.HIDE;
        return true;
    }

    /**
     * 启用短类名
     *
     * <p>
     * {@link ShortClassNameInfoEnhancer}
     * <p>
     * {@link  #enableShortClassName} = true
     * </p>
     *
     * @return 配置结果
     */
    public boolean enableShortClassName() {
        verifyClosed();
        return enableShortClassName = true;
    }

    /**
     * 禁用短类名
     *
     * <p>
     * {@link ShortClassNameInfoEnhancer}
     * </p>
     * <p>
     * {@link  #enableShortClassName} = false
     * </p>
     *
     * @return 配置结果
     * @since 2.0
     */
    public boolean disableShortClassName() {
        verifyClosed();
        enableShortClassName = false;
        return true;
    }

    /**
     * 获取启用短类名详情
     *
     * @return 短类名详情
     * @since 2.0
     */
    public boolean isEnableShortClassName() {
        verifyClosed();
        return enableShortClassName;
    }

    /**
     * 启用线程名
     *
     * <p>
     * {@link ThreadInfoEnhancer}
     * <p>
     * {@link  #enableThreadName} = true
     * </p>
     *
     * @return 配置结果
     * @since 2.0
     */
    public boolean enableThreadName() {
        verifyClosed();
        return enableThreadName = true;
    }

    /**
     * 禁用线程名
     *
     * <p>
     * {@link ThreadInfoEnhancer}
     * </p>
     * <p>
     * {@link  #enableThreadName} = false
     * </p>
     *
     * @return 配置结果
     * @since 2.0
     */
    public boolean disableThreadName() {
        verifyClosed();
        enableThreadName = false;
        return true;
    }

    /**
     * 获取启用线程名详情
     *
     * @return 线程名详情
     * @since 2.0
     */
    public boolean isEnableThreadName() {
        verifyClosed();
        return enableThreadName;
    }

    /**
     * 启用堆栈信息增强
     * <p>
     * 影响{@link StackInfoEnhancer} and {@link ShortClassNameInfoEnhancer}
     * <p>
     * {@link  #enableStack} = true
     * </p>
     *
     * @return 配置结果
     */
    public boolean enableStackInfo() {
        verifyClosed();
        return enableStack = true;
    }

    /**
     * 禁用堆栈信息增强
     * <p>
     * 影响{@link StackInfoEnhancer} and {@link ShortClassNameInfoEnhancer}
     * <p>
     * {@link  #enableStack} = false
     * </p>
     *
     * @return 配置结果
     */
    public boolean disableStackInfo() {
        verifyClosed();
        enableStack = false;
        return true;
    }

    /**
     * 获取启用堆栈信息增强详情
     *
     * @return 堆栈信息增强详情
     * @since 2.0
     */
    public boolean isEnableStackInfo() {
        verifyClosed();
        return enableStack;
    }

    /**
     * 记录信息
     *
     * @param info 信息
     */
    public void log(String info) {
        verifyClosed();
        log(info, Objects.requireNonNullElse(label, RecordLabel.NOW));
    }

    /**
     * 记录格式信息
     *
     * <p>
     * V2.5或者以后可通过参数列表末尾参数进行推断，来达到nolog记录。
     * </p>
     *
     * @param info     格式信息
     * @param argArray 参数列表
     * @since 1.1
     */
    public void log(String info, Object... argArray) {
        verifyClosed();
        if (Objects.equals(RecordLabel.HIDE, deduceRecordLabel(info, argArray))) {
            nolog(info, argArray);
            return;
        }
        log(info, Objects.requireNonNullElse(label, deduceRecordLabel(info, argArray)), argArray);
    }

    /**
     * 不记录信息
     *
     * <p>
     * 推荐使用{@code #log(String, false)}，因为那样更方便
     * </p>
     *
     * @param info 信息
     * @since 1.1
     */
    public void nolog(String info) {
        verifyClosed();
        log(info, Objects.requireNonNullElse(label, RecordLabel.HIDE));
    }

    /**
     * 不记录格式信息
     *
     * <p>
     * 推荐使用{@code #log(String, Object..., false)}，因为那样更方便
     * </p>
     *
     * @param info     格式信息
     * @param argArray 参数列表
     * @since 1.1
     */
    public void nolog(String info, Object... argArray) {
        verifyClosed();
        log(info, Objects.requireNonNullElse(label, RecordLabel.HIDE), argArray);
    }

    /**
     * 记录信息
     *
     * <p>
     * 将信息记录交给跟踪处理器进行处理
     * </p>
     *
     * @param info     信息
     * @param label    记录标签
     * @param argArray 参数列表
     */
    private void log(String info, RecordLabel label, Object... argArray) {
        TRACE_HANDLER.handle(info, label, argArray);
    }

    /**
     * 推断记录标签
     *
     * @param info 格式信息
     * @param argArray 参数列表
     * @return 记录标签
     * @since 2.5
     */
    private RecordLabel deduceRecordLabel(String info, Object... argArray) {
        if (Objects.isNull(argArray) || Objects.equals(0, argArray.length)) {
            return RecordLabel.NOW;
        }

        if (Formats.isMoreArgs(info, argArray)) {
            String maybeBoolArgStr = Objects.toString(argArray[argArray.length - 1]);
            if (Strings.equalsIgnoreCase("false", maybeBoolArgStr)) {
                return RecordLabel.HIDE;
            }
        }

        return RecordLabel.NOW;
    }

    /**
     * 处理器关闭
     *
     * @since 2.1
     */
    public void shutdown() {
        verifyClosed();
        closed = true;
        TRACE_HANDLER.shutdown();
    }

    /**
     * 处理器关闭，会处理完指定时间未完成的任务
     *
     * @param timeout  时间
     * @param timeUnit 时间单位
     * @throws TraceTimeoutException if a timeout occurs before shutdown completes.
     * @since 2.1
     */
    public void shutdown(long timeout, TimeUnit timeUnit) throws TraceTimeoutException {
        verifyClosed();
        closed = true;
        TRACE_HANDLER.shutdown(timeout, timeUnit);
    }

    /**
     * 验证跟踪记录仪是否已关闭
     *
     * @throws TraceClosedException if {@code closed} is true
     * @since 2.2.1
     */
    private void verifyClosed() {
        if (closed) {
            throw new TraceClosedException("TraceRecorder is closed");
        }
    }

    /**
     * 跟踪记录仪详情
     *
     * @return 详情
     */
    @Override
    public String toString() {
        return (
            "TraceRecorder{" +
            "INFO_FILTERS=" +
            Lists.classNames(INFO_FILTERS) +
            ", INFO_ENHANCERS=" +
            Lists.classNames(INFO_ENHANCERS) +
            ", INFO_RECORDERS=" +
            Lists.classNames(INFO_RECORDERS) +
            ", TRACE_HANDLER=" +
            TRACE_HANDLER.getClass().getName() +
            ", LABEL=" +
            label +
            ", enableStack=" +
            enableStack +
            ", enableShortClassName=" +
            enableShortClassName +
            ", enableThreadName=" +
            enableThreadName +
            '}'
        );
    }
}
