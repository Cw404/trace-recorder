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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 构造函数
 *
 * @author WangCai
 * @since 2.5
 */
public class Constructor<T> implements MethodReflect<T>, AnnotationReflect<T> {

    /**
     * 源构造函数
     */
    private java.lang.reflect.Constructor constructor;

    /**
     * 基础构造
     *
     * @param constructor 源构造函数
     */
    public Constructor(java.lang.reflect.Constructor constructor) {
        this.constructor = constructor;
        this.constructor.setAccessible(true);
    }

    @Override
    public String name() {
        return constructor.getName();
    }

    @Override
    public List<Parameter<java.lang.reflect.Parameter>> parameters() {
        List<Parameter<java.lang.reflect.Parameter>> parameters = new ArrayList<>();
        for (java.lang.reflect.Parameter parameter : constructor.getParameters()) {
            parameters.add(new Parameter<>(parameter));
        }
        return parameters;
    }

    @Override
    public Object call(Object... objs) {
        try {
            return constructor.newInstance(objs);
        } catch (Exception e) {
            throw new TraceException(e);
        }
    }

    @Override
    public List<Annotation<java.lang.Class<? extends java.lang.annotation.Annotation>>> annotations() {
        List<Annotation<java.lang.Class<? extends java.lang.annotation.Annotation>>> annotations = new ArrayList<>();
        for (java.lang.annotation.Annotation annotation : constructor.getDeclaredAnnotations()) {
            annotations.add(new Annotation(annotation, annotation.annotationType()));
        }
        return annotations;
    }

    @Override
    public Optional<Parameter<java.lang.reflect.Parameter>> findParameter(String parameterName) {
        return parameters().stream().filter(parameter -> Objects.equals(parameterName, parameter.name())).findFirst();
    }
}
