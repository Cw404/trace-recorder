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
package cn.xusc.trace.dashboard.base;

import cn.xusc.trace.common.util.Lists;
import cn.xusc.trace.common.util.Strings;
import cn.xusc.trace.common.util.Systems;
import cn.xusc.trace.core.TraceRecorderEnvironment;
import cn.xusc.trace.core.util.TraceRecorders;
import cn.xusc.trace.dashboard.*;
import cn.xusc.trace.dashboard.base.config.BaseTraceDashboardConfig;
import cn.xusc.trace.dashboard.base.resource.BaseTraceDashboardComponentResource;
import cn.xusc.trace.dashboard.config.GenerableTraceDashboardConfig;
import cn.xusc.trace.dashboard.constant.Temporary;
import cn.xusc.trace.dashboard.generate.BaseJsonTraceDashboardGenerator;
import cn.xusc.trace.dashboard.resource.SpecificationBaseTraceDashboardComponentResource;
import cn.xusc.trace.server.tomcat.TomcatServer;
import cn.xusc.trace.server.tomcat.config.TomcatServerConfig;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

/**
 * 基础跟踪仪表盘
 *
 * @author WangCai
 * @since 2.5.3
 */
@Slf4j
public class BaseTraceDashboard extends AbstractTraceDashboard {

    /**
     * 基础构造
     */
    public BaseTraceDashboard() {
        super();
    }

    @Override
    protected AbstractTraceDashboardConfig initTraceDashboardConfig() {
        TraceRecorderEnvironment environment = TraceRecorders.get().environment();
        return deduceBaseTraceDashboardConfig(environment);
    }

    @Override
    protected void initTraceDashboardAttribute() {
        // nop
    }

    @Override
    protected void openServerShow(List<TraceDashboardData> traceDashboardData) {
        initTraceDashboardData(traceDashboardData);

        Path generatePath = ((BaseTraceDashboardConfig) TRACE_DASHBOARD_CONFIG).getSpecificGeneratePath();

        new TomcatServer(
            TomcatServerConfig
                .builder()
                .resources(
                    mergeResources(
                        List.of(
                            new cn.xusc.trace.dashboard.resource.BaseTraceDashboardComponentResource(generatePath),
                            new SpecificationBaseTraceDashboardComponentResource(generatePath),
                            new BaseTraceDashboardComponentResource(generatePath)
                        ),
                        components()
                            .stream()
                            .flatMap(traceDashboardComponent -> traceDashboardComponent.resources().stream())
                            .collect(Collectors.toList())
                    )
                )
                .accessRequestPaths(new String[] { "/", "/dashboard" })
                .closeRequestPath(new String[] { "/close", "/specificationClose" })
                .build()
        )
            .start();

        log.debug("started tomcat server successful!");
    }

    /**
     * 合并跟踪仪表盘组件资源列表
     *
     * @param self 自身跟踪仪表盘组件资源列表
     * @param componentResources 跟踪仪表盘组件资源列表
     * @return 合并跟踪仪表盘组件资源列表
     */
    private List<Object> mergeResources(
        List<TraceDashboardComponentResource> self,
        List<TraceDashboardComponentResource> componentResources
    ) {
        return (List<Object>) Lists.merge(self, componentResources);
    }

    /**
     * 初始化跟踪仪表盘数据
     *
     * @param traceDashboardData 跟踪仪表盘数据
     */
    private void initTraceDashboardData(List<TraceDashboardData> traceDashboardData) {
        Path generateJsonFilePath = new BaseJsonTraceDashboardGenerator()
            .generateJsonFilePath((GenerableTraceDashboardConfig) TRACE_DASHBOARD_CONFIG, traceDashboardData);
    }

    /**
     * 推断基础跟踪仪表盘配置
     *
     * @param environment 跟踪记录仪环境
     * @return 基础跟踪仪表盘配置
     */
    private BaseTraceDashboardConfig deduceBaseTraceDashboardConfig(TraceRecorderEnvironment environment) {
        return BaseTraceDashboardConfig
            .builder()
            .ignoreTraceDashboardComponentNames(
                Strings.split(
                    (String) environment
                        .get(
                            BaseTraceDashboardConfig.CONFIG_CLASSNAME
                                .concat(".")
                                .concat("ignoreTraceDashboardComponentNames")
                        )
                        .orElse(""),
                    ','
                )
            )
            .generatePath(
                Path.of(
                    (String) environment
                        .get(BaseTraceDashboardConfig.CONFIG_CLASSNAME.concat(".").concat("generatePath"))
                        .orElse(Systems.getClassPaths(classPath -> Files.isDirectory(Paths.get(classPath)))[0])
                )
            )
            .homePath(Temporary.TEMPLATE_PATH.resolve("dashboard"))
            .specificGenerateDashboardPath(Path.of("base"))
            .jsonFilePath(Path.of("dashboard/base/data/dashboard.json"))
            .build()
            .buildCompleteVerify();
    }
}
