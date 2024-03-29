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

import static org.junit.jupiter.api.Assertions.assertNotNull;

import cn.xusc.trace.core.TraceRecorder;
import cn.xusc.trace.core.util.TraceRecorderProperties;
import java.io.FileInputStream;
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

    /**
     * 初始化环境
     */
    @BeforeEach
    @DisplayName("init Environment")
    public void initEnv() {
        properties = new TraceRecorderProperties();
    }

    /**
     * 加载属性文件
     *
     * @param propertiesPath 属性文件路径
     * @throws IOException if an I/O exception occurs.
     */
    @ParameterizedTest
    @ValueSource(strings = "TraceRecorderPropertiesTest.properties")
    @DisplayName("load properties file")
    public void loadPropertiesTest(String propertiesPath) throws IOException {
        URL resource = ClassLoader.getSystemClassLoader().getResource(propertiesPath);
        properties.load(resource);
        assertNotNull(properties.config());
    }

    /**
     * 加载xml文件
     *
     * @param xmlPath xml文件路径
     * @throws IOException if an I/O exception occurs.
     */
    @ParameterizedTest
    @ValueSource(strings = "TraceRecorderPropertiesTest.xml")
    @DisplayName("load xml file")
    public void loadXmlTest(String xmlPath) throws IOException {
        URL resource = ClassLoader.getSystemClassLoader().getResource(xmlPath);
        properties.loadFromXML(resource);
        assertNotNull(properties.config());
    }

    /**
     * 简易加载文件
     *
     * @param propertiesPath 配置文件路径
     * @throws IOException if an I/O exception occurs.
     */
    @ParameterizedTest
    @ValueSource(strings = { "TraceRecorderPropertiesTest.properties", "TraceRecorderPropertiesTest.xml" })
    @DisplayName("easy load file")
    public void easyLoadTest(String propertiesPath) throws IOException {
        URL resource = ClassLoader.getSystemClassLoader().getResource(propertiesPath);
        assertNotNull(properties.easyLoad(resource));
        assertNotNull(properties.config());
    }

    /**
     * 使用URL简易加载配置的跟踪记录仪 - 使用方式一
     *
     * @param propertiesPath 配置文件路径
     * @throws IOException if an I/O exception occurs.
     */
    @ParameterizedTest
    @ValueSource(strings = { "TraceRecorderPropertiesTest.properties", "TraceRecorderPropertiesTest.xml" })
    @DisplayName("use TraceRecorder of load config")
    public void useTest(String propertiesPath) throws IOException {
        URL resource = ClassLoader.getSystemClassLoader().getResource(propertiesPath);
        TraceRecorder recorder = new TraceRecorder(properties.easyLoad(resource).config());
        recorder.log("hello {}", "TraceRecorderProperties");
    }

    /**
     * 使用文件输入流简易加载配置的跟踪记录仪 - 使用方式二
     *
     * @param propertiesPath 配置文件路径
     * @throws IOException if an I/O exception occurs.
     */
    @ParameterizedTest
    @ValueSource(strings = { "TraceRecorderPropertiesTest.properties", "TraceRecorderPropertiesTest.xml" })
    @DisplayName("use TraceRecorder of load config")
    public void useTest1(String propertiesPath) throws IOException {
        URL resource = ClassLoader.getSystemClassLoader().getResource(propertiesPath);
        TraceRecorder recorder = new TraceRecorder(
            properties.easyLoad(new FileInputStream(resource.getPath())).config()
        );
        recorder.log("hello {}", "TraceRecorderProperties");
    }

    /**
     * 使用类路径声明简易加载配置的跟踪记录仪 - 使用方式三
     *
     * @param propertiesPath 配置文件路径
     * @throws IOException if an I/O exception occurs.
     */
    @ParameterizedTest
    @ValueSource(
        strings = { "classpath:TraceRecorderPropertiesTest.properties", "classpath:TraceRecorderPropertiesTest.xml" }
    )
    public void useTest2(String propertiesPath) throws IOException {
        TraceRecorder recorder = new TraceRecorder(properties.easyLoad(propertiesPath).config());
        recorder.log("hello {}", "TraceRecorderProperties");
    }
}
