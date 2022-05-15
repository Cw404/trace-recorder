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

package cn.xusc.trace.constant;

/**
 * 临时常量
 *
 * <p>
 * 为跟踪记录仪提供全局修改的统一方案
 * </p>
 *
 * @author WangCai
 * @since 1.0
 */
public class Temporary {
    
    /**
     * 启用栈属性名
     */
    public static final String ENABLE_STACK = "enableStack";
    
    /**
     * 启用短类名属性名
     */
    public static final String ENABLE_SHORT_CLASS_NAME = "enableShortClassName";
    
    /**
     * 启用线程名属性名
     *
     * @since 2.0
     */
    public static final String ENABLE_THREAD_NAME = "enableThreadName";
    
    /**
     * 异常属性名
     *
     * @since 2.0
     */
    public static final String EXCEPTION = "exception";
    
    /**
     * 忽略栈包名
     */
    public static final String[] IGNORE_STACK_PACKAGE_NAME = new String[]{
            "cn.xusc.trace.handle.BaseTraceHandler",
            "cn.xusc.trace.handle.SyncTraceHandler",
            "cn.xusc.trace.handle.AsyncTraceHandler",
            "cn.xusc.trace.TraceRecorder",
            "cn.xusc.trace.util.Recorders"
    };
}
