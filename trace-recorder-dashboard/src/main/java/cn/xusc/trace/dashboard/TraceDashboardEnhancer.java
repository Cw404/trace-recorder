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

import cn.xusc.trace.common.annotation.CloseOrder;
import cn.xusc.trace.common.annotation.TraceOrder;
import cn.xusc.trace.common.exception.TraceException;
import cn.xusc.trace.common.exception.TraceUnsupportedOperationException;
import cn.xusc.trace.common.util.*;
import cn.xusc.trace.common.util.reflect.Class;
import cn.xusc.trace.core.EnhanceInfo;
import cn.xusc.trace.core.enhance.AbstractStatisticsInfoEnhancer;
import cn.xusc.trace.core.util.TraceRecorders;
import cn.xusc.trace.core.util.spi.TraceRecorderLoader;
import cn.xusc.trace.server.util.ServerClosedWaiter;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

/**
 * 跟踪仪表盘增强器
 *
 * @author WangCai
 * @since 2.5.3
 */
@Slf4j
@TraceOrder(Integer.MIN_VALUE + 99_9900)
@CloseOrder(1)
public class TraceDashboardEnhancer extends AbstractStatisticsInfoEnhancer {

    /**
     * 跟踪仪表盘
     */
    private final TraceDashboard TRACE_DASHBOARD;

    /**
     * 跟踪仪表盘组件组映射集
     */
    private final Map<String, TraceDashboardComponentGroup> COMPONENT_GROUPS = new TreeMap<>();

    /**
     * 跟踪仪表盘组件广播列表
     */
    private List<TraceDashboardComponent> broadcastTraceDashboardComponents;

    /**
     * 是否已经显示跟踪仪表盘标识
     */
    private boolean alreadyShowTraceDashboard;

    /**
     * 基础构造
     */
    public TraceDashboardEnhancer() {
        super(TraceRecorders.get());
        /*
        发现跟踪仪表盘
         */
        TRACE_DASHBOARD = discoverTraceDashboard();

        if (Objects.isNull(TRACE_DASHBOARD)) {
            return;
        }

        log.debug("discover TraceDashboard: {}", new Class<>(TRACE_DASHBOARD).name());

        Path componentGeneratePath = TRACE_DASHBOARD.config().componentGeneratePath();
        log.debug(Formats.format("trace dashboard component generate path: {}", componentGeneratePath));

        List<String> ignoreTraceDashboardComponentNames = TRACE_DASHBOARD
            .config()
            .getIgnoreTraceDashboardComponentNames(), doneIgnoreTraceDashboardComponentNames = new ArrayList<>();
        if (ignoreTraceDashboardComponentNames.size() > 1) {
            if (ignoreTraceDashboardComponentNames instanceof FastList) {
                ignoreTraceDashboardComponentNames =
                    Lists.transfer(ignoreTraceDashboardComponentNames, new ArrayList<>());
            }
            /*
            为二分查找前进行排序
             */
            Collections.sort(ignoreTraceDashboardComponentNames);
        }

        /*
        发现跟踪仪表盘组件列表
         */
        String traceDashboardComponentName;
        int ignoreTraceDashboardComponentNameIndex;
        List<TraceDashboardComponent> traceDashboardComponents = discoverTraceDashboardComponents();
        for (TraceDashboardComponent traceDashboardComponent : traceDashboardComponents) {
            verifyValidTraceDashboardComponent(traceDashboardComponent);

            traceDashboardComponentName = traceDashboardComponent.componentName();
            if (
                ignoreTraceDashboardComponentNames.size() > 0 &&
                (
                    ignoreTraceDashboardComponentNameIndex =
                        Collections.binarySearch(ignoreTraceDashboardComponentNames, traceDashboardComponentName)
                ) >=
                0
            ) {
                ignoreTraceDashboardComponentNames.remove(ignoreTraceDashboardComponentNameIndex);
                doneIgnoreTraceDashboardComponentNames.add(traceDashboardComponentName);
                continue;
            }

            /*
            启用未忽略的组件
             */
            log.trace("enable TraceDashboardComponent: {}", new Class<>(traceDashboardComponent).name());

            traceDashboardComponent.enable(componentGeneratePath);

            log.debug("enabled TraceDashboardComponent: {}", new Class<>(traceDashboardComponent).name());

            List<TraceDashboardComponentResource> resources;
            if (Objects.isNull(resources = traceDashboardComponent.resources()) || resources.isEmpty()) {
                throw new TraceUnsupportedOperationException(
                    Formats.format(
                        "[ {} ] - resources must to not null and not empty",
                        new Class<>(traceDashboardComponent).name()
                    )
                );
            }

            if (log.isDebugEnabled()) {
                log.debug("     TraceDashboardComponent resources: ");
                for (TraceDashboardComponentResource componentResource : resources) {
                    log.debug("          {}", new Class<>(componentResource).name());
                }
            }

            /*
            组件资源组划分
             */
            COMPONENT_GROUPS.compute(
                traceDashboardComponent.groupName(),
                (k, v) -> {
                    if (Objects.nonNull(v)) {
                        v.getComponents().add(traceDashboardComponent);
                        return v;
                    }
                    return TraceDashboardComponentGroup
                        .builder()
                        .groupName(traceDashboardComponent.groupName())
                        .groupChineseName(traceDashboardComponent.groupChineseName())
                        .components(new ArrayList<>(List.of(traceDashboardComponent)))
                        .build();
                }
            );
        }

        if (log.isDebugEnabled()) {
            if (!doneIgnoreTraceDashboardComponentNames.isEmpty()) {
                log.debug("done ignore TraceDashboardComponent names:");
                for (String componentName : doneIgnoreTraceDashboardComponentNames) {
                    log.debug("     {}", componentName);
                }
            }

            if (!COMPONENT_GROUPS.isEmpty()) {
                log.debug("discover TraceDashboardComponent:");
                COMPONENT_GROUPS
                    .values()
                    .stream()
                    .flatMap(traceDashboardComponentGroup -> traceDashboardComponentGroup.getComponents().stream())
                    .map(TraceDashboardComponent::componentName)
                    .forEach(componentName -> {
                        log.debug("     {}", componentName);
                    });
            } else {
                log.debug("not discover TraceDashboardComponent");
            }
        }

        eagerShow();
    }

