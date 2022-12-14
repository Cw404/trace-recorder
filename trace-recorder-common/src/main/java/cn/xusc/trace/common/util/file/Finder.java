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

import static java.nio.file.FileVisitResult.CONTINUE;

import java.io.IOException;
import java.net.URL;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 文件查找工具类
 *
 * @author WangCai
 * @since 2.5
 */
public final class Finder extends SimpleFileVisitor<Path> {

    /**
     * 路径匹配器
     */
    private final PathMatcher MATCHER;
    /**
     * 匹配的路径列表
     */
    private final List<Path> PATHS = new ArrayList<>();

    /**
     * 基础构造
     *
     * @param pattern 路径表达式
     */
    private Finder(String pattern) {
        MATCHER = FileSystems.getDefault().getPathMatcher("glob:" + pattern);
    }

    /**
     * 查找匹配的路径列表
     *
     * @param originUrl 源url
     * @param pattern 查找表达式
     * @return 匹配的路径列表
     * @throws IOException if an I/O error has occurred.
     * @throws NullPointerException  if {@code originUrl} is null.
     * @throws NullPointerException  if {@code pattern} is null.
     */
    public static List<Path> find(URL originUrl, String pattern) throws IOException {
        Objects.requireNonNull(originUrl);
        Objects.requireNonNull(pattern);

        return find(originUrl, pattern, false);
    }

    /**
     * 查找匹配的路径列表
     *
     * @param originPath 源路径
     * @param pattern 查找表达式
     * @return 匹配的路径列表
     * @throws IOException if an I/O error has occurred.
     * @throws NullPointerException  if {@code originPath} is null.
     * @throws NullPointerException  if {@code pattern} is null.
     */
    public static List<Path> find(Path originPath, String pattern) throws IOException {
        Objects.requireNonNull(originPath);
        Objects.requireNonNull(pattern);

        return find(originPath.toUri().toURL(), pattern, false);
    }

    /**
     * 查找匹配的路径列表
     *
     * @param originPath 源路径
     * @param pattern 查找表达式
     * @return 匹配的路径列表
     * @throws IOException if an I/O error has occurred.
     * @throws NullPointerException  if {@code originPath} is null.
     * @throws NullPointerException  if {@code pattern} is null.
     */
    public static List<Path> find(String originPath, String pattern) throws IOException {
        Objects.requireNonNull(originPath);
        Objects.requireNonNull(pattern);

        return find(Path.of(originPath), pattern, false);
    }

    /**
     * 查找匹配的路径列表
     *
     * @param originUrl 源url
     * @param pattern 查找表达式
     * @param matchDirectorySelf 匹配文件夹自身
     * @return 匹配的路径列表
     * @since 2.5.3
     * @throws IOException if an I/O error has occurred.
     * @throws NullPointerException  if {@code originUrl} is null.
     * @throws NullPointerException  if {@code pattern} is null.
     */
    public static List<Path> find(URL originUrl, String pattern, boolean matchDirectorySelf) throws IOException {
        Objects.requireNonNull(originUrl);
        Objects.requireNonNull(pattern);

        Finder finder = new Finder(pattern);
        Files.FileProtocol fileProtocol = Files.findProtocol(originUrl.getProtocol());
        Files.walkFileTree(originUrl.getPath(), fileProtocol, matchDirectorySelf, finder);
        return finder.done();
    }

    /**
     * 查找匹配的路径列表
     *
     * @param originPath 源路径
     * @param pattern 查找表达式
     * @param matchDirectorySelf 匹配文件夹自身
     * @return 匹配的路径列表
     * @since 2.5.3
     * @throws IOException if an I/O error has occurred.
     * @throws NullPointerException  if {@code originPath} is null.
     * @throws NullPointerException  if {@code pattern} is null.
     */
    public static List<Path> find(Path originPath, String pattern, boolean matchDirectorySelf) throws IOException {
        Objects.requireNonNull(originPath);
        Objects.requireNonNull(pattern);

        return find(originPath.toUri().toURL(), pattern, matchDirectorySelf);
    }

    /**
     * 查找匹配的路径列表
     *
     * @param originPath 源路径
     * @param pattern 查找表达式
     * @param matchDirectorySelf 匹配文件夹自身
     * @return 匹配的路径列表
     * @since 2.5.3
     * @throws IOException if an I/O error has occurred.
     * @throws NullPointerException  if {@code originPath} is null.
     * @throws NullPointerException  if {@code pattern} is null.
     */
    public static List<Path> find(String originPath, String pattern, boolean matchDirectorySelf) throws IOException {
        Objects.requireNonNull(originPath);
        Objects.requireNonNull(pattern);

        return find(Path.of(originPath), pattern, matchDirectorySelf);
    }

    /**
     * 查找匹配的路径
     *
     * @param path 需要匹配的路径
     */
    private void find(Path path) {
        Path name = path.getFileName();
        if (Boolean.logicalAnd(Objects.nonNull(name), MATCHER.matches(name))) {
            PATHS.add(path);
        }
    }

    /**
     * 完成
     *
     * @return 匹配的路径列表
     */
    private List<Path> done() {
        return PATHS;
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
        find(dir);
        return CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
        find(file);
        return CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) {
        // ignore exception
        return CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        // nop
        return CONTINUE;
    }
}
