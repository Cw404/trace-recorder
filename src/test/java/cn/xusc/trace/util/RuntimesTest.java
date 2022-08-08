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

import cn.xusc.trace.TraceRecorder;
import cn.xusc.trace.annotation.CloseOrder;
import cn.xusc.trace.config.TraceRecorderConfig;
import java.io.Closeable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * 运行时工具类测试
 *
 * @author wangcai
 */
public class RuntimesTest {

    @Test
    @DisplayName("Base clean task")
    public void baseCleanTest() {
        Runtimes.addCleanTask(new ShowInfoOfJVMClose());
    }

    @Test
    @DisplayName("order close task")
    public void orderCloseCleanTest() {
        Runtimes.addCleanTask(new ShowInfoOfJVMCloseSecond());
        Runtimes.addCleanTask(new ShowInfoOfJVMCloseFirst());
    }

    private static TraceRecorder recorder;

    @BeforeEach
    @DisplayName("init Environment")
    private void initEnv() {
        recorder = new TraceRecorder(TraceRecorderConfig.builder().enableAsync(false).build());
    }

    @SuppressWarnings("InnerClassMayBeStatic")
    private class ShowInfoOfJVMClose implements Closeable {

        @Override
        public void close() {
            recorder.log("ShowInfoOfJVMClose closed");
        }
    }

    @SuppressWarnings("InnerClassMayBeStatic")
    @CloseOrder(1)
    private class ShowInfoOfJVMCloseFirst implements Closeable {

        @Override
        public void close() {
            recorder.log("ShowInfoOfJVMCloseFirst closed");
        }
    }

    @SuppressWarnings("InnerClassMayBeStatic")
    @CloseOrder(2)
    private class ShowInfoOfJVMCloseSecond implements Closeable {

        @Override
        public void close() {
            recorder.log("ShowInfoOfJVMCloseSecond closed");
        }
    }
}
