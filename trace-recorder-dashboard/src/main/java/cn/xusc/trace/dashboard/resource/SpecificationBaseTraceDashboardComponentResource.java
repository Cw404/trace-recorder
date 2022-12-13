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
package cn.xusc.trace.dashboard.resource;

import cn.xusc.trace.server.annotation.DisableOverrideServerResource;
import cn.xusc.trace.server.annotation.ServerCloseResource;
import java.nio.file.Path;

/**
 * 规范基础跟踪仪表盘组件资源
 *
 * @author WangCai
 * @since 2.5.3
 */
public class SpecificationBaseTraceDashboardComponentResource extends BaseTraceDashboardComponentResource {

    /**
     * 基础构造
     *
     * @param generatePath 生成路径
     */
    public SpecificationBaseTraceDashboardComponentResource(Path generatePath) {
        super(generatePath);
    }

    /**
     * 规范服务关闭资源（禁用重写）
     *
     * @return closed.html
     */
    @DisableOverrideServerResource
    @ServerCloseResource(path = { "/specificationClose" })
    public byte[] specificationClose() {
        return readClassLoaderResourceData("common/closed.html");
    }
}
