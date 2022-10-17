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
import java.util.Optional;

/**
 * 参数反射接口
 *
 * @author WangCai
 * @since 2.5
 */
public interface ParameterReflect<T> extends SpecificationReflect<T>, AnnotationReflect<T> {
    /**
     * 查找可选的参数
     *
     * @param parameterName 参数名
     * @return 可选参数
     * @throws TraceException if the find parameter operation is not supported by this reflect.
     */
    default Optional<Parameter<java.lang.reflect.Parameter>> findParameter(String parameterName) {
        throw new TraceException("not support operation");
    }
}
