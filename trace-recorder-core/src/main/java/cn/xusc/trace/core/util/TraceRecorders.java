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
package cn.xusc.trace.core.util;

import cn.xusc.trace.common.util.ThreadLocals;
import cn.xusc.trace.core.TraceRecorder;
import java.util.Objects;
import lombok.experimental.UtilityClass;

/**
 * 跟踪记录仪工具类
 *
 * <p>
 * 注册跟踪记录仪，以供当前线程全局使用
 * 通过lombok组件{@link UtilityClass}确保工具类的使用
 * </p>
 *
 * @author WangCai
 * @since 2.5
 */
@UtilityClass
public class TraceRecorders {

    /**
     * 跟踪记录仪键
     */
    public final String TRACE_RECORDER_KEY = "TRACE_RECORDER_KEY";

    /**
     * 注册跟踪记录仪
     *
     * @param recorder 跟踪记录仪
     * @return 注册结果
     * @throws NullPointerException if {@code recorder} is null.
     */
    public boolean register(TraceRecorder recorder) {
        Objects.requireNonNull(recorder);

        ThreadLocals.put(TRACE_RECORDER_KEY, recorder);
        return true;
    }

    /**
     * 获取跟踪记录仪
     *
     * @return 跟踪记录仪
     */
    public TraceRecorder get() {
        return (TraceRecorder) ThreadLocals.get(TRACE_RECORDER_KEY);
    }

    /**
     * 根据注册键注册跟踪记录仪
     *
     * <p>
     * 可使当前线程拥有多个跟踪记录仪的能力
     * </p>
     *
     * @param registerKey 注册键
     * @param recorder 跟踪记录仪
     * @return 注册结果
     * @throws NullPointerException if {@code registerKey} is null.
     * @throws NullPointerException if {@code recorder} is null.
     */
    public boolean register(String registerKey, TraceRecorder recorder) {
        Objects.requireNonNull(registerKey);
        Objects.requireNonNull(recorder);

        ThreadLocals.put(registerKey, recorder);
        return true;
    }

    /**
     * 获取注册键相关的跟踪记录仪
     *
     * @param registerKey 注册键
     * @return 跟踪记录仪
     * @throws NullPointerException if {@code registerKey} is null.
     */
    public TraceRecorder get(String registerKey) {
        Objects.requireNonNull(registerKey);

        return (TraceRecorder) ThreadLocals.get(registerKey);
    }
}
