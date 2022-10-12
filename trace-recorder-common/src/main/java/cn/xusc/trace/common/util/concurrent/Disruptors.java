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
package cn.xusc.trace.common.util.concurrent;

import cn.xusc.trace.common.exception.TraceException;
import cn.xusc.trace.common.util.function.TeConsumer;
import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadFactory;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Disruptor工具类
 *
 * <p>
 * 并发环缓冲区数据结构，生产消费模型
 * </p>
 *
 * @author WangCai
 * @see <a href="https://lmax-exchange.github.io/disruptor/user-guide/index.html">Disruptor</a>
 * @since 2.0
 */
public final class Disruptors {

    /**
     * 禁止实例化
     */
    private Disruptors() {}

    /**
     * 生成Disruptor
     *
     * @param bufferSize 缓冲大小
     * @return {@link Disruptor}
     * @throws TraceException if {@code bufferSize} is less 1
     */
    public static Disruptor<GeneralEvent> generate(int bufferSize) {
        return doGenerate(bufferSize, DaemonThreadFactory.INSTANCE, false, new BlockingWaitStrategy());
    }

    /**
     * 生成Disruptor
     *
     * @param bufferSize         缓冲大小
     * @param threadFactory      线程工厂
     * @param isMultipleProducer 是否多个生产者
     * @return {@link Disruptor}
     * @throws TraceException       if {@code bufferSize} is less 1
     * @throws NullPointerException if {@code threadFactory} is null
     */
    public static Disruptor<GeneralEvent> generate(
        int bufferSize,
        ThreadFactory threadFactory,
        boolean isMultipleProducer
    ) {
        return doGenerate(bufferSize, threadFactory, isMultipleProducer, new BlockingWaitStrategy());
    }

    /**
     * 生成Disruptor
     *
     * @param bufferSize         缓冲大小
     * @param threadFactory      线程工厂
     * @param isMultipleProducer 是否多个生产者
     * @param waitStrategy       等待策略
     * @return {@link Disruptor}
     * @throws TraceException       if {@code bufferSize} is less 1
     * @throws NullPointerException if {@code threadFactory} is null
     * @throws NullPointerException if {@code waitStrategy} is null
     */
    public static Disruptor<GeneralEvent> generate(
        int bufferSize,
        ThreadFactory threadFactory,
        boolean isMultipleProducer,
        WaitStrategy waitStrategy
    ) {
        return doGenerate(bufferSize, threadFactory, isMultipleProducer, waitStrategy);
    }

    /**
     * 真正生成Disruptor
     *
     * @param bufferSize         缓冲大小
     * @param threadFactory      线程工厂
     * @param isMultipleProducer 是否多个生产者
     * @param waitStrategy       等待策略
     * @return {@link Disruptor}
     * @throws TraceException       if {@code bufferSize} is less 1
     * @throws NullPointerException if {@code threadFactory} is null
     * @throws NullPointerException if {@code waitStrategy} is null
     */
    private static Disruptor<GeneralEvent> doGenerate(
        int bufferSize,
        ThreadFactory threadFactory,
        boolean isMultipleProducer,
        WaitStrategy waitStrategy
    ) {
        if (bufferSize < 1) {
            throw new TraceException("bufferSize < 1");
        }
        Objects.requireNonNull(threadFactory);
        Objects.requireNonNull(waitStrategy);

        return new Disruptor(
            new GeneralEventFactory(),
            bufferSize,
            threadFactory,
            isMultipleProducer ? ProducerType.MULTI : ProducerType.SINGLE,
            waitStrategy
        );
    }

    /**
     * 生成一个通用生产者
     *
     * @param disruptor 需要生产的Disruptor
     * @return 通用生产者
     * @throws NullPointerException if {@code disruptor} is null
     */
    public static GeneralProducer producer(Disruptor<GeneralEvent> disruptor) {
        return new GeneralProducer(disruptor);
    }

    /**
     * 生成一个通用消费者
     *
     * @param disruptor 需要消费的Disruptor
     * @return 通用消费者
     * @throws NullPointerException if {@code disruptor} is null
     */
    public static GeneralConsumer consumer(Disruptor<GeneralEvent> disruptor) {
        return new GeneralConsumer(disruptor);
    }

    /**
     * 通用生产者
     *
     * <p>生产事件</p>
     */
    public static class GeneralProducer<T> {

        /**
         * {@link RingBuffer}
         */
        private final RingBuffer<GeneralEvent> RING_BUFFER;

