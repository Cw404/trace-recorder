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
package cn.xusc.trace.chart.echarts.relation;

import cn.xusc.trace.chart.*;
import cn.xusc.trace.chart.constant.ChartRefreshStrategy;
import cn.xusc.trace.chart.constant.Temporary;
import cn.xusc.trace.chart.echarts.relation.config.EchartsRelationChartConfig;
import cn.xusc.trace.chart.echarts.relation.process.EchartsRelationChartDataProcessor;
import cn.xusc.trace.chart.echarts.relation.resource.EchartsRelationServerResource;
import cn.xusc.trace.chart.echarts.relation.step.EchartsRelationDataChartProcessStep;
import cn.xusc.trace.chart.echarts.relation.step.EchartsRelationJsonChartProcessStep;
import cn.xusc.trace.chart.resource.BaseChartServerResource;
import cn.xusc.trace.common.util.Systems;
import cn.xusc.trace.core.TraceRecorderEnvironment;
import cn.xusc.trace.core.util.TraceRecorders;
import cn.xusc.trace.server.tomcat.TomcatServer;
import cn.xusc.trace.server.tomcat.config.TomcatServerConfig;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Echarts关系图图表
 *
 * @author WangCai
 * @since 2.5
 */
public class EchartsRelationChart extends AbstractChart {

    /**
     * 基础构造
     */
    public EchartsRelationChart() {
        super();
    }

    @Override
    protected AbstractChartConfig initChartConfig() {
        TraceRecorderEnvironment environment = TraceRecorders.get().environment();
        return deduceEchartsRelationChartConfig(environment);
    }

    @Override
    public void initChartAttribute() {
        EchartsRelationChartConfig chartConfig = (EchartsRelationChartConfig) CHART_CONFIG;
        EchartsRelationChartConfig.RelationAttribute relationAttribute = chartConfig.getRelationAttribute();
        ChartAttribute.INSTANCE.setAttribute("symbolSize", relationAttribute.getSymbolSize());
        ChartAttribute.INSTANCE.setAttribute("maxXAxis", relationAttribute.getMaxXAxis());
        ChartAttribute.INSTANCE.setAttribute("offsetXAxis", relationAttribute.getMaxXAxis());
        ChartAttribute.INSTANCE.setAttribute("offsetYAxis", relationAttribute.getOffsetYAxis());
        ChartAttribute.INSTANCE.setAttribute("minYAxis", relationAttribute.getMinYAxis());
    }

    @Override
    protected void initCharProcessSteps(ChartFlow chartFlow) {
        chartFlow.addChartProcessStep(
            new EchartsRelationDataChartProcessStep(),
            new EchartsRelationJsonChartProcessStep()
        );
    }

    @Override
    protected ChartDataProcessor initChartDataProcessor(Chart chart) {
        return new EchartsRelationChartDataProcessor(chart);
    }

    @Override
    protected void openServerShow() {
        Path generatePath =
            ((EchartsRelationChartConfig) CHART_CONFIG).getGeneratePath().resolve(Temporary.GENERATE_PATH);

        new TomcatServer(
            TomcatServerConfig
                .builder()
                .resources(
                    List.of(new BaseChartServerResource(generatePath), new EchartsRelationServerResource(generatePath))
                )
                .accessRequestPaths(new String[] { "/", "/relation" })
                .closeRequestPath(new String[] { "/close" })
                .build()
        )
            .start();
    }

    /**
     * 推断Echarts关系图图表配置
     *
     * @param environment 跟踪记录仪环境
     * @return Echarts关系图图表配置
     */
    private EchartsRelationChartConfig deduceEchartsRelationChartConfig(TraceRecorderEnvironment environment) {
        return EchartsRelationChartConfig
            .builder()
            .chartRefreshStrategy(
                ChartRefreshStrategy.valueOf(
                    (String) environment
                        .get(EchartsRelationChartConfig.CONFIG_CLASSNAME.concat(".").concat("chartRefreshStrategy"))
                        .orElse("TIMELY")
                )
            )
            .generatePath(
                Path.of(
                    (String) environment
                        .get(EchartsRelationChartConfig.CONFIG_CLASSNAME.concat(".").concat("generatePath"))
                        .orElse(Systems.getClassPaths(classPath -> Files.isDirectory(Paths.get(classPath)))[0])
                )
            )
            .relationAttribute(contextRelationAttribute(environment))
            .build();
    }

    /**
     * 获取上下文图表属性
     *
     * @param environment 跟踪记录仪环境
     * @return 关系图属性
     */
    private EchartsRelationChartConfig.RelationAttribute contextRelationAttribute(
        TraceRecorderEnvironment environment
    ) {
        return EchartsRelationChartConfig.RelationAttribute
            .builder()
            .symbolSize(
                (String) environment
                    .get(
                        EchartsRelationChartConfig.RelationAttribute.ATTRIBUTE_CONFIG_CLASSNAME
                            .concat(".")
                            .concat("symbolSize")
                    )
                    .orElse("20")
            )
            .maxXAxis(
                (String) environment
                    .get(
                        EchartsRelationChartConfig.RelationAttribute.ATTRIBUTE_CONFIG_CLASSNAME
                            .concat(".")
                            .concat("maxXAxis")
                    )
                    .orElse("100")
            )
            .offsetXAxis(
                (String) environment
                    .get(
                        EchartsRelationChartConfig.RelationAttribute.ATTRIBUTE_CONFIG_CLASSNAME
                            .concat(".")
                            .concat("offsetXAxis")
                    )
                    .orElse("4")
            )
            .offsetYAxis(
                (String) environment
                    .get(
                        EchartsRelationChartConfig.RelationAttribute.ATTRIBUTE_CONFIG_CLASSNAME
                            .concat(".")
                            .concat("offsetYAxis")
                    )
                    .orElse("2")
            )
            .minYAxis(
                (String) environment
                    .get(
                        EchartsRelationChartConfig.RelationAttribute.ATTRIBUTE_CONFIG_CLASSNAME
                            .concat(".")
                            .concat("minYAxis")
                    )
                    .orElse("100")
            )
            .build();
    }
}
