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
package cn.xusc.trace.dashboard.base.resource;

import cn.xusc.trace.server.annotation.*;
import java.nio.file.Path;

/**
 * 基础跟踪仪表盘组件服务资源
 *
 * @author WangCai
 * @since 2.5.3
 */
public class BaseTraceDashboardComponentResource
    extends cn.xusc.trace.dashboard.resource.BaseTraceDashboardComponentResource {

    /**
     * 基础构造
     *
     * @param generatePath 生成路径
     */
    public BaseTraceDashboardComponentResource(Path generatePath) {
        super(generatePath);
    }

    /**
     * 跟踪仪表盘资源
     *
     * @return dashboard.html
     */
    @OutputStreamServerResource
    @ServerResource(path = { "/", "/dashboard" })
    public byte[] dashboard() {
        return readGenerateResourceData("dashboard/base/dashboard.html");
    }

    /**
     * 跟踪仪表盘数据资源
     *
     * @return dashboard.json
     */
    @ServerResource(path = { "/data/dashboard.json" })
    public byte[] dashboardJson() {
        return readGenerateResourceData("dashboard/base/data/dashboard.json");
    }

    /**
     * uikit.css
     *
     * @return uikit.css
     */
    @ServerResource(path = { "/uikit-3/css/uikit.css" })
    public byte[] uikitCss() {
        return readGenerateResourceData("dashboard/base/uikit-3/css/uikit.css");
    }

    /**
     * uikit-rtl.css
     *
     * @return uikit-rtl.css
     */
    @ServerResource(path = { "/uikit-3/css/uikit-rtl.css" })
    public byte[] uikitRtlCss() {
        return readGenerateResourceData("dashboard/base/uikit-3/css/uikit-rtl.css");
    }

    /**
     * uikit-extension.css
     *
     * @return uikit-extension.css
     */
    @ServerResource(path = { "/uikit-3/extension/css/uikit-extension.css" })
    public byte[] uikitExtensionCss() {
        return readGenerateResourceData("dashboard/base/uikit-3/extension/css/uikit-extension.css");
    }

    /**
     * uikit-icons-extension.js
     *
     * @return uikit-icons-extension.js
     */
    @ServerResource(path = { "/uikit-3/extension/js/uikit-icons-extension.js" })
    public byte[] uikitIconsExtensionJs() {
        return readGenerateResourceData("dashboard/base/uikit-3/extension/js/uikit-icons-extension.js");
    }

    /**
     * uikit.js
     *
     * @return uikit.js
     */
    @ServerResource(path = { "/uikit-3/js/uikit.js" })
    public byte[] uikitJs() {
        return readGenerateResourceData("dashboard/base/uikit-3/js/uikit.js");
    }

    /**
     * uikit-icons.js
     *
     * @return uikit-icons.js
     */
    @ServerResource(path = { "/uikit-3/js/uikit-icons.js" })
    public byte[] uikitIconsJs() {
        return readGenerateResourceData("dashboard/base/uikit-3/js/uikit-icons.js");
    }

    /**
     * jquery.js
     *
     * @return jquery.js
     */
    @ServerResource(path = { "/js/jquery.js" })
    public byte[] jqueryJs() {
        return readGenerateResourceData("dashboard/base/js/jquery.js");
    }

    /**
     * trace-recorder.svg
     *
     * @return trace-recorder.svg
     */
    @OutputStreamServerResource
    @ServerResource(path = { "/trace-recorder.svg" })
    public byte[] traceRecorderSvg() {
        return readGenerateResourceData("dashboard/base/trace-recorder.svg");
    }

    /**
     * trace-recorder-larger.svg
     *
     * @return trace-recorder-larger.svg
     */
    @OutputStreamServerResource
    @ServerResource(path = { "/trace-recorder-larger.svg" })
    public byte[] traceRecorderLargerSvg() {
        return readGenerateResourceData("dashboard/base/trace-recorder-larger.svg");
    }

    /**
     * 服务关闭资源（重写）
     *
     * @return closed.html
     */
    @OverrideServerResource
    @ServerCloseResource(path = { "/close" })
    public byte[] close() {
        return readClassLoaderResourceData("common/closed.html");
    }
}
