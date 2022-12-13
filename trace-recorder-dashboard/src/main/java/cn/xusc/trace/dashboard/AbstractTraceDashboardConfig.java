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

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * 抽象跟踪仪表盘配置
 *
 * @author WangCai
 * @since 2.5.3
 */
@Setter
@Getter
@SuperBuilder
public abstract class AbstractTraceDashboardConfig {

    /**
     * 忽略的跟踪仪表盘组件名列表
     */
    @Builder.Default
    private List<String> ignoreTraceDashboardComponentNames = new ArrayList<>();

    /**
     * 获取组件生成路径
     *
     * @return 组件生成路径
     */
    public abstract Path componentGeneratePath();
}
