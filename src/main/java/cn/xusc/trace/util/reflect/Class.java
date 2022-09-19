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
import cn.xusc.trace.util.Formats;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 类
 *
 * @author WangCai
 * @since 2.5
 */
public class Class<T> implements ClassReflect<T> {

    /**
     * 源对象
     */
    protected T origin;
    /**
     * 源类
     */
    protected java.lang.Class<T> originClazz;
    /**
     * 禁用源对象功能标识
     */
    protected boolean disableOriginFunction;

    /**
     * 源类构造
     *
     * @param clazz 源类
     */
    public Class(java.lang.Class<T> clazz) {
        this(clazz, null);
    }

    /**
     * 源对象构造
     *
     * @param t 源对象
     */
    public Class(T t) {
        this(null, t);
    }

    /**
     * 核心构造
     *
     * @param clazz 源类
     * @param t 源对象
     * @throws TraceException if {@code clazz} and {@code t} is null or class type not match.
     */
    public Class(java.lang.Class<T> clazz, T t) {
        boolean clazzIsNull, tIsNull;
        if ((clazzIsNull = Objects.isNull(clazz)) & (tIsNull = Objects.isNull(t))) {
            throw new TraceException("clazz and t must have one is not null");
        }
        if (tIsNull) {
            originClazz = clazz;
            disableOriginFunction = true;
            return;
        }
        if (clazzIsNull) {
            originClazz = (java.lang.Class<T>) t.getClass();
            origin = t;
            return;
        }
        originClazz = clazz;
        if (!isInstance(t)) {
            throw new TraceException(
                Formats.format("class type not match, clazz: {}, t class: {}", clazz, t.getClass())
            );
        }
        origin = t;
    }

    @Override
    public boolean isInstance(T t) {
        Objects.requireNonNull(t);

        return originClazz.isInstance(t);
    }

    @Override
    public boolean isEnum() {
        return originClazz.isEnum();
    }

    @Override
    public boolean isArray() {
        return originClazz.isArray();
    }

    @Override
    public boolean isInterface() {
        return originClazz.isInterface();
    }

    @Override
    public boolean isAnnotation() {
        return originClazz.isAnnotation();
    }

    @Override
    public boolean isPrimitive() {
        return originClazz.isPrimitive();
    }

    @Override
    public boolean isSynthetic() {
        return originClazz.isSynthetic();
    }

    @Override
    public boolean isLocalClass() {
        return originClazz.isLocalClass();
    }

    @Override
    public boolean isMemberClass() {
        return originClazz.isMemberClass();
    }

    @Override
    public boolean isAnonymousClass() {
        return originClazz.isAnonymousClass();
    }

    @Override
    public String name() {
        return originClazz.getName();
    }

    @Override
    public java.lang.Class<T> type() {
        return originClazz;
    }

    @Override
    public java.lang.Class<T> componentType() {
        return (java.lang.Class<T>) originClazz.getComponentType();
    }

    @Override
    public List<Constructor<java.lang.reflect.Constructor>> constructors() {
        List<Constructor<java.lang.reflect.Constructor>> constructors = new ArrayList<>();
        for (java.lang.reflect.Constructor constructor : originClazz.getDeclaredConstructors()) {
            constructors.add(new Constructor(constructor));
        }
        return constructors;
    }

    @Override
    public List<Field<java.lang.reflect.Field>> fields() {
        List<Field<java.lang.reflect.Field>> fields = new ArrayList<>();
        for (java.lang.reflect.Field field : originClazz.getDeclaredFields()) {
            fields.add(new Field(origin, field));
        }
        return fields;
    }

    @Override
    public List<Method<java.lang.reflect.Method>> methods() {
        List<Method<java.lang.reflect.Method>> methods = new ArrayList<>();
        for (java.lang.reflect.Method method : originClazz.getDeclaredMethods()) {
            methods.add(new Method(origin, method));
        }
        return methods;
    }

    @Override
    public List<Annotation<Class<? extends java.lang.annotation.Annotation>>> annotations() {
        List<Annotation<Class<? extends java.lang.annotation.Annotation>>> annotations = new ArrayList<>();
        for (java.lang.annotation.Annotation annotation : originClazz.getDeclaredAnnotations()) {
            annotations.add(new Annotation(annotation, annotation.annotationType()));
        }
        return annotations;
    }
}
