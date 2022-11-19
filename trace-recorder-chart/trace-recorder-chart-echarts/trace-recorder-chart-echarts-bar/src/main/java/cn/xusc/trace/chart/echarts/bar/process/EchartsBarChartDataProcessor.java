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
package cn.xusc.trace.chart.echarts.bar.process;

import cn.xusc.trace.chart.Chart;
import cn.xusc.trace.chart.ChartData;
import cn.xusc.trace.chart.ChartDataProcessor;
import cn.xusc.trace.chart.echarts.bar.data.EchartsBarChartData;
import java.util.Objects;
import java.util.Optional;

/**
 * Echarts柱状图图表数据处理者
 *
 * @author WangCai
 * @since 2.5.2
 */
public class EchartsBarChartDataProcessor extends ChartDataProcessor {

    /**
     * 上一个Echarts柱状图图表数据
     */
    private EchartsBarChartData preBarChartData;

    /**
     * 基础构造
     *
     * @param chart 图表
     */
    public EchartsBarChartDataProcessor(Chart chart) {
        super(chart);
    }

    @Override
    protected ChartData processStandardChartData(ChartData chartData) {
        /*
        处理Echarts柱状图图表数据
         */
        EchartsBarChartData barChartData = new EchartsBarChartData();
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
        Optional<EchartsBarChartData> chartDataOptional = findCurrentChartData(chartData);
        chartDataOptional.ifPresentOrElse(
            echartsBarChartData -> {
                EchartsBarChartData needIncrementChartData = chartDataOptional.get();
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
     * 构建可刷新的柱状图数据
     *
     * @return 可刷新的柱状图数据
     */
    private EchartsBarChartData buildRefreshableBarChartData() {
        EchartsBarChartData currentBarChartData = preBarChartData;
        EchartsBarChartData refreshableBarChartData = new EchartsBarChartData();
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
     * 查找可选的当前柱状图图表数据
     *
     * @param chartData 当前柱状图图表数据
     * @return 可选的当前柱状图图表数据
     */
    private Optional<EchartsBarChartData> findCurrentChartData(ChartData chartData) {
        EchartsBarChartData needFindChartData = preBarChartData;
        do {
            if (Objects.equals(chartData.className(), needFindChartData.className())) {
                return Optional.of(needFindChartData);
            }
        } while (Objects.nonNull(needFindChartData = (EchartsBarChartData) needFindChartData.nextChartData()));
        return Optional.empty();
    }
}
