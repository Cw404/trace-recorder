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

/**
 * 规格反射接口
 *
 * @author WangCai
 * @since 2.5
 */
public interface SpecificationReflect<T> {
    /**
     * 获取名字
     *
     * @return 名字
     */
    String name();

    /**
     * 获取类型
     *
     * @return 类型
     */
    default java.lang.Class<T> type() {
        return null;
    }

    /**
     * 获取组件类型
     *
     * @return 组件类型
     */
    default java.lang.Class<T> componentType() {
        return null;
    }

    /**
     * 获取源自身
     *
     * @return 源自身
     */
    Object self();
}
