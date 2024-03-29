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
package cn.xusc.trace.core.enhance;

import cn.xusc.trace.core.EnhanceInfo;
import cn.xusc.trace.core.util.ProtocolFamilies;

/**
 * 协议消除信息增强器
 *
 * <p>
 * 建议搭配ProtocolInfoFilter使用，用来去除协议头
 * </p>
 *
 * @author WangCai
 * @since 2.5
 */
public class ProtocolEliminateInfoEnhancer implements InfoEnhancer {

    @Override
    public EnhanceInfo enhance(EnhanceInfo eInfo) {
        eInfo.setInfo(ProtocolFamilies.eliminate(eInfo.getInfo()));
        return eInfo;
    }

    @Override
    public EnhanceInfo setWriteInfo(EnhanceInfo eInfo) {
        eInfo.setWriteInfo(eInfo.getInfo());
        return eInfo;
    }
}
