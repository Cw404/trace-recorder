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

import cn.xusc.trace.common.util.RenderEngine;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * 渲染引擎测试
 *
 * @author wangcai
 */
public final class RenderEngineTest {

    /**
     * 渲染引擎
     */
    private RenderEngine renderEngine;

    /**
     * 初始化环境
     */
    @BeforeEach
    public void initEnvironment() {
        renderEngine = new RenderEngine("this is ${what}");
    }

    /**
     * 渲染内容
     *
     * @param key 键
     * @param word 词
     */
    @ParameterizedTest
    @MethodSource("generateReaderArgs")
    @DisplayName("Storage value")
    public void readerContent(String key, String word) {
        renderEngine.register(key, word);
        assertEquals("this is TraceRecorder", renderEngine.renderContent());
    }

    /**
     * 生成一组渲染参数
     *
     * @return 一组渲染参数
     */
    @DisplayName("generate a group of reader args")
    private static Stream<Arguments> generateReaderArgs() {
        return Stream.of(Arguments.arguments("what", "TraceRecorder"));
    }
}
