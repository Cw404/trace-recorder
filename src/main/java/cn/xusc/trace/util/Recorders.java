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
package cn.xusc.trace.util;

import cn.xusc.trace.EnhanceInfo;
import cn.xusc.trace.TraceRecorder;
import cn.xusc.trace.annotation.CloseOrder;
import cn.xusc.trace.config.TraceRecorderConfig;
import cn.xusc.trace.enhance.InfoEnhancer;
import cn.xusc.trace.enhance.ShortClassNameInfoEnhancer;
import cn.xusc.trace.enhance.StatisticsInfoEnhancer;
import cn.xusc.trace.enhance.ThreadInfoEnhancer;
import cn.xusc.trace.exception.TraceException;
import cn.xusc.trace.exception.TraceTimeoutException;
import cn.xusc.trace.filter.InfoFilter;
import cn.xusc.trace.record.FileInfoRecorder;
import cn.xusc.trace.record.InfoRecorder;
import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 记录器工具类
 *
 * <p>
 * 简化跟踪记录器的使用
 * </p>
 *
 * @author WangCai
 * @since 1.0
 * @deprecated 由于该类只是为了兼容1.0的方便使用，请使用更灵活的方式{@code TraceRecorder} + {@code TraceRecorderConfig}
 */
@Deprecated
public final class Recorders {

    /**
     * 禁止实例化
     */
    private Recorders() {}

    /**
     * 跟踪记录仪（全局）
     */
    private static TraceRecorder traceRecorder = new TraceRecorder();

    /**
     * 异步标识
     *
     * @since 2.0
     */
    private static boolean asyncIdentify;

    /**
     * 启用同步记录
     *
     * @return 启用情况
     * @since 2.0
     */
    @SuppressWarnings({ "SameReturnValue", "unused" })
    public static boolean enableSync() {
        if (Objects.equals(asyncIdentify, true)) {
            traceRecorder = new TraceRecorder();
            asyncIdentify = false;
        }
        return true;
    }

    /**
     * 启用异步记录
     *
     * @return 启用情况
     * @since 2.0
     */
    @SuppressWarnings({ "UnusedReturnValue", "SameReturnValue" })
    public static boolean enableAsync() {
        if (Objects.equals(asyncIdentify, false)) {
            traceRecorder = new TraceRecorder(true);
            asyncIdentify = true;
        }
        return true;
    }

    /**
     * 配置构建记录仪
     *
     * @param config 配置
     * @since 2.0
     */
    public static void config(TraceRecorderConfig config) {
        traceRecorder = new TraceRecorder(config);
    }

    /**
     * 添加信息过滤器
     *
     * @param filter 信息过滤器
     * @return 添加结果
     */
    public static boolean addInfoFilter(InfoFilter filter) {
        return traceRecorder.addInfoFilter(filter);
    }

    /**
     * 添加信息增强器
     *
     * @param enhancer 信息增强器
     * @return 添加结果
     */
    public static boolean addInfoEnhancer(InfoEnhancer enhancer) {
        return traceRecorder.addInfoEnhancer(enhancer);
    }

    /**
     * 添加信息记录器
     *
     * @param recorder 信息记录器
     * @return 添加结果
     */
    public static boolean addInfoRecorder(InfoRecorder recorder) {
        return traceRecorder.addInfoRecorder(recorder);
    }

    /**
     * 移除信息过滤器
     *
     * @param filter 信息过滤器
     * @return 移除结果
     * @since 1.2.1
     */
    @SuppressWarnings("unused")
    public static boolean removeInfoFilter(InfoFilter filter) {
        return traceRecorder.removeInfoFilter(filter);
    }

    /**
     * 移除信息增强器
     *
     * @param enhancer 信息增强器
     * @return 移除结果
     * @since 1.2.1
     */
    @SuppressWarnings("unused")
    public static boolean removeInfoEnhancer(InfoEnhancer enhancer) {
        return traceRecorder.removeInfoEnhancer(enhancer);
    }

    /**
     * 移除信息记录器
     *
     * @param recorder 信息记录器
     * @return 移除结果
     * @since 1.2.1
     */
    @SuppressWarnings("unused")
    public static boolean removeInfoRecorder(InfoRecorder recorder) {
        return traceRecorder.removeInfoRecorder(recorder);
    }

