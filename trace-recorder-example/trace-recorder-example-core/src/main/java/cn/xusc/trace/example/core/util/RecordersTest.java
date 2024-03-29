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
import static org.mockito.Mockito.mock;

import cn.xusc.trace.common.exception.TraceException;
import cn.xusc.trace.common.util.Formats;
import cn.xusc.trace.common.util.Strings;
import cn.xusc.trace.core.config.TraceRecorderConfig;
import cn.xusc.trace.core.enhance.*;
import cn.xusc.trace.core.filter.InfoFilter;
import cn.xusc.trace.core.filter.RecordLabelInfoFilter;
import cn.xusc.trace.core.record.ConsoleInfoRecorder;
import cn.xusc.trace.core.record.InfoRecorder;
import cn.xusc.trace.core.util.Recorders;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * 记录器工具类测试
 *
 * @author wangcai
 * @deprecated {@link Recorders}
 */
@Deprecated
public class RecordersTest {

    /**
     * 基础记录测试，更多看TraceRecorderTest
     */
    @Test
    @DisplayName("Base record, more see TraceRecorderTest")
    public void baseRecordTest() {
        Recorders.log("base record");
        Recorders.enableAsync();
        Recorders.log("async record");
        Recorders.config(TraceRecorderConfig.builder().build());
        Recorders.log("base {}", "record");
        Recorders.nolog("hide record");
        Recorders.nolog("hide {}", "record");
        InfoFilter infoFilter = mock(InfoFilter.class);
        InfoEnhancer infoEnhancer = mock(InfoEnhancer.class);
        InfoRecorder infoRecorder = mock(InfoRecorder.class);
        assertTrue(Recorders.addInfoFilter(infoFilter));
        assertTrue(Recorders.addInfoEnhancer(infoEnhancer));
        assertTrue(Recorders.addInfoRecorder(infoRecorder));
        assertTrue(Recorders.removeInfoFilter(infoFilter));
        assertTrue(Recorders.removeInfoEnhancer(infoEnhancer));
        assertTrue(Recorders.removeInfoRecorder(infoRecorder));
        assertAll(() ->
            assertInnerIterableClassEquals(
                Recorders.getInfoFilters().iterator(),
                List.of(new RecordLabelInfoFilter()).iterator()
            )
        );
        assertAll(() ->
            assertInnerIterableClassEquals(
                Recorders.getInfoEnhancers().iterator(),
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
        assertAll(() ->
            assertInnerIterableClassEquals(
                Recorders.getInfoRecorders().iterator(),
                List.of(new ConsoleInfoRecorder()).iterator()
            )
        );
        assertTrue(Recorders.resetSpecial());
        assertTrue(Recorders.recordAll());
        assertTrue(Recorders.hideAll());
        assertTrue(Recorders.enableShortClassName());
        assertTrue(Recorders.disableShortClassName());
        assertFalse(Recorders.isEnableShortClassName());
        assertTrue(Recorders.enableThreadName());
        assertTrue(Recorders.disableThreadName());
        assertFalse(Recorders.isEnableThreadName());
        assertTrue(Recorders.enableStackInfo());
        assertTrue(Recorders.disableStackInfo());
        assertFalse(Recorders.isEnableStackInfo());
    }

    /**
     * 添加文件信息记录器，将写出所有非隐藏信息到指定文件
     *
     * @throws IOException If a file could not be created
     */
    @Test
    @DisplayName("add fileInfoRecorder, will write all not hide info to specified file")
    public void addFileInfoRecorderTest() throws IOException {
        /*
          for better experience
         */
        File tempFile = File.createTempFile(String.valueOf(System.currentTimeMillis()), ".txt");
        assertNotNull(tempFile, "create temp file failure");
        Recorders.log("create temp file path: " + tempFile.getAbsolutePath());
        tempFile.deleteOnExit();
        Recorders.log("register JVM exit hook, will delete file" + tempFile.getAbsolutePath());

        assertTrue(Recorders.addFileInfoRecorder(tempFile));
        Recorders.log("base record");
        Recorders.nolog("hide record");

        /*
          at 5s after for trigger JVM hook
         */
        LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(10));
    }

    /**
     * 添通用统计信息增强器，你能打开临时文件进行查看
     *
     * @throws IOException If a file could not be created
     */
    @Test
    @DisplayName("add CommonStatisticsInfoEnhancer, you can open temp file to show")
    public void addCommonStatisticsInfoEnhancerTest() throws IOException {
        /*
          for better experience
         */
        File tempFile = File.createTempFile(String.valueOf(System.currentTimeMillis()), ".txt");
        assertNotNull(tempFile, "create temp file failure");
        Recorders.log("create temp file path: " + tempFile.getAbsolutePath());
        /*
          you should comment it, if you want to watch.
         */
        tempFile.deleteOnExit();
        Recorders.log("register JVM exit hook, will delete file" + tempFile.getAbsolutePath());
        assertTrue(Recorders.addFileInfoRecorder(tempFile));

        /*
          test mechanism question, console not show statistics info, but you can open temp file to show
         */
        assertTrue(Recorders.addCommonStatisticsInfoEnhancer());
        Recorders.log("base record");
        Recorders.nolog("hide record");
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
