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

import cn.xusc.trace.core.EnhanceInfo;
import java.nio.file.Path;
import java.util.List;

/**
 * 跟踪仪表盘组件接口
 *
 * <p>
 * 规范: 跟踪仪表盘组件启用后方可生效，具体{@link #enable(Path)}方法
 * </p>
 *
 * @author WangCai
 * @since 2.5.3
 */
public interface TraceDashboardComponent {
    /**
     * 获取组名
     *
     * @return 组名
     */
    String groupName();

    /**
     * 获取组中文名
     *
     * @return 组中文名
     */
    String groupChineseName();

    /**
     * 获取组件名
     *
     * @return 组件名
     */
    String componentName();

    /**
     * 获取组件中文名
     *
     * @return 组件中文名
     */
    String componentChineseName();

    /**
     * 获取组件访问路径
     *
     * @return 组件访问路径
     */
    String componentAccessPath();

    /**
     * 获取跟踪仪表盘组件资源列表
     *
     * @return 跟踪仪表盘组件资源列表
     */
    List<TraceDashboardComponentResource> resources();

    /**
     * 启用组件
     *
     * <p>
     * 启用组件后，组件才生效
     * </p>
     *
     * @param componentGeneratePath 组件生成路径
     */
    void enable(Path componentGeneratePath);

    /**
     * 传递增强信息
     *
     * @param eInfo 增强信息
     */
    void transmit(EnhanceInfo eInfo);
}
