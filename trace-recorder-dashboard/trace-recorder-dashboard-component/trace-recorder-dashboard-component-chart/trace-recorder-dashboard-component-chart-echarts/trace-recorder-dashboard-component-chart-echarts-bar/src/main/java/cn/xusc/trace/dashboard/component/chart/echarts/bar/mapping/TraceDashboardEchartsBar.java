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
package cn.xusc.trace.dashboard.component.chart.echarts.bar.mapping;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * 跟踪仪表盘Echarts柱状图
 *
 * @author WangCai
 * @since 2.5.3
 */
@Setter
@Getter
@Builder
public class TraceDashboardEchartsBar {

    /**
     * Echarts柱状图x轴数据列表
     */
    @JsonProperty(value = "xAxisData")
    List<String> xAxisData;

    /**
     * Echarts柱状图系列数据列表
     */
    List<Integer> seriesData;

    /**
     * Echarts柱状图美化间隔
     */
    short beautifyInterval;
}
