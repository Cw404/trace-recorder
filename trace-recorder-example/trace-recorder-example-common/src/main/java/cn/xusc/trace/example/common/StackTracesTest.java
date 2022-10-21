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

import static org.junit.jupiter.api.Assertions.*;

import cn.xusc.trace.common.util.StackTraces;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * 堆栈跟踪工具类测试
 *
 * @author wangcai
 */
public final class StackTracesTest {

    /**
     * 验证当前堆栈可用
     */
    @Test
    @DisplayName("Verify current stack is available")
    public void currentStackTest() {
        assertTrue(StackTraces.currentStackTraceElement().isPresent());
        assertTrue(StackTraces.currentFirstStackTraceElement().isPresent());
    }
}
