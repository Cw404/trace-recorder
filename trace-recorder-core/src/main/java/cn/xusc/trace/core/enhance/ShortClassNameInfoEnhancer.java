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
import cn.xusc.trace.core.constant.Temporary;

/**
 * 短类名增强器
 *
 * <p>
 * 依赖{@link StackInfoEnhancer}的栈处理
 * </p>
 *
 * @author WangCai
 * @since 1.0
 */
public class ShortClassNameInfoEnhancer implements InfoEnhancer {

    @Override
    public EnhanceInfo enhance(EnhanceInfo eInfo) {
        /*
          根据增强信息临时值决定是否进行信息处理
         */
        if (
            (boolean) eInfo.getTemporaryValue(Temporary.ENABLE_STACK) &&
            (boolean) eInfo.getTemporaryValue(Temporary.ENABLE_SHORT_CLASS_NAME)
        ) {
            String className = eInfo.getClassName();
            int pointIndex = className.lastIndexOf(".");
            if (pointIndex > -1) {
                eInfo.setClassName(className.substring(pointIndex + 1));
            }
        }
        return eInfo;
    }

    @Override
    public EnhanceInfo setWriteInfo(EnhanceInfo eInfo) {
        /*
          根据增强信息临时值决定是否进行堆栈信息填充处理
         */
        if (
            (boolean) eInfo.getTemporaryValue(Temporary.ENABLE_STACK) &&
            (boolean) eInfo.getTemporaryValue(Temporary.ENABLE_SHORT_CLASS_NAME)
        ) {
            eInfo.setWriteInfo(
                String.format(
                    "%s.%s()[%d] - %s",
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
