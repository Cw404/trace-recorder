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

import java.util.List;
import java.util.Map;

/**
 * 跟踪仪表盘接口
 *
 * @author WangCai
 * @since 2.5.3
 */
public interface TraceDashboard {
    /**
     * 获取跟踪仪表盘配置
     *
     * @return 跟踪仪表盘配置
     */
    AbstractTraceDashboardConfig config();

    /**
     * 获取跟踪仪表盘组件列表
     *
     * @return 跟踪仪表盘组件列表
     */
    List<TraceDashboardComponent> components();

    /**
     * 显示跟踪仪表盘
     *
     * @param componentGroups 跟踪仪表盘组件组映射集
     */
    void show(Map<String, TraceDashboardComponentGroup> componentGroups);
}
