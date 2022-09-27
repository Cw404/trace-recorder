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
package cn.xusc.trace.core.config;

import cn.xusc.trace.common.exception.TraceException;
import cn.xusc.trace.common.util.Lists;
import cn.xusc.trace.core.enhance.InfoEnhancer;
import cn.xusc.trace.core.filter.InfoFilter;
import cn.xusc.trace.core.record.InfoRecorder;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * 跟踪记录仪配置
 *
 * <p>
 * 通过lombok组件{@link Setter}、{@link Getter}、{@link Builder}简化源代码可读性
 * </p>
 *
 * @author WangCai
 * @since 2.0
 */
@Setter
@Getter
@Builder
public class TraceRecorderConfig {

    /**
     * 信息过滤器链
     */
    private List<InfoFilter> infoFilters;
    /**
     * 信息增强器链
     */
    private List<InfoEnhancer> infoEnhancers;
    /**
     * 信息记录器链
     */
    private List<InfoRecorder> infoRecorders;

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
     */
    private boolean enableThreadName;

    /**
     * 启用记录所有
     *
     * <p>
     * 默认不启用，表示走对应记录规则
     * </p>
     */
    private boolean enableRecordAll;

    /**
     * 启用异步记录
     *
     * <p>默认不启用</p>
     *
     * @see <a href="#cn.xusc.trace.handle.SyncTraceHandler">SyncTraceHandler</a>、<a href="#cn.xusc.trace.handle.AsyncTraceHandler">AsyncTraceHandler</a>
     */
    private boolean enableAsync;

    /**
     * 任务处理器数量
     *
     * <p>默认为1个处理器，由构造补偿</p>
     *
     * @see <a href="#cn.xusc.trace.handle.AsyncTraceHandler">AsyncTraceHandler</a>
     */
    private int taskHandlerSize;

    /**
     * 主构建器
     *
     * @param infoFilters          信息过滤器集
     * @param infoEnhancers        信息增强器集
     * @param infoRecorders        信息记录器集
     * @param enableStack          启用堆栈信息标识
     * @param enableShortClassName 启用短类名标识
     * @param enableThreadName     启用线程名
     * @param enableRecordAll      启用记录所有
     * @param enableAsync          启用异步记录
     * @param taskHandlerSize      任务处理器数量
     * @throws TraceException if {@code taskHandlerSize} is less 1
     */
    public TraceRecorderConfig(
        List<InfoFilter> infoFilters,
        List<InfoEnhancer> infoEnhancers,
        List<InfoRecorder> infoRecorders,
        boolean enableStack,
        boolean enableShortClassName,
        boolean enableThreadName,
        boolean enableRecordAll,
        boolean enableAsync,
        int taskHandlerSize
    ) {
        if (enableAsync && taskHandlerSize < 1) {
            throw new TraceException("async case, taskHandlerSize must to greater 0");
        }

        this.infoFilters = Objects.isNull(infoFilters) ? Collections.EMPTY_LIST : infoFilters;
        this.infoEnhancers = Objects.isNull(infoEnhancers) ? Collections.EMPTY_LIST : infoEnhancers;
        this.infoRecorders = Objects.isNull(infoRecorders) ? Collections.EMPTY_LIST : infoRecorders;
        this.enableStack = enableStack;
        this.enableShortClassName = enableShortClassName;
        this.enableThreadName = enableThreadName;
        this.enableRecordAll = enableRecordAll;
        this.enableAsync = enableAsync;
        this.taskHandlerSize = taskHandlerSize;
    }

    /**
     * 跟踪记录仪配置详情
     *
     * @return 详情
     */
    @Override
    public String toString() {
        return (
            "TraceRecorderConfig{" +
            "infoFilters=" +
            Lists.classNames(infoFilters) +
            ", infoEnhancers=" +
            Lists.classNames(infoEnhancers) +
            ", infoRecorders=" +
            Lists.classNames(infoRecorders) +
            ", enableStack=" +
            enableStack +
            ", enableShortClassName=" +
            enableShortClassName +
            ", enableThreadName=" +
            enableThreadName +
            ", enableRecordAll=" +
            enableRecordAll +
            ", enableAsync=" +
            enableAsync +
            ", taskHandlerSize=" +
            taskHandlerSize +
            '}'
        );
    }
}
