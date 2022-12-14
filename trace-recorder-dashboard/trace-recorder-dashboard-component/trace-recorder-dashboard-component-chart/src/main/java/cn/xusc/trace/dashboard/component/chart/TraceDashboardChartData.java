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

/**
 * 跟踪仪表盘图表数据接口
 *
 * @author WangCai
 * @since 2.5.3
 */
public interface TraceDashboardChartData {
    /**
     * 获取线程名
     *
     * @return 线程名
     */
    String threadName();

    /**
     * 获取类名
     *
     * @return 类名
     */
    String className();

    /**
     * 获取方法名
     *
     * @return 方法名
     */
    String methodName();

    /**
     * 获取行号
     *
     * @return 行号
     */
    int lineNumber();

    /**
     * 获取信息
     *
     * @return 信息
     */
    String info();

    /**
     * 获取堆栈
     *
     * @return 堆栈
     */
    StackTraceElement[] stackTraceElements();

    /**
     * 获取下一个跟踪仪表盘图表数据
     *
     * @return 下一个跟踪仪表盘图表数据
     */
    TraceDashboardChartData nextChartData();

    /**
     * 获取基础的跟踪仪表盘图表数据
     *
     * @return 基础的跟踪仪表盘图表数据
     */
    String basicTraceDashboardChartData();
}