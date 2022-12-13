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
package cn.xusc.trace.dashboard.component.chart.echarts.relation;

import cn.xusc.trace.common.util.Systems;
import cn.xusc.trace.core.TraceRecorderEnvironment;
import cn.xusc.trace.core.util.TraceRecorders;
import cn.xusc.trace.dashboard.TraceDashboardComponentResource;
import cn.xusc.trace.dashboard.annotation.TraceDashboardComponentOrder;
import cn.xusc.trace.dashboard.component.chart.*;
import cn.xusc.trace.dashboard.component.chart.constant.TraceDashboardChartRefreshStrategy;
import cn.xusc.trace.dashboard.component.chart.echarts.relation.config.TraceDashboardEchartsRelationChartConfig;
import cn.xusc.trace.dashboard.component.chart.echarts.relation.constant.Temporary;
import cn.xusc.trace.dashboard.component.chart.echarts.relation.mapping.TraceDashboardEchartsRelation;
import cn.xusc.trace.dashboard.component.chart.echarts.relation.process.TraceDashboardEchartsRelationChartDataProcessor;
import cn.xusc.trace.dashboard.component.chart.echarts.relation.resource.TraceDashboardEchartsRelationServerResource;
import cn.xusc.trace.dashboard.component.chart.echarts.relation.step.TraceDashboardEchartsRelationDataChartProcessStep;
import cn.xusc.trace.dashboard.component.chart.echarts.relation.step.TraceDashboardEchartsRelationJsonChartProcessStep;
import cn.xusc.trace.dashboard.component.chart.generate.TraceDashboardChartEnvironmentGenerator;
import cn.xusc.trace.dashboard.component.chart.resource.BaseTraceDashboardChartServerResource;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 * 跟踪仪表盘Echarts关系图图表
 *
 * @author WangCai
 * @since 2.5.3
 */
@TraceDashboardComponentOrder(1)
@Slf4j
public class TraceDashboardEchartsRelationChart extends AbstractTraceDashboardChart {

    /**
     * 基础构造
     */
    public TraceDashboardEchartsRelationChart() {
        super();
    }

    @Override
    protected AbstractTraceDashboardChartConfig initTraceDashboardChartConfig() {
        TraceRecorderEnvironment environment = TraceRecorders.get().environment();
        return deduceTraceDashboardEchartsRelationChartConfig(environment);
    }

    @Override
    public void initTraceDashboardChartAttribute() {
        TraceDashboardEchartsRelationChartConfig config = (TraceDashboardEchartsRelationChartConfig) chartConfig;
        TraceDashboardEchartsRelationChartConfig.RelationAttribute relationAttribute = config.getRelationAttribute();
        chartAttribute.setAttribute("symbolSize", relationAttribute.getSymbolSize());
        chartAttribute.setAttribute("maxXAxis", relationAttribute.getMaxXAxis());
        chartAttribute.setAttribute("offsetXAxis", relationAttribute.getOffsetXAxis());
        chartAttribute.setAttribute("offsetYAxis", relationAttribute.getOffsetYAxis());
        chartAttribute.setAttribute("minYAxis", relationAttribute.getMinYAxis());
    }

    @Override
    protected void initTraceDashboardChartEnvironment() throws Exception {
        new TraceDashboardChartEnvironmentGenerator()
            .generateEnvironment(this, chartConfig, TraceDashboardEchartsRelation.builder().build());
    }

    @Override
    protected void initTraceDashboardCharProcessSteps(TraceDashboardChartFlow chartFlow) {
        chartFlow.addChartProcessStep(
            new TraceDashboardEchartsRelationDataChartProcessStep(),
            new TraceDashboardEchartsRelationJsonChartProcessStep()
        );
    }

    @Override
    protected TraceDashboardChartDataProcessor initTraceDashboardChartDataProcessor(TraceDashboardChart chart) {
        return new TraceDashboardEchartsRelationChartDataProcessor(chart);
    }

