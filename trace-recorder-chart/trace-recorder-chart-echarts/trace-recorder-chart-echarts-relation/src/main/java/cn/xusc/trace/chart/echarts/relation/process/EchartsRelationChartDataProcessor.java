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
package cn.xusc.trace.chart.echarts.relation.process;

import cn.xusc.trace.chart.Chart;
import cn.xusc.trace.chart.ChartAttribute;
import cn.xusc.trace.chart.ChartData;
import cn.xusc.trace.chart.ChartDataProcessor;
import cn.xusc.trace.chart.echarts.relation.data.EchartsRelationChartData;
import cn.xusc.trace.common.util.Arrays;
import cn.xusc.trace.common.util.FastList;
import cn.xusc.trace.common.util.Spaces;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Echarts关系图图表数据处理者
 *
 * @author WangCai
 * @since 2.5
 */
public class EchartsRelationChartDataProcessor extends ChartDataProcessor {

    /**
     * 类名段
     */
    private List<String> classNameSegments;

    /**
     * 类名Y轴值段
     */
    private List<Integer> classNameYSegments;

    /**
     * 上一个Echarts关系图图表数据
     */
    private EchartsRelationChartData preRelationChartData;

    /**
     * 基础构造
     *
     * @param chart 图表
     */
    public EchartsRelationChartDataProcessor(Chart chart) {
        super(chart);
        classNameSegments = new ArrayList<>();
        classNameYSegments = new FastList<>(Integer.class);
    }

    @Override
    protected ChartData processStandardChartData(ChartData chartData) {
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
                classNameYSegments.add(Integer.valueOf(ChartAttribute.INSTANCE.getAttribute("minYAxis")));
            }
        }

        /*
        段索引推断：如果最后节点段索引不存在，必定是添加到最后
         */
        segmentIndex = segmentIndex == -1 ? classNameSegments.size() - 1 : segmentIndex;

        /*
        处理Echarts关系图图表数据
         */
        EchartsRelationChartData relationChartData = new EchartsRelationChartData();
        relationChartData.setSymbolSize(Integer.valueOf(ChartAttribute.INSTANCE.getAttribute("symbolSize")));
        relationChartData.setCoordinateAxis(
            Spaces.create(
                Integer.valueOf(ChartAttribute.INSTANCE.getAttribute("maxXAxis")) +
                (segmentIndex << Integer.valueOf(ChartAttribute.INSTANCE.getAttribute("offsetXAxis"))),
                classNameYSegments.set(
                    segmentIndex,
                    classNameYSegments.get(segmentIndex) +
                    Integer.valueOf(ChartAttribute.INSTANCE.getAttribute("offsetYAxis"))
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
