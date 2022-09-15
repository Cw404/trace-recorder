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
package cn.xusc.trace.util.reflect;

import cn.xusc.trace.exception.TraceException;
import java.util.List;

/**
 * 字段反射接口
 *
 * @author WangCai
 * @since 2.5
 */
public interface FieldReflect<T> {
    /**
     * 获取字段列表
     *
     * @return 字段列表
     * @throws TraceException if the fields operation is not supported by this reflect.
     */
    default List<Field<java.lang.reflect.Field>> fields() {
        throw new TraceException("not support operation");
    }

    /**
     * 获取字段值
     *
     * @return 字段值
     * @throws TraceException if the value operation is not supported by this reflect.
     */
    default T value() {
        throw new TraceException("not support operation");
    }
}
