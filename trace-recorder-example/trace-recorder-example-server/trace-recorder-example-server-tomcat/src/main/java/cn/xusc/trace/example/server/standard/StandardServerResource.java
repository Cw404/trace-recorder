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

import cn.xusc.trace.chart.resource.BaseChartServerResource;
import cn.xusc.trace.server.annotation.ServerResource;
import java.nio.file.Path;

/**
 * 标准服务资源
 *
 * @author WangCai
 * @since 2.5
 */
public class StandardServerResource extends BaseChartServerResource {

    /**
     * 基础构造
     *
     * @param generatePath 生成路径
     */
    public StandardServerResource(Path generatePath) {
        super(generatePath);
    }

    /**
     * trace-recorder服务资源
     *
     * @return trace-recorder页面资源
     */
    @ServerResource(path = { "/", "/trace-recorder" })
    public String traceRecorder() {
        return (
            "<!DOCTYPE html>\n" +
            "<html lang=\"zh-CN\">\n" +
            "<head>\n" +
            "    <meta charset=\"UTF-8\">\n" +
            "    <title>trace-recorder</title>\n" +
            "    <link rel=\"shortcut icon\" href=\"trace-recorder-icon.png\" />\n" +
            "\n" +
            "    <link rel=\"stylesheet\" href=\"css/spectre.min.css\">\n" +
            "</head>\n" +
            "<body class=\"bg-gray container pt-2\">\n" +
            "    <div class=\"columns\">\n" +
            "        <div class=\"column col-1 col-mr-auto\"></div>\n" +
            "        <div class=\"column col-4 col-mr-auto bg-gray hero\">\n" +
            "            <div class=\"hero-body\">\n" +
            "                <h1>trace-recorder</h1>\n" +
            "            </div>\n" +
            "        </div>\n" +
            "        <div class=\"column col-1\"></div>\n" +
            "    </div>\n" +
            "</body>\n" +
            "</html>"
        );
    }
}
