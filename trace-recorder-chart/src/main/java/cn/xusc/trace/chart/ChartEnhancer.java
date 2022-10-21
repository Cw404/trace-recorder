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

import cn.xusc.trace.chart.constant.ChartRefreshStrategy;
import cn.xusc.trace.chart.constant.Temporary;
import cn.xusc.trace.chart.standard.StandardChartData;
import cn.xusc.trace.common.annotation.CloseOrder;
import cn.xusc.trace.common.annotation.TraceOrder;
import cn.xusc.trace.common.exception.TraceException;
import cn.xusc.trace.common.util.Formats;
import cn.xusc.trace.common.util.Lists;
import cn.xusc.trace.common.util.StackTraces;
import cn.xusc.trace.common.util.Systems;
import cn.xusc.trace.common.util.reflect.Class;
import cn.xusc.trace.core.EnhanceInfo;
import cn.xusc.trace.core.enhance.AbstractStatisticsInfoEnhancer;
import cn.xusc.trace.core.util.TraceRecorders;
import cn.xusc.trace.core.util.spi.TraceRecorderLoader;
import cn.xusc.trace.server.util.ServerClosedWaiter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

/**
 * 图表增强器
 *
 * <p>
 * 使用信息栈描绘图表
 * </p>
 *
 * @author WangCai
 * @since 2.5
 */
@Slf4j
@TraceOrder(Integer.MIN_VALUE + 100_0000)
@CloseOrder(1)
public class ChartEnhancer extends AbstractStatisticsInfoEnhancer {

    /**
     * 图表
     */
    private final Chart chart;

    /**
     * 图表刷新策略
     */
    private final ChartRefreshStrategy STRATEGY;

    /**
     * 是否已经显示图表标识
     */
    private boolean alreadyShowChart;

    /**
     * 基础构造
     */
    public ChartEnhancer() {
        super(TraceRecorders.get());
        chart = discoverChart();
        if (Objects.isNull(chart)) {
            STRATEGY = ChartRefreshStrategy.IDLE;
            return;
        }
        STRATEGY = chart.config().getChartRefreshStrategy();

        if (log.isDebugEnabled()) {
            log.debug("discover chart: {}, refresh strategy: {}", new Class<>(chart).name(), STRATEGY.name());
        }
    }

    /**
     * 发现图表
     *
     * @return 图表
     */
    private Chart discoverChart() {
        TraceRecorderLoader<Chart> loader = TraceRecorderLoader.getTraceRecorderLoader(Chart.class);
        Optional<List<Chart>> chartsOptional = loader.findAll();
        if (chartsOptional.isEmpty()) {
            Systems.report("No Chart component were found.");
            return null;
        }
        List<Chart> charts = chartsOptional.get();
        if (charts.size() > 1) {
            throw new TraceException(Formats.format("discover more chart: {} ", Lists.classNames(charts)));
        }
        return charts.get(0);
    }

    /**
     * 增强信息构建为标准图数据，并填充到存储库（数据采集）
     *
     * @param eInfo 增强前消息
     * @return 源增强信息
     */
    @Override
    protected EnhanceInfo doEnhance(EnhanceInfo eInfo) {
        /*
        闲置处理
         */
        if (Objects.equals(STRATEGY, ChartRefreshStrategy.IDLE)) {
            return eInfo;
        }

        /*
        堆栈信息获取
         */
        StackTraceElement[] stackTraceElements = StackTraces
            .currentStackTraceElement(Temporary.IGNORE_STACK_CLASS_NAMES)
            .get();
        StackTraceElement currentStackTraceElement = stackTraceElements[0];

        /*
        标准图表数据构建
         */
        StandardChartData standardChartData = new StandardChartData();
        standardChartData.setThreadName(Thread.currentThread().getName());
        standardChartData.setClassName(currentStackTraceElement.getClassName());
        standardChartData.setMethodName(currentStackTraceElement.getMethodName());
        standardChartData.setLineNumber(currentStackTraceElement.getLineNumber());
        standardChartData.setInfo(eInfo.getInfo());
        standardChartData.setStackTraceElements(stackTraceElements);

        if (log.isTraceEnabled()) {
            log.trace("generate standard chart data: {}", standardChartData.basicChartData());
        }

        /*
        标准图表数据填充到存储库
         */
        for (;;) {
            try {
                ChartDataRepository.INSTANCE.put(standardChartData);
                if (log.isTraceEnabled()) {
                    log.trace("put standard chart data to repository");
                }
                break;
            } catch (InterruptedException e) {
                // nop
            }
        }

        if (!alreadyShowChart && Objects.equals(STRATEGY, ChartRefreshStrategy.TIMELY)) {
            /*
             及时刷新策略显示
             */
            innerShow();
        }

        return eInfo;
    }

    /**
     * 图表显示，并等待图表显示完
     *
     * @return {@inheritDoc}
     */
    @Override
    protected String showInfo() {
        /*
        闲置处理
         */
        if (Objects.equals(STRATEGY, ChartRefreshStrategy.IDLE)) {
            return "";
        }

        if (!alreadyShowChart && Objects.equals(STRATEGY, ChartRefreshStrategy.DEATH)) {
            /*
             JVM关闭时刷新策略显示
             */
            innerShow();
        }

        /*
        陷入服务关闭等待，等待服务关闭
         */
        try {
            ServerClosedWaiter.INSTANCE.doWait(1);
        } catch (InterruptedException e) {
            // nop
        }
        return "";
    }

    /**
     * 内部显示图表
     */
    private void innerShow() {
        String chartName = new Class<>(chart).name();

        if (log.isTraceEnabled()) {
            log.trace("show chart: {}", chartName);
        }

        chart.show();
        alreadyShowChart = true;

        if (log.isDebugEnabled()) {
            log.debug("showed chart: {}", chartName);
        }
    }
}
