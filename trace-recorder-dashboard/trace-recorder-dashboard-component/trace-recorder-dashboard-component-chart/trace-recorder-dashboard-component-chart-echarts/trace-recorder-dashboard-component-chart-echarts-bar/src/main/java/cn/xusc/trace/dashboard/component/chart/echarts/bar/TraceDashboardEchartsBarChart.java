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
package cn.xusc.trace.dashboard.component.chart.echarts.bar;

import cn.xusc.trace.common.util.Systems;
import cn.xusc.trace.core.TraceRecorderEnvironment;
import cn.xusc.trace.core.util.TraceRecorders;
import cn.xusc.trace.dashboard.TraceDashboardComponentResource;
import cn.xusc.trace.dashboard.annotation.TraceDashboardComponentOrder;
import cn.xusc.trace.dashboard.component.chart.*;
import cn.xusc.trace.dashboard.component.chart.constant.TraceDashboardChartRefreshStrategy;
import cn.xusc.trace.dashboard.component.chart.echarts.bar.config.TraceDashboardEchartsBarChartConfig;
import cn.xusc.trace.dashboard.component.chart.echarts.bar.constant.Temporary;
import cn.xusc.trace.dashboard.component.chart.echarts.bar.mapping.TraceDashboardEchartsBar;
import cn.xusc.trace.dashboard.component.chart.echarts.bar.process.TraceDashboardEchartsBarChartDataProcessor;
import cn.xusc.trace.dashboard.component.chart.echarts.bar.resource.TraceDashboardEchartsBarServerResource;
import cn.xusc.trace.dashboard.component.chart.echarts.bar.step.TraceDashboardEchartsBarDataChartProcessStep;
import cn.xusc.trace.dashboard.component.chart.echarts.bar.step.TraceDashboardEchartsBarJsonChartProcessStep;
import cn.xusc.trace.dashboard.component.chart.generate.TraceDashboardChartEnvironmentGenerator;
import cn.xusc.trace.dashboard.component.chart.resource.BaseTraceDashboardChartServerResource;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 * 跟踪仪表盘Echarts柱状图图表
 *
 * @author WangCai
 * @since 2.5.3
 */
@TraceDashboardComponentOrder(2)
@Slf4j
public class TraceDashboardEchartsBarChart extends AbstractTraceDashboardChart {

    /**
     * 基础构造
     */
    public TraceDashboardEchartsBarChart() {
        super();
    }

    @Override
    protected AbstractTraceDashboardChartConfig initTraceDashboardChartConfig() {
        TraceRecorderEnvironment environment = TraceRecorders.get().environment();
        return deduceTraceDashboardEchartsBarChartConfig(environment);
    }

    @Override
    public void initTraceDashboardChartAttribute() {
        TraceDashboardEchartsBarChartConfig config = (TraceDashboardEchartsBarChartConfig) chartConfig;
        TraceDashboardEchartsBarChartConfig.BarAttribute barAttribute = config.getBarAttribute();
        chartAttribute.setAttribute("beautifyInterval", barAttribute.getBeautifyInterval().toString());
    }

    @Override
    protected void initTraceDashboardChartEnvironment() throws Exception {
        new TraceDashboardChartEnvironmentGenerator()
            .generateEnvironment(this, chartConfig, TraceDashboardEchartsBar.builder().build());
    }

    @Override
    protected void initTraceDashboardCharProcessSteps(TraceDashboardChartFlow chartFlow) {
        chartFlow.addChartProcessStep(
            new TraceDashboardEchartsBarDataChartProcessStep(),
            new TraceDashboardEchartsBarJsonChartProcessStep()
        );
    }

    @Override
    protected TraceDashboardChartDataProcessor initTraceDashboardChartDataProcessor(TraceDashboardChart chart) {
        return new TraceDashboardEchartsBarChartDataProcessor(chart);
    }

    /**
     * 推断跟踪仪表盘Echarts柱状图图表配置
     *
     * @param environment 跟踪记录仪环境
     * @return 跟踪仪表盘Echarts柱状图图表配置
     */
    private TraceDashboardEchartsBarChartConfig deduceTraceDashboardEchartsBarChartConfig(
        TraceRecorderEnvironment environment
    ) {
        return TraceDashboardEchartsBarChartConfig
            .builder()
            .chartRefreshStrategy(
                TraceDashboardChartRefreshStrategy.valueOf(
                    (String) environment
                        .get(
                            TraceDashboardEchartsBarChartConfig.CONFIG_CLASSNAME
                                .concat(".")
                                .concat("chartRefreshStrategy")
                        )
                        .orElse("TIMELY")
                )
            )
            .generatePath(
                Path.of(
                    (String) environment
                        .get(TraceDashboardEchartsBarChartConfig.CONFIG_CLASSNAME.concat(".").concat("generatePath"))
                        .orElse(Systems.getClassPaths(classPath -> Files.isDirectory(Paths.get(classPath)))[0])
                )
            )
            .homePath(cn.xusc.trace.dashboard.component.chart.constant.Temporary.TEMPLATE_PATH.resolve("echarts/bar"))
            .specificGenerateChartPath(Path.of("echarts/bar"))
            .jsonFilePath(Path.of("echarts/bar/data/bar.json"))
            .barAttribute(contextBarAttribute(environment))
            .build()
            .buildCompleteVerify();
    }

    /**
     * 获取上下文跟踪仪表盘图表属性
     *
     * @param environment 跟踪记录仪环境
     * @return 跟踪仪表盘柱状图属性
     */
    private TraceDashboardEchartsBarChartConfig.BarAttribute contextBarAttribute(TraceRecorderEnvironment environment) {
        return TraceDashboardEchartsBarChartConfig.BarAttribute
            .builder()
            .beautifyInterval(
                Boolean.valueOf(
                    (String) environment
                        .get(
                            TraceDashboardEchartsBarChartConfig.BarAttribute.ATTRIBUTE_CONFIG_CLASSNAME
                                .concat(".")
                                .concat("beautifyInterval")
                        )
                        .orElse("true")
                )
            )
            .build();
    }

    @Override
    public String groupName() {
        return "Echarts";
    }

    @Override
    public String groupChineseName() {
        return "Echarts";
    }

    @Override
    public String componentName() {
        return "EchartsBarChart";
    }

    @Override
    public String componentChineseName() {
        return "Echarts柱状图";
    }

    @Override
    public String componentAccessPath() {
        return Temporary.ACCESS_REQUEST_PATH;
    }

    @Override
    public List<TraceDashboardComponentResource> resources() {
        Path generatePath = ((TraceDashboardEchartsBarChartConfig) config()).getSpecificGeneratePath();
        return List.of(
            new BaseTraceDashboardChartServerResource(generatePath),
            new TraceDashboardEchartsBarServerResource(generatePath)
        );
    }
}
