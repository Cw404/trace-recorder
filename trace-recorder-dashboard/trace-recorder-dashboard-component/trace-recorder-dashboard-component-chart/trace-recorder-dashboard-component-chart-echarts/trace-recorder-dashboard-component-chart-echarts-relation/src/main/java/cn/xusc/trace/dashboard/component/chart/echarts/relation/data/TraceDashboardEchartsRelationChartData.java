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
package cn.xusc.trace.dashboard.component.chart.echarts.relation.data;

import cn.xusc.trace.common.util.Spaces;
import cn.xusc.trace.dashboard.component.chart.AbstractTraceDashboardChartData;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

/**
 * 跟踪仪表盘Echarts关系图图表数据
 *
 * @author WangCai
 * @since 2.5.3
 */
@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TraceDashboardEchartsRelationChartData extends AbstractTraceDashboardChartData {

    /**
     * 节点标记大小
     */
    int symbolSize;

    /**
     * 坐标轴
     */
    Spaces.CoordinateAxis coordinateAxis;

    @Override
    public String basicTraceDashboardChartData() {
        return (
            "{" +
            "symbolSize=" +
            symbolSize +
            ", coordinateAxis=" +
            coordinateAxis +
            ", threadName='" +
            threadName +
            '\'' +
            ", className='" +
            className +
            '\'' +
            ", methodName='" +
            methodName +
            '\'' +
            ", lineNumber=" +
            lineNumber +
            ", info='" +
            info +
            '\'' +
            '}'
        );
    }
}
