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
package cn.xusc.trace.example.server;

import cn.xusc.trace.example.server.standard.StandardServer;
import cn.xusc.trace.example.server.standard.StandardServerConfig;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * 服务测试
 *
 * @author wangcai
 */
public final class ServerTest {

    /**
     * 运行服务
     *
     * @param server 标准服务
     */
    @ParameterizedTest
    @MethodSource("generateServerArgs")
    @DisplayName("run server")
    public void runServer(StandardServer server) {
        server.start();
    }

    /**
     * 生成一组服务参数
     *
     * @return 一组服务参数
     */
    @DisplayName("generate a group of server args")
    private static Stream<Arguments> generateServerArgs() {
        return Stream.of(Arguments.arguments(new StandardServer(StandardServerConfig.builder().build())));
    }
}
