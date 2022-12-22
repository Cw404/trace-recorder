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
package cn.xusc.trace.dashboard.component.chart.echarts.relation.mapping;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * 跟踪仪表盘Echarts关系图
 *
 * @author WangCai
 * @since 2.5.3
 */
@Setter
@Getter
@Builder
public class TraceDashboardEchartsRelation {

    /**
     * Echarts关系图节点列表
     */
    List<EchartsRelationNode> nodes;

    /**
     * Echarts关系图链接列表
     */
    List<EchartsRelationLink> links;

    /**
     * Echarts关系图类别列表
     */
    List<EchartsRelationCategory> categories;

    /**
     * Echarts关系图节点
     */
    @Setter
    @Getter
    @Builder
    public static class EchartsRelationNode {

        /**
         * 编号
         */
        String id;

        /**
         * 名称 - 线程名 - 类名#方法名()[行号]
         */
        String name;

        /**
         * 节点标记大小
         */
        int symbolSize;

        /**
         * 横轴值
         */
        double x;

        /**
         * 纵轴值
         */
        double y;

        /**
         * 值 - 记录信息
         */
        String value;

        /**
         * 类别索引
         */
        int category;
    }

    /**
     * Echarts关系图链接
     */
    @Setter
    @Getter
    @Builder
    public static class EchartsRelationLink {

        /**
         * 源编号
         */
        String source;

        /**
         * 目标编号
         */
        String target;
    }

    /**
     * Echarts关系图类别
     */
    @Setter
    @Getter
    @Builder
    public static class EchartsRelationCategory {

        /**
         * 名称
         */
        String name;
    }
}
