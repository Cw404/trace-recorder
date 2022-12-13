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

import cn.xusc.trace.common.exception.TraceException;
import cn.xusc.trace.common.util.Arrays;
import cn.xusc.trace.common.util.StackTraces;
import cn.xusc.trace.common.util.reflect.Class;
import cn.xusc.trace.core.EnhanceInfo;
import cn.xusc.trace.dashboard.TraceDashboardComponent;
import cn.xusc.trace.dashboard.component.chart.constant.Temporary;
import cn.xusc.trace.dashboard.component.chart.constant.TraceDashboardChartRefreshStrategy;
import cn.xusc.trace.dashboard.component.chart.standard.TraceDashboardStandardChartData;
import java.nio.file.Path;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;

/**
 * 抽象跟踪仪表盘图表
 *
 * <p>图表处理流程:</p>
 * <ul>
 *     <li>初始化跟踪仪表盘图表配置</li>
 *     <li>初始化跟踪仪表盘图表属性</li>
 *     <li>初始化跟踪仪表盘图表环境</li>
 *     <li>初始化跟踪仪表盘图表数据存储库</li>
 *     <li>初始化跟踪仪表盘图表处理流程</li>
 *     <li>初始化跟踪仪表盘图表处理步骤</li>
 *     <li>初始化跟踪仪表盘图表数据处理者，并进行调度处理</li>
 * </ul>
 *
 * @author WangCai
 * @since 2.5.3
 */
@Slf4j
public abstract class AbstractTraceDashboardChart implements TraceDashboardChart, TraceDashboardComponent {

    /**
     * 跟踪仪表盘图表配置
     */
    protected AbstractTraceDashboardChartConfig chartConfig;

    /**
     * 跟踪仪表盘图表属性
     */
    protected TraceDashboardChartAttribute chartAttribute;

    /**
     * 跟踪仪表盘图表处理流程
     */
    private TraceDashboardChartFlow chartFlow;

    /**
     * 跟踪仪表盘图表数据处理者
     */
    protected TraceDashboardChartDataProcessor chartDataProcessor;

    /**
     * 跟踪仪表盘图表数据持续显示者
     */
    protected TraceDashboardChartDataContinueShower chartDataContinueShower;

    /**
     * 跟踪仪表盘图表数据存储库
     */
    protected TraceDashboardChartDataRepository chartDataRepository;

    /**
     * 跟踪仪表盘图表数据
     */
    private TraceDashboardChartData chartData;

    /**
     * 是否已经显示图表标识
     */
    private boolean alreadyShowChart;

    /**
     * 基础构造
     */
    public AbstractTraceDashboardChart() {}

    @Override
    public boolean init(Path componentGeneratePath) {
        log.debug("init TraceDashboardChart: {}", new Class<>(this).name());

        this.chartConfig = initTraceDashboardChartConfig();
        /*
        配置组件化
         */
        this.chartConfig.setGeneratePath(componentGeneratePath);
        log.debug("init TraceDashboardChart config successful!");

        this.chartAttribute = new TraceDashboardChartAttribute();
        initTraceDashboardChartAttribute();
        log.debug("init TraceDashboardChart attribute successful!");

        try {
            initTraceDashboardChartEnvironment();
        } catch (Exception e) {
            throw new TraceException(e);
        }
        log.debug("init TraceDashboardChart environment successful!");

        this.chartDataRepository = new TraceDashboardChartDataRepository();
        log.debug("creat TraceDashboardChart data repository successful!");

        this.chartFlow = new TraceDashboardChartFlow();
        log.debug("creat TraceDashboardChart flow successful!");

        initTraceDashboardCharProcessSteps(chartFlow);
        log.debug("init TraceDashboardChart processSteps successful!");

        this.chartDataProcessor = initTraceDashboardChartDataProcessor(this);
        this.chartDataProcessor.setDaemon(true);
        this.chartDataProcessor.start();
        log.debug("started TraceDashboardChart data processor [ {} ] successful!", chartDataProcessor.getName());

        log.debug("init TraceDashboardChart: [ {} ] successful!", new Class<>(this).name());
        return true;
    }

