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
import java.lang.annotation.*;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 注释反射接口
 *
 * @author WangCai
 * @since 2.5
 */
public interface AnnotationReflect<T> extends SpecificationReflect<T>, ValueReflect<T> {
    /**
     * 忽略的注释列表
     *
     * <p>
     * 列表中的注释会构成循环依赖
     * </p>
     */
    List<java.lang.Class<?>> IGNORE_ANNOTATIONS = List.of(Documented.class, Retention.class, Target.class);

    /**
     * 获取注释列表
     *
     * @return 注释列表
     * @throws TraceException if the fields operation is not supported by this reflect.
     */
    default List<Annotation<java.lang.Class<? extends java.lang.annotation.Annotation>>> annotations() {
        throw new TraceException("not support operation");
    }

    /**
     * 是否有该注释
     *
     * @param targetAnnotationClass 目标注释类
     * @return 详情
     */
    default boolean hasAnnotation(java.lang.Class<?> targetAnnotationClass) {
        return findAnnotation(targetAnnotationClass).isPresent();
    }

    /**
     * 查找可选的注释
     *
     * @param targetAnnotationClass 目标注释类
     * @return 可选注释
     */
    default Optional<Annotation<java.lang.Class<? extends java.lang.annotation.Annotation>>> findAnnotation(
        java.lang.Class<?> targetAnnotationClass
    ) {
        /*
        查找自身注释
         */
        Optional<Annotation<java.lang.Class<? extends java.lang.annotation.Annotation>>> firstLevelAnnotationOptional = findSelfAnnotation(
            targetAnnotationClass
        );

        if (firstLevelAnnotationOptional.isPresent()) {
            return firstLevelAnnotationOptional;
        }

        /*
        查找注释声明的注释
         */
        Optional<Annotation<java.lang.Class<? extends java.lang.annotation.Annotation>>> secondLevelAnnotationOptional;
        for (Annotation<java.lang.Class<? extends java.lang.annotation.Annotation>> annotation : annotations()) {
            if (IGNORE_ANNOTATIONS.contains(annotation.type())) {
                continue;
            }
            secondLevelAnnotationOptional = annotation.findAnnotation(targetAnnotationClass);
            if (secondLevelAnnotationOptional.isPresent()) {
                return Optional.of(annotation);
            }
        }

        return Optional.empty();
    }

    /**
     * 查找可选可用的注释
     *
     * @param targetAnnotationClass 目标注释类
     * @return 可选注释
     */
    default Optional<Annotation<java.lang.Class<? extends java.lang.annotation.Annotation>>> findAvailableAnnotation(
        java.lang.Class<?> targetAnnotationClass
    ) {
        return findAnnotation(targetAnnotationClass);
    }

    /**
     * 查找自身可选的注释
     *
     * @param targetAnnotationClass 目标注释类
     * @return 可选注释
     */
    default Optional<Annotation<java.lang.Class<? extends java.lang.annotation.Annotation>>> findSelfAnnotation(
        java.lang.Class<?> targetAnnotationClass
    ) {
        return annotations()
            .stream()
            .filter(annotation -> Objects.equals(targetAnnotationClass, annotation.type()))
            .findFirst();
    }

    /**
     * 是否是可继承的可选注释
     *
     * @param annotationOptional 可选注释
     * @return 可继承详情
     */
    default boolean isInherited(
        Optional<Annotation<java.lang.Class<? extends java.lang.annotation.Annotation>>> annotationOptional
    ) {
        return (
            annotationOptional.isPresent() &&
            annotationOptional
                .get()
                .annotations()
                .stream()
                .anyMatch(annotation -> Objects.equals(annotation.type(), Inherited.class))
        );
    }
}
