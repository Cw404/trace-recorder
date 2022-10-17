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
import java.util.Objects;
import java.util.Optional;

/**
 * 注释
 *
 * @author WangCai
 * @since 2.5
 */
public class Annotation<T> implements MethodReflect<T> {

    /**
     * 源对象
     */
    private T origin;
    /**
     * 注释类
     */
    private java.lang.Class<? extends java.lang.annotation.Annotation> annotation;
    /**
     * 禁用源对象功能标识
     */
    private boolean disableOriginFunction;

    /**
     * 基础构造
     *
     * @param origin 源对象
     * @param annotation 注释类
     */
    public Annotation(T origin, java.lang.Class<? extends java.lang.annotation.Annotation> annotation) {
        this.origin = origin;
        this.annotation = annotation;
        disableOriginFunction = Objects.isNull(origin);
    }

    @Override
    public String name() {
        return annotation.getName();
    }

    @Override
    public java.lang.Class<T> type() {
        return (java.lang.Class<T>) annotation;
    }

    @Override
    public java.lang.Class<T> componentType() {
        return (java.lang.Class<T>) annotation.getComponentType();
    }

    @Override
    public List<Method<java.lang.reflect.Method>> methods() {
        if (disableOriginFunction) {
            return MethodReflect.super.methods();
        }

        List<Method<java.lang.reflect.Method>> methods = new ArrayList<>();
        for (java.lang.reflect.Method method : annotation.getDeclaredMethods()) {
            methods.add(new Method(origin, method));
        }
        return methods;
    }

    @Override
    public Object value() {
        if (disableOriginFunction) {
            return MethodReflect.super.call();
        }

        Optional<Method<java.lang.reflect.Method>> valueMethod = methods()
            .stream()
            .filter(method -> Objects.equals(method.name(), "value"))
            .findFirst();
        if (valueMethod.isEmpty()) {
            return null;
        }

        return valueMethod.get().call();
    }

    @Override
    public List<Annotation<java.lang.Class<? extends java.lang.annotation.Annotation>>> annotations() {
        if (disableOriginFunction) {
            return MethodReflect.super.annotations();
        }

        List<Annotation<java.lang.Class<? extends java.lang.annotation.Annotation>>> annotations = new ArrayList<>();
        for (java.lang.annotation.Annotation annotation : annotation.getDeclaredAnnotations()) {
            annotations.add(new Annotation(annotation, annotation.annotationType()));
        }
        return annotations;
    }

    @Override
    public Optional<Method<java.lang.reflect.Method>> findMethod(String methodName) {
        return methods().stream().filter(method -> Objects.equals(methodName, method.name())).findFirst();
    }
}
