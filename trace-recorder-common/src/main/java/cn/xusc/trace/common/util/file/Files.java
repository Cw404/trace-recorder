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
package cn.xusc.trace.common.util.file;

import cn.xusc.trace.common.exception.TraceException;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.file.*;
import java.util.Enumeration;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import lombok.experimental.UtilityClass;

/**
 * 文件工具类
 *
 * <p>
 * 通过lombok组件{@link UtilityClass}确保工具类的使用
 * </p>
 *
 * @author WangCai
 * @since 2.5
 */
@UtilityClass
class Files {

    /**
     * 游走文件树
     *
     * @param start 起始路径
     * @param protocol 文件协议
     * @param matchDirectorySelf 匹配文件夹自身
     *                           jar协议的一种补偿机制，如果你需要当前文件夹
     * @param visitor 文件访问者
     * @return 起始路径
     * @throws IOException if an I/O error has occurred.
     */
    public String walkFileTree(
        String start,
        FileProtocol protocol,
        boolean matchDirectorySelf,
        FileVisitor<? super Path> visitor
    ) throws IOException {
        switch (protocol) {
            case FILE:
                {
                    java.nio.file.Files.walkFileTree(Path.of(start), visitor);
                    break;
                }
            case JAR:
                {
                    URL jarFileURL;
                    String spec = start, entryName = null;

                    int separator = spec.indexOf("!/");
                    /* 注意: 暂不处理纯jar包 */
                    if (separator == -1) {
                        throw new TraceException("no !/ found in url spec:" + spec);
                    }

                    jarFileURL = new URL(spec.substring(0, separator++));

                    /* 如果!是最后路径, entryName为null */
                    if (++separator != spec.length()) {
                        entryName = spec.substring(separator);
                        entryName = URLDecoder.decode(entryName, "UTF-8");
                    }

                    if (Objects.isNull(entryName)) {
                        throw new TraceException("not support entry parse");
                    }

                    JarFile jarFile = new JarFile(jarFileURL.getFile());
                    /* 构建路径表达式匹配器 */
                    PathMatcher directorySelfMatcher = null;
                    String matcherPattern = "glob:".concat(entryName);
                    if (jarFile.getJarEntry(entryName).isDirectory()) {
                        if (matchDirectorySelf) {
                            directorySelfMatcher = FileSystems.getDefault().getPathMatcher(matcherPattern);
                        }
                        matcherPattern = matcherPattern.concat("/**");
                    }
                    PathMatcher matcher = FileSystems.getDefault().getPathMatcher(matcherPattern);

                    /* jar文件访问 */
                    Enumeration<JarEntry> entries = jarFile.entries();
                    JarLoader jarLoader = new ClassLoaderJarLoader(null);
                    FileVisitResult result = FileVisitResult.CONTINUE;
                    while (entries.hasMoreElements()) {
                        if (Objects.requireNonNull(result) != FileVisitResult.CONTINUE) {
                            if (result == FileVisitResult.TERMINATE) {
                                break;
                            }
                        }
                        JarEntry entry = entries.nextElement();
                        /* 文件夹自身匹配 */
                        if (
                            Objects.nonNull(directorySelfMatcher) &&
                            directorySelfMatcher.matches(Path.of(entry.getName()))
                        ) {
                            result = visitor.preVisitDirectory(new JarPath(entry, jarLoader), null);
                            continue;
                        }
                        /* 文件夹子集匹配 */
                        if (matcher.matches(Path.of(entry.getName()))) {
                            if (entry.isDirectory()) {
                                result = visitor.preVisitDirectory(new JarPath(entry, jarLoader), null);
                                continue;
                            }
                            result = visitor.visitFile(new JarPath(entry, jarLoader), null);
                        }
                    }
                }
        }
        return start;
    }

    /**
     * 查找协议
     *
     * @param protocolName 协议名
     * @return 具体协议
     * @throws NullPointerException if {@code protocolName} is null.
     * @throws TraceException if file protocol not support.
     */
    public FileProtocol findProtocol(String protocolName) {
        Objects.requireNonNull(protocolName);

        for (FileProtocol fileProtocol : FileProtocol.values()) {
            if (Objects.equals(protocolName, fileProtocol.PROTOCOL_NAME)) {
                return fileProtocol;
            }
        }
        throw new TraceException("not support file protocol");
    }

    /**
     * 文件协议
     */
    public enum FileProtocol {
        /**
         * 文件
         */
        FILE("file"),
        /**
         * jar
         */
        JAR("jar");

        /**
         * 协议名
         */
        private final String PROTOCOL_NAME;

        /**
         * 基础构造
         *
         * @param protocolName 协议名
         */
        FileProtocol(String protocolName) {
            PROTOCOL_NAME = protocolName;
        }
    }
}
