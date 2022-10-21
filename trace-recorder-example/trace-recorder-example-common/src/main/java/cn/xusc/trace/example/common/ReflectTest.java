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
package cn.xusc.trace.example.common;

import cn.xusc.trace.common.exception.TraceException;
import cn.xusc.trace.common.util.Jsons;
import cn.xusc.trace.example.common.reflect.model.ReflectModel;
import cn.xusc.trace.example.common.reflect.model.ReflectShowModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * 反射测试
 *
 * @author wangcai
 */
public final class ReflectTest {

    /**
     * 显示ReflectModel引用信息
     */
    @Test
    @DisplayName("show reflect info of ReflectModel")
    public void showReflectInfoTest() {
        new ReflectorShow(new ReflectModel()).classInfo().constructorInfo().fieldInfo().methodInfo().printInfo();
    }

    /**
     * 反射显示
     * @param <T> 反射对象类型
     */
    class ReflectorShow<T> {

        /**
         * 对象类
         */
        private cn.xusc.trace.common.util.reflect.Class<T> clazz;
        /**
         * 反射显示模型
         */
        private ReflectShowModel reflectShowModel;

        /**
         * 默认构造
         *
         * @param t 反射对象
         */
        public ReflectorShow(T t) {
            clazz = new cn.xusc.trace.common.util.reflect.Class<>(t);
            reflectShowModel = new ReflectShowModel();
        }

        /**
         * 类信息
         *
         * @return 反射显示自身
         */
        public ReflectorShow classInfo() {
            reflectShowModel.name = clazz.name();
            reflectShowModel.type = clazz.type();
            reflectShowModel.componentType = clazz.componentType();
            reflectShowModel.annotationShowModels = annotationShowModels(clazz.annotations());
            return this;
        }

        /**
         * 构造函数信息
         *
         * @return 反射显示自身
         */
        public ReflectorShow constructorInfo() {
            reflectShowModel.constructorShowModels =
                clazz
                    .constructors()
                    .stream()
                    .map(constructor ->
                        new ReflectShowModel.ConstructorShowModel(
                            constructor.name(),
                            constructor
                                .parameters()
                                .stream()
                                .map(parameter ->
                                    new ReflectShowModel.ParameterShowModel(
                                        parameter.name(),
                                        parameter.type(),
                                        annotationShowModels(parameter.annotations())
                                    )
                                )
                                .collect(Collectors.toList()),
                            annotationShowModels(constructor.annotations())
                        )
                    )
                    .collect(Collectors.toList());
            return this;
        }

        /**
         * 字段信息
         *
         * @return 反射显示自身
         */
        public ReflectorShow fieldInfo() {
            reflectShowModel.filedShowModels =
                clazz
                    .fields()
                    .stream()
                    .map(field ->
                        new ReflectShowModel.FiledShowModel(
                            field.name(),
                            field.type(),
                            field.value(),
                            annotationShowModels(field.annotations())
                        )
                    )
                    .collect(Collectors.toList());
            return this;
        }

        /**
         * 方法信息
         *
         * @return 反射显示自身
         */
        public ReflectorShow methodInfo() {
            reflectShowModel.methodShowModels =
                clazz
                    .methods()
                    .stream()
                    .map(method ->
                        new ReflectShowModel.MethodShowModel(
                            method.name(),
                            method.returnType(),
                            method
                                .parameters()
                                .stream()
                                .map(parameter ->
                                    new ReflectShowModel.ParameterShowModel(
                                        parameter.name(),
                                        parameter.type(),
                                        annotationShowModels(parameter.annotations())
                                    )
                                )
                                .collect(Collectors.toList()),
                            annotationShowModels(method.annotations())
                        )
                    )
                    .collect(Collectors.toList());
            return this;
        }

        /**
         * 注释显示模型
         *
         * @param annotations 注释列表
         * @return 注释显示模型列表
         */
        private List<ReflectShowModel.AnnotationShowModel> annotationShowModels(
            List<cn.xusc.trace.common.util.reflect.Annotation<Class<? extends Annotation>>> annotations
        ) {
            return annotations
                .stream()
                .map(annotation ->
                    new ReflectShowModel.AnnotationShowModel(
                        annotation.name(),
                        annotation.type(),
                        annotation.componentType(),
                        annotation.value()
                    )
                )
                .collect(Collectors.toList());
        }

        /**
         * 打印信息
         */
        public void printInfo() {
            Jsons.write(objectMapper -> {
                try {
                    System.out.println(objectMapper.writeValueAsString(reflectShowModel));
                } catch (JsonProcessingException e) {
                    throw new TraceException(e);
                }
            });
        }
    }
}
