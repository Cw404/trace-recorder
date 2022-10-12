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
package cn.xusc.trace.example.core.util;

import static org.junit.jupiter.api.Assertions.*;

import cn.xusc.trace.core.enhance.InfoEnhancer;
import cn.xusc.trace.core.filter.InfoFilter;
import cn.xusc.trace.core.record.InfoRecorder;
import cn.xusc.trace.core.util.spi.TraceRecorderLoader;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * 跟踪记录仪加载器测试
 *
 * @author wangcai
 */
public class TraceRecorderLoaderTest {

    /**
     * 验证TraceRecorderLoader初始化是否可用
     *
     * @param traceRecorderLoader 跟踪记录仪加载器
     */
    @ParameterizedTest
    @MethodSource("generateTraceRecorderLoader")
    @DisplayName("verify TraceRecorderLoader initialization is available")
    public void verifyTraceRecorderLoaderAvailableTest(TraceRecorderLoader traceRecorderLoader) {
        assertNotNull(traceRecorderLoader);
    }

    /**
     * 验证findAll方法是否可用
     *
     * @param traceRecorderLoader 跟踪记录仪加载器
     */
    @ParameterizedTest
    @MethodSource("generateTraceRecorderLoader")
    @DisplayName("verify findAll method is available")
    public void verifyFindAllAvailableTest(TraceRecorderLoader traceRecorderLoader) {
        assertTrue(traceRecorderLoader.findAll().isPresent());
    }

    /**
     * 验证find方法是否可用
     *
     * @param traceRecorderLoader 跟踪记录仪加载器
     */
    @ParameterizedTest
    @MethodSource("generateTraceRecorderLoader")
    @DisplayName("verify find method is available")
    public void verifyFindAvailableTest(TraceRecorderLoader traceRecorderLoader) {
        assertTrue(traceRecorderLoader.find("test").isPresent());
        assertFalse(traceRecorderLoader.find("test1").isPresent());
    }

    /**
     * 生成跟踪记录仪加载器
     *
     * @return 跟踪记录仪加载器流
     */
    @DisplayName("generate TraceRecorderLoader")
    public static Stream<TraceRecorderLoader> generateTraceRecorderLoader() {
        return Stream.of(
            TraceRecorderLoader.getTraceRecorderLoader(InfoFilter.class),
            TraceRecorderLoader.getTraceRecorderLoader(InfoEnhancer.class),
            TraceRecorderLoader.getTraceRecorderLoader(InfoRecorder.class)
        );
    }
}
