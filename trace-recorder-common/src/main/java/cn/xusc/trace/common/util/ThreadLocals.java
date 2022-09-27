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
package cn.xusc.trace.common.util;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;

/**
 * 线程局部变量表工具类
 *
 * <p>
 * 通过lombok组件{@link UtilityClass}确保工具类的使用
 * </p>
 *
 * @author WangCai
 * @since 2.5
 */
@UtilityClass
public class ThreadLocals {

    /**
     * 线程局部变量
     */
    private final ThreadLocal<Map<String, Object>> THREAD_LOCAL = ThreadLocal.withInitial(() -> new HashMap<>());

    /**
     * 设置一个线程局部表键值
     *
     * @param key 局部变量键
     * @param value 局部变量值
     * @return 局部变量键的旧值
     * @throws NullPointerException if {@code key} is null.
     * @throws NullPointerException if {@code value} is null.
     */
    public Object put(String key, Object value) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(value);

        return innerGet().put(key, value);
    }

    /**
     * 获取当前线程局部变量表的键值
     *
     * @param key 局部变量键
     * @return 局部变量键对应的值
     * @throws NullPointerException if {@code key} is null.
     */
    public Object get(String key) {
        Objects.requireNonNull(key);

        return innerGet().get(key);
    }

    /**
     * 获取当前线程局部变量表的键集
     *
     * @return 键集
     */
    public Set<String> keys() {
        return innerGet().keySet();
    }

    /**
     * 获取当前线程局部变量表匹配的键集
     *
     * @param keyPredicate 键断言
     * @return 匹配键集
     * @throws NullPointerException if {@code keyPredicate} is null.
     */
    public Set<String> keys(Predicate<String> keyPredicate) {
        Objects.requireNonNull(keyPredicate);

        return keys().stream().filter(keyPredicate::test).collect(Collectors.toSet());
    }

    /**
     * 获取当前线程局部变量表的值集
     *
     * @return 值集
     */
    public Collection<Object> values() {
        return innerGet().values();
    }

    /**
     * 获取当前线程局部变量表匹配的值集
     *
     * @param valuePredicate 值断言
     * @return 匹配值集
     * @throws NullPointerException if {@code valuePredicate} is null.
     */
    public Collection<Object> values(Predicate<Object> valuePredicate) {
        Objects.requireNonNull(valuePredicate);

        return values().stream().filter(valuePredicate::test).collect(Collectors.toList());
    }

    /**
     * 获取当前线程局部变量表的键值集
     *
     * @return 键值集
     */
    public Set<Map.Entry<String, Object>> entrySet() {
        return innerGet().entrySet();
    }

    /**
     * 获取当前线程局部变量表并游走元素
     *
     * @param walkElement 游走的元素函数
     * @throws NullPointerException if {@code walkElement} is null.
     */
    public void walk(BiFunction<String, Object, Boolean> walkElement) {
        Objects.requireNonNull(walkElement);

        for (Map.Entry<String, Object> entry : innerGet().entrySet()) {
            if (!walkElement.apply(entry.getKey(), entry.getValue())) {
                break;
            }
        }
    }

    /**
     * 获取当前线程局部变量表
     *
     * @return 局部变量表
     */
    private Map<String, Object> innerGet() {
        return THREAD_LOCAL.get();
    }
}
