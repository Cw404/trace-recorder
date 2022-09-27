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
package cn.xusc.trace.core.record;

import cn.xusc.trace.common.annotation.CloseOrder;
import cn.xusc.trace.common.exception.TraceException;
import cn.xusc.trace.common.util.Runtimes;
import java.io.Closeable;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

/**
 * 文件信息记录器
 *
 * <p>
 * 文件资源应该确保最后关闭
 * </p>
 *
 * @author WangCai
 * @since 1.0
 */
@CloseOrder
public class FileInfoRecorder implements InfoRecorder, Closeable {

    /**
     * 文件写出器
     */
    private final FileWriter writer;

    /**
     * 构建一个指定{@code filePath}的文件信息记录器
     *
     * @param filePath 文件路径
     * @throws TraceException if generate IOException or file can't write
     * @throws NullPointerException if {@code file} is null
     * @since 2.5
     */
    public FileInfoRecorder(String filePath) {
        this(new File(Objects.requireNonNull(filePath)));
    }

    /**
     * 构建一个指定{@link File}的文件信息记录器
     *
     * @param file 文件
     * @throws TraceException if generate IOException or file can't write
     * @throws NullPointerException if {@code file} is null
     */
    public FileInfoRecorder(File file) {
        Objects.requireNonNull(file);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new TraceException(e);
            }
        }
        if (!file.canWrite()) {
            throw new TraceException("target file can't write");
        }
        try {
            writer = new FileWriter(file);
            Runtimes.addCleanTask(this);
        } catch (IOException e) {
            throw new TraceException(e);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws TraceException if generate IOException
     */
    @Override
    public void record(String writeInfo) {
        try {
            writer.write(writeInfo);
        } catch (IOException e) {
            throw new TraceException(e);
        }
    }

    /**
     * 关闭{@link #writer}
     */
    @Override
    public void close() throws IOException {
        writer.close();
    }
}
