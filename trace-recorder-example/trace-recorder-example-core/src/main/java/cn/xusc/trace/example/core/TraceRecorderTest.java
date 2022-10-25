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
package cn.xusc.trace.example.core;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import cn.xusc.trace.common.exception.TraceClosedException;
import cn.xusc.trace.common.exception.TraceException;
import cn.xusc.trace.common.util.Formats;
import cn.xusc.trace.common.util.Strings;
import cn.xusc.trace.core.TraceRecorder;
import cn.xusc.trace.core.TraceRecorderEnvironment;
import cn.xusc.trace.core.TraceRecorderVersion;
import cn.xusc.trace.core.config.TraceRecorderConfig;
import cn.xusc.trace.core.enhance.*;
import cn.xusc.trace.core.filter.InfoFilter;
import cn.xusc.trace.core.filter.RecordLabelInfoFilter;
import cn.xusc.trace.core.record.ConsoleInfoRecorder;
import cn.xusc.trace.core.record.InfoRecorder;
import cn.xusc.trace.core.util.TraceRecorders;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * 跟踪记录仪测试
 *
 * @author wangcai
 */
public class TraceRecorderTest {

    /**
     * 基础记录
     *
     * @param recorder 跟踪记录仪
     */
    @ParameterizedTest
    @MethodSource("generateSyncTraceRecorder")
    @DisplayName("Base record")
    public void baseRecordTest(TraceRecorder recorder) {
        recorder.log("base record");
    }

    /**
     * 异步记录
     *
     * @param recorder 跟踪记录仪
     */
    @ParameterizedTest
    @MethodSource("generateAsyncTraceRecorder")
    @DisplayName("Async record")
    public void asyncRecordTest(TraceRecorder recorder) {
        recorder.log("async record");
    }

    /**
     * 配置记录
     *
     * @param recorder 跟踪记录仪
     */
    @ParameterizedTest
    @MethodSource("generateConfigTraceRecorder")
    @DisplayName("Config record")
    public void configRecordTest(TraceRecorder recorder) {
        recorder.log("config record");
    }

    /**
     * 基础格式记录
     *
     * @param recorder 跟踪记录仪
     */
    @ParameterizedTest
    @MethodSource("generateSyncTraceRecorder")
    @DisplayName("Base record of format")
    public void baseRecordOfFormatTest(TraceRecorder recorder) {
        recorder.log("base {}", "record");
    }

    /**
     * 隐藏记录
     *
     * @param recorder 跟踪记录仪
     */
    @ParameterizedTest
    @MethodSource("generateSyncTraceRecorder")
    @DisplayName("hide record")
    public void hideRecordTest(TraceRecorder recorder) {
        recorder.nolog("hide record");
    }

    /**
     * 隐藏格式记录
     *
     * @param recorder 跟踪记录仪
     */
    @ParameterizedTest
    @MethodSource("generateSyncTraceRecorder")
    @DisplayName("hide record of format")
    public void hideRecordOfFormatTest(TraceRecorder recorder) {
        recorder.nolog("hide {}", "record");
    }

    /**
     * 添加一个信息过滤器
     *
     * @param recorder 跟踪记录仪
     */
    @ParameterizedTest
    @MethodSource("generateSyncTraceRecorder")
    @DisplayName("add a infoFilter")
    public void addInfoFilterTest(TraceRecorder recorder) {
        assertTrue(recorder.addInfoFilter(mock(InfoFilter.class)));
    }

    /**
     * 添加一个信息增强器
     *
     * @param recorder 跟踪记录仪
     */
    @ParameterizedTest
    @MethodSource("generateSyncTraceRecorder")
    @DisplayName("add a infoEnhancer")
    public void addInfoEnhancerTest(TraceRecorder recorder) {
        assertTrue(recorder.addInfoEnhancer(mock(InfoEnhancer.class)));
    }

    /**
     * 添加一个信息记录器
     *
     * @param recorder 跟踪记录仪
     */
    @ParameterizedTest
    @MethodSource("generateSyncTraceRecorder")
    @DisplayName("add a infoRecorder")
    public void addInfoRecorderTest(TraceRecorder recorder) {
        assertTrue(recorder.addInfoRecorder(mock(InfoRecorder.class)));
    }