    /**
     * 获取信息过滤器集
     *
     * @return 信息过滤器集
     * @since 2.0
     */
    public static List<InfoFilter> getInfoFilters() {
        return traceRecorder.getInfoFilters();
    }

    /**
     * 获取信息增强器集
     *
     * @return 信息增强器集
     * @since 2.0
     */
    public static List<InfoEnhancer> getInfoEnhancers() {
        return traceRecorder.getInfoEnhancers();
    }

    /**
     * 获取信息记录器集
     *
     * @return 信息记录器集
     * @since 2.0
     */
    public static List<InfoRecorder> getInfoRecorders() {
        return traceRecorder.getInfoRecorders();
    }

    /**
     * 重置特殊的结构
     *
     * @return 重置后详情
     * @since 2.2
     */
    public static boolean resetSpecial() {
        return traceRecorder.resetSpecial();
    }

    /**
     * 记录所有
     *
     * <p>启用记录所有标签</p>
     *
     * @return 配置结果
     */
    public static boolean recordALL() {
        return traceRecorder.recordALL();
    }

    /**
     * 隐藏所有
     *
     * <p>启用隐藏所有标签</p>
     *
     * @return 配置结果
     */
    public static boolean hideALL() {
        return traceRecorder.hideALL();
    }

    /**
     * 启用短类名
     *
     * <p>
     * {@link cn.xusc.trace.enhance.ShortClassNameInfoEnhancer}
     * </p>
     *
     * @return 配置结果
     */
    public static boolean enableShortClassName() {
        return traceRecorder.enableShortClassName();
    }

    /**
     * 禁用短类名
     *
     * <p>
     * {@link ShortClassNameInfoEnhancer}
     * </p>
     *
     * @return 配置结果
     * @since 2.0
     */
    public static boolean disableShortClassName() {
        return traceRecorder.disableShortClassName();
    }

    /**
     * 获取启用短类名详情
     *
     * @return 短类名详情
     * @since 2.0
     */
    public static boolean isEnableShortClassName() {
        return traceRecorder.isEnableShortClassName();
    }

    /**
     * 启用线程名
     *
     * <p>
     * {@link ThreadInfoEnhancer}
     * </p>
     *
     * @return 配置结果
     * @since 2.0
     */
    public static boolean enableThreadName() {
        return traceRecorder.enableThreadName();
    }

    /**
     * 禁用线程名
     *
     * <p>
     * {@link ThreadInfoEnhancer}
     * </p>
     *
     * @return 配置结果
     * @since 2.0
     */
    public static boolean disableThreadName() {
        return traceRecorder.disableThreadName();
    }

    /**
     * 获取启用线程名详情
     *
     * @return 线程名详情
     * @since 2.0
     */
    public static boolean isEnableThreadName() {
        return traceRecorder.isEnableThreadName();
    }

    /**
     * 启用堆栈
     *
     * @return 配置结果
     */
    public static boolean enableStackInfo() {
        return traceRecorder.enableStackInfo();
    }

    /**
     * 禁用堆栈信息
     *
     * @return 配置结果
     */
    public static boolean disableStackInfo() {
        return traceRecorder.disableStackInfo();
    }

    /**
     * 获取启用堆栈信息增强详情
     *
     * @return 堆栈信息增强详情
     * @since 2.0
     */
    public static boolean isEnableStackInfo() {
        return traceRecorder.isEnableStackInfo();
    }

    /**
     * 记录信息
     *
     * @param info 信息
     */
    public static void log(String info) {
        traceRecorder.log(info);
    }

    /**
     * 记录格式信息
     *
     * @param info     格式信息
     * @param argArray 参数列表
     * @since 1.1
     */
    @SuppressWarnings("unused")
    public static void log(String info, Object... argArray) {
        traceRecorder.log(info, argArray);
    }

    /**
     * 不记录信息
     *
     * @param info 信息
     * @since 1.1
     */
    public static void nolog(String info) {
        traceRecorder.nolog(info);
    }

