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
package cn.xusc.trace.dashboard.component.chart.resource;

import cn.xusc.trace.dashboard.resource.BaseTraceDashboardComponentResource;
import cn.xusc.trace.server.annotation.*;
import java.nio.file.Path;

/**
 * 基础跟踪仪表盘图表服务资源
 *
 * <p>
 * 提供基础的跟踪仪表盘图服务资源
 * </p>
 *
 * @author WangCai
 * @since 2.5.3
 */
@ServerResourceRegister
public class BaseTraceDashboardChartServerResource extends BaseTraceDashboardComponentResource {

    /**
     * 基础构造
     *
     * @param generatePath 生成路径
     */
    public BaseTraceDashboardChartServerResource(Path generatePath) {
        super(generatePath);
    }

    /**
     * chart_echarts.js
     *
     * @return chart_echarts.min.js
     */
    @OverrideServerResource
    @OutputStreamServerResource
    @ServerResource(path = { "/js/chart_echarts.min.js" })
    public byte[] chartEchartsJs() {
        return readClassLoaderResourceData("common/js/chart_echarts.min.js");
    }

    /**
     * chart_jquery.js
     *
     * @return chart_jquery.js
     */
    @OverrideServerResource
    @OutputStreamServerResource
    @ServerResource(path = { "/js/chart_jquery.js" })
    public byte[] chartJqueryJs() {
        return readClassLoaderResourceData("common/js/chart_jquery.js");
    }
}
