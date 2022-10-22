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

import cn.xusc.trace.chart.resource.BaseChartServerResource;
import cn.xusc.trace.common.util.Systems;
import cn.xusc.trace.example.server.standard.StandardServerResource;
import cn.xusc.trace.server.tomcat.TomcatServer;
import cn.xusc.trace.server.tomcat.config.TomcatServerConfig;
import cn.xusc.trace.server.util.ServerClosedWaiter;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * tomcat服务测试
 *
 * @author wangcai
 */
public final class TomcatServerTest {

    /**
     * 运行tomcat服务
     */
    @SneakyThrows
    @ParameterizedTest
    @MethodSource("generateServerArgs")
    @DisplayName("run tomcat server")
    public void runServer(TomcatServer server) {
        server.start();
        waitServer();
    }

    /**
     * 等待服务关闭
     *
     * @throws InterruptedException if interrupted while sleeping.
     */
    private void waitServer() throws InterruptedException {
        ServerClosedWaiter.INSTANCE.doWait(1);
    }

    /**
     * 生成一组服务参数
     *
     * @return 一组服务参数
     */
    @DisplayName("generate a group of server args")
    private static Stream<Arguments> generateServerArgs() {
        Path tempPath = Path.of(Systems.getProperties("java.io.tmpdir"));
        return Stream.of(
            Arguments.arguments(
                new TomcatServer(
                    TomcatServerConfig
                        .builder()
                        .resources(List.of(new BaseChartServerResource(tempPath), new StandardServerResource(tempPath)))
                        .accessRequestPaths(new String[] { "/", "/trace-recorder" })
                        .build()
                )
            )
        );
    }
}
