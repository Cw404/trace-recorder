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

import java.util.List;

/**
 * 类反射接口
 *
 * @author WangCai
 * @since 2.5
 */
public interface ClassReflect<T> extends FieldReflect<T>, MethodReflect<T>, AnnotationReflect<T> {
    /**
     * 对象是否为类实例
     *
     * @param obj 对象
     * @return 详情
     */
    boolean isInstance(Object obj);

    /**
     * 是否为枚举类
     *
     * @return 详情
     */
    boolean isEnum();

    /**
     * 是否为数组类
     *
     * @return 详情
     */
    boolean isArray();

    /**
     * 是否为接口类
     *
     * @return 详情
     */
    boolean isInterface();

    /**
     * 是否为注释类
     *
     * @return 详情
     */
    boolean isAnnotation();

    /**
     * 是否为原始的基本类型
     *
     * <ul>
     *     <li>Boolean.TYPE</li>
     *     <li>Character.TYPE</li>
     *     <li>Byte.TYPE</li>
     *     <li>Short.TYPE</li>
     *     <li>Integer.TYPE</li>
     *     <li>Long.TYPE</li>
     *     <li>Float.TYPE</li>
     *     <li>Double.TYPE</li>
     *     <li>Void.TYPE</li>
     * </ul>
     *
     * @return 详情
     */
    boolean isPrimitive();

    /**
     * 是否为合成类
     *
     * @return 详情
     */
    boolean isSynthetic();

    /**
     * 是否为本地类
     *
     * @return 详情
     */
    boolean isLocalClass();

    /**
     * 是否为成员类
     *
     * @return 详情
     */
    boolean isMemberClass();

    /**
     * 是否为匿名类
     *
     * @return 详情
     */
    boolean isAnonymousClass();

    /**
     * 获取构造函数列表
     *
     * @return 构造函数列表
     */
    List<Constructor<java.lang.reflect.Constructor>> constructors();
}
