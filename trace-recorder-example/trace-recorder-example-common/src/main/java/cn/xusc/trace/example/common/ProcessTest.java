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

import cn.xusc.trace.common.util.Formats;
import cn.xusc.trace.common.util.Process;
import cn.xusc.trace.core.TraceRecorder;
import cn.xusc.trace.core.config.TraceRecorderConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * 进程工具类测试
 *
 * @author wangcai
 */
public final class ProcessTest {

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
     * 基础ps测试
     */
    @Test
    @DisplayName("base ps test")
    public void psTest() {
        Process
            .ps()
            .stream()
            .map(info -> Formats.format("pid: {}, cmd: {}", info.getPID(), info.getCMD()))
            .forEach(recorder::log);
    }

    /**
     * 根据线程名测试ps
     */
    @Test
    @DisplayName("ps test by process name")
    public void ps1Test() {
        Process
            .ps("java")
            .stream()
            .map(info -> Formats.format("pid: {}, cmd: {}", info.getPID(), info.getCMD()))
            .forEach(recorder::log);
    }

    /**
     * 基础jps测试
     */
    @Test
    @DisplayName("base jps test")
    public void jpsTest() {
        Process
            .jps()
            .stream()
            .map(info -> Formats.format("pid: {}, cmd: {}", info.getPID(), info.getCMD()))
            .forEach(recorder::log);
    }

    /**
     * 根据线程名测试jps
     */
    @Test
    @DisplayName("jps test by process name")
    public void jps1Test() {
        Process
            .jps("jps")
            .stream()
            .map(info -> Formats.format("pid: {}, cmd: {}", info.getPID(), info.getCMD()))
            .forEach(recorder::log);
    }
}
