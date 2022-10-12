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
package cn.xusc.trace.core.filter.protocol;

/**
 * 协议过滤器
 *
 * @author WangCai
 * @since 2.5
 */
public interface ProtocolFilter {
    /**
     * 是否支持协议
     *
     * @param protocolStr 协议串
     * @return 协议支持情况
     */
    boolean isSupport(String protocolStr);
}