    /**
     * 推断跟踪仪表盘Echarts关系图图表配置
     *
     * @param environment 跟踪记录仪环境
     * @return 跟踪仪表盘Echarts关系图图表配置
     */
    private TraceDashboardEchartsRelationChartConfig deduceTraceDashboardEchartsRelationChartConfig(
        TraceRecorderEnvironment environment
    ) {
        return TraceDashboardEchartsRelationChartConfig
            .builder()
            .chartRefreshStrategy(
                TraceDashboardChartRefreshStrategy.valueOf(
                    (String) environment
                        .get(
                            TraceDashboardEchartsRelationChartConfig.CONFIG_CLASSNAME
                                .concat(".")
                                .concat("chartRefreshStrategy")
                        )
                        .orElse("TIMELY")
                )
            )
            .generatePath(
                Path.of(
                    (String) environment
                        .get(
                            TraceDashboardEchartsRelationChartConfig.CONFIG_CLASSNAME.concat(".").concat("generatePath")
                        )
                        .orElse(Systems.getClassPaths(classPath -> Files.isDirectory(Paths.get(classPath)))[0])
                )
            )
            .homePath(
                cn.xusc.trace.dashboard.component.chart.constant.Temporary.TEMPLATE_PATH.resolve("echarts/relation")
            )
            .specificGenerateChartPath(Path.of("echarts/relation"))
            .jsonFilePath(Path.of("echarts/relation/data/relation.json"))
            .relationAttribute(contextRelationAttribute(environment))
            .build()
            .buildCompleteVerify();
    }

    /**
     * 获取上下文跟踪仪表盘图表属性
     *
     * @param environment 跟踪记录仪环境
     * @return 跟踪仪表盘关系图属性
     */
    private TraceDashboardEchartsRelationChartConfig.RelationAttribute contextRelationAttribute(
        TraceRecorderEnvironment environment
    ) {
        return TraceDashboardEchartsRelationChartConfig.RelationAttribute
            .builder()
            .symbolSize(
                (String) environment
                    .get(
                        TraceDashboardEchartsRelationChartConfig.RelationAttribute.ATTRIBUTE_CONFIG_CLASSNAME
                            .concat(".")
                            .concat("symbolSize")
                    )
                    .orElse("20")
            )
            .maxXAxis(
                (String) environment
                    .get(
                        TraceDashboardEchartsRelationChartConfig.RelationAttribute.ATTRIBUTE_CONFIG_CLASSNAME
                            .concat(".")
                            .concat("maxXAxis")
                    )
                    .orElse("100")
            )
            .offsetXAxis(
                (String) environment
                    .get(
                        TraceDashboardEchartsRelationChartConfig.RelationAttribute.ATTRIBUTE_CONFIG_CLASSNAME
                            .concat(".")
                            .concat("offsetXAxis")
                    )
                    .orElse("4")
            )
            .offsetYAxis(
                (String) environment
                    .get(
                        TraceDashboardEchartsRelationChartConfig.RelationAttribute.ATTRIBUTE_CONFIG_CLASSNAME
                            .concat(".")
                            .concat("offsetYAxis")
                    )
                    .orElse("2")
            )
            .minYAxis(
                (String) environment
                    .get(
                        TraceDashboardEchartsRelationChartConfig.RelationAttribute.ATTRIBUTE_CONFIG_CLASSNAME
                            .concat(".")
                            .concat("minYAxis")
                    )
                    .orElse("100")
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
        return "EchartsRelationChart";
    }

    @Override
    public String componentChineseName() {
        return "Echarts关系图";
    }

    @Override
    public String componentAccessPath() {
        return Temporary.ACCESS_REQUEST_PATH;
    }

    @Override
    public List<TraceDashboardComponentResource> resources() {
        Path generatePath = ((TraceDashboardEchartsRelationChartConfig) config()).getSpecificGeneratePath();
        return List.of(
            new BaseTraceDashboardChartServerResource(generatePath),
            new TraceDashboardEchartsRelationServerResource(generatePath)
        );
    }
}
