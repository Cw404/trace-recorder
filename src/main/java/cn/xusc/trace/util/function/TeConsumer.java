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
package cn.xusc.trace.util.function;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * 表示接受三个输入参数且不返回结果的操作
 *
 * @param <T> 操作的第一个参数的类型
 * @param <U> 操作的第二个参数的类型
 * @param <O> 操作的第三个参数的类型
 * @author WangCai
 * @see Consumer
 * @since 2.0
 */
@FunctionalInterface
public interface TeConsumer<T, U, O> {
    /**
     * 对给定的参数执行此操作
     *
     * @param t 第一个输入参数
     * @param u 第二个输入参数
     * @param o 第三个输入参数
     */
    void accept(T t, U u, O o);

    /**
     * 链接一个{@code TeConsumer} 三元消费操作，并返回一个带有执行顺序的三元消费者
     *
     * @param after 原始消费操作后执行的链接消费操作
     * @return 一个带有执行顺序的三元消费者
     * @throws NullPointerException if {@code after} is null
     */
    default TeConsumer<T, U, O> andThen(TeConsumer<? super T, ? super U, ? super O> after) {
        Objects.requireNonNull(after);

        return (l, c, r) -> {
            accept(l, c, r);
            after.accept(l, c, r);
        };
    }
}
