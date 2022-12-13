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

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 跟踪仪表盘图表数据持续显示者
 *
 * @author WangCai
 * @since 2.5.3
 */
class TraceDashboardChartDataContinueShower extends Thread {

    /**
     * 跟踪仪表盘图表
     */
    private final TraceDashboardChart CHART;

    /**
     * 基础构造
     *
     * @param chart 跟踪仪表盘图表
     */
    public TraceDashboardChartDataContinueShower(TraceDashboardChart chart) {
        super("traceDashboard_ChartDataContinueShower");
        CHART = chart;
    }

    @Override
    public void run() {
        TraceDashboardChartData chartData = CHART.data();
        while (true) {
            try {
                TimeUnit.MILLISECONDS.sleep(300);
                if (Objects.equals(chartData, CHART.data())) {
                    continue;
                }
                chartData = CHART.render();
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
