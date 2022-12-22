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
package cn.xusc.trace.dashboard.constant;

import java.nio.file.Path;

/**
 * 临时常量
 *
 * <p>
 * 为跟踪仪表盘提供全局修改的统一方案
 * </p>
 *
 * @author WangCai
 * @since 2.5.3
 */
public class Temporary {

    /**
     * SPI组件前缀
     */
    public static final String SPI_COMPONENT_PREFIX = "inner_xusc_dashboard_";

    /**
     * 忽略堆栈类名
     */
    public static final String[] IGNORE_STACK_CLASS_NAMES = new String[] {
        "cn.xusc.trace.dashboard.TraceDashboardEnhancer",
        "cn.xusc.trace.core.enhance.AbstractStatisticsInfoEnhancer",
        "cn.xusc.trace.core.handle.BaseTraceHandler",
        "cn.xusc.trace.core.handle.SyncTraceHandler",
        "cn.xusc.trace.core.handle.AsyncTraceHandler",
        "cn.xusc.trace.core.TraceRecorder",
        "cn.xusc.trace.core.util.Recorders",
    };

    /**
     * 模版目录路径
     */
    public static final Path TEMPLATE_PATH = Path.of("template");

    /**
     * 生成目录路径
     */
    public static final Path GENERATE_PATH = Path.of("generate");
}
