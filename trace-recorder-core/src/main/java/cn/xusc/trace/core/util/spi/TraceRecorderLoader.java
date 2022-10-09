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
package cn.xusc.trace.core.util.spi;

import cn.xusc.trace.common.exception.TraceException;
import cn.xusc.trace.common.util.Formats;
import cn.xusc.trace.common.util.Lists;
import java.io.*;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.*;
import java.util.function.Predicate;
import java.util.function.Supplier;
import lombok.*;

/**
 * 跟踪记录仪加载器
 *
 * @author WangCai
 * @since 2.5
 */
public class TraceRecorderLoader<T> {

    /**
     * 跟踪记录仪目录
     */
    private static final String TRACE_RECORDER_DIRECTORY = "META-INF/trace-recorder/";

    /**
     * 类对象池
     */
    private static final Map<Class<?>, List<Holder<Object>>> CLASS_OBJECT_POOL = new HashMap<>();

    /**
     * 类名称池
     */
    private static final Map<Class<?>, List<String>> CLASS_NAME_POOL = new HashMap<>();

    /**
     * 加载类
     */
    private final Class<T> CLAZZ;

    /**
     * 加载标识
     */
    private boolean isLoad;

    /**
     * 基础构造
     *
     * @param clazz 加载类
     * @throws NullPointerException if {@code clazz} is null.
     */
    private TraceRecorderLoader(Class<T> clazz) {
        Objects.requireNonNull(clazz);

        this.CLAZZ = clazz;
    }

    /**
     * 获取跟踪记录仪加载器
     *
     * @param clazz 加载类
     * @return 跟踪记录仪加载器
     * @param <T> 跟踪记录仪加载器类型
     */
    public static <T> TraceRecorderLoader<T> getTraceRecorderLoader(Class<T> clazz) {
        return new TraceRecorderLoader(clazz);
    }

    /**
     * 查找可选所有值列表
     *
     * @return 可选所有值列表
     */
    public Optional<List<T>> findAll() {
        return Optional.ofNullable(innerFind(holderName -> true));
    }

    /**
     * 查找可选指定名称的值
     *
     * @param name 名称
     * @return 可选指定名称的值
     * @throws NullPointerException if {@code name} is null.
     */
    public Optional<T> find(String name) {
        Objects.requireNonNull(name);

        List<T> ts;
        return Optional.ofNullable(
            Objects.isNull(ts = innerFind(holderName -> holderName.equalsIgnoreCase(name))) || ts.isEmpty()
                ? null
                : ts.get(0)
        );
    }

    /**
     * 内部查找
     *
     * @param holderNamePredicate 持有者名称断言
     * @return 查找值列表
     */
    private List<T> innerFind(Predicate<String> holderNamePredicate) {
        loadTrap();
        List<Holder<Object>> holders = CLASS_OBJECT_POOL.get(CLAZZ);
        if (Objects.isNull(holders)) {
            return null;
        }
        List<T> ts = new ArrayList<>();
        holders.forEach(holder -> {
            if (holderNamePredicate.test(holder.getName())) {
                ts.add((T) holder.getValue());
            }
        });
        return ts;
    }

    /**
     * 加载陷阱
     */
    private void loadTrap() {
        if (Boolean.logicalAnd(!isLoad, !CLASS_OBJECT_POOL.containsKey(CLAZZ))) {
            synchronized (CLASS_OBJECT_POOL) {
                if (!isLoad) {
                    load();
                    isLoad = true;
                }
            }
        }
    }

