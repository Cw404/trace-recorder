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

import cn.xusc.trace.annotation.CloseOrder;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import lombok.experimental.UtilityClass;

/**
 * 运行时工具类
 *
 * <p>
 * 通过lombok组件{@link UtilityClass}确保工具类的使用
 * </p>
 *
 * @author WangCai
 * @since 1.0
 */
@UtilityClass
public class Runtimes {

    /**
     * 关闭处理队列
     */
    private final List<AutoCloseable> CLOSEABLES = new ArrayList<>();

    static {
        Runtime.getRuntime().addShutdownHook(new ShowdownCleaner());
    }

    /**
     * 添加一个清理任务
     *
     * <p>
     * 会根据{@link CloseOrder#value()}保证执行的顺序准确性
     * </p>
     *
     * @param closeable 关闭对象
     */
    public void addCleanTask(AutoCloseable closeable) {
        CLOSEABLES.add(closeable);

        /*
          确保顺序准确；没有自定义顺序，默认排列最前
         */
        if (CLOSEABLES.size() > 0) {
            CLOSEABLES.sort(
                Comparator.comparing(closeable1 -> {
                    CloseOrder closeOrder = closeable1.getClass().getAnnotation(CloseOrder.class);
                    return Objects.isNull(closeOrder) ? Integer.MIN_VALUE : closeOrder.value();
                })
            );
        }
    }

    /**
     * JVM关闭时的清理线程
     */
    private class ShowdownCleaner extends Thread {

        @Override
        public void run() {
            FastList<Exception> exceptions = new FastList<>(Exception.class);

            for (AutoCloseable closeable : CLOSEABLES) {
                try {
                    closeable.close();
                } catch (Exception e) {
                    /*
                      一个任务的失败不能影响其它任务的正常执行
                     */
                    exceptions.add(new RuntimeException(e));
                }
            }
            for (Exception exception : exceptions) {
                /*
                  失败任务堆栈输出
                 */
                exception.printStackTrace();
                /*
                  失败任务排版换行
                 */
                System.out.println();
            }
        }
    }
}
