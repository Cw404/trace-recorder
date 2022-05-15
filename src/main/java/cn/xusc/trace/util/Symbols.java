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

/**
 * 符号工具类
 *
 * <p>
 * 为避免没必要的字符串对常量池的负载，这里只提供常用的符号。
 * 可以通过{@link #generate(String, int)}进行指定数量的符号获取
 * </p>
 *
 * @author WangCai
 * @since 1.0
 */
public final class Symbols {
    
    /**
     * 禁止实例化
     */
    private Symbols() {
    }
    
    /**
     * 空格符号
     */
    private static final Symbol SPACE = new Symbol(" ", 1);
    /**
     * 制表符号
     */
    private static final Symbol TAB = new Symbol(" ", 4);
    /**
     * 换行符号
     *
     * <p>
     * 平台相关
     * </p>
     */
    private static final Symbol LINE_SEPARATOR = new Symbol(System.lineSeparator(), 1);
    /**
     * 连接符号
     */
    private static final Symbol CONNECTIVE = new Symbol("-", 1);
    
    /**
     * 获取空格符
     *
     * @return 空格符
     */
    @SuppressWarnings("unused")
    public static String space() {
        return SPACE.getSymbol();
    }
    
    /**
     * 获取制表符号
     *
     * @return 制表符
     */
    @SuppressWarnings("unused")
    public static String tab() {
        return TAB.getSymbol();
    }
    
    /**
     * 获取换行符号
     *
     * @return 换行符
     */
    public static String lineSeparator() {
        return LINE_SEPARATOR.getSymbol();
    }
    
    /**
     * 获取连接符号
     *
     * @return 连接符
     */
    @SuppressWarnings("unused")
    public static String connective() {
        return CONNECTIVE.getSymbol();
    }
    
    /**
     * 生成指定数量符号
     *
     * @param originSymbol 原始符号
     * @param size         数量
     * @return 自定义数量符号
     */
    @SuppressWarnings("unused")
    public static String generate(String originSymbol, int size) {
        return new Symbol(originSymbol, size).getSymbol();
    }
    
    /**
     * 符号
     */
    private static class Symbol {
        
        /**
         * 原始符号
         */
        private final String originSymbol;
        /**
         * 数量
         */
        private final int size;
        /**
         * 最终符号
         */
        private String ultimateSymbol;
        
        /**
         * 初始化符号
         *
         * @param originSymbol 原始符号
         * @param size         符号数量
         */
        public Symbol(String originSymbol, int size) {
            this.originSymbol = originSymbol;
            this.size = size;
        }
        
        /**
         * 获取符号
         *
         * @return 最终符号
         */
        public String getSymbol() {
            if (Objects.nonNull(ultimateSymbol)) {
                return ultimateSymbol;
            }
            return ultimateSymbol = originSymbol.repeat(size);
        }
    }
}
