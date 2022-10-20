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
package cn.xusc.trace.chart.echarts.relation.resource;

import cn.xusc.trace.chart.resource.BaseChartServerResource;
import cn.xusc.trace.server.annotation.OverrideServerResource;
import cn.xusc.trace.server.annotation.ServerCloseResource;
import cn.xusc.trace.server.annotation.ServerResource;
import cn.xusc.trace.server.annotation.TransientServerResource;
import java.nio.file.Path;

/**
 * Echarts关系图服务资源
 *
 * @author WangCai
 * @since 2.5
 */
public class EchartsRelationServerResource extends BaseChartServerResource {

    /**
     * 基础构造
     *
     * @param generatePath 生成路径
     */
    public EchartsRelationServerResource(Path generatePath) {
        super(generatePath);
    }

    /**
     * 关系图资源
     *
     * @return relation.html
     */
    @ServerResource(path = { "/", "/relation" })
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

    /**
     * echarts.js
     *
     * @return echarts.min.js
     */
    @ServerResource(path = { "/js/echarts.min.js" })
    public byte[] echartsJs() {
        return readGenerateResourceData("echarts/relation/js/echarts.min.js");
    }

    /**
     * jquery.js
     *
     * @return jquery.js
     */
    @ServerResource(path = { "/js/jquery.js" })
    public byte[] jqueryJs() {
        return readGenerateResourceData("echarts/relation/js/jquery.js");
    }

    /**
     * 服务关闭资源（重写）
     *
     * @return closed.html
     */
    @OverrideServerResource
    @ServerCloseResource(path = { "/close" })
    public byte[] close() {
        return readClassLoaderResourceData("common/closed.html");
    }
}
