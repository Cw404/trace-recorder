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

import java.util.ArrayList;
import java.util.List;

/**
 * 参数
 *
 * @author WangCai
 * @since 2.5
 */
public class Parameter<T> implements ParameterReflect<T> {

    /**
     * 源参数
     */
    private java.lang.reflect.Parameter parameter;

    /**
     * 基础构造
     *
     * @param parameter 源参数
     */
    public Parameter(java.lang.reflect.Parameter parameter) {
        this.parameter = parameter;
    }

    @Override
    public String name() {
        return parameter.getName();
    }

    @Override
    public java.lang.Class<T> type() {
        return (java.lang.Class<T>) parameter.getType();
    }

    @Override
    public List<Annotation<java.lang.Class<? extends java.lang.annotation.Annotation>>> annotations() {
        List<Annotation<java.lang.Class<? extends java.lang.annotation.Annotation>>> annotations = new ArrayList<>();
        for (java.lang.annotation.Annotation annotation : parameter.getDeclaredAnnotations()) {
            annotations.add(new Annotation(annotation, annotation.annotationType()));
        }
        return annotations;
    }
}
