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
package cn.xusc.trace.common.util.reflect;

import cn.xusc.trace.common.exception.TraceException;

/**
 * 值反射接口
 *
 * @author WangCai
 * @since 2.5
 */
public interface ValueReflect<T> {
    /**
     * 获取值
     *
     * <p>
     * 字段返回字段值，注释返回value方法值
     * </p>
     *
     * @return 值
     * @throws TraceException if the value operation is not supported by this reflect.
     */
    default Object value() {
        throw new TraceException("not support operation");
    }
}