    /**
     * 移除一个信息过滤器
     *
     * @param recorder 跟踪记录仪
     */
    @ParameterizedTest
    @MethodSource("generateSyncTraceRecorder")
    @DisplayName("remove a infoFilter")
    public void removeInfoFilterTest(TraceRecorder recorder) {
        InfoFilter infoFilter = mock(InfoFilter.class);
        recorder.addInfoFilter(infoFilter);
        assertTrue(recorder.removeInfoFilter(infoFilter));
    }

    /**
     * 移除一个信息增强器
     *
     * @param recorder 跟踪记录仪
     */
    @ParameterizedTest
    @MethodSource("generateSyncTraceRecorder")
    @DisplayName("remove a infoEnhancer")
    public void removeInfoEnhancerTest(TraceRecorder recorder) {
        InfoEnhancer infoEnhancer = mock(InfoEnhancer.class);
        recorder.addInfoEnhancer(infoEnhancer);
        assertTrue(recorder.removeInfoEnhancer(infoEnhancer));
    }

    /**
     * 移除一个信息记录器
     *
     * @param recorder 跟踪记录仪
     */
    @ParameterizedTest
    @MethodSource("generateSyncTraceRecorder")
    @DisplayName("remove a infoRecorder")
    public void removeInfoRecorderTest(TraceRecorder recorder) {
        InfoRecorder infoRecorder = mock(InfoRecorder.class);
        recorder.addInfoRecorder(infoRecorder);
        assertTrue(recorder.removeInfoRecorder(infoRecorder));
    }

    /**
     * 获取信息过滤器列表
     *
     * @param recorder 跟踪记录仪
     */
    @ParameterizedTest
    @MethodSource("generateSyncTraceRecorder")
    @DisplayName("get infoFilter list")
    public void getInfoFiltersTest(TraceRecorder recorder) {
        assertAll(() ->
            assertInnerIterableClassEquals(
                recorder.getInfoFilters().iterator(),
                List.of(new RecordLabelInfoFilter()).iterator()
            )
        );
    }

    /**
     * 获取信息增强器列表
     *
     * @param recorder 跟踪记录仪
     */
    @ParameterizedTest
    @MethodSource("generateSyncTraceRecorder")
    @DisplayName("get infoEnhancer list")
    public void getInfoEnhancersTest(TraceRecorder recorder) {
        assertAll(() ->
            assertInnerIterableClassEquals(
                recorder.getInfoEnhancers().iterator(),
                List
                    .of(
                        new LineInfoEnhancer(),
                        new StackInfoEnhancer(),
                        new ShortClassNameInfoEnhancer(),
                        new ThreadInfoEnhancer()
                    )
                    .iterator()
            )
        );
    }

    /**
     * 获取信息记录器列表
     *
     * @param recorder 跟踪记录仪
     */
    @ParameterizedTest
    @MethodSource("generateSyncTraceRecorder")
    @DisplayName("get infoRecorder list")
    public void getInfoRecordersTest(TraceRecorder recorder) {
        assertAll(() ->
            assertInnerIterableClassEquals(
                recorder.getInfoRecorders().iterator(),
                List.of(new ConsoleInfoRecorder()).iterator()
            )
        );
    }

    /**
     * 重置特殊的结构
     *
     * @param recorder 跟踪记录仪
     */
    @ParameterizedTest
    @MethodSource("generateSyncTraceRecorder")
    @DisplayName("reset special structure")
    public void resetSpecialTest(TraceRecorder recorder) {
        recorder.addInfoFilter(mock(InfoFilter.class));
        recorder.addInfoEnhancer(mock(InfoEnhancer.class));
        recorder.addInfoRecorder(mock(InfoRecorder.class));
        assertTrue(recorder.resetSpecial());
    }

