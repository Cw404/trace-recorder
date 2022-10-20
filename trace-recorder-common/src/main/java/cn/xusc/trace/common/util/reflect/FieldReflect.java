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
import java.util.List;
import java.util.Optional;

/**
 * 字段反射接口
 *
 * @author WangCai
 * @since 2.5
 */
public interface FieldReflect<T> extends SpecificationReflect<T>, ValueReflect<T>, AnnotationReflect<T> {
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
     * 查找可选的字段
     *
     * @param fieldName 字段名
     * @return 可选字段
     * @throws TraceException if the find field operation is not supported by this reflect.
     */
    default Optional<Field<java.lang.reflect.Field>> findField(String fieldName) {
        throw new TraceException("not support operation");
    }
}
