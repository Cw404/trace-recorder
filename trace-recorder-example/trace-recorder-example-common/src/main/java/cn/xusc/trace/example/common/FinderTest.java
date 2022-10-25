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

import static org.junit.jupiter.api.Assertions.assertNotEquals;

import cn.xusc.trace.common.util.file.Finder;
import cn.xusc.trace.core.TraceRecorder;
import cn.xusc.trace.core.config.TraceRecorderConfig;
import java.nio.file.*;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * 文件查找工具类测试
 *
 * @author wangcai
 */
public final class FinderTest {

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
        recorder =
            new TraceRecorder(
                TraceRecorderConfig.builder().enableAsync(false).enableStack(true).enableThreadName(true).build()
            );
    }

    /**
     * 从项目中查找java文件
     *
     * <p>
     * 注意{@code promptMessage}提示用语，单测时修改为自身项目存储的实际路径
     * </p>
     */
    @SneakyThrows
    @Test
    @DisplayName("Find java files from project")
    public void findJavaFilesTest() {
        String promptMessage = "please update projectStoragePath value to real path!";
        String projectStoragePath = promptMessage;
        String projectPath =
            "/trace-recorder/trace-recorder-example/trace-recorder-example-common/src/main/java/cn/xusc/trace/example/common";

        assertNotEquals(promptMessage, projectStoragePath);

        String specificPath = projectStoragePath + projectPath;
        Finder
            .find(specificPath, "**.java")
            .forEach(path -> recorder.log("{}", Path.of(specificPath).relativize(path)));
    }
}
