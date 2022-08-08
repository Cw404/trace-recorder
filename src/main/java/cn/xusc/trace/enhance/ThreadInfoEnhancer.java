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
package cn.xusc.trace.enhance;

import cn.xusc.trace.EnhanceInfo;
import cn.xusc.trace.constant.Temporary;

/**
 * 线程信息增强
 *
 * @author WangCai
 * @since 2.0
 */
public class ThreadInfoEnhancer implements InfoEnhancer {

    @Override
    public EnhanceInfo enhance(EnhanceInfo eInfo) {
        return eInfo;
    }

    @Override
    public EnhanceInfo setWriteInfo(EnhanceInfo eInfo) {
        /*
          根据增强信息临时值决定是否进行线程信息处理
         */
        if ((boolean) eInfo.getTemporaryValue(Temporary.ENABLE_THREAD_NAME)) {
            eInfo.setWriteInfo(
                String.format(
                    "%s %s.%s()[%d] - %s",
                    Thread.currentThread().getName(),
                    eInfo.getClassName(),
                    eInfo.getMethodName(),
                    eInfo.getLineNumber(),
                    eInfo.getInfo()
                )
            );
        }
        return eInfo;
    }
}
