/*
 * Copyright 20022 WangCai.
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

package cn.xusc.trace.record;

import cn.xusc.trace.annotation.CloseOrder;
import cn.xusc.trace.util.Runtimes;

import java.io.Closeable;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

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
     * 构建一个指定{@link File}的文件信息记录器
     *
     * @param file 文件
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public FileInfoRecorder(File file) {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (!file.canWrite()) {
            throw new IllegalStateException("target file can't write");
        }
        try {
            writer = new FileWriter(file);
            Runtimes.addCleanTask(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void record(String writeInfo) {
        try {
            writer.write(writeInfo);
        } catch (IOException e) {
            throw new RuntimeException(e);
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