        /**
         * 基础构造
         *
         * @param disruptor {@link Disruptor}
         * @throws NullPointerException if {@code disruptor} is null
         */
        public GeneralProducer(Disruptor<GeneralEvent> disruptor) {
            Objects.requireNonNull(disruptor);

            this.RING_BUFFER = disruptor.getRingBuffer();
        }

        /**
         * 提供一个提供者
         *
         * @param supplier 提供者
         * @return 当前通用生产者
         * @throws NullPointerException if {@code supplier} is null
         */
        public GeneralProducer<T> provide(Supplier<T> supplier) {
            Objects.requireNonNull(supplier);

            RING_BUFFER.publishEvent((event, sequence, supplier1) -> event.set(supplier1.get()), supplier);
            return this;
        }

        /**
         * 提供{@code count}数量的相同提供者
         *
         * @param supplier 提供者
         * @param count    提供数量
         * @return 当前通用生产者
         * @throws NullPointerException if {@code supplier} is null
         * @throws TraceException       if {@code count} is less 1
         */
        public GeneralProducer<T> provide(Supplier<T> supplier, int count) {
            Objects.requireNonNull(supplier);
            if (count < 1) {
                throw new TraceException("count < 1");
            }
            for (int i = 0; i < count; i++) {
                RING_BUFFER.publishEvent((event, sequence, supplier1) -> event.set(supplier1.get()), supplier);
            }
            return this;
        }
    }

    /**
     * 通用消费者
     */
    public static class GeneralConsumer {

        /**
         * {@link Disruptor}
         */
        private final Disruptor<GeneralEvent> DISRUPTOR;
        /**
         * 无竞争的事件处理器集
         */
        private final List<EventHandler<GeneralEvent>> EVENT_HANDLERS;
        /**
         * 有竞争的工作处理器集
         */
        private final List<WorkHandler<GeneralEvent>> WORK_HANDLERS;

        /**
         * 基础构造
         *
         * @param disruptor {@link Disruptor}
         * @throws NullPointerException if {@code disruptor} is null
         */
        public GeneralConsumer(Disruptor<GeneralEvent> disruptor) {
            Objects.requireNonNull(disruptor);

            this.DISRUPTOR = disruptor;
            this.EVENT_HANDLERS = new ArrayList<>();
            this.WORK_HANDLERS = new ArrayList<>();
        }

        /**
         * 添加一个消费方式
         *
         * <p>线程没有竞争，事件会被消耗等同线程总数</p>
         *
         * @param teConsumer 消费者接口
         * @return 当前通用消费者
         */
        public GeneralConsumer addConsumptionPatterns(
            TeConsumer<? super GeneralEvent, ? super Long, ? super Boolean> teConsumer
        ) {
            EventHandler<GeneralEvent> handler = teConsumer::accept;
            EVENT_HANDLERS.add(handler);
            return this;
        }

        /**
         * 添加一个消费方式
         *
         * <p>线程有竞争，事件只会被消费一次</p>
         *
         * @param consumer 消费者接口
         * @return 当前通用消费者
         */
        public GeneralConsumer addConsumptionPatterns(Consumer<? super GeneralEvent> consumer) {
            WorkHandler<GeneralEvent> handler = consumer::accept;
            WORK_HANDLERS.add(handler);
            return this;
        }

        /**
         * 消费
         *
         * <p>处理事件分发，并启动消费</p>
         */
        public void consume() {
            if (!EVENT_HANDLERS.isEmpty()) {
                DISRUPTOR.handleEventsWith(EVENT_HANDLERS.toArray(new EventHandler[0]));
            }
            if (!WORK_HANDLERS.isEmpty()) {
                DISRUPTOR.handleEventsWithWorkerPool(WORK_HANDLERS.toArray(new WorkHandler[0]));
            }

            DISRUPTOR.start();
        }
    }

    /**
     * 通用事件工厂
     */
    private static class GeneralEventFactory implements EventFactory<GeneralEvent> {

        /**
         * 实例化{@code GeneralEvent}对象
         *
         * @return 实例对象
         */
        @Override
        public GeneralEvent newInstance() {
            return new GeneralEvent();
        }
    }

    /**
     * 通用事件
     *
     * @param <T> 事件对象 or 事件值
     */
    public static class GeneralEvent<T> {

        /**
         * 事件对象 or 事件值
         */
        private T value;

        /**
         * 设置事件值
         *
         * @param value 事件值
         */
        public void set(T value) {
            this.value = value;
        }

        /**
         * 获取事件值
         *
         * @return 事件值
         */
        public T get() {
            return value;
        }
    }
}
