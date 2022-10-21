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

import static org.junit.jupiter.api.Assertions.assertEquals;

import cn.xusc.trace.common.util.ThreadLocals;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * 线程局部变量表工具类测试
 *
 * @author wangcai
 */
public final class ThreadLocalsTest {

    /**
     * 验证基本使用
     */
    @Test
    @DisplayName("Verify basic use")
    public void basicUseTest() {
        ThreadLocals.put("name", "trace-recorder");
        assertEquals("trace-recorder", ThreadLocals.get("name"));
    }
}
