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
package cn.xusc.trace.util;

import cn.xusc.trace.constant.ProtocolFamily;
import java.util.Objects;
import lombok.experimental.UtilityClass;

/**
 * 协议族工具类
 *
 * <p>
 * 通过lombok组件{@link UtilityClass}确保工具类的使用
 * </p>
 *
 * @author WangCai
 * @since 2.5
 */
@UtilityClass
public class ProtocolFamilies {

    /**
     * 协议族注册初始化标识
     */
    private boolean protocolFamilyRegisterInit = false;

    /**
     * 协议消除
     *
     * @param protocolStr 协议字符串
     * @return 消除协议后的字符串
     * @throws NullPointerException if {@code protocolStr} is null.
     */
    public String eliminate(String protocolStr) {
        Objects.requireNonNull(protocolStr);

        ensureProtocolFamilyRegisterInit();

        String eliminateProtocolStr = null;
        String head;
        for (ProtocolFamily protocolFamily : ProtocolFamily.PROTOCOL_FAMILIES) {
            if (protocolStr.startsWith(head = protocolFamily.head())) {
                eliminateProtocolStr = protocolStr.substring(head.length());
                break;
            }
        }
        return Objects.isNull(eliminateProtocolStr) ? protocolStr : eliminateProtocolStr;
    }

    /**
     * 确保协议族注册初始化
     */
    private void ensureProtocolFamilyRegisterInit() {
        if (!protocolFamilyRegisterInit) {
            ProtocolFamily.Net.HTTP.HTTP.register();
            ProtocolFamily.Net.HTTP.HTTPS.register();
            protocolFamilyRegisterInit = true;
        }
    }
}
