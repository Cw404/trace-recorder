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
package cn.xusc.trace.common.util;

import java.io.IOException;
import java.io.StringReader;
import java.util.*;
import lombok.experimental.UtilityClass;

/**
 * 字符串工具类
 *
 * <p>
 * 通过lombok组件{@link UtilityClass}确保工具类的使用
 * </p>
 *
 * @author WangCai
 * @since 2.0
 */
@UtilityClass
public class Strings {

    /**
     * 空字符串
     */
    private final String EMPTY = "";

    /**
     * 空字符串数组
     *
     * @since 2.5
     */
    private final String[] EMPTY_ARRAY = {};

    /**
     * 空字符串
     *
     * @return 空字符串
     */
    public String empty() {
        return EMPTY;
    }

    /**
     * 是否为空字符串
     *
     * @param str 判断字符串
     * @return 是否为空结果
     */
    public boolean isEmpty(String str) {
        return EMPTY.equals(str);
    }

    /**
     * 字符串是否相等
     *
     * @param str  第一个字符串
     * @param str1 第二个字符串
     * @return 字符串是否相等结果
     * @throws NullPointerException if {@code str} or {@code str1} is null
     */
    public boolean equals(String str, String str1) {
        Objects.requireNonNull(str);
        Objects.requireNonNull(str1);

        return str.equals(str1);
    }

    /**
     * 字符串忽略大小写是否相等
     *
     * @param str  第一个字符串
     * @param str1 第二个字符串
     * @return 字符串是否相等结果
     * @throws NullPointerException if {@code str} or {@code str1} is null
     * @since 2.5
     */
    public boolean equalsIgnoreCase(String str, String str1) {
        Objects.requireNonNull(str);
        Objects.requireNonNull(str1);

        return str.equalsIgnoreCase(str1);
    }

    /**
     * 根据指定分隔符分割字符串
     *
     * @param originStr 原字符串
     * @param separator 分隔符
     * @return 分割后的字符串列表
     * @throws NullPointerException if {@code originStr} is null.
     * @since 2.4
     */
    public List<String> split(String originStr, char separator) {
        Objects.requireNonNull(originStr);

        if (isEmpty(originStr)) {
            return Collections.emptyList();
        }

        FastList<String> splits = new FastList<>(String.class, 16);
        StringReader reader = new StringReader(originStr);
        StringBuilder sb = new StringBuilder();
        int read;
        char readChar;
        try {
            while (reader.ready()) {
                if ((read = reader.read()) == -1) {
                    break;
                }
                if ((readChar = (char) read) == separator) {
                    splits.add(sb.toString());
                    sb = new StringBuilder();
                    continue;
                }
                sb.append(readChar);
            }

            if (sb.length() > 0) {
                splits.add(sb.toString());
            }
        } catch (IOException e) {
            // not to this
        }

        return splits;
    }

    /**
     * 字符串分词为字符串数组
     *
     * @param str 字符串
     * @param delimiters 分隔符
     * @param trimTokens 是否修剪令牌
     * @param ignoreEmptyTokens 是否忽略空令牌
     * @return 分词的字符串数组
     * @since 2.5
     */
    public static String[] tokenizeToStringArray(
        String str,
        String delimiters,
        boolean trimTokens,
        boolean ignoreEmptyTokens
    ) {
        if (Objects.isNull(str)) {
            return EMPTY_ARRAY;
        }

        StringTokenizer st = new StringTokenizer(str, delimiters);
        List<String> tokens = new ArrayList<>();
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            if (trimTokens) {
                token = token.trim();
            }
            if (!ignoreEmptyTokens || token.length() > 0) {
                tokens.add(token);
            }
        }
        return toStringArray(tokens);
    }

    /**
     * 集合转换成数组
     *
     * @param collection 集合
     * @return 转换后的数组
     * @since 2.5
     */
    public static String[] toStringArray(Collection<String> collection) {
        return Boolean.logicalAnd(Objects.nonNull(collection), !collection.isEmpty())
            ? collection.toArray(EMPTY_ARRAY)
            : EMPTY_ARRAY;
    }
}
