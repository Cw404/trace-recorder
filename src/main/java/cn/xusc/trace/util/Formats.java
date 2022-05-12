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

package cn.xusc.trace.util;

import java.util.Objects;

/**
 * 消息格式化工具类
 *
 * <p>
 * 尽可能格式化参数列表数据到表达式
 * </p>
 *
 * @author WangCai
 * @since 1.1
 */
public final class Formats {
    
    /**
     * 禁止实例化
     */
    private Formats() {
    }
    
    /**
     * 分割符
     */
    private static final String SEPARATOR = "{}";
    /**
     * 转义符
     */
    private static final char ESCAPE = '\\';
    
    /**
     * 格式化消息
     *
     * @param messagePattern 格式消息
     * @param arg            参数
     * @return 格式化消息
     */
    public static String format(String messagePattern, Object arg) {
        return format(messagePattern, new Object[]{arg});
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
     */
    public static String format(String messagePattern, Object... argArray) {
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
                sb.append(messagePattern, start, start = end + 1);
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
}
