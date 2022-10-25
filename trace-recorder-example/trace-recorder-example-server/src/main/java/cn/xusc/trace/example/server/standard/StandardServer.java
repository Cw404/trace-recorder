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
package cn.xusc.trace.example.server.standard;

import cn.xusc.trace.common.util.Systems;
import cn.xusc.trace.server.AbstractServer;

/**
 * 标准服务
 *
 * @author wangcai
 */
public class StandardServer extends AbstractServer {

    /**
     * 基础构造
     *
     * @param config 标准服务配置
     */
    public StandardServer(StandardServerConfig config) {
        super(config);
    }

    @Override
    protected void doStart() {
        Systems.report("server start");
    }

    @Override
    protected void doShutdown() {
        Systems.report("server shutdown");
    }

    @Override
    protected void doDestroy() {
        Systems.report("server destroy");
    }

    @Override
    protected void doPrintStartedInfo() {
        Systems.report("this is standard server!");
        mockServerProcess();
    }

    /**
     * 模拟服务处理
     */
    private void mockServerProcess() {
        Systems.report("wait request...");
        Systems.report("request to close server");
        shutdown();
        destroy();
    }
}
