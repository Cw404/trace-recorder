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
package cn.xusc.trace.server;

import cn.xusc.trace.common.util.reflect.Annotation;
import cn.xusc.trace.common.util.reflect.Class;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 服务资源接口
 *
 * @author WangCai
 * @since 2.5
 */
interface ServerResource {
    /**
     * 支持的数据类型
     */
    List<String> supportDataTypes = List.of("byte[]", "java.lang.String");

    /**
     * 获取资源路径
     *
     * @return 资源路径
     */
    String path();

    /**
     * 获取资源数据
     *
     * @return 资源数据
     */
    byte[] data();

    /**
     * 获取源注释列表
     *
     * <p>
     * 源注释列表会伴随整个服务渲染周期
     * </p>
     *
     * @return 源注释列表
     */
    List<Annotation<java.lang.annotation.Annotation>> metaAnnotations();

    /**
     * 是否为支持的数据类型
     *
     * @param dataType 数据类型
     * @return 支持详情
     */
    static boolean isSupportDataType(String dataType) {
        return supportDataTypes.contains(dataType);
    }

    /**
     * 解析为规范资源数据
     *
     * @param data 资源数据
     * @return 规范资源数据
     */
    static byte[] parseSpecificData(Object data) {
        if (new Class<>(String.class).isInstance(data)) {
            return ((String) data).getBytes(StandardCharsets.UTF_8);
        }
        return (byte[]) data;
    }
}
