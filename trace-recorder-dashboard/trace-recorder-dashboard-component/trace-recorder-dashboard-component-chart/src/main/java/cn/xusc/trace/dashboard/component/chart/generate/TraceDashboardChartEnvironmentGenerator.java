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
package cn.xusc.trace.dashboard.component.chart.generate;

import cn.xusc.trace.dashboard.component.chart.AbstractTraceDashboardChart;
import cn.xusc.trace.dashboard.component.chart.AbstractTraceDashboardChartConfig;
import cn.xusc.trace.dashboard.component.chart.TraceDashboardChartProcessStep;
import cn.xusc.trace.dashboard.component.chart.step.BaseJsonTraceDashboardChartProcessStep;

/**
 * 跟踪仪表盘图表环境生成器
 *
 * @author WangCai
 * @since 2.5.3
 */
public class TraceDashboardChartEnvironmentGenerator {

    /**
     * 基础构造
     */
    public TraceDashboardChartEnvironmentGenerator() {}

    /**
     * 生成跟踪仪表盘文件环境
     *
     * @param chart  跟踪仪表盘图表
     * @param config 仪表盘图表配置
     * @param value  数据值
     * @return 生成目录的路径
     * @throws Exception if execute happen any question.
     */
    public boolean generateEnvironment(
        AbstractTraceDashboardChart chart,
        AbstractTraceDashboardChartConfig config,
        Object value
    ) throws Exception {
        new InnerBaseJsonTraceDashboardChartProcessStep()
            .run(chart, config, new TraceDashboardChartProcessStep.ValuePipeline<>(value));
        return true;
    }

    /**
     * 内部基础json化跟踪仪表盘图表处理步骤
     */
    private final class InnerBaseJsonTraceDashboardChartProcessStep extends BaseJsonTraceDashboardChartProcessStep {

        /**
         * 基础构造
         */
        public InnerBaseJsonTraceDashboardChartProcessStep() {
            super();
        }
    }
}
