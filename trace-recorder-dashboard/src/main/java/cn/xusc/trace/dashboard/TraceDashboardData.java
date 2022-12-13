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

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

/**
 * 跟踪仪表盘数据
 *
 * @author WangCai
 * @since 2.5.3
 */
@Setter
@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TraceDashboardData {

    /**
     * 组名
     */
    String groupName;

    /**
     * 组中文名
     */
    String groupChineseName;

    /**
     * 跟踪仪表盘组件数据列表
     */
    @JsonProperty("components")
    List<TraceDashboardComponentData> componentData;

    /**
     * 跟踪仪表盘组件数据
     */
    @Setter
    @Getter
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class TraceDashboardComponentData {

        /**
         * 组件名
         */
        String name;

        /**
         * 组件中文名
         */
        String chineseName;

        /**
         * 组件访问路径
         */
        String accessPath;
    }
}
