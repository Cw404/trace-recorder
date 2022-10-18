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

import cn.xusc.trace.common.util.AntPathMatcher;
import cn.xusc.trace.common.util.Ognls;
import cn.xusc.trace.common.util.RenderEngine;
import cn.xusc.trace.common.util.reflect.Annotation;
import cn.xusc.trace.server.annotation.RenderServerResource;
import jakarta.servlet.ServletException;
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
        Writer innerWriter = new Writer(resp.getWriter());
        PrintWriter writer = innerWriter.getWriter();
        resources.walkServerResource((path, serverResource) -> {
            /*
            匹配路径资源写出资源数据
             */
            String servletPath;
            if (ANT_PATH_MATCHER.match(path, servletPath = req.getServletPath())) {
                writer.write(parseWriteData(req, serverResource));
                writer.flush();
                innerWriter.setWritten(true);
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
                writer.write(parseWriteData(req, notFoundServerResourceOptional.get()));
                writer.flush();
                return;
            }
            writer.println("Hello trace-recorder with a cute tomcat");
        }
    }

    /**
     * 解析写数据
     *
     * @param req http请求
     * @param serverResource 服务资源
     * @return 解析的数据
     */
    private String parseWriteData(HttpServletRequest req, ServerResource serverResource) {
        byte[] data = serverResource.data();
        /*
        短暂服务资源获取
         */
        if (serverResource instanceof TransientServerResource) {
            data = ((TransientServerResource) serverResource).getDataSupplier().get();
        }

        if (serverResource.metaAnnotations().isEmpty()) {
            return new String(data, StandardCharsets.UTF_8);
        }
        String key, value;
        RenderEngine renderEngine = new RenderEngine(new String(data, StandardCharsets.UTF_8));
        for (Annotation<java.lang.annotation.Annotation> metaAnnotation : serverResource.metaAnnotations()) {
            /*
            当前为RenderServerResource注释
             */
            key = (String) metaAnnotation.findMethod("key").get().call();
            value = (String) metaAnnotation.findMethod("value").get().call();
            if (Objects.equals(metaAnnotation.findMethod("model").get().call(), RenderServerResource.ValueModel.OGNL)) {
                renderEngine.register(key, String.valueOf(Ognls.getValue(value, req)));
                continue;
            }
            renderEngine.register(key, value);
        }

        return renderEngine.renderContent();
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
         * 打印写者
         */
        private final PrintWriter writer;
    }
}
