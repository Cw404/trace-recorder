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
package cn.xusc.trace.example.common.reflect.model;

import java.util.List;

/**
 * 反射显示模型
 *
 * @author wangcai
 */
public class ReflectShowModel {

    /**
     * 名字
     */
    public String name;
    /**
     * 类型
     */
    public Object type;
    /**
     * 组件类型
     */
    public Object componentType;
    /**
     * 构造函数显示模型列表
     */
    public List<ConstructorShowModel> constructorShowModels;
    /**
     * 注释显示模型列表
     */
    public List<AnnotationShowModel> annotationShowModels;
    /**
     * 字段显示模型列表
     */
    public List<FiledShowModel> filedShowModels;
    /**
     * 方法显示模型列表
     */
    public List<MethodShowModel> methodShowModels;

    /**
     * 构造函数显示模型
     */
    public static class ConstructorShowModel {

        /**
         * 名字
         */
        public String name;
        /**
         * 参数显示模型列表
         */
        public List<ParameterShowModel> parameterShowModels;
        /**
         * 注释显示模型列表
         */
        public List<AnnotationShowModel> annotationShowModels;

        /**
         * 基础构造
         *
         * @param name 名字
         * @param parameterShowModels 参数显示模型列表
         * @param annotationShowModels 注释显示模型列表
         */
        public ConstructorShowModel(
            String name,
            List<ParameterShowModel> parameterShowModels,
            List<AnnotationShowModel> annotationShowModels
        ) {
            this.name = name;
            this.parameterShowModels = parameterShowModels;
            this.annotationShowModels = annotationShowModels;
        }
    }

    /**
     * 字段显示模型
     */
    public static class FiledShowModel {

        /**
         * 名字
         */
        public String name;
        /**
         * 类型
         */
        public Object type;
        /**
         * 值
         */
        public Object value;
        /**
         * 注释显示模型列表
         */
        public List<AnnotationShowModel> annotationShowModels;

        /**
         * 基础构造
         *
         * @param name 名字
         * @param type 类型
         * @param value 值
         * @param annotationShowModels 注释显示模型列表
         */
        public FiledShowModel(String name, Object type, Object value, List<AnnotationShowModel> annotationShowModels) {
            this.name = name;
            this.type = type;
            this.value = value;
            this.annotationShowModels = annotationShowModels;
        }
    }

    /**
     * 方法显示模型
     */
    public static class MethodShowModel {

        /**
         * 名字
         */
        public String name;
        /**
         * 返回类型
         */
        public String returnType;
        /**
         * 参数显示模型列表
         */
        public List<ParameterShowModel> parameterShowModels;
        /**
         * 注释显示模型列表
         */
        public List<AnnotationShowModel> annotationShowModels;

        /**
         * 基础构造
         * @param name 名字
         * @param returnType 返回类型
         * @param parameterShowModels 参数显示模型列表
         * @param annotationShowModels 注释显示模型列表
         */
        public MethodShowModel(
            String name,
            String returnType,
            List<ParameterShowModel> parameterShowModels,
            List<AnnotationShowModel> annotationShowModels
        ) {
            this.name = name;
            this.returnType = returnType;
            this.parameterShowModels = parameterShowModels;
            this.annotationShowModels = annotationShowModels;
        }
    }

    /**
     * 参数显示模型
     */
    public static class ParameterShowModel {

        /**
         * 名字
         */
        public String name;
        /**
         * 类型
         */
        public Object type;
        /**
         * 注释显示模型列表
         */
        public List<AnnotationShowModel> annotationShowModels;

        /**
         * 基础构造
         *
         * @param name 名字
         * @param type 类型
         * @param annotationShowModels 注释显示模型列表
         */
        public ParameterShowModel(String name, Object type, List<AnnotationShowModel> annotationShowModels) {
            this.name = name;
            this.type = type;
            this.annotationShowModels = annotationShowModels;
        }
    }

    /**
     * 注释显示模型
     */
    public static class AnnotationShowModel {

        /**
         * 名字
         */
        public String name;
        /**
         * 类型
         */
        public Object type;
        /**
         * 组件类型
         */
        public Object componentType;
        /**
         * 值
         */
        public Object value;

        /**
         * 基础构造
         *
         * @param name 名字
         * @param type 类型
         * @param componentType 组件类型
         * @param value 值
         */
        public AnnotationShowModel(String name, Object type, Object componentType, Object value) {
            this.name = name;
            this.type = type;
            this.componentType = componentType;
            this.value = value;
        }
    }
}
