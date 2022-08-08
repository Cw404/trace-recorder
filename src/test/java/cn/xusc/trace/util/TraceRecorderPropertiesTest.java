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

import static org.junit.jupiter.api.Assertions.assertNotNull;

import cn.xusc.trace.TraceRecorder;
import cn.xusc.trace.config.TraceRecorderConfig;
import java.io.IOException;
import java.net.URL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * 跟踪记录仪属性测试
 *
 * @author wangcai
 */
public class TraceRecorderPropertiesTest {

    /**
     * 跟踪记录仪属性
     */
    private TraceRecorderProperties properties;

    @BeforeEach
    @DisplayName("init Environment")
    public void initEnv() {
        properties = new TraceRecorderProperties();
    }

    @ParameterizedTest
    @ValueSource(strings = "TraceRecorderPropertiesTest.properties")
    public void loadPropertiesTest(String propertiesPath) throws IOException {
        URL resource = ClassLoader.getSystemClassLoader().getResource(propertiesPath);
        properties.load(resource);
        assertNotNull(properties.config());
    }

    @ParameterizedTest
    @ValueSource(strings = "TraceRecorderPropertiesTest.xml")
    public void loadXmlTest(String xmlPath) throws IOException {
        URL resource = ClassLoader.getSystemClassLoader().getResource(xmlPath);
        properties.loadFromXML(resource);
        assertNotNull(properties.config());
    }

    @ParameterizedTest
    @ValueSource(strings = "TraceRecorderPropertiesTest.properties")
    public void useTest(String propertiesPath) throws IOException {
        URL resource = ClassLoader.getSystemClassLoader().getResource(propertiesPath);
        properties.load(resource);
        TraceRecorderConfig config = properties.config();
        TraceRecorder recorder = new TraceRecorder(config);
        recorder.log("hello {}", "TraceRecorderProperties");
    }
}
