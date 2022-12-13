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
package cn.xusc.trace.dashboard.component.chart;

/**
 * 跟踪仪表盘图表数据处理者
 *
 * @author WangCai
 * @since 2.5.3
 */
public abstract class TraceDashboardChartDataProcessor extends Thread {

    /**
     * 跟踪仪表盘图表
     */
    protected final TraceDashboardChart CHART;

    /**
     * 基础构造
     *
     * @param chart 跟踪仪表盘图表
     */
    public TraceDashboardChartDataProcessor(TraceDashboardChart chart) {
        super("traceDashboard_ChartDataProcessor");
        CHART = chart;
    }

    @Override
    public void run() {
        while (true) {
            /*
            提取标准化跟踪仪表盘图表数据
             */
            TraceDashboardChartData standardChartData;
            try {
                standardChartData = CHART.chartDataRepository().take();
            } catch (InterruptedException e) {
                break;
            }

            TraceDashboardChartData processedChartData = processStandardChartData(standardChartData);
            CHART.fillData(processedChartData);
        }
    }

    /**
     * 处理标准跟踪仪表盘图表数据
     *
     * @param chartData 标准跟踪仪表盘图表数据
     * @return 处理后相关性跟踪仪表盘图表数据
     */
    protected abstract TraceDashboardChartData processStandardChartData(TraceDashboardChartData chartData);
}
