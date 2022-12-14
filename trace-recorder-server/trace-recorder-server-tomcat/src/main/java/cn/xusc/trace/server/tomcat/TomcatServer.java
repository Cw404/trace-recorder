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
package cn.xusc.trace.server.tomcat;

import cn.xusc.trace.common.exception.TraceException;
import cn.xusc.trace.common.util.Formats;
import cn.xusc.trace.common.util.Painter;
import cn.xusc.trace.common.util.Strings;
import cn.xusc.trace.common.util.Systems;
import cn.xusc.trace.server.AbstractServer;
import cn.xusc.trace.server.ServerDispatchServlet;
import cn.xusc.trace.server.tomcat.config.TomcatServerConfig;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.logging.LogManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.*;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;

/**
 * tomcat服务
 *
 * @author WangCai
 * @since 2.5
 */
@Slf4j
public class TomcatServer extends AbstractServer {

    /**
     * tomcat（猫）
     */
    private Tomcat tomcat;

    /**
     * tomcat服务配置
     */
    private TomcatServerConfig config;

    /**
     * 基础构造
     *
     * @param config tomcat服务配置
     */
    public TomcatServer(TomcatServerConfig config) {
        super(config);
        this.config = config;
        tomcat = new Tomcat();
        log.debug("create tomcat successful!");

        initConfig();
        log.debug("init tomcat config successful!");
    }

    /**
     * 初始化配置
     */
    private void initConfig() {
        initBaseConfig();
        initConnectorConfig();
        initContextConfig();
        initLoggingConfig();
    }

    /**
     * 初始化基础配置
     */
    private void initBaseConfig() {
        /*
        基本目录初始化
         */
        String baseDir;
        try {
            tomcat.setBaseDir(
                baseDir =
                    Objects.nonNull(config.getBaseDir())
                        ? config.getBaseDir()
                        : Files.createTempDirectory(Formats.format("tomcat.{}.", config.getPort())).toString()
            );
        } catch (IOException e) {
            throw new TraceException(
                "Unable to create tempDir. java.io.tmpdir is set to " + Systems.getProperties("java.io.tmpdir"),
                e
            );
        }

        /*
        主机地址、端口初始化，去除自动部署
         */
        tomcat.setHostname(config.getHost());
        tomcat.setPort(config.getPort());
        tomcat.getHost().setAutoDeploy(false);

        log.trace(
            "init base config: { baseDir: {}, hostname: {}, port: {}, autoDeploy: false }",
            baseDir,
            config.getHost(),
            config.getPort()
        );
    }

    /**
     * 初始化连接器配置
     */
    private void initConnectorConfig() {
        /*
        连接器初始化
         */
        Connector connector = tomcat.getConnector();
        connector.setThrowOnFailure(true);

        log.trace("init connector config: { throwOnFailure: true }");
    }

    /**
     * 初始化上下文配置
     */
    private void initContextConfig() {
        /*
        添加上下文
         */
        String contextPath;
        Context context = tomcat.addContext(contextPath = config.getContextPath(), config.getDocBase());
        /*
        添加基础Servlet
         */
        ServerDispatchServlet dispatchServlet = new ServerDispatchServlet(resources);
        dispatchServlet.registerCloseServer(() -> {
            shutdown();
            destroy();
        });
        Tomcat.addServlet(context, "dispatch", dispatchServlet);
        /*
        添加Servlet映射
         */
        context.addServletMappingDecoded("/", "dispatch");

        log.trace(
            "init context config: { contextPath: {}, docBase: {}}, servlet mapping [ ' / ' -> ' {} ' ]",
            contextPath,
            config.getDocBase(),
            contextPath
        );
    }

    /**
     * 初始化日志配置
     */
    private void initLoggingConfig() {
        String loggingPath;
        try {
            LogManager
                .getLogManager()
                .readConfiguration(
                    Objects.nonNull(loggingPath = config.getLoggingPropertiesPath())
                        ? Files.newInputStream(Paths.get(loggingPath))
                        : config.getUseFileProperties()
                            ? ClassLoader.getSystemResourceAsStream(
                                loggingPath = "logging/java/logging-file.properties"
                            )
                            : ClassLoader.getSystemResourceAsStream(loggingPath = "logging/java/logging.properties")
                );
        } catch (IOException e) {
            throw new TraceException(e);
        }

        log.trace("init logging config: { loggingPath: {} }", loggingPath);
    }

    @Override
    protected void doStart() {
        try {
            tomcat.start();
        } catch (LifecycleException e) {
            throw new TraceException(e);
        }
    }

    @Override
    protected void doShutdown() {
        try {
            tomcat.stop();
        } catch (LifecycleException e) {
            throw new TraceException(e);
        }
    }

    @Override
    protected void doDestroy() {
        try {
            tomcat.destroy();
        } catch (LifecycleException e) {
            throw new TraceException(e);
        }
    }

    @Override
    protected void doPrintStartedInfo() {
        Objects.requireNonNull(config.getAccessRequestPaths());
        Objects.requireNonNull(config.getCloseRequestPath());

        String host = config.getHost();
        int port = config.getPort();
        String contextPath = Strings.equals(contextPath = config.getContextPath(), "/") ? Strings.empty() : contextPath;
        Painter painter = new Painter();
        painter.addContent("Tomcat Server stared!");
        for (String accessRequestPath : config.getAccessRequestPaths()) {
            painter.addContent(Formats.format("access: http://{}:{}{}{}", host, port, contextPath, accessRequestPath));
        }
        for (String closeRequestPath : config.getCloseRequestPath()) {
            painter.addContent(Formats.format("close : http://{}:{}{}{}", host, port, contextPath, closeRequestPath));
        }

        System.out.println(painter.drawFrame());
    }
}
