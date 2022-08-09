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
import java.util.Iterator;
import java.util.Objects;

/**
 * 备忘录
 *
 * @author WangCai
 * @since 2.2
 */
public final class Memo<T> {

    /**
     * 最近的回忆点
     */
    private RecallPoint head;

    /**
     * 默认构造
     */
    public Memo() {}

    /**
     * 存储事物并获取对应回忆点标签
     *
     * @param value 事物值
     * @return 回忆点标签
     * @throws NullPointerException if {@code value} is null
     */
    public String storage(T value) {
        Objects.requireNonNull(value);

        RecallPoint h = head;
        String label = computeLabel();
        if (Objects.isNull(h)) {
            h = new RecallPoint(label, value, null);
        } else {
            h = new RecallPoint(label, value, h);
        }
        head = h;
        return label;
    }

    /**
     * 根据回忆点获取对应事物值
     *
     * @param label 回忆点
     * @return 事物值
     * @throws NullPointerException if {@code label} is null
     * @throws TraceException       if {@code head} is null
     */
    public T read(String label) {
        Objects.requireNonNull(label);

        RecallPoint h = head;
        if (Objects.isNull(h)) throw new TraceException("no store value");

        for (Object point : h) {
            RecallPoint rp = (RecallPoint) point;
            if (Objects.equals(rp.LABEL, label)) {
                return (T) rp.VALUE;
            }
        }
        return null;
    }

    /**
     * 计算标签
     *
     * @return 标签
     */
    public String computeLabel() {
        return Randoms.uuid("");
    }

    /**
     * 回忆点
     *
     * @param <T> 事物值
     */
    private class RecallPoint<T> implements Iterable<T> {

        /**
         * 回忆点标签
         */
        private final String LABEL;
        /**
         * 事物值
         */
        private final T VALUE;
        /**
         * 上一个回忆点
         */
        private final RecallPoint PRE;

        /**
         * 回忆点构造
         *
         * @param label 回忆点标签
         * @param value 事物值
         * @param pre   上一个回忆点
         */
        public RecallPoint(String label, T value, RecallPoint pre) {
            this.LABEL = label;
            this.VALUE = value;
            this.PRE = pre;
        }

        /**
         * 迭代当前回忆点
         *
         * @return 回忆点迭代器
         */
        @Override
        public Iterator<T> iterator() {
            return new Iterator() {
                /**
                 * 当前回忆点
                 */
                private RecallPoint point = RecallPoint.this;

                @Override
                public boolean hasNext() {
                    return Objects.nonNull(point);
                }

                @Override
                public T next() {
                    RecallPoint recall = point;
                    point = recall.PRE;
                    return (T) recall;
                }
            };
        }
    }
}
