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

import cn.xusc.trace.core.EnhanceInfo;
import java.nio.file.Path;

/**
 * 跟踪仪表盘图表接口
 *
 * @author WangCai
 * @since 2.5.3
 */
public interface TraceDashboardChart {
    /**
     * 初始化
     *
     * @param componentGeneratePath 组件生成路径
     * @return 初始化详情
     */
    boolean init(Path componentGeneratePath);

    /**
     * 处理增强信息
     *
     * @param eInfo 增强信息
     * @return 处理详情
     */
    boolean handleEnhanceInfo(EnhanceInfo eInfo);

    /**
     * 获取跟踪仪表盘图表配置
     *
     * @return 跟踪仪表盘图表配置
     */
    AbstractTraceDashboardChartConfig config();

    /**
     * 获取跟踪仪表盘图表属性
     *
     * @return 跟踪仪表盘图表属性
     */
    TraceDashboardChartAttribute chartAttribute();

    /**
     * 获取跟踪仪表盘图表数据存储库
     *
     * @return 跟踪仪表盘图表数据存储库
     */
    TraceDashboardChartDataRepository chartDataRepository();

    /**
     * 填充跟踪仪表盘图表数据
     *
     * @param chartData 跟踪仪表盘图表数据
     * @return 填充情况
     */
    boolean fillData(TraceDashboardChartData chartData);

    /**
     * 获取跟踪仪表盘图表数据
     *
     * @return 跟踪仪表盘图表数据
     */
    TraceDashboardChartData data();

    /**
     * 渲染图
     *
     * @return 渲染时的数据
     */
    TraceDashboardChartData render();

    /**
     * 显示图表
     */
    void show();
}
