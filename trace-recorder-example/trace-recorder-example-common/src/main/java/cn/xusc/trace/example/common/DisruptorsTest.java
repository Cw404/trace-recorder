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
package cn.xusc.trace.example.common;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import cn.xusc.trace.common.util.concurrent.Disruptors;
import cn.xusc.trace.common.util.function.TeConsumer;
import cn.xusc.trace.core.TraceRecorder;
import cn.xusc.trace.core.config.TraceRecorderConfig;
import com.lmax.disruptor.dsl.Disruptor;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * {@link Disruptor}工具类测试
 *
 * @author wangcai
 */
public class DisruptorsTest {

    /**
     * 基础Disruptors测试
     *
     * @param bufferSize 缓冲大小
     */
    @ParameterizedTest
    @ValueSource(ints = { 2, 2 << 1, 2 << 2 })
    @DisplayName("Base Disruptors")
    public void baseDisruptorsTest(int bufferSize) {
        assertNotNull(Disruptors.generate(bufferSize));
    }

    /**
     * 验证Disruptors组件
     *
     * @param bufferSize 缓冲大小
     */
    @ParameterizedTest
    @ValueSource(ints = { 2, 2 << 1, 2 << 2 })
    @DisplayName("Verify Disruptors components")
    public void verifyDisruptorsTest(int bufferSize) {
        Disruptor<Disruptors.GeneralEvent> disruptor = Disruptors.generate(bufferSize);
        Disruptors.GeneralConsumer consumer = Disruptors.consumer(disruptor);
        assertNotNull(consumer);
        Disruptors.GeneralProducer producer = Disruptors.producer(disruptor);
        assertNotNull(producer);
    }

    /**
     * 生产-消费模型，单个共享消费者
     *
     * @param bufferSize 缓冲大小
     * @param teConsumer 事件消费者
     * @param supplier 事件提供者
     * @param count 生产事件数
     * @param <T> 事件类型
     */
    @ParameterizedTest
    @MethodSource("generateSharedArgs")
    @DisplayName("Production-consumption model, single shared consumer")
    public <T> void singleSharedConsumerModelDisruptorsTest(
        int bufferSize,
        TeConsumer<? super Disruptors.GeneralEvent, ? super Long, ? super Boolean> teConsumer,
        Supplier<T> supplier,
        int count
    ) {
        Disruptor<Disruptors.GeneralEvent> disruptor = Disruptors.generate(bufferSize);
        Disruptors.consumer(disruptor).addConsumptionPatterns(teConsumer).consume();
        Disruptors.producer(disruptor).provide(supplier, count);
    }

    /**
     * 生产-消费模型，多个共享消费者
     *
     * @param bufferSize 缓冲大小
     * @param teConsumer 事件消费者
     * @param supplier 事件提供者
     * @param count 生产事件数
     * @param <T> 事件类型
     */
    @ParameterizedTest
    @MethodSource("generateSharedArgs")
    @DisplayName("Production-consumption model, multiple shared consumer")
    public <T> void multipleSharedConsumerModelDisruptorsTest(
        int bufferSize,
        TeConsumer<? super Disruptors.GeneralEvent, ? super Long, ? super Boolean> teConsumer,
        Supplier<T> supplier,
        int count
    ) {
        Disruptor<Disruptors.GeneralEvent> disruptor = Disruptors.generate(bufferSize);
        Disruptors
            .consumer(disruptor)
            .addConsumptionPatterns(teConsumer)
            .addConsumptionPatterns(teConsumer)
            .addConsumptionPatterns(teConsumer)
            .consume();
        Disruptors.producer(disruptor).provide(supplier, count);
    }

    /**
     * 生产-消费模型，单个非共享消费者
     *
     * @param bufferSize 缓冲大小
     * @param consumer 事件消费者
     * @param supplier 事件提供者
     * @param count 生产事件数
     * @param <T> 事件类型
     */
    @ParameterizedTest
    @MethodSource("generateNoSharedArgs")
    @DisplayName("Production-consumption model, single no shared of consumer")
    public <T> void singleNoSharedConsumerModelDisruptorsTest(
        int bufferSize,
        Consumer<? super Disruptors.GeneralEvent> consumer,
        Supplier<T> supplier,
        int count
    ) {
        Disruptor<Disruptors.GeneralEvent> disruptor = Disruptors.generate(bufferSize);
        Disruptors.consumer(disruptor).addConsumptionPatterns(consumer).consume();
        Disruptors.producer(disruptor).provide(supplier, count);
    }

    /**
     * 生产-消费模型，多个非共享消费者
     *
     * @param bufferSize 缓冲大小
     * @param consumer 事件消费者
     * @param supplier 事件提供者
     * @param count 生产事件数
     * @param <T> 事件类型
     */
    @ParameterizedTest
    @MethodSource("generateNoSharedArgs")
    @DisplayName("Production-consumption model, multiple no shared of consumer")
    public <T> void multipleNoSharedConsumerModelDisruptorsTest(
        int bufferSize,
        Consumer<? super Disruptors.GeneralEvent> consumer,
        Supplier<T> supplier,
        int count
    ) {
        Disruptor<Disruptors.GeneralEvent> disruptor = Disruptors.generate(bufferSize);
        Disruptors
            .consumer(disruptor)
            .addConsumptionPatterns(consumer)
            .addConsumptionPatterns(consumer)
            .addConsumptionPatterns(consumer)
            .consume();
        Disruptors.producer(disruptor).provide(supplier, count);
    }

    /**
     * 跟踪记录仪
     */
    private static TraceRecorder recorder;

    /**
     * 初始化环境
     */
    @BeforeEach
    @DisplayName("init Environment")
    public void initEnv() {
        recorder = new TraceRecorder(TraceRecorderConfig.builder().enableAsync(false).build());
    }

    /**
     * 生成一组共享参数
     *
     * @return 一组共享参数
     */
    @DisplayName("generate a group of shared args")
    private static Stream<Arguments> generateSharedArgs() {
        AtomicInteger ai = new AtomicInteger(0);
        TeConsumer<? super Disruptors.GeneralEvent, ? super Long, ? super Boolean> teConsumer = (
                event,
                sequence,
                endOfBatch
            ) ->
            recorder.log("thread: {} - value: {}", Thread.currentThread().getName(), event.get());
        Supplier supplier = ai::incrementAndGet;
        return Stream.of(Arguments.arguments(2, teConsumer, supplier, 2));
    }

    /**
     * 生成一组非共享参数
     *
     * @return 一组非共享参数
     */
    @DisplayName("generate a group of no shared args")
    private static Stream<Arguments> generateNoSharedArgs() {
        AtomicInteger ai = new AtomicInteger(0);
        Consumer<? super Disruptors.GeneralEvent> consumer = event ->
            recorder.log("thread: {} - value: {}", Thread.currentThread().getName(), event.get());
        Supplier supplier = ai::incrementAndGet;
        return Stream.of(Arguments.arguments(2, consumer, supplier, 2));
    }
}
