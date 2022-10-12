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

import cn.xusc.trace.common.util.StackTraces;
import cn.xusc.trace.core.EnhanceInfo;
import cn.xusc.trace.core.constant.Temporary;
import java.util.Optional;

/**
 * 栈信息增强
 *
 * @author WangCai
 * @since 1.0
 */
public class StackInfoEnhancer implements InfoEnhancer {

    @Override
    public EnhanceInfo enhance(EnhanceInfo eInfo) {
        /*
          根据增强信息临时值决定是否进行堆栈扫描处理
         */
        if ((boolean) eInfo.getTemporaryValue(Temporary.ENABLE_STACK)) {
            /*
              异常从临时值获取，容错异步处理的堆栈
             */
            Exception exception = ((Exception) eInfo.getTemporaryValue(Temporary.EXCEPTION));
            Optional<StackTraceElement> stackTraceElementOptional = StackTraces.currentFirstStackTraceElement(
                exception,
                Temporary.IGNORE_STACK_CLASS_NAMES
            );

            StackTraceElement stackTraceElement = stackTraceElementOptional.get();
            eInfo.setClassName(stackTraceElement.getClassName());
            eInfo.setMethodName(stackTraceElement.getMethodName());
            eInfo.setLineNumber(stackTraceElement.getLineNumber());
        }
        return eInfo;
    }

    @Override
    public EnhanceInfo setWriteInfo(EnhanceInfo eInfo) {
        /*
          根据增强信息临时值决定是否进行堆栈信息填充处理
         */
        if ((boolean) eInfo.getTemporaryValue(Temporary.ENABLE_STACK)) {
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
