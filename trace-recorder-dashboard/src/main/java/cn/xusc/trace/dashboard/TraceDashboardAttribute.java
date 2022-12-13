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

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 跟踪仪表盘属性
 *
 * @author WangCai
 * @since 2.5.3
 */
public enum TraceDashboardAttribute {
    /**
     * 实例
     */
    INSTANCE;

    /**
     * 属性池
     */
    private static final Map<String, String> ATTRIBUTES = new HashMap<>();

    /**
     * 设置属性
     *
     * @param name 属性名
     * @param value 属性值
     * @return 设置详情
     * @throws NullPointerException if {@code name} is null.
     * @throws NullPointerException if {@code value} is null.
     */
    public boolean setAttribute(String name, String value) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(value);

        ATTRIBUTES.put(name, value);
        return true;
    }

    /**
     * 根据属性名获取属性值
     *
     * @param name 属性名
     * @return 属性值
     * @throws NullPointerException if {@code name} is null.
     */
    public String getAttribute(String name) {
        Objects.requireNonNull(name);

        return ATTRIBUTES.get(name);
    }
}