    /**
     * 发现跟踪仪表盘
     *
     * @return 跟踪仪表盘
     */
    private TraceDashboard discoverTraceDashboard() {
        TraceRecorderLoader<TraceDashboard> loader = TraceRecorderLoader.getTraceRecorderLoader(TraceDashboard.class);
        Optional<List<TraceDashboard>> dashboardOptional = loader.findAll();
        if (dashboardOptional.isEmpty()) {
            Systems.report("No TraceDashboard were found.");
            return null;
        }
        List<TraceDashboard> dashboards = dashboardOptional.get();
        if (dashboards.size() > 1) {
            throw new TraceException(Formats.format("discover more TraceDashboard: {} ", Lists.classNames(dashboards)));
        }
        return dashboards.get(0);
    }

    /**
     * 发现跟踪仪表盘组件列表
     *
     * @return 跟踪仪表盘组件列表
     */
    private List<TraceDashboardComponent> discoverTraceDashboardComponents() {
        TraceRecorderLoader<TraceDashboardComponent> loader = TraceRecorderLoader.getTraceRecorderLoader(
            TraceDashboardComponent.class
        );
        Optional<List<TraceDashboardComponent>> elementsOptional = loader.findAll();
        return elementsOptional.orElse(List.of());
    }

    /**
     * 急切的显示跟踪仪表盘
     */
    private void eagerShow() {
        log.trace("show TraceDashboard: {}", new Class<>(TRACE_DASHBOARD).name());

        TRACE_DASHBOARD.show(COMPONENT_GROUPS);
        alreadyShowTraceDashboard = true;

        log.debug("showed TraceDashboard: {}", new Class<>(TRACE_DASHBOARD).name());
    }