    @Override
    public boolean handleEnhanceInfo(EnhanceInfo eInfo) {
        TraceDashboardChartRefreshStrategy chartRefreshStrategy = chartConfig.getChartRefreshStrategy();
        /*
        闲置处理
         */
        if (Objects.equals(chartRefreshStrategy, TraceDashboardChartRefreshStrategy.IDLE)) {
            return true;
        }

        /*
        堆栈信息获取
         */
        StackTraceElement[] stackTraceElements = StackTraces
            .currentStackTraceElement(
                Arrays.merge(Temporary.IGNORE_STACK_CLASS_NAMES, new Class<>(this).type().getName())
            )
            .get();
        StackTraceElement currentStackTraceElement = stackTraceElements[0];

        /*
        跟踪仪表盘标准图表数据构建
         */
        TraceDashboardStandardChartData standardChartData = new TraceDashboardStandardChartData();
        standardChartData.setThreadName(Thread.currentThread().getName());
        standardChartData.setClassName(currentStackTraceElement.getClassName());
        standardChartData.setMethodName(currentStackTraceElement.getMethodName());
        standardChartData.setLineNumber(currentStackTraceElement.getLineNumber());
        standardChartData.setInfo(eInfo.getInfo());
        standardChartData.setStackTraceElements(stackTraceElements);

        log.trace("generate trace dashboard standard chart data: {}", standardChartData.basicTraceDashboardChartData());

        /*
        标准图表数据填充到存储库
         */
        for (;;) {
            try {
                chartDataRepository.put(standardChartData);
                log.trace("put trace dashboard standard chart data to repository");
                break;
            } catch (InterruptedException e) {
                // nop
            }
        }

        if (!alreadyShowChart) {
            /*
             及时刷新策略显示
             */
            show();
            alreadyShowChart = true;
        }

        return true;
    }

    @Override
    public AbstractTraceDashboardChartConfig config() {
        return chartConfig;
    }

    @Override
    public TraceDashboardChartAttribute chartAttribute() {
        return chartAttribute;
    }

    @Override
    public TraceDashboardChartDataRepository chartDataRepository() {
        return chartDataRepository;
    }

    @Override
    public boolean fillData(TraceDashboardChartData chartData) {
        this.chartData = chartData;
        return true;
    }

    @Override
    public TraceDashboardChartData data() {
        return chartData;
    }

    @Override
    public TraceDashboardChartData render() {
        TraceDashboardChartData renderChartData;
        TraceDashboardChartProcessStep.ValuePipeline<TraceDashboardChartData> valuePipeline = new TraceDashboardChartProcessStep.ValuePipeline<>(
            renderChartData = chartData
        );

        log.debug("render TraceDashboardChart data: {}", renderChartData.basicTraceDashboardChartData());

        for (TraceDashboardChartProcessStep chartProcessStep : chartFlow.flow()) {
            try {
                chartProcessStep.run(this, chartConfig, valuePipeline);
            } catch (Exception e) {
                throw new TraceException(e);
            }

            log.debug(
                "TraceDashboardChart process step [ {} ] generate value: {}",
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
     * </ol>
     */
    @Override
    public void show() {
        waitFirstData();
        render();
        continueShow();

        log.debug("open server show TraceDashboardChart: [ {} ] successful!", new Class<>(this).name());
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
        this.chartDataContinueShower = new TraceDashboardChartDataContinueShower(this);
        this.chartDataContinueShower.setDaemon(true);
        this.chartDataContinueShower.start();

        log.debug(
            "started TraceDashboardChart data continue shower [ {} ] successful!",
            chartDataContinueShower.getName()
        );
    }

    @Override
    public void enable(Path componentGeneratePath) {
        init(componentGeneratePath);
    }

    @Override
    public void transmit(EnhanceInfo eInfo) {
        handleEnhanceInfo(eInfo);
    }

    /**
     * 子类初始化跟踪仪表盘图表配置
     *
     * @return 跟踪仪表盘图表配置
     */
    protected abstract AbstractTraceDashboardChartConfig initTraceDashboardChartConfig();

    /**
     * 子类初始化跟踪仪表盘图表属性
     */
    protected abstract void initTraceDashboardChartAttribute();

    /**
     * 子类初始化跟踪仪表盘图表环境
     *
     * @throws Exception if an I/O error has occurred.
     */
    protected abstract void initTraceDashboardChartEnvironment() throws Exception;

    /**
     * 子类初始化跟踪仪表盘图表处理步骤
     *
     * @param chartFlow 跟踪仪表盘图表处理流程
     */
    protected abstract void initTraceDashboardCharProcessSteps(TraceDashboardChartFlow chartFlow);

    /**
     * 子类初始化跟踪仪表盘图表数据处理者
     *
     * @param chart 跟踪仪表盘图表
     * @return 跟踪仪表盘图表数据处理者
     */
    protected abstract TraceDashboardChartDataProcessor initTraceDashboardChartDataProcessor(TraceDashboardChart chart);
}
