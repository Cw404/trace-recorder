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
import java.util.Optional;

/**
 * ClassLoader jar加载器
 *
 * @author WangCai
 * @since 2.5
 */
public class ClassLoaderJarLoader implements JarLoader {

    /**
     * 类加载器
     */
    private ClassLoader classLoader;

    /**
     * 基础构造
     *
     * @param classLoader 类加载器
     */
    public ClassLoaderJarLoader(ClassLoader classLoader) {
        this.classLoader = Optional.ofNullable(classLoader).orElse(ClassLoader.getSystemClassLoader());
    }

    @Override
    public InputStream load(String jarFilePath) {
        return classLoader.getResourceAsStream(jarFilePath);
    }
}