    /**
     * 验证有效的跟踪仪表盘组件
     *
     * @param traceDashboardComponent 跟踪仪表盘组件
     */
    private void verifyValidTraceDashboardComponent(TraceDashboardComponent traceDashboardComponent) {
        String groupName, groupChineseName, componentName, componentChineseName, componentAccessPath;
        if (Objects.isNull(groupName = traceDashboardComponent.groupName()) || groupName.isBlank()) {
            throw new TraceUnsupportedOperationException(
                Formats.format(
                    "[ {} ] - groupName must to not null and not blank",
                    new Class<>(traceDashboardComponent).name()
                )
            );
        }
        if (
            Objects.isNull(groupChineseName = traceDashboardComponent.groupChineseName()) || groupChineseName.isBlank()
        ) {
            throw new TraceUnsupportedOperationException(
                Formats.format(
                    "[ {} ] - groupChineseName must to not null and not blank",
                    new Class<>(traceDashboardComponent).name()
                )
            );
        }
        if (Objects.isNull(componentName = traceDashboardComponent.componentName()) || componentName.isBlank()) {
            throw new TraceUnsupportedOperationException(
                Formats.format(
                    "[ {} ] - componentName must to not null and not blank",
                    new Class<>(traceDashboardComponent).name()
                )
            );
        }
        if (
            Objects.isNull(componentChineseName = traceDashboardComponent.componentChineseName()) ||
            componentChineseName.isBlank()
        ) {
            throw new TraceUnsupportedOperationException(
                Formats.format(
                    "[ {} ] - componentChineseName must to not null and not blank",
                    new Class<>(traceDashboardComponent).name()
                )
            );
        }
        if (
            Objects.isNull(componentAccessPath = traceDashboardComponent.componentAccessPath()) ||
            componentAccessPath.isBlank()
        ) {
            throw new TraceUnsupportedOperationException(
                Formats.format(
                    "[ {} ] - componentAccessPath must to not null and not blank",
                    new Class<>(traceDashboardComponent).name()
                )
            );
        }
    }

    /**
     * 已显示跟踪仪表盘就广播增强消息
     *
     * @param eInfo 增强消息
     * @return 增强消息
     */
    @Override
    protected EnhanceInfo doEnhance(EnhanceInfo eInfo) {
        if (alreadyShowTraceDashboard) {
            broadcastEnhanceInfo(eInfo);
        }
        return eInfo;
    }

    /**
     * 不做显示，仅跟踪仪表盘启动后进行等待关闭
     *
     * @return {@inheritDoc}
     */
    @Override
    protected String showInfo() {
        if (alreadyShowTraceDashboard) {
            /*
            陷入服务关闭等待，等待服务关闭
             */
            try {
                ServerClosedWaiter.INSTANCE.doWait(1);
            } catch (InterruptedException e) {
                // nop
            }
        }
        return "";
    }

    /**
     * 广播增强消息
     *
     * @param eInfo 增强消息
     */
    private void broadcastEnhanceInfo(EnhanceInfo eInfo) {
        if (Objects.isNull(broadcastTraceDashboardComponents)) {
            broadcastTraceDashboardComponents =
                COMPONENT_GROUPS
                    .values()
                    .stream()
                    .flatMap(traceDashboardComponentGroup -> traceDashboardComponentGroup.getComponents().stream())
                    .collect(Collectors.toList());
        }

        log.trace("broadcast EnhanceInfo: {}", eInfo.toString());

        EnhanceInfo transmitEnhanceInfo = eInfo.clone();
        for (TraceDashboardComponent traceDashboardComponent : broadcastTraceDashboardComponents) {
            log.trace(
                "transmit EnhanceInfo [ {} ] to TraceDashboardComponent [ {} ]",
                transmitEnhanceInfo.toString(),
                new Class<>(traceDashboardComponent).name()
            );
            traceDashboardComponent.transmit(transmitEnhanceInfo);
            log.trace("transmitted EnhanceInfo [ {} ] successful!", eInfo.getInfo());
        }

        log.debug("broadcast EnhanceInfo [ {} ] successful!", eInfo.getInfo());
    }
}
