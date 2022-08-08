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
import cn.xusc.trace.config.TraceRecorderConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * 消息格式化工具类测试
 *
 * @author wangcai
 */
public class FormatsTest {

    private static TraceRecorder recorder;

    @BeforeEach
    @DisplayName("init Environment")
    private void initEnv() {
        recorder = new TraceRecorder(TraceRecorderConfig.builder().enableAsync(false).build());
    }

    @Test
    @DisplayName("Single value format")
    public void singleValueFormatTest() {
        recorder.log(Formats.format("{}", "hello Formats"));
    }

    @Test
    @DisplayName("Multiple value format")
    public void multipleValueFormatTest() {
        recorder.log(Formats.format("{} {}", "hello", "Formats"));
    }

    @Test
    @DisplayName("Multiple value format of escape")
    public void multipleValueFormatOfEscapeTest() {
        recorder.log(Formats.format("\\{} {} {}", "hello", "Formats"));
    }
}
