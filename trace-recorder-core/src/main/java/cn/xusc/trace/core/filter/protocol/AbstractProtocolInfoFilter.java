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

import cn.xusc.trace.core.constant.RecordLabel;
import cn.xusc.trace.core.filter.InfoFilter;
import java.util.Objects;

/**
 * 抽象协议信息过滤器
 *
 * @author WangCai
 * @since 2.5
 */
public abstract class AbstractProtocolInfoFilter implements InfoFilter, ProtocolFilter {

    /**
     * 内部协议
     */
    private final String INNER_PROTOCOL;

    /**
     * 基础构造
     *
     * <p>
     * 协议初始化
     * </p>
     *
     * @param innerProtocol 内部协议
     */
    public AbstractProtocolInfoFilter(String innerProtocol) {
        INNER_PROTOCOL = innerProtocol;
    }

    @Override
    public boolean isRecord(String info, RecordLabel label) {
        return isSupport(info);
    }

    @Override
    public boolean isSupport(String protocolStr) {
        if (Objects.isNull(protocolStr)) {
            return false;
        }
        return protocolStr.startsWith(INNER_PROTOCOL);
    }
}
