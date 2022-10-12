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

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;

/**
 * 映射工具类
 *
 * <p>
 * 通过lombok组件{@link UtilityClass}确保工具类的使用
 * </p>
 *
 * @author WangCai
 * @since 2.5
 */
@UtilityClass
public class Maps {

    /**
     * 获取映射匹配的键集
     * @param map 映射
     * @param keyPredicate 键断言
     * @return 匹配键集
     * @param <K> 键的类型
     * @param <V> 值的类型
     * @throws NullPointerException if {@code map} is null.
     * @throws NullPointerException if {@code keyPredicate} is null.
     */
    public <K, V> Set<K> keys(Map<K, V> map, Predicate<K> keyPredicate) {
        Objects.requireNonNull(map);
        Objects.requireNonNull(keyPredicate);

        return map.keySet().stream().filter(keyPredicate::test).collect(Collectors.toSet());
    }

    /**
     * 获取映射匹配的值集
     *
     * @param map 映射
     * @param valuePredicate 值断言
     * @return 匹配值集
     * @param <K> 键的类型
     * @param <V> 值的类型
     * @throws NullPointerException if {@code map} is null.
     * @throws NullPointerException if {@code valuePredicate} is null.
     */
    public <K, V> Collection<V> values(Map<K, V> map, Predicate<V> valuePredicate) {
        Objects.requireNonNull(map);
        Objects.requireNonNull(valuePredicate);

        return map.values().stream().filter(valuePredicate::test).collect(Collectors.toList());
    }

    /**
     * 游走映射元素
     *
     * @param map 映射
     * @param walkElement 游走的元素函数
     * @param <K> 键的类型
     * @param <V> 值的类型
     * @throws NullPointerException if {@code map} is null.
     * @throws NullPointerException if {@code walkElement} is null.
     */
    public <K, V> void walk(Map<K, V> map, BiFunction<K, V, Boolean> walkElement) {
        Objects.requireNonNull(map);
        Objects.requireNonNull(walkElement);

        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (!walkElement.apply(entry.getKey(), entry.getValue())) {
                break;
            }
        }
    }

    /**
     * 游走映射元素，直到最后
     *
     * @param map 映射
     * @param walkElement 游走的元素函数
     * @param <K> 键的类型
     * @param <V> 值的类型
     * @throws NullPointerException if {@code map} is null.
     * @throws NullPointerException if {@code walkElement} is null.
     */
    public <K, V> void walkEnd(Map<K, V> map, BiConsumer<K, V> walkElement) {
        Objects.requireNonNull(map);
        Objects.requireNonNull(walkElement);

        for (Map.Entry<K, V> entry : map.entrySet()) {
            walkElement.accept(entry.getKey(), entry.getValue());
        }
    }
}
