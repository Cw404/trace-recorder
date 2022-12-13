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
package cn.xusc.trace.dashboard;

import cn.xusc.trace.common.util.Maps;
import cn.xusc.trace.common.util.reflect.Class;
import cn.xusc.trace.dashboard.annotation.TraceDashboardComponentOrder;
import cn.xusc.trace.dashboard.mapping.TraceDashboardData;
import java.lang.annotation.Annotation;
import java.util.*;
import lombok.extern.slf4j.Slf4j;

/**
 * 抽象跟踪仪表盘
 *
 * <p>跟踪仪表盘处理流程:</p>
 * <ul>
 *     <li>初始化跟踪仪表盘配置</li>
 *     <li>初始化跟踪仪表盘属性</li>
 *     <li>调度后进行跟踪仪表盘显示</li>
 * </ul>
 *
 * @author WangCai
 * @since 2.5.3
 */
@Slf4j
public abstract class AbstractTraceDashboard implements TraceDashboard {

    /**
     * 跟踪仪表盘配置
     */
    protected final AbstractTraceDashboardConfig TRACE_DASHBOARD_CONFIG;

    /**
     * 跟踪仪表盘组件列表
     */
    protected final List<TraceDashboardComponent> TRACE_DASHBOARD_COMPONENTS = new ArrayList<>();

    /**
     * 基础构造
     */
    public AbstractTraceDashboard() {
        log.debug("init TraceDashboard: {}", new Class<>(this).name());

        this.TRACE_DASHBOARD_CONFIG = initTraceDashboardConfig();
        log.debug("init TraceDashboard config successful!");

        initTraceDashboardAttribute();
        log.debug("init TraceDashboard attribute successful!");

        log.debug("init TraceDashboard: [ {} ] successful!", new Class<>(this).name());
    }

    @Override
    public AbstractTraceDashboardConfig config() {
        return TRACE_DASHBOARD_CONFIG;
    }

    @Override
    public List<TraceDashboardComponent> components() {
        return TRACE_DASHBOARD_COMPONENTS;
    }

    /**
     * {@inheritDoc}
     *
     * <p>显示流程:</p>
     * <ol>
     *     <li>包装跟踪仪表盘数据</li>
     *     <li>开启服务显示跟踪仪表盘</li>
     * </ol>
     */
    @Override
    public void show(Map<String, TraceDashboardComponentGroup> componentGroups) {
        List<TraceDashboardData> traceDashboardData = wrappedTraceDashboardData(componentGroups);
        openServerShow(traceDashboardData);

        log.debug("open server show TraceDashboard successful!");
    }

    /**
     * 包装跟踪仪表盘数据
     *
     * @param componentGroups 跟踪仪表盘组件组映射集
     * @return 跟踪仪表盘数据列表
     */
    private List<TraceDashboardData> wrappedTraceDashboardData(
        Map<String, TraceDashboardComponentGroup> componentGroups
    ) {
        List<TraceDashboardData> traceDashboardData = new ArrayList<>();
        Maps.walkEnd(
            componentGroups,
            (groupName, componentGroup) -> {
                List<TraceDashboardComponent> components = componentGroup.getComponents();
                /*
                组件排序
                 */
                components.sort(
                    Comparator.comparing(component -> {
                        Optional<cn.xusc.trace.common.util.reflect.Annotation<java.lang.Class<? extends Annotation>>> componentOrderOptional = new Class<>(
                            component
                        )
                            .findAvailableAnnotation(TraceDashboardComponentOrder.class);
                        return componentOrderOptional.isEmpty()
                            ? Integer.MAX_VALUE
                            : (Integer) componentOrderOptional.get().value();
                    })
                );
                TRACE_DASHBOARD_COMPONENTS.addAll(components);

                List<TraceDashboardData.TraceDashboardComponentData> traceDashboardComponentData = new ArrayList<>();
                for (TraceDashboardComponent component : components) {
                    traceDashboardComponentData.add(
                        TraceDashboardData.TraceDashboardComponentData
                            .builder()
                            .name(component.componentName())
                            .chineseName(component.componentChineseName())
                            .accessPath(component.componentAccessPath())
                            .build()
                    );
                }
                traceDashboardData.add(
                    TraceDashboardData
                        .builder()
                        .groupName(groupName)
                        .groupChineseName(componentGroup.getGroupChineseName())
                        .componentData(traceDashboardComponentData)
                        .build()
                );
            }
        );
        return traceDashboardData;
    }

    /**
     * 子类初始化跟踪仪表盘配置
     *
     * @return 跟踪仪表盘配置
     */
    protected abstract AbstractTraceDashboardConfig initTraceDashboardConfig();

    /**
     * 子类初始化跟踪仪表盘属性
     */
    protected abstract void initTraceDashboardAttribute();

    /**
     * 子类开启服务显示
     *
     * @param traceDashboardData 跟踪仪表盘数据列表
     */
    protected abstract void openServerShow(List<TraceDashboardData> traceDashboardData);
}
