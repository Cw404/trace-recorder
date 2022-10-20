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
package cn.xusc.trace.server;

import cn.xusc.trace.common.exception.TraceException;
import cn.xusc.trace.common.util.AntPathMatcher;
import cn.xusc.trace.common.util.Formats;
import cn.xusc.trace.common.util.Ognls;
import cn.xusc.trace.common.util.RenderEngine;
import cn.xusc.trace.common.util.reflect.Annotation;
import cn.xusc.trace.server.annotation.OutputStreamServerResource;
import cn.xusc.trace.server.annotation.RenderServerResource;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * 服务调度器
 *
 * <p>
 * 统一调度实现{@link HttpServlet}规范的服务资源
 * </p>
 *
 * @author WangCai
 * @since 2.5
 */
public class ServerDispatchServlet extends HttpServlet {

    /**
     * ant路径匹配器
     */
    private static final AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher();

    /**
     * 服务资源点
     */
    private ServerResources resources;

    /**
     * 关闭资源路径列表
     */
    private List<String> closeResourcePaths = new ArrayList<>();

    /**
     * 关闭服务运行程序
     */
    private Runnable closeServerRunnable;

    /**
     * 基础构造
     *
     * @param resources 服务资源点
     * @throws NullPointerException if {@code resources} is null.
     */
    public ServerDispatchServlet(ServerResources resources) {
        Objects.requireNonNull(resources);

        this.resources = resources;
        resources.walkCloseServerResource((path, serverResource) -> {
            closeResourcePaths.add(path);
            return true;
        });
    }

    /**
     * 注册关闭服务
     *
     * @param closeServerRunnable 关闭服务运行程序
     * @throws NullPointerException if {@code closeServerRunnable} is null.
     */
    public void registerCloseServer(Runnable closeServerRunnable) {
        Objects.requireNonNull(closeServerRunnable);

        this.closeServerRunnable = closeServerRunnable;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Writer innerWriter = new Writer(resp);
        resources.walkServerResource((path, serverResource) -> {
            /*
            匹配路径资源写出资源数据
             */
            String servletPath;
            if (ANT_PATH_MATCHER.match(path, servletPath = req.getServletPath())) {
                byte[] writeData = parseWriteData(req, serverResource, innerWriter);
                doWriteData(writeData, innerWriter);
                if (
                    Boolean.logicalAnd(closeResourcePaths.contains(servletPath), Objects.nonNull(closeServerRunnable))
                ) {
                    /*
                    关闭资源服务
                     */
                    closeServerRunnable.run();
                }
                return false;
            }
            return true;
        });

        /*
         资源不匹配处理
         */
        if (!innerWriter.isWritten()) {
            Optional<ServerResource> notFoundServerResourceOptional = resources.serverResource("/notFound");
            if (notFoundServerResourceOptional.isPresent()) {
                doWriteData(parseWriteData(req, notFoundServerResourceOptional.get(), innerWriter), innerWriter);
                return;
            }
            doWriteData("Hello trace-recorder with a cute tomcat".getBytes(StandardCharsets.UTF_8), innerWriter);
        }
    }

    /**
     * 解析写数据
     *
     * @param req            http请求
     * @param serverResource 服务资源
     * @param writer    写者
     * @return 解析的数据
     */
    private byte[] parseWriteData(HttpServletRequest req, ServerResource serverResource, Writer writer) {
        byte[] data = serverResource.data();
        /*
        短暂服务资源获取
         */
        if (serverResource instanceof TransientServerResource) {
            data = ((TransientServerResource) serverResource).getDataSupplier().get();
        }

        /*
        推断写者模式
         */
        if (
            serverResource
                .metaAnnotations()
                .stream()
                .anyMatch(annotation -> annotation.self() instanceof OutputStreamServerResource)
        ) {
            writer.setWriterModel(WriterModel.OUTPUT_STREAM);
            if (serverResource.metaAnnotations().size() == 1) {
                return data;
            }
        }

        /*
        源注释处理
         */
        if (serverResource.metaAnnotations().isEmpty()) {
            return data;
        }
        String key, value;
        RenderEngine renderEngine = new RenderEngine(new String(data, StandardCharsets.UTF_8));
        for (Annotation<java.lang.annotation.Annotation> metaAnnotation : serverResource.metaAnnotations()) {
            if (metaAnnotation.self() instanceof RenderServerResource) {
                /*
                RenderServerResource注释
                 */
                key = (String) metaAnnotation.findMethod("key").get().call();
                value = (String) metaAnnotation.findMethod("value").get().call();
                if (
                    Objects.equals(
                        metaAnnotation.findMethod("model").get().call(),
                        RenderServerResource.ValueModel.OGNL
                    )
                ) {
                    renderEngine.register(key, String.valueOf(Ognls.getValue(value, req)));
                    continue;
                }
                renderEngine.register(key, value);
            }
        }

        return renderEngine.renderContent().getBytes(StandardCharsets.UTF_8);
    }

    /**
     * 写出数据
     *
     * @param writeData 写数据
     * @param innerWriter 内部写者
     * @throws TraceException if an input or output exception occurred or an I/O error occurs.
     */
    private void doWriteData(byte[] writeData, Writer innerWriter) {
        if (Objects.equals(innerWriter.getWriterModel(), WriterModel.WRITER)) {
            try (PrintWriter writer = innerWriter.getWriter()) {
                writer.write(new String(writeData, StandardCharsets.UTF_8));
                writer.flush();
            }
        } else {
            try (ServletOutputStream outputStream = innerWriter.getOutputStream()) {
                outputStream.write(writeData);
                outputStream.flush();
            } catch (IOException e) {
                throw new TraceException(e);
            }
        }
        innerWriter.setWritten(true);
    }

    /**
     * 写者
     */
    @Setter
    @Getter
    @RequiredArgsConstructor
    private class Writer {

        /**
         * 是否已写
         */
        private boolean isWritten;

        /**
         * 写者模式
         */
        private WriterModel writerModel = WriterModel.WRITER;

        /**
         * http响应
         */
        private final HttpServletResponse response;

        /**
         * 获取输出流
         *
         * @return 输出流
         * @throws TraceException if an input or output exception occurred or writer model not match.
         */
        public ServletOutputStream getOutputStream() {
            if (Objects.equals(writerModel, WriterModel.OUTPUT_STREAM)) {
                try {
                    return response.getOutputStream();
                } catch (IOException e) {
                    throw new TraceException(e);
                }
            }
            throw new TraceException(
                Formats.format(
                    "not match writer model, current model: {}, get model: {}",
                    writerModel,
                    WriterModel.OUTPUT_STREAM
                )
            );
        }

        /**
         * 获取写者
         *
         * @return 打印写者
         * @throws TraceException if an input or output exception occurred or writer model not match.
         */
        public PrintWriter getWriter() {
            if (Objects.equals(writerModel, WriterModel.WRITER)) {
                try {
                    return response.getWriter();
                } catch (IOException e) {
                    throw new TraceException(e);
                }
            }
            throw new TraceException(
                Formats.format(
                    "not match writer model, current model: {}, get model: {}",
                    writerModel,
                    WriterModel.WRITER
                )
            );
        }
    }

    /**
     * 写者模式
     */
    private enum WriterModel {
        /**
         * 写者
         */
        WRITER,
        /**
         * 输出流
         */
        OUTPUT_STREAM,
    }
}
