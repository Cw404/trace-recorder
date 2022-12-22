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

import cn.xusc.trace.common.exception.TraceException;
import cn.xusc.trace.common.util.reflect.Class;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;

/**
 * 抽象图表
 *
 * <p>图表处理流程:</p>
 * <ul>
 *     <li>初始化图表配置</li>
 *     <li>初始化图表属性</li>
 *     <li>初始化图表处理步骤</li>
 *     <li>初始化图表数据处理者，并进行调度处理</li>
 * </ul>
 *
 * @author WangCai
 * @since 2.5
 */
@Slf4j
public abstract class AbstractChart implements Chart {

    /**
     * 图表配置
     */
    protected final AbstractChartConfig CHART_CONFIG;

    /**
     * 图表处理流程
     */
    private final ChartFlow CHART_FLOW;

    /**
     * 图表数据处理者
     */
    protected final ChartDataProcessor CHART_DATA_PROCESSOR;

    /**
     * 图表数据持续显示者
     */
    protected ChartDataContinueShower chartDataContinueShower;

    /**
     * 图表数据
     */
    private ChartData chartData;

    /**
     * 基础构造
     */
    public AbstractChart() {
        String chartName = new Class<>(this).name();
        log.debug("init chart: {}", chartName);

        this.CHART_CONFIG = initChartConfig();
        log.debug("init chart config successful!");

        initChartAttribute();
        log.debug("init chart attribute successful!");

        this.CHART_FLOW = new ChartFlow();
        log.debug("creat chart flow successful!");

        initCharProcessSteps(CHART_FLOW);
        log.debug("init chart processSteps successful!");

        this.CHART_DATA_PROCESSOR = initChartDataProcessor(this);
        this.CHART_DATA_PROCESSOR.setDaemon(true);
        this.CHART_DATA_PROCESSOR.start();
        log.debug("started chart data processor [ {} ] successful!", CHART_DATA_PROCESSOR.getName());

        log.debug("init chart: [ {} ] successful!", chartName);
    }

    @Override
    public AbstractChartConfig config() {
        return CHART_CONFIG;
    }

    @Override
    public boolean fillData(ChartData chartData) {
        this.chartData = chartData;
        return true;
    }

    @Override
    public ChartData data() {
        return chartData;
    }

    @Override
    public ChartData render() {
        ChartData renderChartData;
        ChartProcessStep.ValuePipeline<ChartData> valuePipeline = new ChartProcessStep.ValuePipeline<>(
            renderChartData = chartData
        );

        log.debug("render chart data: {}", renderChartData.basicChartData());

        for (ChartProcessStep chartProcessStep : CHART_FLOW.flow()) {
            try {
                chartProcessStep.run(CHART_CONFIG, valuePipeline);
            } catch (Exception e) {
                throw new TraceException(e);
            }

            log.debug(
                "chart process step [ {} ] generate value: {}",
                new Class<>(chartProcessStep).name(),
                valuePipeline.getValue()
            );
        }
        return renderChartData;
    }

    /**
     * {@inheritDoc}
     *
     * <p>显示流程:</p>
     * <ol>
     *     <li>等待第一个数据产生</li>
     *     <li>渲染第一幅图表</li>
     *     <li>持续渲染（异步）</li>
     *     <li>开启服务显示</li>
     * </ol>
     */
    @Override
    public void show() {
        waitFirstData();
        render();
        continueShow();
        openServerShow();

        log.debug("open server show chart successful!");
    }

    /**
     * 等待第一个数据产生
     */
    private void waitFirstData() {
        log.debug("wait for first chart data of generate");

        while (true) {
            if (Objects.nonNull(chartData)) {
                break;
            }
            try {
                TimeUnit.MICROSECONDS.sleep(200);
            } catch (InterruptedException e) {
                // nop
            }
        }
    }

    /**
     * 持续显示
     */
    private void continueShow() {
        this.chartDataContinueShower = new ChartDataContinueShower(this);
        this.chartDataContinueShower.setDaemon(true);
        this.chartDataContinueShower.start();

        log.debug("started chart data continue shower [ {} ] successful!", chartDataContinueShower.getName());
    }

    /**
     * 子类初始化图表配置
     *
     * @return 图表配置
     */
    protected abstract AbstractChartConfig initChartConfig();

    /**
     * 子类初始化图表属性
     */
    protected abstract void initChartAttribute();

    /**
     * 子类初始化图表处理步骤
     *
     * @param chartFlow 图表处理流程
     */
    protected abstract void initCharProcessSteps(ChartFlow chartFlow);

    /**
     * 子类初始化图表数据处理者
     *
     * @param chart 图表
     * @return 图表数据处理者
     */
    protected abstract ChartDataProcessor initChartDataProcessor(Chart chart);

    /**
     * 子类开启服务显示
     */
    protected abstract void openServerShow();
}