    /**
     * 启用记录所有配置，将显示所有记录信息
     *
     * @param recorder 跟踪记录仪
     */
    @ParameterizedTest
    @MethodSource("generateSyncTraceRecorder")
    @DisplayName("enable record all config, will show all info of record")
    public void recordALLConfigTest(TraceRecorder recorder) {
        assertTrue(recorder.recordAll());
        recorder.log("show one");
        recorder.nolog("hide two");
    }

    /**
     * 启用隐藏所有配置，将隐藏所有记录信息
     *
     * @param recorder 跟踪记录仪
     */
    @ParameterizedTest
    @MethodSource("generateSyncTraceRecorder")
    @DisplayName("enable hide all config, will hide all info of record")
    public void hideALLConfigTest(TraceRecorder recorder) {
        assertTrue(recorder.hideAll());
        recorder.log("show one");
        recorder.nolog("hide two");
    }

    /**
     * 启用短类名配置，仅显示栈信息类名，非包名+类名
     *
     * @param recorder 跟踪记录仪
     */
    @ParameterizedTest
    @MethodSource("generateSyncTraceRecorder")
    @DisplayName("enable short class name config, just show class name of stack info, not package + class name")
    public void enableShortClassNameConfigTest(TraceRecorder recorder) {
        recorder.log("show package + class name");
        assertTrue(recorder.enableShortClassName());
        recorder.log("only class name of show");
    }

    /**
     * 禁用短类名配置，显示包名+类名
     *
     * @param recorder 跟踪记录仪
     */
    @ParameterizedTest
    @MethodSource("generateSyncTraceRecorder")
    @DisplayName("disable short class name config, show package + class name")
    public void disableShortClassNameConfigTest(TraceRecorder recorder) {
        assertTrue(recorder.disableShortClassName());
        recorder.log("show package + class name");
    }

    /**
     * 验证短类名配置信息
     *
     * @param recorder 跟踪记录仪
     */
    @ParameterizedTest
    @MethodSource("generateSyncTraceRecorder")
    @DisplayName("verify short class name config info")
    public void isEnableShortClassNameConfigTest(TraceRecorder recorder) {
        recorder.enableShortClassName();
        assertTrue(recorder.isEnableShortClassName());
        recorder.disableShortClassName();
        assertFalse(recorder.isEnableShortClassName());
    }

    /**
     * 启用线程名配置，显示线程名，默认启用
     *
     * @param recorder 跟踪记录仪
     */
    @ParameterizedTest
    @MethodSource("generateSyncTraceRecorder")
    @DisplayName("enable thread name config, show thread name, default enable")
    public void enableThreadNameConfigTest(TraceRecorder recorder) {
        assertTrue(recorder.enableThreadName());
        recorder.log("show thread name");
    }

    /**
     * 禁用线程名配置，不显示线程名
     *
     * @param recorder 跟踪记录仪
     */
    @ParameterizedTest
    @MethodSource("generateSyncTraceRecorder")
    @DisplayName("disable thread name config, no show thread name")
    public void disableThreadNameConfigTest(TraceRecorder recorder) {
        assertTrue(recorder.disableThreadName());
        recorder.log("no show thread name");
    }

    /**
     * 验证线程名配置信息
     *
     * @param recorder 跟踪记录仪
     */
    @ParameterizedTest
    @MethodSource("generateSyncTraceRecorder")
    @DisplayName("verify thread name config info")
    public void isEnableThreadNameConfigTest(TraceRecorder recorder) {
        recorder.enableThreadName();
        assertTrue(recorder.isEnableThreadName());
        recorder.disableThreadName();
        assertFalse(recorder.isEnableThreadName());
    }

    /**
     * 启用栈信息配置，将捕获记录点信息，默认启用
     *
     * @param recorder 跟踪记录仪
     */
    @ParameterizedTest
    @MethodSource("generateSyncTraceRecorder")
    @DisplayName("enable stack info config, will capture info of record point. default enable")
    public void enableStackInfoConfigTest(TraceRecorder recorder) {
        assertTrue(recorder.enableStackInfo());
        recorder.log("enable stack info, default enable");
    }

