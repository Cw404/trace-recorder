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
package cn.xusc.trace.chart.echarts.relation.data;

import cn.xusc.trace.chart.AbstractChartData;
import cn.xusc.trace.common.util.Spaces;
import lombok.Getter;
import lombok.Setter;

/**
 * Echarts关系图图表数据
 *
 * @author WangCai
 * @since 2.5
 */
@Setter
@Getter
public class EchartsRelationChartData extends AbstractChartData {

    /**
     * 节点标记大小
     */
    private int symbolSize;

    /**
     * 坐标轴
     */
    Spaces.CoordinateAxis coordinateAxis;
}
