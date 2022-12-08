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
package cn.xusc.trace.chart.echarts.bar.resource;

import cn.xusc.trace.chart.resource.BaseChartServerResource;
import cn.xusc.trace.server.annotation.*;
import java.nio.file.Path;

/**
 * Echarts柱状图服务资源
 *
 * @author WangCai
 * @since 2.5.2
 */
public class EchartsBarServerResource extends BaseChartServerResource {

    /**
     * 基础构造
     *
     * @param generatePath 生成路径
     */
    public EchartsBarServerResource(Path generatePath) {
        super(generatePath);
    }

    /**
     * 柱状图资源
     *
     * @return bar.html
     */
    @OutputStreamServerResource
    @ServerResource(path = { "/", "/bar" })
    public byte[] bar() {
        return readGenerateResourceData("echarts/bar/bar.html");
    }

    /**
     * 柱状图数据资源（短暂的服务资源）
     *
     * <p>
     * 柱状图数据会一直产生处理，所以是瞬态的
     * </p>
     *
     * @return bar.json
     */
    @TransientServerResource
    @ServerResource(path = { "/data/bar.json" })
    public byte[] relationJson() {
        return readGenerateResourceData("echarts/bar/data/bar.json");
    }

    /**
     * echarts.js
     *
     * @return echarts.min.js
     */
    @OutputStreamServerResource
    @ServerResource(path = { "/js/echarts.min.js" })
    public byte[] echartsJs() {
        return readGenerateResourceData("echarts/bar/js/echarts.min.js");
    }

    /**
     * jquery.js
     *
     * @return jquery.js
     */
    @ServerResource(path = { "/js/jquery.js" })
    public byte[] jqueryJs() {
        return readGenerateResourceData("echarts/bar/js/jquery.js");
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
