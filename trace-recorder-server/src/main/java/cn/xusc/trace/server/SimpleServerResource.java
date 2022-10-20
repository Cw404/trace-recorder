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
import java.util.List;
import lombok.AllArgsConstructor;

/**
 * 简单的服务资源
 *
 * @author WangCai
 * @since 2.5
 */
@AllArgsConstructor
class SimpleServerResource implements ServerResource {

    /**
     * 资源路径
     */
    private String path;

    /**
     * 资源数据
     */
    private byte[] data;

    /**
     * 源注释列表
     */
    private List<Annotation<java.lang.annotation.Annotation>> metaAnnotations;

    @Override
    public String path() {
        return path;
    }

    @Override
    public byte[] data() {
        return data;
    }

    @Override
    public List<Annotation<java.lang.annotation.Annotation>> metaAnnotations() {
        return metaAnnotations;
    }
}
