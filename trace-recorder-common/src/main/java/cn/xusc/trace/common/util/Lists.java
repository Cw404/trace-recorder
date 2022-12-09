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
import java.util.function.Predicate;
import lombok.experimental.UtilityClass;

/**
 * 列表工具类
 *
 * <p>
 * 通过lombok组件{@link UtilityClass}确保工具类的使用
 * </p>
 *
 * @author WangCai
 * @since 2.0
 */
@UtilityClass
public class Lists {

    /**
     * 获取列表元素的类名
     *
     * @param list 列表
     * @return 元素类名字符串
     */
    public String classNames(List<?> list) {
        if (Objects.isNull(list)) {
            return Strings.empty();
        }
        return classNames(list.iterator());
    }

    /**
     * 获取迭代器的元素类名
     *
     * @param iterator 迭代器
     * @return 元素类名字符串
     */
    public String classNames(Iterator<?> iterator) {
        if (Objects.isNull(iterator)) {
            return Strings.empty();
        }

        StringJoiner sj = new StringJoiner(", ", "[", "]");
        while (iterator.hasNext()) {
            sj.add(iterator.next().getClass().getName());
        }
        return sj.toString();
    }

    /**
     * 合并列表数据
     *
     * @param lists 列表集
     * @return 合并列表
     * @since 2.2
     */
    public List<?> merge(List<?>... lists) {
        if (lists.length == 0) {
            return Collections.emptyList();
        }
        List<Object> mergeList = new ArrayList<>();
        if (lists[0] instanceof FastList) {
            Iterator iterator;
            for (List fastList : lists) {
                iterator = fastList.iterator();
                while (iterator.hasNext()) {
                    mergeList.add(iterator.next());
                }
            }
            return mergeList;
        }

        for (List list : lists) {
            mergeList.addAll(list);
        }
        return mergeList;
    }

    /**
     * 列表元素转移
     *
     * @param from 源列表
     * @param to 转移列表
     * @return 转移列表
     * @param <E> 元素类型
     * @throws NullPointerException if {@code from} is null.
     * @throws NullPointerException if {@code to} is null.
     * @since 2.5.3
     */
    public <E> List<E> transfer(List<E> from, List<E> to) {
        Objects.requireNonNull(from);
        Objects.requireNonNull(to);

        if (from instanceof FastList) {
            Iterator iterator = from.iterator();
            while (iterator.hasNext()) {
                to.add((E) iterator.next());
            }
            return to;
        }

        to.addAll(from);
        return to;
    }

    /**
     * 统计列表中符合条件的列表数据
     *
     * @param list      列表数据
     * @param condition 条件
     * @param <T>       列表对象类型
     * @return 列表中符合条件的列表数据
     * @throws NullPointerException if {@code list} is null.
     * @throws NullPointerException if {@code condition} is null.
     * @since 2.4
     */
    public <T> List<T> statistic(List<T> list, Predicate<T> condition) {
        Objects.requireNonNull(list);
        Objects.requireNonNull(condition);

        ArrayList<T> statisticResult = new ArrayList<>();
        for (T statisticObj : list) {
            if (condition.test(statisticObj)) {
                statisticResult.add(statisticObj);
            }
        }
        return statisticResult;
    }
}
