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
package cn.xusc.trace.util;

import cn.xusc.trace.exception.TraceException;
import java.util.Objects;
import lombok.experimental.UtilityClass;

/**
 * 数组工具类
 *
 * <p>
 * 通过lombok组件{@link UtilityClass}确保工具类的使用
 * </p>
 *
 * @author WangCai
 * @since 2.5
 */
@UtilityClass
public class Arrays {

    /**
     * 反转
     *
     * @param arrays 数组
     * @throws NullPointerException if {@code arrays} is null.
     */
    public void reverse(Object[] arrays) {
        Objects.requireNonNull(arrays);

        int length = arrays.length;
        for (int i = 0, mid = length >> 1, j = length - 1; i < mid; i++, j--) {
            swap(arrays, i, j);
        }
    }

    /**
     * 交换
     *
     * @param arrays 数组
     * @param from 起始索引
     * @param to 目标索引
     * @throws NullPointerException if {@code arrays} is null.
     */
    public static void swap(Object[] arrays, int from, int to) {
        Objects.requireNonNull(arrays);

        int length = arrays.length;
        int bound = length - 1;
        if (Boolean.logicalOr(Boolean.logicalOr(0 > from, from > bound), Boolean.logicalOr(0 > to, to > bound))) {
            throw new TraceException(" index out of bounds");
        }

        Object t = arrays[from];
        arrays[from] = arrays[to];
        arrays[to] = t;
    }
}
