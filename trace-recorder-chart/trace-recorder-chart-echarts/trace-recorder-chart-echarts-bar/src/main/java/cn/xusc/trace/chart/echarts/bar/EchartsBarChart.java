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
package cn.xusc.trace.chart.echarts.bar;

import cn.xusc.trace.chart.*;
import cn.xusc.trace.chart.constant.ChartRefreshStrategy;
import cn.xusc.trace.chart.constant.Temporary;
import cn.xusc.trace.chart.echarts.bar.config.EchartsBarChartConfig;
import cn.xusc.trace.chart.echarts.bar.process.EchartsBarChartDataProcessor;
import cn.xusc.trace.chart.echarts.bar.resource.EchartsBarServerResource;
import cn.xusc.trace.chart.echarts.bar.step.EchartsBarDataChartProcessStep;
import cn.xusc.trace.chart.echarts.bar.step.EchartsBarJsonChartProcessStep;
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
import lombok.extern.slf4j.Slf4j;

/**
 * Echarts柱状图图表
 *
 * @author WangCai
 * @since 2.5.2
 */
@Slf4j
public class EchartsBarChart extends AbstractChart {

    /**
     * 基础构造
     */
    public EchartsBarChart() {
        super();
    }

    @Override
    protected AbstractChartConfig initChartConfig() {
        TraceRecorderEnvironment environment = TraceRecorders.get().environment();
        return deduceEchartsBarChartConfig(environment);
    }

    @Override
    public void initChartAttribute() {
        EchartsBarChartConfig chartConfig = (EchartsBarChartConfig) CHART_CONFIG;
        EchartsBarChartConfig.BarAttribute barAttribute = chartConfig.getBarAttribute();
        ChartAttribute.INSTANCE.setAttribute("beautifyInterval", barAttribute.getBeautifyInterval().toString());
    }

    @Override
    protected void initCharProcessSteps(ChartFlow chartFlow) {
        chartFlow.addChartProcessStep(new EchartsBarDataChartProcessStep(), new EchartsBarJsonChartProcessStep());
    }

    @Override
    protected ChartDataProcessor initChartDataProcessor(Chart chart) {
        return new EchartsBarChartDataProcessor(chart);
    }

    @Override
    protected void openServerShow() {
        Path generatePath = ((EchartsBarChartConfig) CHART_CONFIG).getSpecificGeneratePath();

        new TomcatServer(
            TomcatServerConfig
                .builder()
                .resources(
                    List.of(new BaseChartServerResource(generatePath), new EchartsBarServerResource(generatePath))
                )
                .accessRequestPaths(new String[] { "/", "/bar" })
                .closeRequestPath(new String[] { "/close" })
                .build()
        )
            .start();

        if (log.isDebugEnabled()) {
            log.debug("started tomcat server successful!");
        }
    }

    /**
     * 推断Echarts柱状图图表配置
     *
     * @param environment 跟踪记录仪环境
     * @return Echarts柱状图图表配置
     */
    private EchartsBarChartConfig deduceEchartsBarChartConfig(TraceRecorderEnvironment environment) {
        return EchartsBarChartConfig
            .builder()
            .chartRefreshStrategy(
                ChartRefreshStrategy.valueOf(
                    (String) environment
                        .get(EchartsBarChartConfig.CONFIG_CLASSNAME.concat(".").concat("chartRefreshStrategy"))
                        .orElse("TIMELY")
                )
            )
            .generatePath(
                Path.of(
                    (String) environment
                        .get(EchartsBarChartConfig.CONFIG_CLASSNAME.concat(".").concat("generatePath"))
                        .orElse(Systems.getClassPaths(classPath -> Files.isDirectory(Paths.get(classPath)))[0])
                )
            )
            .homePath(Temporary.TEMPLATE_PATH.resolve("echarts/bar"))
            .specificGenerateChartPath(Path.of("echarts/bar"))
            .jsonFilePath(Path.of("echarts/bar/data/bar.json"))
            .barAttribute(contextBarAttribute(environment))
            .build()
            .buildCompleteVerify();
    }

    /**
     * 获取上下文图表属性
     *
     * @param environment 跟踪记录仪环境
     * @return 柱状图属性
     */
    private EchartsBarChartConfig.BarAttribute contextBarAttribute(TraceRecorderEnvironment environment) {
        return EchartsBarChartConfig.BarAttribute
            .builder()
            .beautifyInterval(
                Boolean.valueOf(
                    (String) environment
                        .get(
                            EchartsBarChartConfig.BarAttribute.ATTRIBUTE_CONFIG_CLASSNAME
                                .concat(".")
                                .concat("beautifyInterval")
                        )
                        .orElse("true")
                )
            )
            .build();
    }
}
