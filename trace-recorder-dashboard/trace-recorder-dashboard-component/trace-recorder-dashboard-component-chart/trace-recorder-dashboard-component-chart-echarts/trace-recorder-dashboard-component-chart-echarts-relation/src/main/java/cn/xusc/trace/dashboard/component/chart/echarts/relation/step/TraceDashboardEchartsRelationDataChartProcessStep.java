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
package cn.xusc.trace.dashboard.component.chart.echarts.relation.step;

import cn.xusc.trace.common.util.Formats;
import cn.xusc.trace.common.util.Spaces;
import cn.xusc.trace.dashboard.component.chart.AbstractTraceDashboardChart;
import cn.xusc.trace.dashboard.component.chart.AbstractTraceDashboardChartConfig;
import cn.xusc.trace.dashboard.component.chart.TraceDashboardChartProcessStep;
import cn.xusc.trace.dashboard.component.chart.echarts.relation.data.TraceDashboardEchartsRelationChartData;
import cn.xusc.trace.dashboard.component.chart.echarts.relation.mapping.TraceDashboardEchartsRelation;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 跟踪仪表盘Echarts关系图数据图表处理步骤
 *
 * @author WangCai
 * @since 2.5.3
 */
public class TraceDashboardEchartsRelationDataChartProcessStep implements TraceDashboardChartProcessStep {

    /**
     * 基础构造
     */
    public TraceDashboardEchartsRelationDataChartProcessStep() {}

    @Override
    public <T> void run(
        AbstractTraceDashboardChart chart,
        AbstractTraceDashboardChartConfig config,
        ValuePipeline<T> valuePipeline
    ) throws Exception {
        TraceDashboardEchartsRelationChartData relationChartData = (TraceDashboardEchartsRelationChartData) valuePipeline.getValue();

        Deque<TraceDashboardEchartsRelationChartData> tempChartsData = new LinkedList<>();
        do {
            tempChartsData.addFirst(relationChartData);
        } while (
            Objects.nonNull(
                relationChartData = (TraceDashboardEchartsRelationChartData) relationChartData.nextChartData()
            )
        );

        int id = 0, offset = -1;
        String className;
        Spaces.CoordinateAxis coordinateAxis;
        List<String> classNames = new ArrayList<>();
        List<TraceDashboardEchartsRelation.EchartsRelationNode> relationNodes = new ArrayList<>();
        List<TraceDashboardEchartsRelation.EchartsRelationLink> relationLinks = new ArrayList<>();
        for (TraceDashboardEchartsRelationChartData chartData : tempChartsData) {
            className = chartData.className();
            coordinateAxis = chartData.getCoordinateAxis();
            if ((offset = classNames.indexOf(className)) < 0) {
                classNames.add(className);
            }

            /*
            偏移推断：如果最后节点段索引不存在，必定是添加到最后
             */
            offset = offset == -1 ? classNames.size() - 1 : offset;

            relationNodes.add(
                TraceDashboardEchartsRelation.EchartsRelationNode
                    .builder()
                    .id(String.valueOf(id))
                    .name(
                        generateRelationNodeName(
                            chartData.threadName(),
                            chartData.className(),
                            chartData.methodName(),
                            chartData.lineNumber()
                        )
                    )
                    .symbolSize(chartData.getSymbolSize())
                    .x(coordinateAxis.getX())
                    .y(coordinateAxis.getY())
                    .value(chartData.info())
                    .category(offset)
                    .build()
            );

            relationLinks.add(
                TraceDashboardEchartsRelation.EchartsRelationLink
                    .builder()
                    .source(String.valueOf(id))
                    .target(String.valueOf(id - 1))
                    .build()
            );
            id++;
        }
        TraceDashboardEchartsRelation relation = TraceDashboardEchartsRelation
            .builder()
            .nodes(relationNodes)
            .links(relationLinks)
            .categories(
                classNames
                    .stream()
                    .map(categoriesClassName ->
                        TraceDashboardEchartsRelation.EchartsRelationCategory
                            .builder()
                            .name(categoriesClassName)
                            .build()
                    )
                    .collect(Collectors.toList())
            )
            .build();

        valuePipeline.setValue((T) relation);
    }

    /**
     * 生成关系图节点名称
     *
     * @param threadName 线程名
     * @param className 类名
     * @param methodName 方法名
     * @param lineNumber 行号
     * @return 关系图节点名称
     */
    private String generateRelationNodeName(String threadName, String className, String methodName, int lineNumber) {
        return Formats.format("{} - {}#{}()[{}]", threadName, className, methodName, lineNumber);
    }
}