    /**
     * 不记录格式信息
     *
     * @param info     格式信息
     * @param argArray 参数列表
     * @since 1.1
     */
    @SuppressWarnings("unused")
    public static void nolog(String info, Object... argArray) {
        traceRecorder.nolog(info, argArray);
    }

    /**
     * 处理器关闭
     *
     * @since 2.1
     */
    @SuppressWarnings("unused")
    public static void shutdown() {
        traceRecorder.shutdown();
    }

    /**
     * 处理器关闭，会处理完指定时间未完成的任务
     *
     * @param timeout  时间
     * @param timeUnit 时间单位
     * @throws TraceTimeoutException if a timeout occurs before shutdown completes.
     * @since 2.1
     */
    @SuppressWarnings("unused")
    public static void shutdown(long timeout, TimeUnit timeUnit) throws TraceTimeoutException {
        traceRecorder.shutdown(timeout, timeUnit);
    }

    /*
      =========================== extension ===========================
     */

    /**
     * 添加文件信息记录器
     *
     * @param fileName 记录文件名
     * @return 添加结果
     */
    @SuppressWarnings("unused")
    public static boolean addFileInfoRecorder(String fileName) {
        Objects.requireNonNull(fileName);
        return addFileInfoRecorder(new File(fileName));
    }

    /**
     * 添加文件信息记录器
     *
     * @param file 记录文件
     *             仅文件地址，需要具备可写权限，目标文件不存在会自动创建
     * @return 添加结果
     */
    public static boolean addFileInfoRecorder(File file) {
        Objects.requireNonNull(file);
        if (file.isDirectory()) {
            throw new TraceException("file can't directory");
        }
        return traceRecorder.addInfoRecorder(new FileInfoRecorder(file));
    }

    /**
     * 添加通用统计信息增强器
     *
     * @return 添加结果
     */
    public static boolean addCommonStatisticsInfoEnhancer() {
        return traceRecorder.addInfoEnhancer(new CommonStatisticsInfoEnhancer(traceRecorder));
    }

    /**
     * 通用统计信息增强器
     */
    @CloseOrder(1)
    private static class CommonStatisticsInfoEnhancer extends StatisticsInfoEnhancer {

        private TraceNode HEAD;

        public CommonStatisticsInfoEnhancer(TraceRecorder recorder) {
            super(recorder);
        }

        @Override
        protected EnhanceInfo doEnhance(EnhanceInfo eInfo) {
            TraceNode head = HEAD;
            if (Objects.isNull(head)) {
                /*
                  first , initialize
                 */
                HEAD = new TraceNode();
                HEAD.setClassName(eInfo.getClassName());
                HEAD.setCount(1);
                return eInfo;
            }

            /*
              increment count
             */
            do {
                if (Objects.equals(head.getClassName(), eInfo.getClassName())) {
                    head.increment();
                    break;
                }
            } while (Objects.nonNull(head = head.getNext()));
            return eInfo;
        }

        @Override
        protected String showInfo() {
            TraceNode head = HEAD;
            if (Objects.isNull(head)) {
                /*
                  没有记录任何一条显示记录，head没有生成
                 */
                return Strings.empty();
            }

            StringBuilder sb = new StringBuilder();
            String lineSeparator = Symbols.lineSeparator();
            sb.append("--------------------").append(lineSeparator);
            do {
                sb.append(String.format("%s - %d", head.getClassName(), head.getCount())).append(lineSeparator);
            } while (Objects.nonNull(head = head.getNext()));

            sb.append("--------------------").append(lineSeparator);
            return sb.toString();
        }

        /**
         * 跟踪锚点节点
         *
         * <p>类锚点总数跟踪</p>
         */
        @SuppressWarnings("InnerClassMayBeStatic")
        private class TraceNode {

            private String className;
            private long count;

            @SuppressWarnings("unused")
            private TraceNode next;

            public TraceNode() {}

            public String getClassName() {
                return className;
            }

            public void setClassName(String className) {
                this.className = className;
            }

            public long getCount() {
                return count;
            }

            public void setCount(long count) {
                this.count = count;
            }

            public void increment() {
                this.count++;
            }

            public TraceNode getNext() {
                return next;
            }

            @SuppressWarnings("unused")
            public boolean hasNext() {
                return next != null;
            }
        }
    }
}
