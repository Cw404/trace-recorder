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
package cn.xusc.trace.dashboard.component.chart.echarts.relation.process;

import cn.xusc.trace.common.util.Arrays;
import cn.xusc.trace.common.util.FastList;
import cn.xusc.trace.common.util.Spaces;
import cn.xusc.trace.dashboard.component.chart.TraceDashboardChart;
import cn.xusc.trace.dashboard.component.chart.TraceDashboardChartAttribute;
import cn.xusc.trace.dashboard.component.chart.TraceDashboardChartData;
import cn.xusc.trace.dashboard.component.chart.TraceDashboardChartDataProcessor;
import cn.xusc.trace.dashboard.component.chart.echarts.relation.data.TraceDashboardEchartsRelationChartData;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 跟踪仪表盘Echarts关系图图表数据处理者
 *
 * @author WangCai
 * @since 2.5.3
 */
public class TraceDashboardEchartsRelationChartDataProcessor extends TraceDashboardChartDataProcessor {

    /**
     * 类名段
     */
    private List<String> classNameSegments;

    /**
     * 类名Y轴值段
     */
    private List<Integer> classNameYSegments;

    /**
     * 上一个跟踪仪表盘Echarts关系图图表数据
     */
    private TraceDashboardEchartsRelationChartData preRelationChartData;

    /**
     * 基础构造
     *
     * @param chart 跟踪仪表盘图表
     */
    public TraceDashboardEchartsRelationChartDataProcessor(TraceDashboardChart chart) {
        super(chart);
        classNameSegments = new ArrayList<>();
        classNameYSegments = new FastList<>(Integer.class);
    }

    @Override
    protected TraceDashboardChartData processStandardChartData(TraceDashboardChartData chartData) {
        TraceDashboardChartAttribute chartAttribute = CHART.chartAttribute();
        /*
        堆栈段设置
         */
        StackTraceElement[] stackTraceElements = chartData.stackTraceElements();
        Arrays.reverse(stackTraceElements);
        int segmentIndex = -1;
        String classNameSegment;
        for (StackTraceElement stackTraceElement : stackTraceElements) {
            classNameSegment = stackTraceElement.getClassName();
            if ((segmentIndex = classNameSegments.indexOf(classNameSegment)) < 0) {
                classNameSegments.add(classNameSegment);
                classNameYSegments.add(Integer.valueOf(chartAttribute.getAttribute("minYAxis")));
            }
        }

        /*
        段索引推断：如果最后节点段索引不存在，必定是添加到最后
         */
        segmentIndex = segmentIndex == -1 ? classNameSegments.size() - 1 : segmentIndex;

        /*
        处理Echarts关系图图表数据
         */
        TraceDashboardEchartsRelationChartData relationChartData = new TraceDashboardEchartsRelationChartData();
        relationChartData.setSymbolSize(Integer.valueOf(chartAttribute.getAttribute("symbolSize")));
        relationChartData.setCoordinateAxis(
            Spaces.create(
                Integer.valueOf(chartAttribute.getAttribute("maxXAxis")) +
                (segmentIndex << Integer.valueOf(chartAttribute.getAttribute("offsetXAxis"))),
                classNameYSegments.set(
                    segmentIndex,
                    classNameYSegments.get(segmentIndex) + Integer.valueOf(chartAttribute.getAttribute("offsetYAxis"))
                )
            )
        );
        relationChartData.setThreadName(chartData.threadName());
        relationChartData.setClassName(chartData.className());
        relationChartData.setMethodName(chartData.methodName());
        relationChartData.setLineNumber(chartData.lineNumber());
        relationChartData.setInfo(chartData.info());
        relationChartData.setStackTraceElements(stackTraceElements);
        if (Objects.nonNull(preRelationChartData)) {
            relationChartData.setNextChartData(preRelationChartData);
        }
        preRelationChartData = relationChartData;

        return relationChartData;
    }
}
