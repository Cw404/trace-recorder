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
package cn.xusc.trace.handle;

import cn.xusc.trace.TraceRecorder;
import cn.xusc.trace.constant.RecordLabel;
import cn.xusc.trace.exception.TraceException;
import cn.xusc.trace.exception.TraceTimeoutException;
import cn.xusc.trace.util.concurrent.Disruptors;
import com.lmax.disruptor.TimeoutException;
import com.lmax.disruptor.dsl.Disruptor;
import java.util.Objects;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 异步处理器
 *
 * @author WangCai
 * @since 2.0
 */
public class AsyncTraceHandler extends BaseTraceHandler {

    /**
     * {@link Disruptor}
     */
    private final Disruptor<Disruptors.GeneralEvent> DISRUPTOR;

    /**
     * 缓冲大小
     */
    private static final int BUFFER_SIZE = 2 << 4;

    /**
     * 通用生产者
     */
    private Disruptors.GeneralProducer producer;

    /**
     * 任务处理者数量
     */
    private final int TASK_HANDLER_SIZE;

    /**
     * 基本构造
     *
     * @param recorder 跟踪记录仪
     */
    public AsyncTraceHandler(TraceRecorder recorder) {
        super(recorder);
        this.TASK_HANDLER_SIZE = 1;
        DISRUPTOR = Disruptors.generate(BUFFER_SIZE, new TaskHandlerFactory(), false);
        initConsumerModel(DISRUPTOR);
        initProducerModel(DISRUPTOR);
    }

    /**
     * 指定任务处理器数量构造
     *
     * @param recorder        跟踪记录仪
     * @param taskHandlerSize 任务处理器数量
     * @throws TraceException if {@code taskHandlerSize} is less 1
     */
    public AsyncTraceHandler(TraceRecorder recorder, int taskHandlerSize) {
        super(recorder);
        if (taskHandlerSize < 1) {
            throw new TraceException("taskHandlerSize < 1");
        }
        this.TASK_HANDLER_SIZE = taskHandlerSize;
        DISRUPTOR = Disruptors.generate(BUFFER_SIZE, new TaskHandlerFactory(), false);
        initConsumerModel(DISRUPTOR);
        initProducerModel(DISRUPTOR);
    }

    /**
     * 初始化消费者模型
     *
     * <p>根据{@link #TASK_HANDLER_SIZE}进行消费模型推断，选择合适的消费模型</p>
     *
     * @param disruptor {@link Disruptor}
     */
    private void initConsumerModel(Disruptor<Disruptors.GeneralEvent> disruptor) {
        Disruptors.GeneralConsumer consumer = Disruptors.consumer(disruptor);
        if (TASK_HANDLER_SIZE == 1) {
            /*
              构建没有事件竞争的消费者处理集群
             */
            consumer.addConsumptionPatterns((event, sequence, endOfBatch) -> {
                Task task = (Task) event.get();
                handling(task.getInfo(), task.getLabel(), task.getException(), task.getArgArray());
            });
        } else {
            /*
              构建有事件竞争的消费者处理集群
             */
            for (int i = 0; i < TASK_HANDLER_SIZE; i++) {
                consumer.addConsumptionPatterns(event -> {
                    Task task = (Task) event.get();
                    handling(task.getInfo(), task.getLabel(), task.getException(), task.getArgArray());
                });
            }
        }
        consumer.consume();
    }

    /**
     * 初始化生产者模型
     *
     * @param disruptor {@link Disruptor}
     */
    private void initProducerModel(Disruptor<Disruptors.GeneralEvent> disruptor) {
        this.producer = Disruptors.producer(disruptor);
    }

    /**
     * 信息处理
     *
     * <p>构建任务并提供</p>
     *
     * @param info     信息
     * @param label    记录标签
     * @param argArray 参数列表
     */
    @Override
    public void doHandle(String info, RecordLabel label, Object... argArray) {
        Task task = new Task(info, label, new TraceException(), argArray);
        producer.provide(() -> task);
    }

    @Override
    public void shutdown() {
        DISRUPTOR.shutdown();
    }

    @Override
    public void shutdown(long timeout, TimeUnit timeUnit) throws TraceTimeoutException {
        try {
            DISRUPTOR.shutdown(timeout, timeUnit);
        } catch (TimeoutException e) {
            throw new TraceTimeoutException(e);
        }
    }

    /**
     * 异步处理器详情
     *
     * @return 详情
     */
    @Override
    public String toString() {
        String producerStatue = Objects.nonNull(producer) ? "true" : "false";
        return (
            "AsyncTraceHandler{" +
            "DISRUPTOR=" +
            DISRUPTOR +
            ", producer=" +
            producerStatue +
            ", TASK_HANDLER_SIZE=" +
            TASK_HANDLER_SIZE +
            '}'
        );
    }

    /**
     * 任务
     *
     * <p>
     * 通过lombok组件{@link Setter}、{@link Getter}、{@link AllArgsConstructor}简化源代码可读性
     * </p>
     */
    @Setter
    @Getter
    @AllArgsConstructor
    private class Task {

        /**
         * 信息
         */
        private String info;
        /**
         * 记录标签
         */
        private RecordLabel label;
        /**
         * 异常
         */
        private Exception exception;
        /**
         * 参数列表
         */
        private Object[] argArray;
    }

    /**
     * 任务处理者工厂
     */
    private class TaskHandlerFactory implements ThreadFactory {

        /**
         * 原子性编号生成器
         */
        private final AtomicLong AL = new AtomicLong(1);

        @Override
        public Thread newThread(Runnable r) {
            return new TaskHandler(r, "TaskHandler-" + AL.getAndIncrement());
        }
    }

    /**
     * 任务处理者
     */
    private class TaskHandler extends Thread {

        public TaskHandler(Runnable target, String name) {
            super(target, name);
        }

        @Override
        public void run() {
            super.run();
        }
    }
}
