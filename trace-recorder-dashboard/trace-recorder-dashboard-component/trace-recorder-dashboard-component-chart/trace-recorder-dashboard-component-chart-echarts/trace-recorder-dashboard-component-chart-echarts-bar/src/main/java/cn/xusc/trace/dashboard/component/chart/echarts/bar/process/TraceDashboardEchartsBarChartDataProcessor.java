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
package cn.xusc.trace.dashboard.component.chart.echarts.bar.process;

import cn.xusc.trace.dashboard.component.chart.TraceDashboardChart;
import cn.xusc.trace.dashboard.component.chart.TraceDashboardChartData;
import cn.xusc.trace.dashboard.component.chart.TraceDashboardChartDataProcessor;
import cn.xusc.trace.dashboard.component.chart.echarts.bar.data.TraceDashboardEchartsBarChartData;
import java.util.Objects;
import java.util.Optional;

/**
 * 跟踪仪表盘Echarts柱状图图表数据处理者
 *
 * @author WangCai
 * @since 2.5.3
 */
public class TraceDashboardEchartsBarChartDataProcessor extends TraceDashboardChartDataProcessor {

    /**
     * 上一个跟踪仪表盘Echarts柱状图图表数据
     */
    private TraceDashboardEchartsBarChartData preBarChartData;

    /**
     * 基础构造
     *
     * @param chart 跟踪仪表盘图表
     */
    public TraceDashboardEchartsBarChartDataProcessor(TraceDashboardChart chart) {
        super(chart);
    }

    @Override
    protected TraceDashboardChartData processStandardChartData(TraceDashboardChartData chartData) {
        /*
        处理Echarts柱状图图表数据
         */
        TraceDashboardEchartsBarChartData barChartData = new TraceDashboardEchartsBarChartData();
        barChartData.setCounter(1);
        barChartData.setThreadName(chartData.threadName());
        barChartData.setClassName(chartData.className());
        barChartData.setMethodName(chartData.methodName());
        barChartData.setLineNumber(chartData.lineNumber());
        barChartData.setInfo(chartData.info());
        barChartData.setStackTraceElements(null);

        if (Objects.isNull(preBarChartData)) {
            return preBarChartData = barChartData;
        }

        /*
        查找当前匹配的图表数据
            1、存在对计数器进行自增、构建可刷新的柱状图数据（持续显示）
            2、不存在链接图表数据
         */
        Optional<TraceDashboardEchartsBarChartData> chartDataOptional = findCurrentChartData(chartData);
        chartDataOptional.ifPresentOrElse(
            echartsBarChartData -> {
                TraceDashboardEchartsBarChartData needIncrementChartData = chartDataOptional.get();
                needIncrementChartData.setCounter(needIncrementChartData.getCounter() + 1);
                preBarChartData = buildRefreshableBarChartData();
            },
            () -> {
                barChartData.setNextChartData(preBarChartData);
                preBarChartData = barChartData;
            }
        );

        return preBarChartData;
    }

    /**
     * 构建可刷新的跟踪仪表盘柱状图数据
     *
     * @return 可刷新的跟踪仪表盘柱状图数据
     */
    private TraceDashboardEchartsBarChartData buildRefreshableBarChartData() {
        TraceDashboardEchartsBarChartData currentBarChartData = preBarChartData;
        TraceDashboardEchartsBarChartData refreshableBarChartData = new TraceDashboardEchartsBarChartData();
        refreshableBarChartData.setCounter(currentBarChartData.getCounter());
        refreshableBarChartData.setThreadName(currentBarChartData.threadName());
        refreshableBarChartData.setClassName(currentBarChartData.className());
        refreshableBarChartData.setMethodName(currentBarChartData.methodName());
        refreshableBarChartData.setLineNumber(currentBarChartData.lineNumber());
        refreshableBarChartData.setInfo(currentBarChartData.info());
        refreshableBarChartData.setStackTraceElements(null);
        /*
        链接前面的柱状图数据
         */
        refreshableBarChartData.setNextChartData(currentBarChartData.nextChartData());
        return refreshableBarChartData;
    }

    /**
     * 查找可选的当前跟踪仪表盘柱状图图表数据
     *
     * @param chartData 当前跟踪仪表盘柱状图图表数据
     * @return 可选的当前跟踪仪表盘柱状图图表数据
     */
    private Optional<TraceDashboardEchartsBarChartData> findCurrentChartData(TraceDashboardChartData chartData) {
        TraceDashboardEchartsBarChartData needFindChartData = preBarChartData;
        do {
            if (Objects.equals(chartData.className(), needFindChartData.className())) {
                return Optional.of(needFindChartData);
            }
        } while (
            Objects.nonNull(needFindChartData = (TraceDashboardEchartsBarChartData) needFindChartData.nextChartData())
        );
        return Optional.empty();
    }
}
