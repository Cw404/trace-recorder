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

import java.util.List;

/**
 * 注释反射接口
 *
 * @author WangCai
 * @since 2.5
 */
public interface AnnotationReflect {
    /**
     * 获取注释列表
     *
     * @return 注释列表
     */
    List<Annotation<Class<? extends java.lang.annotation.Annotation>>> annotations();
}
