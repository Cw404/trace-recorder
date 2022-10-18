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
import cn.xusc.trace.common.exception.TraceUnsupportedOperationException;
import cn.xusc.trace.common.util.Formats;
import cn.xusc.trace.common.util.reflect.Annotation;
import cn.xusc.trace.common.util.reflect.Method;
import cn.xusc.trace.common.util.reflect.Reflector;
import cn.xusc.trace.server.annotation.*;
import cn.xusc.trace.server.util.ServerClosedWaiter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 抽象服务
 *
 * <p>启动流程:</p>
 * <ol>
 *     <li>初始化服务时，完成服务资源注册</li>
 *     <li>启动后进行服务关闭等候者可等标识变更，调用线程根据情况进行服务等待处理</li>
 *     <li>服务关闭资源调度后，自动唤醒服务关闭等候者</li>
 * </ol>
 *
 * @author WangCai
 * @since 2.5
 */
public abstract class AbstractServer implements Server {

    /**
     * 服务资源点
     */
    protected ServerResources resources;

    /**
     * 服务配置
     */
    protected AbstractServerConfig serverConfig;

    /**
     * 默认无值注释
     */
    private Annotation defaultNoValueAnnotation;

    /**
     * 基础构造
     *
     * @param config 服务配置
     * @throws NullPointerException if {@code config} is null.
     */
    public AbstractServer(AbstractServerConfig config) {
        Objects.requireNonNull(config);

        this.serverConfig = config;
        this.resources = new ServerResources();
        this.defaultNoValueAnnotation =
            new Annotation(new Object(), Object.class) {
                @Override
                public Object value() {
                    return false;
                }
            };
        registerServerResources();
    }

    /**
     * 注册服务资源
     */
    private void registerServerResources() {
        if (Objects.nonNull(serverConfig.getResources())) {
            serverConfig.getResources().forEach(this::registerServerResourceRegister);
        }
    }

    /**
     * 注册服务资源注册器
     *
     * @param serverResourceRegister 服务资源注册器
     * @param <R> 服务资源
     * @throws NullPointerException if {@code serverResourceRegister} is null.
     */
    private <R> void registerServerResourceRegister(R serverResourceRegister) {
        Objects.requireNonNull(serverResourceRegister);

        Reflector
            .of(serverResourceRegister)
            .walkClass(clazz -> {
                if (
                    (boolean) clazz
                        .findAvailableAnnotation(ServerResourceRegister.class)
                        .orElse(defaultNoValueAnnotation)
                        .value()
                ) {
                    clazz
                        .methods()
                        .stream()
                        .filter(method ->
                            (boolean) (
                                method
                                    .findAvailableAnnotation(cn.xusc.trace.server.annotation.ServerResource.class)
                                    .orElse(defaultNoValueAnnotation)
                            ).value()
                        )
                        .forEach(method -> {
                            String returnType = method.returnType();
                            if (!ServerResource.isSupportDataType(returnType)) {
                                throw new TraceUnsupportedOperationException(
                                    Formats.format(
                                        "not support server resource [ class: {}, method: {}, type: {} ]",
                                        clazz.name(),
                                        method.name(),
                                        returnType
                                    )
                                );
                            }

                            Annotation<Class<? extends java.lang.annotation.Annotation>> serverResourcesAnnotation = method
                                .findAvailableAnnotation(cn.xusc.trace.server.annotation.ServerResource.class)
                                .get();
                            /*
                            获取关闭服务资源标志
                             */
                            boolean isCloseServerResource = Objects.equals(
                                serverResourcesAnnotation.type(),
                                ServerCloseResource.class
                            );

                            /*
                            获取服务资源路径
                             */
                            Method<java.lang.reflect.Method> pathMethod = serverResourcesAnnotation
                                .findMethod("path")
                                .get();

                            /*
                            获取重写许可
                             */
                            boolean isOverridePermit = (Boolean) method
                                .findAvailableAnnotation(OverrideServerResource.class)
                                .orElse(defaultNoValueAnnotation)
                                .value();

                            /*
                            探测源注释
                             */
                            List<Annotation<java.lang.annotation.Annotation>> metaAnnotations = detectionMetaAnnotations(
                                method
                            );

                            /*
                            服务资源正式注册
                             */
                            String[] paths = (String[]) pathMethod.call();
                            byte[] data = ServerResource.parseSpecificData(method.call());
                            for (String path : paths) {
                                if (isOverridePermit) {
                                    /*
                                    重写服务资源许可移除
                                     */
                                    resources.remove(path);
                                }
                                registerServerResource(
                                    (boolean) method
                                            .findAvailableAnnotation(
                                                cn.xusc.trace.server.annotation.TransientServerResource.class
                                            )
                                            .orElse(defaultNoValueAnnotation)
                                            .value()
                                        ? new TransientServerResource(
                                            path,
                                            data,
                                            metaAnnotations,
                                            () -> ServerResource.parseSpecificData(method.call())
                                        )
                                        : new SimpleServerResource(path, data, metaAnnotations),
                                    isCloseServerResource
                                );
                            }
                        });
                }
            });
    }

