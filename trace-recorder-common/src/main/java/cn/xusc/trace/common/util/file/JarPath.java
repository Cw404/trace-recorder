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

import cn.xusc.trace.common.exception.TraceUnsupportedOperationException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.*;
import java.util.jar.JarEntry;

/**
 * Jar路径
 *
 * @author WangCai
 * @since 2.5
 */
public class JarPath implements Path {

    /**
     * jar条目
     */
    private JarEntry entry;
    /**
     * jar加载器
     */
    private JarLoader jarLoader;

    /**
     * 基础构造
     *
     * @param entry jar条目
     * @param loader jar加载器
     */
    public JarPath(JarEntry entry, JarLoader loader) {
        this.entry = entry;
        this.jarLoader = loader;
    }

    /**
     * 获取jar文件输入流
     *
     * @return jar文件输入流
     */
    public InputStream getInputStream() {
        return jarLoader.load(entry.getName());
    }

    /**
     * jar文件是否为目录路径
     *
     * @return 详情
     */
    public boolean isDirectoryPath() {
        return entry.isDirectory();
    }

    @Override
    public Path getFileName() {
        throw new TraceUnsupportedOperationException();
    }

    @Override
    public FileSystem getFileSystem() {
        throw new TraceUnsupportedOperationException();
    }

    @Override
    public boolean isAbsolute() {
        throw new TraceUnsupportedOperationException();
    }

    @Override
    public Path getRoot() {
        throw new TraceUnsupportedOperationException();
    }

    @Override
    public Path getParent() {
        throw new TraceUnsupportedOperationException();
    }

    @Override
    public int getNameCount() {
        throw new TraceUnsupportedOperationException();
    }

    @Override
    public Path getName(int index) {
        throw new TraceUnsupportedOperationException();
    }

    @Override
    public Path subpath(int beginIndex, int endIndex) {
        throw new TraceUnsupportedOperationException();
    }

    @Override
    public boolean startsWith(Path other) {
        throw new TraceUnsupportedOperationException();
    }

    @Override
    public boolean endsWith(Path other) {
        throw new TraceUnsupportedOperationException();
    }

    @Override
    public Path normalize() {
        throw new TraceUnsupportedOperationException();
    }

    @Override
    public Path resolve(Path other) {
        throw new TraceUnsupportedOperationException();
    }

    @Override
    public Path relativize(Path other) {
        throw new TraceUnsupportedOperationException();
    }

    @Override
    public URI toUri() {
        throw new TraceUnsupportedOperationException();
    }

    @Override
    public Path toAbsolutePath() {
        throw new TraceUnsupportedOperationException();
    }

    @Override
    public Path toRealPath(LinkOption... options) throws IOException {
        throw new TraceUnsupportedOperationException();
    }

    @Override
    public WatchKey register(WatchService watcher, WatchEvent.Kind<?>[] events, WatchEvent.Modifier... modifiers)
        throws IOException {
        throw new TraceUnsupportedOperationException();
    }

    @Override
    public int compareTo(Path other) {
        throw new TraceUnsupportedOperationException();
    }
}
