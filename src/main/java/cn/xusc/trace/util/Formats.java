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

import java.util.Objects;
import lombok.experimental.UtilityClass;

/**
 * 消息格式化工具类
 *
 * <p>
 * 尽可能格式化参数列表数据到表达式
 * 通过lombok组件{@link UtilityClass}确保工具类的使用
 * </p>
 *
 * @author WangCai
 * @since 1.1
 */
@UtilityClass
public class Formats {

    /**
     * 分割符
     */
    private final String SEPARATOR = "{}";
    /**
     * 转义符
     */
    private final char ESCAPE = '\\';

    /**
     * 格式化消息
     *
     * @param messagePattern 格式消息
     * @param arg            参数
     * @return 格式化消息
     */
    public String format(String messagePattern, Object arg) {
        return format(messagePattern, new Object[] { arg });
    }

    /**
     * 格式化消息
     *
     * <p>
     * 尽可能格式化消息，如果参数列表数量多于格式分隔符，则丢弃
     * </p>
     *
     * @param messagePattern 格式消息
     * @param argArray       参数列表
     * @return 格式化消息
     * @throws NullPointerException if {@code messagePattern} is null.
     * @throws NullPointerException if {@code argArray} is null.
     */
    public String format(String messagePattern, Object... argArray) {
        Objects.requireNonNull(messagePattern);
        Objects.requireNonNull(argArray);

        int mLength = messagePattern.length();
        if (mLength < SEPARATOR.length()) {
            /*
              小于分隔符长度的格式消息直接返回
             */
            return messagePattern;
        }

        StringBuilder sb = new StringBuilder(mLength + 50);
        int start = 0, end, index = 0;
        while ((end = messagePattern.indexOf(SEPARATOR, start)) > -1) {
            if (end > 0 && Objects.equals(messagePattern.charAt(end - 1), ESCAPE)) {
                sb.append(messagePattern, start, start = end + 2);
                continue;
            }

            sb.append(messagePattern, start, end);
            sb.append(argArray[index++]);
            start = end + 2;
            if (index == argArray.length) {
                /*
                  参数列表已经填充完了
                 */
                break;
            }
        }

        return start == 0 ? messagePattern : sb.append(messagePattern, start, mLength).toString();
    }

    /**
     * 是否有更多格式参数
     *
     * @param messagePattern 格式消息
     * @param argArray       参数列表
     * @return 更多参数情况
     * @throws NullPointerException if {@code messagePattern} is null.
     * @throws NullPointerException if {@code argArray} is null.
     * @since 2.5
     */
    public boolean isMoreArgs(String messagePattern, Object... argArray) {
        Objects.requireNonNull(messagePattern);
        Objects.requireNonNull(argArray);

        int mLength = messagePattern.length();
        if (mLength < SEPARATOR.length()) {
            /*
               小于分隔符长度的格式消息，存在两种情况
                情况一：
                    存在参数组
                情况二:
                    不存在参数组
             */
            if (argArray.length > 0) {
                return true;
            }
            return false;
        }

        int start = 0, end, aryArrayCount = argArray.length;
        while ((end = messagePattern.indexOf(SEPARATOR, start)) > -1) {
            if (end > 0 && Objects.equals(messagePattern.charAt(end - 1), ESCAPE)) {
                start = end + 2;
                continue;
            }

            aryArrayCount--;
            if (aryArrayCount < 0) {
                /*
                  还需填充占位，但参数列表已经填充完了
                 */
                return false;
            }
            start = end + 2;
        }

        return aryArrayCount == 0 ? false : true;
    }
}
