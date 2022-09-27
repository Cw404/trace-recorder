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
package cn.xusc.trace.core.handle;

import cn.xusc.trace.common.exception.TraceTimeoutException;
import cn.xusc.trace.core.constant.RecordLabel;
import java.util.concurrent.TimeUnit;

/**
 * 跟踪处理器
 *
 * @author WangCai
 * @since 2.0
 */
public interface TraceHandler {
    /**
     * 信息处理
     *
     * @param info     信息
     * @param label    记录标签
     * @param argArray 参数列表
     */
    void handle(String info, RecordLabel label, Object... argArray);

    /**
     * 处理器关闭
     *
     * @since 2.1
     */
    void shutdown();

    /**
     * 处理器关闭，会处理完指定时间未完成的任务
     *
     * @param timeout  时间
     * @param timeUnit 时间单位
     * @throws TraceTimeoutException if a timeout occurs before shutdown completes.
     * @since 2.1
     */
    void shutdown(long timeout, TimeUnit timeUnit) throws TraceTimeoutException;
}
