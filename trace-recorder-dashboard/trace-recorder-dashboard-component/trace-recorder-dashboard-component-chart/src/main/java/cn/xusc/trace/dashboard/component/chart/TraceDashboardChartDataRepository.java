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

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * 跟踪仪表盘图表数据存储库
 *
 * @author WangCai
 * @since 2.5.3
 */
class TraceDashboardChartDataRepository {

    /**
     * 跟踪仪表盘标准图表数据队列
     */
    private final BlockingQueue<TraceDashboardChartData> STANDARD_CHART_DATA_QUEUE = new ArrayBlockingQueue<>(1024);

    /**
     * 填充图表数据到队列
     *
     * @param chartData 图表数据
     * @return 填充详情
     * @throws InterruptedException if some property of the specified element prevents it from being added to this queue.
     */
    public boolean put(TraceDashboardChartData chartData) throws InterruptedException {
        STANDARD_CHART_DATA_QUEUE.put(chartData);
        return true;
    }

    /**
     * 提取队列图表数据
     *
     * @return 图表数据
     * @throws InterruptedException if interrupted while waiting.
     */
    public TraceDashboardChartData take() throws InterruptedException {
        return STANDARD_CHART_DATA_QUEUE.take();
    }
}
