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

import cn.xusc.trace.common.annotation.CloseOrder;
import cn.xusc.trace.common.util.Runtimes;
import cn.xusc.trace.core.TraceRecorder;
import cn.xusc.trace.core.config.TraceRecorderConfig;
import java.io.Closeable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * 运行时工具类测试
 *
 * @author wangcai
 */
public final class RuntimesTest {

    /**
     * 基础清理任务
     */
    @Test
    @DisplayName("Base clean task")
    public void baseCleanTest() {
        Runtimes.addCleanTask(new ShowInfoOfJVMClose());
    }

    /**
     * 顺序关闭任务
     */
    @Test
    @DisplayName("order close task")
    public void orderCloseCleanTest() {
        Runtimes.addCleanTask(new ShowInfoOfJVMCloseSecond());
        Runtimes.addCleanTask(new ShowInfoOfJVMCloseFirst());
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
     * 显示JVM关闭的信息
     */
    private class ShowInfoOfJVMClose implements Closeable {

        @Override
        public void close() {
            recorder.log("ShowInfoOfJVMClose closed");
        }
    }

    /**
     * 第一显示JVM关闭的信息
     */
    @CloseOrder(1)
    private class ShowInfoOfJVMCloseFirst implements Closeable {

        @Override
        public void close() {
            recorder.log("ShowInfoOfJVMCloseFirst closed");
        }
    }

    /**
     * 第二显示JVM关闭的信息
     */
    @CloseOrder(2)
    private class ShowInfoOfJVMCloseSecond implements Closeable {

        @Override
        public void close() {
            recorder.log("ShowInfoOfJVMCloseSecond closed");
        }
    }
}