    /**
     * 加载
     */
    private void load() {
        String loadFileName = TRACE_RECORDER_DIRECTORY.concat(CLAZZ.getName());
        try {
            ClassLoader classLoader = Optional
                .ofNullable(CLAZZ.getClassLoader())
                .orElse(ClassLoader.getSystemClassLoader());
            Enumeration<URL> resources = classLoader.getResources(loadFileName);
            if (!resources.hasMoreElements()) {
                return;
            }
            List<Holder<Object>> holders = new ArrayList<>();
            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                List<Holder<Object>> innerHolders = loadResource(classLoader, resource);
                innerHolders.forEach(innerHolder -> {
                    if (
                        !Lists
                            .statistic(holders, holder -> Objects.equals(holder.getName(), innerHolder.getName()))
                            .isEmpty()
                    ) {
                        throw new TraceException(
                            Formats.format("load failure, name [ {} ] already exits", innerHolder.getName())
                        );
                    }
                });
                holders.addAll(innerHolders);
            }
            CLASS_OBJECT_POOL.put(CLAZZ, holders);
        } catch (IOException e) {
            throw new TraceException(e);
        }
    }

    /**
     * 加载资源
     *
     * @param classLoader 类加载器
     * @param resource 资源
     * @return 持有者组
     * @throws IOException if an I/O exception occurs.
     */
    private List<Holder<Object>> loadResource(ClassLoader classLoader, URL resource) throws IOException {
        List<Holder<Object>> holders = new ArrayList<>();
        ResourceReader resourceReader = new ResourceReader(resource.openStream());
        List<String> names = CLASS_NAME_POOL.computeIfAbsent(CLAZZ, k -> new ArrayList<>());
        while (resourceReader.ready()) {
            ResourcePair resourcePair = resourceReader.readResourcePair();
            String name = (String) resourcePair.getName();
            String classPath = (String) resourcePair.getClassPath();
            if (names.contains(name)) {
                throw new TraceException(Formats.format("name [ {} ] already exist", name));
            }
            if (Boolean.logicalOr(name.isBlank(), classPath.isBlank())) {
                throw new TraceException("not support load, name or classpath must to not blank");
            }
            try {
                Class<?> loadClass = loadClass(classLoader, classPath);
                if (!CLAZZ.isAssignableFrom(loadClass)) {
                    throw new TraceException(
                        Formats.format("load failure, [ {} ] not [ {} ] subClass", classPath, CLAZZ.getName())
                    );
                }
                Holder<Object> holder = Holder
                    .builder()
                    .name(name)
                    .lazyValueSupplier(() -> getConstructor(loadClass).call())
                    .build();
                names.add(name);
                holders.add(holder);
            } catch (ClassNotFoundException e) {
                throw new TraceException(e);
            }
        }
        return holders;
    }

    /**
     * 获取无参构造函数
     *
     * @param loadClass 加载的类
     * @return 加载的类的无参构造函数
     */
    private cn.xusc.trace.common.util.reflect.Constructor<Constructor> getConstructor(Class<?> loadClass) {
        return new cn.xusc.trace.common.util.reflect.Class<>(loadClass)
            .constructors()
            .stream()
            .filter(constructor -> constructor.parameters().isEmpty())
            .findFirst()
            .get();
    }

    /**
     * 加载类
     *
     * @param classLoader 类加载器
     * @param classPath 类路径
     * @return 加载的类
     * @throws ClassNotFoundException if the class cannot be located by the specified class loader.
     */
    private Class<?> loadClass(ClassLoader classLoader, String classPath) throws ClassNotFoundException {
        return Class.forName(classPath, true, classLoader);
    }

    /**
     * 持有者
     *
     * @param <T> 持有者类型
     */
    @Builder
    @AllArgsConstructor
    private static class Holder<T> {

        /**
         * 名称
         */
        private String name;

        /**
         * 值对象
         */
        private T value;

        /**
         * 值对象懒加载提供者
         */
        private Supplier<T> lazyValueSupplier;

        /**
         * 获取名称
         *
         * @return 名称
         */
        public String getName() {
            return name;
        }

        /**
         * 获取值对象
         *
         * @return 值对象
         */
        public T getValue() {
            return Optional.ofNullable(value).orElseGet(() -> value = lazyValueSupplier.get());
        }
    }

    /**
     * 资源对
     *
     * @param <N> 资源名类型
     * @param <P> 类路径类型
     */
    @Setter
    @Getter
    @AllArgsConstructor
    private class ResourcePair<N, P> {

        /**
         * 资源名称
         */
        N name;

        /**
         * 类路径
         */
        P classPath;
    }

    /**
     * 资源读取器
     */
    private class ResourceReader extends Reader {

        /**
         * 缓冲的资源读取器
         */
        private final BufferedReader READER;

        /**
         * 基础构造
         *
         * @param inputStream 资源输入流
         */
        public ResourceReader(InputStream inputStream) {
            READER = new BufferedReader(new InputStreamReader(inputStream));
        }

        /**
         * 读取资源对
         *
         * @return 资源对
         * @throws IOException If an I/O error occurs.
         */
        public ResourcePair readResourcePair() throws IOException {
            while (ready()) {
                String line = READER.readLine();
                if (line.startsWith("#") || line.isBlank()) {
                    continue;
                }
                if (line.indexOf("=") == -1) {
                    throw new TraceException(Formats.format("[ {} ] is invalid data format, need = split", line));
                }
                String[] pair = line.split("=");
                if (pair.length > 2) {
                    throw new TraceException(Formats.format("[ {} ] is invalid data format, exist more = split", line));
                }
                return new ResourcePair(pair[0], pair[1]);
            }
            return null;
        }

        @Override
        public int read(char[] cbuf, int off, int len) {
            throw new TraceException("not support operation");
        }

        @Override
        public boolean ready() throws IOException {
            return READER.ready();
        }

        @Override
        public void close() throws IOException {
            READER.close();
        }
    }
}