    /**
     * 探测源注释
     *
     * @param method 探测方法
     * @return 源注释列表
     */
    private List<Annotation<java.lang.annotation.Annotation>> detectionMetaAnnotations(
        Method<java.lang.reflect.Method> method
    ) {
        List<Annotation<java.lang.annotation.Annotation>> metaAnnotations = new ArrayList<>();

        /*
        源注释获取(RenderServerResources、RenderServerResource)
         */
        Optional<Annotation<Class<? extends java.lang.annotation.Annotation>>> renderServerResourcesAnnotationOptional = method.findAvailableAnnotation(
            RenderServerResources.class
        );
        if (renderServerResourcesAnnotationOptional.isPresent()) {
            for (RenderServerResource renderServerResource : (
                (RenderServerResource[]) renderServerResourcesAnnotationOptional.get().value()
            )) {
                metaAnnotations.add(new Annotation<>(renderServerResource, renderServerResource.annotationType()));
            }
        } else {
            Optional<Annotation<Class<? extends java.lang.annotation.Annotation>>> renderServerResourceAnnotationOptional = method.findAvailableAnnotation(
                RenderServerResource.class
            );
            if (renderServerResourceAnnotationOptional.isPresent()) {
                java.lang.annotation.Annotation renderServerResource = (java.lang.annotation.Annotation) renderServerResourceAnnotationOptional
                    .get()
                    .self();
                metaAnnotations.add(new Annotation<>(renderServerResource, renderServerResource.annotationType()));
            }
        }

        return metaAnnotations;
    }

    /**
     * 注册服务资源
     *
     * @param resource              服务资源
     * @param isCloseServerResource 关闭服务资源标志
     * @return 注册详情
     * @throws NullPointerException if {@code resource} is null.
     */
    protected boolean registerServerResource(ServerResource resource, boolean isCloseServerResource) {
        Objects.requireNonNull(resource);

        String path = resource.path();
        if (resources.exist(path)) {
            throw new TraceException(Formats.format("resource path [ {} ] already exist", path));
        }
        resources.register(resource, isCloseServerResource);
        return true;
    }

    @Override
    public void start() {
        doStart();
        printStartedInfo();
        ServerClosedWaiter.INSTANCE.canWait();
    }

    @Override
    public void shutdown() {
        doShutdown();
        ServerClosedWaiter.INSTANCE.doNotifyAll();
    }

    @Override
    public void destroy() {
        doDestroy();
        ServerClosedWaiter.INSTANCE.doNotifyAll();
    }

    @Override
    public void printStartedInfo() {
        if (serverConfig.getPrintStartedInfo()) {
            doPrintStartedInfo();
        }
    }

    /**
     * 子类启动
     */
    protected abstract void doStart();

    /**
     * 子类关闭
     */
    protected abstract void doShutdown();

    /**
     * 子类销毁
     */
    protected abstract void doDestroy();

    /**
     * 子类打印启动信息
     */
    protected abstract void doPrintStartedInfo();
}
