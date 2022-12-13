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
package cn.xusc.trace.dashboard.component.chart.echarts.relation.constant;

/**
 * 临时常量
 *
 * <p>
 * 为跟踪仪表盘Echarts关系图图表提供全局修改的统一方案
 * </p>
 *
 * @author WangCai
 * @since 2.5.3
 */
public class Temporary {

    /**
     * SPI组件前缀
     */
    public static final String SPI_COMPONENT_PREFIX = "inner_xusc_dashboard_component_chart_";

    /**
     * 访问请求路径
     *
     * <p>
     * 具体上下文请求路径交给服务处理
     * </p>
     */
    public static final String ACCESS_REQUEST_PATH = "relation";
}
