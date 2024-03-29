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
package cn.xusc.trace.dashboard.component.chart.echarts.bar.step;

import cn.xusc.trace.dashboard.component.chart.AbstractTraceDashboardChart;
import cn.xusc.trace.dashboard.component.chart.AbstractTraceDashboardChartConfig;
import cn.xusc.trace.dashboard.component.chart.TraceDashboardChartProcessStep;
import cn.xusc.trace.dashboard.component.chart.echarts.bar.data.TraceDashboardEchartsBarChartData;
import cn.xusc.trace.dashboard.component.chart.echarts.bar.mapping.TraceDashboardEchartsBar;
import java.util.*;

/**
 * 跟踪仪表盘Echarts柱状图数据图表处理步骤
 *
 * @author WangCai
 * @since 2.5.3
 */
public class TraceDashboardEchartsBarDataChartProcessStep implements TraceDashboardChartProcessStep {

    /**
     * 基础构造
     */
    public TraceDashboardEchartsBarDataChartProcessStep() {}

    @Override
    public <T> void run(
        AbstractTraceDashboardChart chart,
        AbstractTraceDashboardChartConfig config,
        TraceDashboardChartProcessStep.ValuePipeline<T> valuePipeline
    ) throws Exception {
        TraceDashboardEchartsBarChartData barChartData = (TraceDashboardEchartsBarChartData) valuePipeline.getValue();

        Deque<TraceDashboardEchartsBarChartData> tempChartsData = new LinkedList<>();
        do {
            tempChartsData.addFirst(barChartData);
        } while (Objects.nonNull(barChartData = (TraceDashboardEchartsBarChartData) barChartData.nextChartData()));

        List<String> xAxisData = new ArrayList<>();
        List<Integer> seriesData = new ArrayList<>();
        for (TraceDashboardEchartsBarChartData chartData : tempChartsData) {
            xAxisData.add(chartData.className());

            seriesData.add(chartData.getCounter());
        }
        boolean beautifyInterval = Boolean.parseBoolean(chart.chartAttribute().getAttribute("beautifyInterval"));
        TraceDashboardEchartsBar bar = TraceDashboardEchartsBar
            .builder()
            .xAxisData(xAxisData)
            .seriesData(seriesData)
            .beautifyInterval(beautifyInterval ? deduceBeautifyInterval(xAxisData.size()) : 0)
            .build();

        valuePipeline.setValue((T) bar);
    }

    /**
     * 推断美化间隔
     *
     * <table border="1">
     * <caption>美化间隔描述</caption>
     * <tr><th>x轴数据数量</th><th>美化间隔</th></tr>
     * <tr><td>size <= 8</td><td>0</td></tr>
     * <tr><td>size > 8 && 奇数</td><td>1</td></tr>
     * <tr><td>size > 8 && 偶数</td><td>2</td></tr>
     * </table>
     *
     * @param xAxisDataSize x轴数据数量
     * @return 美化间隔
     */
    private short deduceBeautifyInterval(int xAxisDataSize) {
        short beautifyInterval = 0;
        if (xAxisDataSize > 8) {
            if ((xAxisDataSize & 1) == 1) {
                /*
                奇数
                 */
                beautifyInterval = 1;
            } else {
                /*
                偶数
                 */
                beautifyInterval = 2;
            }
        }
        return beautifyInterval;
    }
}
