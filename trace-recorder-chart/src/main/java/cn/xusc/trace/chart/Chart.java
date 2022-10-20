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
package cn.xusc.trace.chart;

/**
 * 图表接口
 *
 * @author WangCai
 * @since 2.5
 */
public interface Chart {
    /**
     * 获取图表配置
     *
     * @return 图表配置
     */
    AbstractChartConfig config();

    /**
     * 填充图表数据
     *
     * @param chartData 图表数据
     * @return 填充情况
     */
    boolean fillData(ChartData chartData);

    /**
     * 获取图表数据
     *
     * @return 图表数据
     */
    ChartData data();

    /**
     * 渲染图
     *
     * @return 渲染时的数据
     */
    ChartData render();

    /**
     * 显示图表
     */
    void show();
}
