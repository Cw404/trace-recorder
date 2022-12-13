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
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

/**
 * 跟踪仪表盘组件组
 *
 * @author WangCai
 * @since 2.5.3
 */
@Setter
@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
class TraceDashboardComponentGroup {

    /**
     * 组名
     */
    String groupName;

    /**
     * 组中文名
     */
    String groupChineseName;

    /**
     * 跟踪仪表盘组件列表
     */
    List<TraceDashboardComponent> components;
}
