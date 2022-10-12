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

import java.io.InputStream;

/**
 * Jar加载器
 *
 * @author WangCai
 * @since 2.5
 */
public interface JarLoader {
    /**
     * 加载jar为流数据
     *
     * @param jarFilePath jar文件路径
     * @return jar文件输入流
     */
    InputStream load(String jarFilePath);
}
