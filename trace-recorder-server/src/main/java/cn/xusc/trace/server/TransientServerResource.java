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
import java.util.function.Supplier;
import lombok.Getter;

/**
 * 短暂的服务资源
 *
 * @author WangCai
 * @since 2.5
 */
@Getter
class TransientServerResource extends SimpleServerResource {

    /**
     * 数据提供者
     */
    private Supplier<byte[]> dataSupplier;

    /**
     * 基础构造
     *
     * @param path 资源路径
     * @param data 资源数据
     * @param metaAnnotations 源注释列表
     * @param dataSupplier 数据提供者
     */
    public TransientServerResource(
        String path,
        byte[] data,
        List<Annotation<java.lang.annotation.Annotation>> metaAnnotations,
        Supplier<byte[]> dataSupplier
    ) {
        super(path, data, metaAnnotations);
        this.dataSupplier = dataSupplier;
    }

    @Override
    public String path() {
        return super.path();
    }

    @Override
    public byte[] data() {
        return super.data();
    }

    @Override
    public List<Annotation<java.lang.annotation.Annotation>> metaAnnotations() {
        return super.metaAnnotations();
    }
}
