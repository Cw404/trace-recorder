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

import cn.xusc.trace.common.util.FastList;
import java.util.List;

/**
 * 跟踪仪表盘图表处理流程
 *
 * @author WangCai
 * @since 2.5.3
 */
public class TraceDashboardChartFlow {

    /**
     * 跟踪仪表盘图表处理步骤列表
     */
    private final List<TraceDashboardChartProcessStep> CHART_PROCESS_STEPS = new FastList<>(
        TraceDashboardChartProcessStep.class
    );

    /**
     * 添加一个跟踪仪表盘图表处理步骤
     *
     * @param chartProcessStep 跟踪仪表盘图表处理步骤
     * @return 添加详情
     */
    public boolean addChartProcessStep(TraceDashboardChartProcessStep chartProcessStep) {
        return CHART_PROCESS_STEPS.add(chartProcessStep);
    }

    /**
     * 添加多个跟踪仪表盘图表处理步骤
     *
     * @param chartProcessSteps 跟踪仪表盘图表处理步骤集
     * @return 添加详情
     */
    public boolean addChartProcessStep(TraceDashboardChartProcessStep... chartProcessSteps) {
        for (TraceDashboardChartProcessStep chartProcessStep : chartProcessSteps) {
            addChartProcessStep(chartProcessStep);
        }
        return true;
    }

    /**
     * 获取跟踪仪表盘图表处理流程
     *
     * @return 跟踪仪表盘图表处理流程
     */
    public List<TraceDashboardChartProcessStep> flow() {
        return CHART_PROCESS_STEPS;
    }
}
