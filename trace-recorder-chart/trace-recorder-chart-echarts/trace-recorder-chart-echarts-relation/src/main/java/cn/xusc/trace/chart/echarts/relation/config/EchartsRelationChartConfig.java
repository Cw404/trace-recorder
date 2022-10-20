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
package cn.xusc.trace.chart.echarts.relation.config;

import cn.xusc.trace.chart.AbstractChartConfig;
import cn.xusc.trace.chart.constant.Temporary;
import java.nio.file.Path;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Echarts关系图图表配置
 *
 * @author WangCai
 * @since 2.5
 */
@Setter
@Getter
@SuperBuilder
public class EchartsRelationChartConfig extends AbstractChartConfig {

    /**
     * 关系图图表配置类名
     */
    public static final String CONFIG_CLASSNAME = EchartsRelationChartConfig.class.getName();

    /**
     * 关系图生成路径
     */
    private Path generatePath;

    /**
     * 关系图家路径
     */
    @Builder.Default
    private Path homePath = Temporary.TEMPLATE_PATH.resolve("echarts/relation");

    /**
     * 关系图属性
     */
    private RelationAttribute relationAttribute;

    /**
     * 关系图属性
     */
    @Setter
    @Getter
    @Builder
    public static class RelationAttribute {

        /**
         *关系图属性类名
         */
        public static final String ATTRIBUTE_CONFIG_CLASSNAME = CONFIG_CLASSNAME
            .concat(".")
            .concat("RelationAttribute");

        /**
         * 符号大小
         */
        private String symbolSize;

        /**
         * 最大X轴值
         */
        private String maxXAxis;

        /**
         * X轴偏移值
         */
        private String offsetXAxis;

        /**
         * Y轴偏移值
         */
        private String offsetYAxis;

        /**
         * 最小Y轴值
         */
        private String minYAxis;
    }
}
