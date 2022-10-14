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

/**
 * 方法
 *
 * @author WangCai
 * @since 2.5
 */
public class Method<T> implements MethodReflect<T> {

    /**
     * 源对象
     */
    private T origin;
    /**
     * 源方法
     */
    private java.lang.reflect.Method method;
    /**
     * 禁用源对象功能标识
     */
    private boolean disableOriginFunction;

    /**
     * 基础构造
     *
     * @param origin 源对象
     * @param method 源方法
     * @throws NullPointerException if {@code method} is null.
     */
    public Method(T origin, java.lang.reflect.Method method) {
        Objects.requireNonNull(method);

        this.origin = origin;
        this.method = method;
        this.method.setAccessible(true);
        this.disableOriginFunction = Objects.isNull(origin);
    }

    @Override
    public String name() {
        return method.getName();
    }

    @Override
    public List<Parameter<T>> parameters() {
        List<Parameter<T>> parameters = new ArrayList<>();
        for (java.lang.reflect.Parameter parameter : method.getParameters()) {
            parameters.add(new Parameter<>(parameter));
        }
        return parameters;
    }

    @Override
    public String returnType() {
        return method.getGenericReturnType().getTypeName();
    }

    @Override
    public Object call(Object... objs) {
        if (disableOriginFunction) {
            return MethodReflect.super.call();
        }

        try {
            return method.invoke(origin, objs);
        } catch (Exception e) {
            throw new TraceException(e);
        }
    }

    @Override
    public List<Annotation<java.lang.Class<? extends java.lang.annotation.Annotation>>> annotations() {
        List<Annotation<java.lang.Class<? extends java.lang.annotation.Annotation>>> annotations = new ArrayList<>();
        for (java.lang.annotation.Annotation annotation : method.getDeclaredAnnotations()) {
            annotations.add(new Annotation(annotation, annotation.annotationType()));
        }
        return annotations;
    }
}
