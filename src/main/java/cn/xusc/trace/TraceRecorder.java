/*
 * Copyright 20022 WangCai.
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

import cn.xusc.trace.constant.RecordLabel;
import cn.xusc.trace.constant.Temporary;
import cn.xusc.trace.enhance.InfoEnhancer;
import cn.xusc.trace.enhance.LineInfoEnhancer;
import cn.xusc.trace.enhance.ShortClassNameInfoEnhancer;
import cn.xusc.trace.enhance.StackInfoEnhancer;
import cn.xusc.trace.filter.InfoFilter;
import cn.xusc.trace.filter.RecordLabelInfoFilter;
import cn.xusc.trace.record.ConsoleInfoRecorder;
import cn.xusc.trace.record.InfoRecorder;
import cn.xusc.trace.util.FastList;
import cn.xusc.trace.util.Formats;

import java.util.List;
import java.util.Objects;

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
    private final List<InfoEnhancer> INFO_ENHANCERS = new FastList<>(InfoEnhancer.class);
    /**
     * 信息记录器链
     */
    private final List<InfoRecorder> INFO_RECORDERS = new FastList<>(InfoRecorder.class);
    
    /**
     * 记录标签
     */
    private RecordLabel LABEL;
    
    /**
     * 启用堆栈信息
     */
    private boolean enableStack;
    
    /**
     * 启用短类名
     */
    private boolean enableShortClassName;
    
    {
        /*
          初始化基础跟踪记录仪
         */
        INFO_FILTERS.add(new RecordLabelInfoFilter());
        INFO_ENHANCERS.add(new LineInfoEnhancer());
        INFO_ENHANCERS.add(new StackInfoEnhancer());
        INFO_ENHANCERS.add(new ShortClassNameInfoEnhancer());
        INFO_RECORDERS.add(new ConsoleInfoRecorder());
        
        enableStack = true;
    }
    
    /**
     * 添加信息过滤器
     *
     * @param filter 信息过滤器
     * @return 添加结果
     */
    public boolean addInfoFilter(InfoFilter filter) {
        return INFO_FILTERS.add(filter);
    }
    
    /**
     * 添加信息增强器
     *
     * @param enhancer 信息增强器
     * @return 添加结果
     */
    public boolean addInfoEnhancer(InfoEnhancer enhancer) {
        return INFO_ENHANCERS.add(enhancer);
    }
    
    /**
     * 添加信息记录器
     *
     * @param recorder 信息记录器
     * @return 添加结果
     */
    public boolean addInfoRecorder(InfoRecorder recorder) {
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
        return INFO_RECORDERS.remove(recorder);
    }
    
    /**
     * 记录所有
     *
     * <p>启用记录所有标签</p>
     *
     * @return 配置结果
     */
    @SuppressWarnings("SameReturnValue")
    public boolean recordALL() {
        LABEL = RecordLabel.ALL;
        return true;
    }
    
    /**
     * 隐藏所有
     *
     * <p>启用隐藏所有标签</p>
     *
     * @return 配置结果
     */
    @SuppressWarnings("SameReturnValue")
    public boolean hideALL() {
        LABEL = RecordLabel.HIDE;
        return true;
    }
    
    /**
     * 启用短类名
     *
     * <p>
     * {@link ShortClassNameInfoEnhancer}
     * </p>
     *
     * @return 配置结果
     */
    public boolean enableShortClassName() {
        return enableShortClassName = true;
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
    @SuppressWarnings("SameReturnValue")
    public boolean disableStackInfo() {
        enableStack = false;
        return true;
    }
    
    /**
     * 记录信息
     *
     * @param info 信息
     */
    public void log(String info) {
        log(info, Objects.requireNonNullElse(LABEL, RecordLabel.NOW));
    }
    
    /**
     * 记录格式信息
     *
     * @param info     格式信息
     * @param argArray 参数列表
     * @since 1.1
     */
    public void log(String info, Object... argArray) {
        log(info, Objects.requireNonNullElse(LABEL, RecordLabel.NOW), argArray);
    }
    
    /**
     * 不记录信息
     *
     * @param info 信息
     * @since 1.1
     */
    public void nolog(String info) {
        log(info, Objects.requireNonNullElse(LABEL, RecordLabel.HIDE));
    }
    
    /**
     * 不记录格式信息
     *
     * @param info     格式信息
     * @param argArray 参数列表
     * @since 1.1
     */
    public void nolog(String info, Object... argArray) {
        log(info, Objects.requireNonNullElse(LABEL, RecordLabel.HIDE), argArray);
    }
    
    /**
     * 记录信息
     *
     * <p>
     * 如果消息在增强器中被当null指针返回，则直接断链返回
     * </p>
     *
     * @param info  信息
     * @param label 记录标签
     */
    private void log(String info, RecordLabel label, Object... argArray) {
        boolean isRecord = true;
        /*
          过滤信息
         */
        for (InfoFilter infoFilter : INFO_FILTERS) {
            if (!infoFilter.isRecord(info, label)) {
                /*
                  此刻，在某个过滤器中过滤掉了
                 */
                isRecord = false;
                break;
            }
        }
        
        if (isRecord) {
            if (Objects.nonNull(argArray) && argArray.length > 0) {
                /*
                  格式化信息
                 */
                info = Formats.format(info, argArray);
            }
            EnhanceInfo enhanceInfo = new EnhanceInfo(info);
            enhanceInfo.setTemporaryValue(Temporary.ENABLE_STACK, enableStack);
            enhanceInfo.setTemporaryValue(Temporary.ENABLE_SHORT_CLASS_NAME, enableShortClassName);
            /*
              信息增强
             */
            for (InfoEnhancer infoEnhancer : INFO_ENHANCERS) {
                enhanceInfo = infoEnhancer.enhance(enhanceInfo);
                if (Objects.isNull(enhanceInfo)) return;
                enhanceInfo = infoEnhancer.setWriteInfo(enhanceInfo);
                if (Objects.isNull(enhanceInfo)) return;
            }
            
            String writeInfo = enhanceInfo.getWriteInfo();
            if (Objects.nonNull(writeInfo)) {
                /*
                  信息记录
                 */
                for (InfoRecorder infoRecorder : INFO_RECORDERS) {
                    infoRecorder.record(writeInfo);
                }
            }
            
        }
    }
    
}
