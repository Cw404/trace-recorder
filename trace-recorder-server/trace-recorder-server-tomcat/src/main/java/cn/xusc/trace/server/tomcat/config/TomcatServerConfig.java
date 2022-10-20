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
package cn.xusc.trace.server.tomcat.config;

import cn.xusc.trace.server.AbstractServerConfig;
import cn.xusc.trace.server.constant.Temporary;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * tomcat服务配置
 *
 * @author WangCai
 * @since 2.5
 */
@Setter
@Getter
@SuperBuilder
public class TomcatServerConfig<R> extends AbstractServerConfig<R> {

    /**
     * 主机
     */
    @Builder.Default
    private String host = Temporary.HOST;

    /**
     * 端口
     */
    @Builder.Default
    private int port = Temporary.PORT;

    /**
     * 上下文路径
     */
    @Builder.Default
    private String contextPath = "/trace-recorder";

    /**
     * 基本目录
     */
    private String baseDir;

    /**
     * 静态文件的上下文的基本目录
     */
    private String docBase;

    /**
     * 使用文件属性文件
     */
    @Builder.Default
    private Boolean useFileProperties = Boolean.TRUE;

    /**
     * 日志属性文件路径
     */
    private String loggingPropertiesPath;

    /**
     * 访问请求资源路径
     */
    @Builder.Default
    private String[] accessRequestPaths = { "/" };

    /**
     * 关闭请求资源路径
     */
    @Builder.Default
    private String[] closeRequestPath = { "/close" };
}
