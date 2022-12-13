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
package cn.xusc.trace.dashboard.resource;

import cn.xusc.trace.common.exception.TraceException;
import cn.xusc.trace.common.util.Systems;
import cn.xusc.trace.dashboard.TraceDashboardComponentResource;
import cn.xusc.trace.dashboard.constant.Temporary;
import cn.xusc.trace.server.annotation.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * 基础跟踪仪表盘组件资源
 *
 * <p>
 * 提供基础的跟踪仪表盘组件服务资源
 * </p>
 *
 * @author WangCai
 * @since 2.5.3
 */
@ServerResourceRegister
public class BaseTraceDashboardComponentResource implements TraceDashboardComponentResource {

    /**
     * 生成路径
     */
    private Path generatePath;

    /**
     * 基础构造
     *
     * @param generatePath 生成路径
     */
    public BaseTraceDashboardComponentResource(Path generatePath) {
        this.generatePath = generatePath;
    }

    /**
     * 跟踪记录仪icon图标资源
     *
     * @return trace-recorder-icon.png
     */
    @OutputStreamServerResource
    @ServerResource(path = { "/trace-recorder-icon.png" })
    public byte[] traceRecorderIcon() {
        return readClassLoaderResourceData("common/trace-recorder-icon.png");
    }

    /**
     * 跟踪记录仪主图标资源
     *
     * @return trace-recorder.png
     */
    @OutputStreamServerResource
    @ServerResource(path = { "/trace-recorder.png" })
    public byte[] traceRecorderMaster() {
        return readClassLoaderResourceData("common/trace-recorder.png");
    }

    /**
     * spectre.css
     *
     * @return spectre.min.css
     */
    @ServerResource(path = { "/css/spectre.min.css" })
    public byte[] spectreCss() {
        return readClassLoaderResourceData("common/css/spectre.min.css");
    }

    /**
     * 服务找不到资源
     *
     * @return 404.html
     */
    @RenderServerResource(
        key = "notFoundPath",
        value = "getServletPath()",
        model = RenderServerResource.ValueModel.OGNL
    )
    @ServerResource(path = { "/notFound" })
    public byte[] notFound() {
        return readClassLoaderResourceData("common/404.html");
    }

    /**
     * 服务关闭资源
     *
     * @return closed.html
     */
    @ServerCloseResource(path = { "/close" })
    public byte[] close() {
        return readClassLoaderResourceData("common/closed.html");
    }

    /**
     * 读取指定跟踪仪表盘路径生成资源文件数据
     *
     * @param resourcePath 资源文件路径
     * @return 资源文件数据
     * @throws TraceException if an I/O error occurs reading from the stream.
     */
    protected byte[] readGenerateResourceData(String resourcePath) {
        try {
            return Files.readAllBytes(generatePath.resolve(resourcePath));
        } catch (IOException e) {
            throw new TraceException(e);
        }
    }

    /**
     * 读取指定类路径资源文件数据
     *
     * @param resourcePath 资源文件路径
     * @return 资源文件数据
     * @throws TraceException if an I/O error occurs.
     */
    protected byte[] readClassLoaderResourceData(String resourcePath) {
        try (
            InputStream inputStream = ClassLoader.getSystemResourceAsStream(
                Systems.getSystemClassPath(Temporary.TEMPLATE_PATH.resolve(resourcePath).toString())
            )
        ) {
            return inputStream.readAllBytes();
        } catch (IOException e) {
            throw new TraceException(e);
        }
    }
}
