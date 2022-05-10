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
import cn.xusc.trace.constant.Temporary;

import java.util.Arrays;
import java.util.Objects;

/**
 * 栈信息增强
 *
 * @author WangCai
 * @since 1.0
 */
public class StackInfoEnhancer implements InfoEnhancer {
    
    /**
     * 忽略的栈包名
     */
    private static final String[] IGNORE_STACK_PACKAGE_NAME = Temporary.IGNORE_STACK_PACKAGE_NAME;
    
    static {
        /*
          为二分查找前进行排序
         */
        Arrays.sort(IGNORE_STACK_PACKAGE_NAME);
    }
    
    @Override
    public EnhanceInfo enhance(EnhanceInfo eInfo) {
        /*
          根据增强信息临时值决定是否进行堆栈扫描处理
         */
        if ((boolean) eInfo.getTemporaryValue(Temporary.ENABLE_STACK)) {
            StackTraceElement[] stackTrace = new RuntimeException().getStackTrace();
            StackTraceElement stackTraceElement = null;
            for (StackTraceElement stackTraceElementF : stackTrace) {
                if (Arrays.binarySearch(IGNORE_STACK_PACKAGE_NAME,stackTraceElementF.getClassName()) < 0) {
                    stackTraceElement = stackTraceElementF;
                    break;
                }
            }
            if (Objects.isNull(stackTraceElement)) {
                throw new RuntimeException("not found specify stack, stack info: " + Arrays.toString(stackTrace));
            }
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
            eInfo.setWriteInfo(String.format("%s.%s()[%d] - %s", eInfo.getClassName(), eInfo.getMethodName(),
                    eInfo.getLineNumber(), eInfo.getInfo()));
        }
        return eInfo;
    }
}
