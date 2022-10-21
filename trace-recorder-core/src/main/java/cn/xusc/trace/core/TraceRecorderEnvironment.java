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
package cn.xusc.trace.core;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * 跟踪记录仪环境
 *
 * @author WangCai
 * @since 2.5
 */
public final class TraceRecorderEnvironment {

    /**
     * 环境属性池
     */
    private static final Map<String, Supplier<Object>> ENV = new HashMap<>();

    /**
     * 基础构造
     */
    public TraceRecorderEnvironment() {}

    /**
     * 设置值属性
     *
     * @param key 键
     * @param value 值
     * @return 详情
     * @throws NullPointerException if {@code key} is null or {@code valueSupplier} is null.
     */
    public boolean put(String key, Object value) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(value);

        ENV.put(key, () -> value);
        return true;
    }

    /**
     * 设置值提供者的属性
     *
     * @param key 键
     * @param valueSupplier 值提供者
     * @return 详情
     * @throws NullPointerException if {@code key} is null or {@code valueSupplier} is null.
     */
    public boolean put(String key, Supplier<Object> valueSupplier) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(valueSupplier);

        ENV.put(key, valueSupplier);
        return true;
    }

    /**
     * 设置属性
     *
     * @param properties 属性
     * @return 详情
     * @throws NullPointerException if {@code properties} is null.
     */
    public boolean put(Properties properties) {
        Objects.requireNonNull(properties);

        properties.forEach((k, v) -> put((String) k, v));
        return true;
    }

    /**
     * 设置映射集
     *
     * @param maps 映射集
     * @return 详情
     * @throws NullPointerException if {@code maps} is null.
     */
    public boolean put(Map<String, Supplier<Object>> maps) {
        Objects.requireNonNull(maps);

        ENV.putAll(maps);
        return true;
    }

    /**
     * 通过键获取具体的可选值
     *
     * @param key 键
     * @return 具体的可选值
     * @throws NullPointerException if {@code key} is null.
     */
    public Optional<Object> get(String key) {
        Objects.requireNonNull(key);

        Supplier<Object> supplier = ENV.get(key);
        if (Objects.isNull(supplier)) {
            return Optional.empty();
        }
        return Optional.ofNullable(supplier.get());
    }

    /**
     * 游走环境属性池
     *
     * @param walkElement 游走节点
     * @throws NullPointerException if {@code walkElement} is null.
     */
    public void walk(BiConsumer<String, Object> walkElement) {
        Objects.requireNonNull(walkElement);

        ENV.forEach((k, v) -> walkElement.accept(k, v.get()));
    }
}
