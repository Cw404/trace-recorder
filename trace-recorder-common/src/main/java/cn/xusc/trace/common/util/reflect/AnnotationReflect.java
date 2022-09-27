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

/**
 * 注释反射接口
 *
 * @author WangCai
 * @since 2.5
 */
public interface AnnotationReflect<T> extends SpecificationReflect<T>, ValueReflect<T> {
    /**
     * 获取注释列表
     *
     * @return 注释列表
     * @throws TraceException if the fields operation is not supported by this reflect.
     */
    default List<Annotation<java.lang.Class<? extends java.lang.annotation.Annotation>>> annotations() {
        throw new TraceException("not support operation");
    }
}
