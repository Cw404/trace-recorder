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
package cn.xusc.trace.chart.echarts.bar.config;

import cn.xusc.trace.chart.AbstractChartConfig;
import cn.xusc.trace.chart.constant.Temporary;
import java.nio.file.Path;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Echarts柱状图图表配置
 *
 * @author WangCai
 * @since 2.5.2
 */
@Setter
@Getter
@SuperBuilder
public class EchartsBarChartConfig extends AbstractChartConfig {

    /**
     * 柱状图图表配置类名
     */
    public static final String CONFIG_CLASSNAME = EchartsBarChartConfig.class.getName();

    /**
     * 柱状图生成路径
     */
    private Path generatePath;

    /**
     * 柱状图家路径
     */
    @Builder.Default
    private Path homePath = Temporary.TEMPLATE_PATH.resolve("echarts/bar");

    /**
     * 柱状图属性
     */
    private BarAttribute barAttribute;

    /**
     * 柱状图属性
     */
    @Setter
    @Getter
    @Builder
    public static class BarAttribute {

        /**
         *柱状图属性类名
         */
        public static final String ATTRIBUTE_CONFIG_CLASSNAME = CONFIG_CLASSNAME.concat(".").concat("BarAttribute");

        /**
         * 美化间隔
         */
        private Boolean beautifyInterval;
    }
}