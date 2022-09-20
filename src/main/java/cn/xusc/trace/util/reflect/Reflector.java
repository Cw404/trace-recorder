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
import java.util.function.Consumer;

/**
 * 反射者
 *
 * @author WangCai
 * @since 2.5
 */
public final class Reflector<T> {

    /**
     * 类
     */
    private Class<T> clazz;

    /**
     * 基础构造
     *
     * @param clazz 类
     * @param t 对象
     * @throws TraceException if {@code clazz} and {@code t} is null or class type not match.
     */
    private Reflector(java.lang.Class<T> clazz, T t) {
        this.clazz = new Class(clazz, t);
    }

    /**
     * 构造一个类反射者
     *
     * @param clazz 类
     * @return 类反射者
     * @param <T> 对象类型
     * @throws TraceException if {@code clazz} and {@code t} is null or class type not match.
     */
    public static <T> Reflector<T> of(java.lang.Class<T> clazz) {
        return new Reflector<>(clazz, null);
    }

    /**
     * 构造一个对象反射者
     *
     * @param t 对象
     * @return 对象反射者
     * @param <T> 对象类型
     * @throws TraceException if {@code clazz} and {@code t} is null or class type not match.
     */
    public static <T> Reflector<T> of(T t) {
        return new Reflector<>(null, t);
    }

    /**
     * 构造一个详细的反射者
     *
     * @param clazz 类
     * @param t 对象
     * @return 详细的反射者
     * @param <T> 对象类型
     * @throws TraceException if {@code clazz} and {@code t} is null or class type not match.
     */
    public static <T> Reflector<T> of(java.lang.Class<T> clazz, T t) {
        return new Reflector<>(clazz, t);
    }

    /**
     * 游走所有
     *
     * @param classConsumer 类消费者
     * @param fieldConsumer 字段消费者
     * @param methodConsumer 方法消费者
     * @param annotationConsumer 注释消费者
     */
    public void walk(
        Consumer<Class<T>> classConsumer,
        Consumer<Field<java.lang.reflect.Field>> fieldConsumer,
        Consumer<Method<java.lang.reflect.Method>> methodConsumer,
        Consumer<Annotation<Class<? extends java.lang.annotation.Annotation>>> annotationConsumer
    ) {
        walkClass(classConsumer);
        walkFields(fieldConsumer);
        walkMethods(methodConsumer);
        walkAnnotations(annotationConsumer);
    }

    /**
     * 游走类
     *
     * @param classConsumer 类消费者
     */
    public void walkClass(Consumer<Class<T>> classConsumer) {
        classConsumer.accept(clazz);
    }

    /**
     * 游走字段
     *
     * @param fieldConsumer 字段消费者
     */
    public void walkFields(Consumer<Field<java.lang.reflect.Field>> fieldConsumer) {
        clazz.fields().forEach(fieldConsumer::accept);
    }

    /**
     * 游走方法
     *
     * @param methodConsumer 方法消费者
     */
    public void walkMethods(Consumer<Method<java.lang.reflect.Method>> methodConsumer) {
        clazz.methods().forEach(methodConsumer::accept);
    }

    /**
     * 游走注释
     *
     * @param annotationConsumer 注释消费者
     */
    public void walkAnnotations(
        Consumer<Annotation<Class<? extends java.lang.annotation.Annotation>>> annotationConsumer
    ) {
        clazz.annotations().forEach(annotationConsumer::accept);
    }
}
