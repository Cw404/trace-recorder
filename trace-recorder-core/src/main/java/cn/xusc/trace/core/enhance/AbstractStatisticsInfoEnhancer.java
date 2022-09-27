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
package cn.xusc.trace.core.enhance;

import cn.xusc.trace.common.annotation.CloseOrder;
import cn.xusc.trace.common.util.Runtimes;
import cn.xusc.trace.core.EnhanceInfo;
import cn.xusc.trace.core.TraceRecorder;
import java.io.Closeable;
import java.util.Objects;

/**
 * 统计信息增强器
 *
 * <p>
 * 模版抽象统计，需子类定制实现
 * ps: 只需实现{@link #enhance(EnhanceInfo)}方法，
 * 因为里面已经包含所有信息了，并且对应增强语意
 * ps1: 当前类统一实现了关闭钩子；
 * 如果子类需要确保关闭有序性，请自定义标记{@link CloseOrder#value()}顺序值
 * </p>
 *
 * @author WangCai
 * @since 1.0
 */
public abstract class AbstractStatisticsInfoEnhancer implements InfoEnhancer, Closeable {

    /**
     * 跟踪记录仪
     */
    private final TraceRecorder TRACE_RECORDER;

    /**
     * 是否关闭
     */
    private boolean isClose;

    /**
     * 应用跟踪记录仪进行详细信息记录
     *
     * @param traceRecorder 跟踪记录仪
     * @throws NullPointerException if {@code traceRecorder} is null
     */
    public AbstractStatisticsInfoEnhancer(TraceRecorder traceRecorder) {
        Objects.requireNonNull(traceRecorder);
        this.TRACE_RECORDER = traceRecorder;

        /*
          注册JVM关闭显示统计信息钩子
         */
        Runtimes.addCleanTask(this);
    }

    @Override
    public EnhanceInfo enhance(EnhanceInfo eInfo) {
        if (isClose) {
            /*
              如果到此处，证明是JVM进行关闭回掉，不做增强
             */
            return eInfo;
        }
        return doEnhance(eInfo);
    }

    @Override
    public EnhanceInfo setWriteInfo(EnhanceInfo eInfo) {
        if (isClose) {
            /*
              如果到此处，证明是JVM进行关闭回掉，写信息应该设置为传入信息
             */
            eInfo.setWriteInfo(eInfo.getInfo());
        }
        return eInfo;
    }

    @Override
    public void close() {
        isClose = true;
        String showInfo = showInfo();
        if (Objects.isNull(showInfo) || showInfo.isEmpty()) {
            /*
              此种方式，表示不需要进行记录
             */
            return;
        }

        /*
          最终统计不需要开启堆栈信息增强
         */
        TRACE_RECORDER.disableStackInfo();
        TRACE_RECORDER.log(showInfo);
    }

    /**
     * 子类增强
     *
     * @param eInfo 增强前消息
     * @return 增强后消息
     */
    protected abstract EnhanceInfo doEnhance(EnhanceInfo eInfo);

    /**
     * 显示信息
     *
     * @return 具体显示消息
     */
    protected abstract String showInfo();
}
