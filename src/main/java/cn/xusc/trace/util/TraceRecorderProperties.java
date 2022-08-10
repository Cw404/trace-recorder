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

import cn.xusc.trace.config.TraceRecorderConfig;
import cn.xusc.trace.enhance.InfoEnhancer;
import cn.xusc.trace.exception.TraceException;
import cn.xusc.trace.filter.InfoFilter;
import cn.xusc.trace.record.InfoRecorder;
import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

/**
 * 跟踪记录仪属性
 *
 * @author WangCai
 * @since 2.4
 */
public final class TraceRecorderProperties extends Properties {

    /**
     * 配置类名
     */
    private static final String CONFIG_CLASSNAME = "cn.xusc.trace.config.TraceRecorderConfig";

    /**
     * 分割符
     */
    private static final char SEPARATOR = ',';

    /**
     * 是否已加载
     */
    private boolean isLoad;

    /**
     * 基础构造
     */
    public TraceRecorderProperties() {
        super();
    }

    /**
     * 初始化容量的构造
     *
     * @param initialCapacity 初始化容量
     */
    public TraceRecorderProperties(int initialCapacity) {
        super(initialCapacity);
    }

    /**
     * 带有默认属性的构造
     *
     * @param defaults 默认属性
     */
    public TraceRecorderProperties(Properties defaults) {
        super(defaults);
    }

    /**
     * 获取配置
     *
     * @return 跟踪记录仪配置
     * @throws TraceException if not load TraceRecorder properties
     */
    public synchronized TraceRecorderConfig config() {
        if (isLoad) {
            return TraceRecorderConfig
                .builder()
                .infoFilters(innerInfoFilters())
                .infoEnhancers(innerInfoEnhancers())
                .infoRecorders(innerInfoRecorders())
                .enableStack(innerEnableStack())
                .enableShortClassName(innerEnableShortClassName())
                .enableThreadName(innerEnableThreadName())
                .enableRecordAll(innerEnableRecordAll())
                .enableAsync(innerEnableAsync())
                .taskHandlerSize(innerTaskHandlerSize())
                .build();
        }
        throw new TraceException("not load TraceRecorder properties");
    }

    /**
     * 内部获取配置消息过滤器列表
     *
     * @return 消息过滤器列表
     */
    private List<InfoFilter> innerInfoFilters() {
        String infoFiltersStr = getProperty(parseConfigPropertiesName("infoFilters"));
        if (Objects.isNull(infoFiltersStr)) {
            return null;
        }

        List<InfoFilter> infoFilters = new FastList<>(InfoFilter.class, 8);
        for (String infoFilterClass : Strings.split(infoFiltersStr.trim(), SEPARATOR)) {
            infoFilters.add((InfoFilter) loadAndInstance(infoFilterClass));
        }
        return infoFilters;
    }

    /**
     * 内部获取配置消息增强器列表
     *
     * @return 消息增强器列表
     */
    private List<InfoEnhancer> innerInfoEnhancers() {
        String infoEnhancersStr = getProperty(parseConfigPropertiesName("infoEnhancers"));
        if (Objects.isNull(infoEnhancersStr)) {
            return null;
        }

        List<InfoEnhancer> infoEnhancers = new FastList<>(InfoEnhancer.class, 8);
        for (String infoEnhancerClass : Strings.split(infoEnhancersStr.trim(), SEPARATOR)) {
            infoEnhancers.add((InfoEnhancer) loadAndInstance(infoEnhancerClass));
        }
        return infoEnhancers;
    }

    /**
     * 内部获取配置消息记录器列表
     *
     * @return 消息记录器列表
     */
    private List<InfoRecorder> innerInfoRecorders() {
        String infoRecordersStr = getProperty(parseConfigPropertiesName("infoRecorders"));
        if (Objects.isNull(infoRecordersStr)) {
            return null;
        }

        List<InfoRecorder> infoRecorders = new FastList<>(InfoRecorder.class, 8);
        for (String infoRecorderClass : Strings.split(infoRecordersStr.trim(), SEPARATOR)) {
            infoRecorders.add((InfoRecorder) loadAndInstance(infoRecorderClass));
        }
        return infoRecorders;
    }

    /**
     * 内部获取启用堆栈信息
     *
     * @return 启用堆栈信息
     */
    private boolean innerEnableStack() {
        return Boolean.valueOf(getProperty(parseConfigPropertiesName("enableStack")));
    }

