/*
 * Copyright 20022 WangCai.
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

package cn.xusc.trace.enhance;

import cn.xusc.trace.EnhanceInfo;
import cn.xusc.trace.util.Symbols;

import java.util.Objects;

/**
 * 行信息增强器
 *
 * <p>
 * 为信息换行，根据平台相关性{@link System#lineSeparator()}
 * </p>
 *
 * @author WangCai
 * @since 1.0
 */
public class LineInfoEnhancer implements InfoEnhancer {
    
    @Override
    public EnhanceInfo enhance(EnhanceInfo eInfo) {
        eInfo.setInfo(eInfo.getInfo() + Symbols.lineSeparator());
        return eInfo;
    }
    
    @Override
    public EnhanceInfo setWriteInfo(EnhanceInfo eInfo) {
        if (Objects.nonNull(eInfo.getInfo())) {
            /*
              如果消息不为空，默认写出消息就为输入消息
             */
            eInfo.setWriteInfo(eInfo.getInfo() + Symbols.lineSeparator());
        }
        return eInfo;
    }
}
