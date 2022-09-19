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
import java.util.ArrayList;
import java.util.List;

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
    public List<Parameter<T>> parameters() {
        List<Parameter<T>> parameters = new ArrayList<>();
        for (java.lang.reflect.Parameter parameter : constructor.getParameters()) {
            parameters.add(new Parameter<>(parameter));
        }
        return parameters;
    }

    @Override
    public T call(T... ts) {
        try {
            return (T) constructor.newInstance(ts);
        } catch (Exception e) {
            throw new TraceException(e);
        }
    }

    @Override
    public List<Annotation<Class<? extends java.lang.annotation.Annotation>>> annotations() {
        List<Annotation<Class<? extends java.lang.annotation.Annotation>>> annotations = new ArrayList<>();
        for (java.lang.annotation.Annotation annotation : constructor.getDeclaredAnnotations()) {
            annotations.add(new Annotation(annotation, annotation.annotationType()));
        }
        return annotations;
    }
}