    /**
     * 内部获取启用短类名
     *
     * @return 启用短类名
     */
    private boolean innerEnableShortClassName() {
        return Boolean.valueOf(getProperty(parseConfigPropertiesName("enableShortClassName")));
    }

    /**
     * 内部获取启用线程名
     *
     * @return 启用线程名
     */
    private boolean innerEnableThreadName() {
        return Boolean.valueOf(getProperty(parseConfigPropertiesName("enableThreadName")));
    }

    /**
     * 内部获取启用记录所有
     *
     * @return 启用记录所有
     */
    private boolean innerEnableRecordAll() {
        return Boolean.valueOf(getProperty(parseConfigPropertiesName("enableRecordAll")));
    }

    /**
     * 内部获取启用异步记录
     *
     * @return 启用异步记录
     */
    private boolean innerEnableAsync() {
        return Boolean.valueOf(getProperty(parseConfigPropertiesName("enableAsync")));
    }

    /**
     * 内部获取任务处理器数量
     *
     * @return 任务处理器数量
     */
    private int innerTaskHandlerSize() {
        try {
            return Integer.valueOf(getProperty(parseConfigPropertiesName("taskHandlerSize")));
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 解析配置属性名
     *
     * @param propertiesName 属性名
     * @return 配置属性名
     */
    private String parseConfigPropertiesName(String propertiesName) {
        return CONFIG_CLASSNAME + "." + propertiesName;
    }

    /**
     * 加载类并实例化对象
     *
     * @param className 类名
     * @return 类实例化对象
     */
    private Object loadAndInstance(String className) {
        try {
            Class<?> clazz = Class.forName(className);
            Constructor<?> constructor = clazz.getConstructor();
            return constructor.newInstance();
        } catch (ClassNotFoundException e) {
            throw new TraceException(e);
        } catch (InvocationTargetException e) {
            throw new TraceException(e);
        } catch (NoSuchMethodException e) {
            throw new TraceException(e);
        } catch (InstantiationException e) {
            throw new TraceException(e);
        } catch (IllegalAccessException e) {
            throw new TraceException(e);
        }
    }

    /**
     * 根据统一资源定位符进行加载
     *
     * @param url 文件的统一资源定位符
     * @throws IOException          if an I/O exception occurs.
     * @throws NullPointerException if {@code url} is null.
     */
    public synchronized void load(URL url) throws IOException {
        Objects.requireNonNull(url, "url parameter is null");
        load(url.openStream());
    }

    @Override
    public synchronized void load(Reader reader) throws IOException {
        super.load(reader);
        isLoad = true;
    }

    @Override
    public synchronized void load(InputStream inStream) throws IOException {
        super.load(inStream);
        isLoad = true;
    }

    /**
     * 根据xml统一资源定位符进行加载
     *
     * @param url xml文件的统一资源定位符
     * @throws IOException          if reading from the specified input stream
     *                              results in an {@code IOException}.
     * @throws NullPointerException if {@code url} is null.
     */
    public synchronized void loadFromXML(URL url) throws IOException {
        Objects.requireNonNull(url, "url parameter is null");
        loadFromXML(url.openStream());
    }

    @Override
    public synchronized void loadFromXML(InputStream in) throws IOException {
        super.loadFromXML(in);
        isLoad = true;
    }

    /**
     * 简易的加载
     *
     * @param t 资源对象
     * @return 当前跟踪记录仪属性
     * @param <T> 资源对象类型
     * @throws IOException          if an I/O exception occurs.
     * @throws NullPointerException if {@code t} is null.
     * @since 2.5
     */
    public synchronized <T> TraceRecorderProperties easyLoad(T t) throws IOException {
        Objects.requireNonNull(t, "t parameter is null");
        if (t instanceof URL) {
            URL url = (URL) t;
            if (Objects.equals(Files.probeContentType(Paths.get(url.getPath())), "application/xml")) {
                loadFromXML(url);
            } else {
                load(url);
            }
        } else if (t instanceof Reader) {
            load((Reader) t);
        } else if (t instanceof InputStream) {
            InputStream inputStream = (InputStream) t;
            /*
            构建可重复读的输入流
             */
            InputStream repeatableReadableInputStream = new ByteArrayInputStream(inputStream.readAllBytes());
            try {
                loadFromXML(repeatableReadableInputStream);
            } catch (Exception e) {
                /*
                properties文件无法被xml解析加载，这里继续进行加载
                 */
                repeatableReadableInputStream.reset();
                load(repeatableReadableInputStream);
            }
        } else {
            throw new TraceException("not support load mode");
        }
        return this;
    }
}
