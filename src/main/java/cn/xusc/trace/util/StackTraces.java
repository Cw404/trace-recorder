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

import cn.xusc.trace.exception.TraceException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;
import lombok.experimental.UtilityClass;

/**
 * 堆栈跟踪工具类
 *
 * <p>
 * 通过lombok组件{@link UtilityClass}确保工具类的使用
 * </p>
 *
 * @author WangCai
 * @since 2.5
 */
@UtilityClass
public class StackTraces {

    /**
     * 获取当前忽略堆栈类名的可选堆栈元素
     *
     * @param ignoreStackClassNames 忽略堆栈类名集
     * @return 当前忽略堆栈类名的可选堆栈元素
     */
    public Optional<StackTraceElement[]> currentStackTraceElement(String... ignoreStackClassNames) {
        Objects.requireNonNull(ignoreStackClassNames);

        Optional<StackTraceElement[]> stackTraceElements = innerCurrentStackTraceElement(
            Optional.empty(),
            () -> ignoreStackClassNames,
            false
        );
        return stackTraceElements.isPresent() ? Optional.of(stackTraceElements.get()) : Optional.empty();
    }

    /**
     * 获取当前忽略堆栈类名的第一个可选堆栈元素
     *
     * @param ignoreStackClassNames 忽略堆栈类名集
     * @return 当前忽略堆栈类名的第一个可选堆栈元素
     */
    public Optional<StackTraceElement> currentFirstStackTraceElement(String... ignoreStackClassNames) {
        Objects.requireNonNull(ignoreStackClassNames);

        Optional<StackTraceElement[]> stackTraceElements = innerCurrentStackTraceElement(
            Optional.empty(),
            () -> ignoreStackClassNames,
            true
        );
        return stackTraceElements.isPresent() ? Optional.of(stackTraceElements.get()[0]) : Optional.empty();
    }

    /**
     * 获取当前异常忽略堆栈类名的可选堆栈元素
     *
     * @param exception 处理的异常
     * @param ignoreStackClassNames 忽略堆栈类名集
     * @return 当前忽略堆栈类名的可选堆栈元素
     */
    public Optional<StackTraceElement[]> currentStackTraceElement(
        Exception exception,
        String... ignoreStackClassNames
    ) {
        Objects.requireNonNull(exception);
        Objects.requireNonNull(ignoreStackClassNames);

        Optional<StackTraceElement[]> stackTraceElements = innerCurrentStackTraceElement(
            Optional.of(exception),
            () -> ignoreStackClassNames,
            false
        );
        return stackTraceElements.isPresent() ? Optional.of(stackTraceElements.get()) : Optional.empty();
    }

    /**
     * 获取当前异常忽略堆栈类名的第一个可选堆栈元素
     *
     * @param exception 处理的异常
     * @param ignoreStackClassNames 忽略堆栈类名集
     * @return 当前忽略堆栈类名的第一个可选堆栈元素
     */
    public Optional<StackTraceElement> currentFirstStackTraceElement(
        Exception exception,
        String... ignoreStackClassNames
    ) {
        Objects.requireNonNull(exception);
        Objects.requireNonNull(ignoreStackClassNames);

        Optional<StackTraceElement[]> stackTraceElements = innerCurrentStackTraceElement(
            Optional.of(exception),
            () -> ignoreStackClassNames,
            true
        );
        return stackTraceElements.isPresent() ? Optional.of(stackTraceElements.get()[0]) : Optional.empty();
    }

    /**
     * 内部获取当前忽略堆栈类名的可选堆栈元素
     *
     * @param ignoreStackClassNamesSupplier 忽略堆栈类名集提供者
     * @return 当前忽略堆栈类名的可选堆栈元素
     */
    private Optional<StackTraceElement[]> innerCurrentStackTraceElement(
        Optional<Exception> exceptionOptional,
        Supplier<String[]> ignoreStackClassNamesSupplier,
        boolean onlyFirstStackTraceElement
    ) {
        String[] ignoreStackClassNames = ignoreStackClassNamesSupplier.get();

        int srcPosition = exceptionOptional.isEmpty() ? 4 : 0;
        StackTraceElement[] stackTraceElements = exceptionOptional
            .orElseGet(() -> new TraceException())
            .getStackTrace();
        int ignoreStackClassNamesLength = ignoreStackClassNames.length;
        int stackTraceElementsLength = stackTraceElements.length;
        if (ignoreStackClassNamesLength > 0) {
            if (ignoreStackClassNamesLength > 1) {
                /*
                  为二分查找前进行排序
                 */
                Arrays.sort(ignoreStackClassNames);
            }

            /*
              如果匹配成功，起始位置进行偏移
             */
            for (int index = srcPosition; index < stackTraceElementsLength; srcPosition = ++index) {
                if (Arrays.binarySearch(ignoreStackClassNames, stackTraceElements[index].getClassName()) < 0) {
                    if (onlyFirstStackTraceElement) {
                        return Optional.of(new StackTraceElement[] { stackTraceElements[index] });
                    }
                    break;
                }
            }

            if (srcPosition == stackTraceElementsLength) {
                return Optional.empty();
            }
        }

        if (onlyFirstStackTraceElement) {
            return Optional.of(new StackTraceElement[] { stackTraceElements[srcPosition] });
        }

        int newLength = stackTraceElementsLength - srcPosition;
        StackTraceElement[] currentStackTraceElements = (StackTraceElement[]) Array.newInstance(
            StackTraceElement.class,
            newLength
        );
        System.arraycopy(stackTraceElements, srcPosition, currentStackTraceElements, 0, newLength);
        return Optional.of(currentStackTraceElements);
    }
}
