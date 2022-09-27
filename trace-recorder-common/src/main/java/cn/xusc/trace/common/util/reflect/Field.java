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
 * 字段
 *
 * @author WangCai
 * @since 2.5
 */
public class Field<T> implements FieldReflect<T> {

    /**
     * 源对象
     */
    private T origin;
    /**
     * 源字段
     */
    private java.lang.reflect.Field field;
    /**
     * 禁用源对象功能标识
     */
    private boolean disableOriginFunction;

    /**
     * 基础构造
     *
     * @param origin 源对象
     * @param field 源字段
     * @throws NullPointerException if {@code field} is null.
     */
    public Field(T origin, java.lang.reflect.Field field) {
        Objects.requireNonNull(field);

        this.origin = origin;
        this.field = field;
        this.field.setAccessible(true);
        disableOriginFunction = Objects.isNull(origin);
    }

    @Override
    public String name() {
        return field.getName();
    }

    @Override
    public java.lang.Class<T> type() {
        return (java.lang.Class<T>) field.getType();
    }

    @Override
    public T value() {
        if (disableOriginFunction) {
            return FieldReflect.super.value();
        }

        try {
            return (T) field.get(origin);
        } catch (Exception e) {
            throw new TraceException(e);
        }
    }

    @Override
    public List<Annotation<java.lang.Class<? extends java.lang.annotation.Annotation>>> annotations() {
        List<Annotation<java.lang.Class<? extends java.lang.annotation.Annotation>>> annotations = new ArrayList<>();
        for (java.lang.annotation.Annotation annotation : field.getDeclaredAnnotations()) {
            annotations.add(new Annotation(annotation, annotation.annotationType()));
        }
        return annotations;
    }
}