    /**
     * 禁用栈信息配置，不捕获记录点信息
     *
     * @param recorder 跟踪记录仪
     */
    @ParameterizedTest
    @MethodSource("generateSyncTraceRecorder")
    @DisplayName("disable stack info config, not capture info of record point")
    public void disableStackInfoConfigTest(TraceRecorder recorder) {
        assertTrue(recorder.disableStackInfo());
        recorder.log("disable capture stack info record point");
    }

    /**
     * 验证栈信息配置信息
     *
     * @param recorder 跟踪记录仪
     */
    @ParameterizedTest
    @MethodSource("generateSyncTraceRecorder")
    @DisplayName("verify stack info config info")
    public void isEnableStackInfoConfigTest(TraceRecorder recorder) {
        recorder.enableStackInfo();
        assertTrue(recorder.isEnableStackInfo());
        recorder.disableStackInfo();
        assertFalse(recorder.isEnableStackInfo());
    }

    /**
     * 验证跟踪记录仪环境
     *
     * @param recorder 跟踪记录仪
     */
    @ParameterizedTest
    @MethodSource("generateSyncTraceRecorder")
    @DisplayName("verify environment")
    public void environment(TraceRecorder recorder) {
        /*
        基本访问
         */
        TraceRecorderEnvironment environment = recorder.environment();
        assertNotNull(environment);
        assertEquals(TraceRecorderVersion.INSTANCE, environment.get("version").get());

        /*
        关闭跟踪记录仪，不能访问环境和环境中元素
         */
        recorder.shutdown();
        assertThrowsExactly(TraceClosedException.class, () -> recorder.environment());
        assertThrowsExactly(TraceClosedException.class, () -> environment.get("version"));
    }

    /**
     * 验证本地共享跟踪记录仪
     *
     * @param recorder 跟踪记录仪
     */
    @ParameterizedTest
    @MethodSource("generateSyncTraceRecorder")
    @DisplayName("verify local shared TraceRecorder")
    public void localShared(TraceRecorder recorder) {
        assertEquals(recorder, TraceRecorders.get());
    }

    /**
     * 生成同步跟踪记录仪
     */
    @DisplayName("generate sync TraceRecorder")
    private static Stream<TraceRecorder> generateSyncTraceRecorder() {
        return Stream.of(new TraceRecorder());
    }

    /**
     * 生成异步跟踪记录仪
     */
    @DisplayName("generate async TraceRecorder")
    private static Stream<TraceRecorder> generateAsyncTraceRecorder() {
        return Stream.of(new TraceRecorder(true));
    }

    /**
     * 生成配置跟踪记录仪
     */
    @DisplayName("generate config TraceRecorder")
    private static Stream<TraceRecorder> generateConfigTraceRecorder() {
        return Stream.of(new TraceRecorder(TraceRecorderConfig.builder().build()));
    }

    /**
     * 断言内部两个迭代器是否相等 -> 类比较
     *
     * @param iterator 第一个迭代器
     * @param iterator1 第二个迭代器
     */
    @DisplayName("assert inner two iterable is equals -> class equals")
    private void assertInnerIterableClassEquals(Iterator<?> iterator, Iterator<?> iterator1) {
        Object value, value1;
        while (iterator.hasNext() && iterator1.hasNext()) {
            value = iterator.next();
            value1 = iterator1.next();
            if (!Objects.equals(value.getClass(), value1.getClass())) {
                throw new TraceException(
                    Formats.format(
                        "not match class value at two iterator - [ iterator: {} ,iterator1: {} ]",
                        value.getClass(),
                        value1.getClass()
                    )
                );
            }
        }
        if (iterator.hasNext() || iterator1.hasNext()) {
            throw new TraceException(
                Formats.format(
                    "not match size at two iterator - [ iterator hasNext: {} ,iterator1 hasNext: {} ]\n" +
                    "iterator: {} ,iterator1: {}",
                    iterator.hasNext(),
                    iterator1.hasNext(),
                    iterator.hasNext() ? iterator.next().getClass() : Strings.empty(),
                    iterator1.hasNext() ? iterator1.next().getClass() : Strings.empty()
                )
            );
        }
    }
}
