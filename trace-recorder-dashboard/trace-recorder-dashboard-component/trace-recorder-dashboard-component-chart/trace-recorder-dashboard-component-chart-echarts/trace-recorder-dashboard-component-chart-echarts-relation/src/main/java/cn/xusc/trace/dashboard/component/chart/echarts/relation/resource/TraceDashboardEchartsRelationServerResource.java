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
package cn.xusc.trace.dashboard.component.chart.echarts.relation.resource;

import cn.xusc.trace.dashboard.resource.BaseTraceDashboardComponentResource;
import cn.xusc.trace.server.annotation.ServerResource;
import cn.xusc.trace.server.annotation.TransientServerResource;
import java.nio.file.Path;

/**
 * 跟踪仪表盘Echarts关系图服务资源
 *
 * @author WangCai
 * @since 2.5.3
 */
public class TraceDashboardEchartsRelationServerResource extends BaseTraceDashboardComponentResource {

    /**
     * 基础构造
     *
     * @param generatePath 生成路径
     */
    public TraceDashboardEchartsRelationServerResource(Path generatePath) {
        super(generatePath);
    }

    /**
     * 关系图资源
     *
     * @return relation.html
     */
    @ServerResource(path = { "/relation" })
    public byte[] relation() {
        return readGenerateResourceData("echarts/relation/relation.html");
    }

    /**
     * 关系图数据资源（短暂的服务资源）
     *
     * <p>
     * 关系图数据会一直产生处理，所以是瞬态的
     * </p>
     *
     * @return relation.json
     */
    @TransientServerResource
    @ServerResource(path = { "/data/relation.json" })
    public byte[] relationJson() {
        return readGenerateResourceData("echarts/relation/data/relation.json");
    }
}
