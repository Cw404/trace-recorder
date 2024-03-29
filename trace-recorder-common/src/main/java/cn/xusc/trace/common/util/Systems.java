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

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import lombok.experimental.UtilityClass;

/**
 * 系统工具类
 *
 * <p>
 * 通过lombok组件{@link UtilityClass}确保工具类的使用
 * </p>
 *
 * @author WangCai
 * @since 2.5
 */
@UtilityClass
public class Systems {

    /**
     * 系统类型
     */
    private SystemType systemType;

    /**
     * 属性分割符
     */
    private String splitSymbol;

    static {
        systemType = SystemType.systemType(getProperties("os.name"));
        splitSymbol = Objects.equals(systemType, SystemType.WINDOWS) ? ";" : ":";
    }

    /**
     * 设置系统属性
     *
     * @param key 属性键
     * @param value 属性值
     * @return 设置详情
     * @throws NullPointerException if {@code key} is null.
     * @throws NullPointerException if {@code value} is null.
     */
    public boolean setProperties(String key, String value) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(value);

        System.getProperties().setProperty(key, value);
        return true;
    }

    /**
     * 获取系统属性值
     *
     * @param key 属性键
     * @return 属性值
     * @throws NullPointerException if {@code key} is null.
     */
    public String getProperties(String key) {
        Objects.requireNonNull(key);

        return (String) System.getProperties().get(key);
    }

    /**
     * 获取类路径值
     *
     * @return 类路径值
     */
    public String getClassPath() {
        return getProperties("java.class.path");
    }

    /**
     * 获取类路径值集
     *
     * @return 类路径值集
     */
    public String[] getClassPaths() {
        return getClassPath().split(splitSymbol);
    }

    /**
     * 获取忽略后类路径的值集
     *
     * @param ignoreClassPathPredicate 忽略类路径断言
     * @return 忽略后类路径的值集
     * @throws NullPointerException if {@code ignoreClassPathPredicate} is null.
     */
    public String[] getClassPaths(Predicate<String> ignoreClassPathPredicate) {
        Objects.requireNonNull(ignoreClassPathPredicate);

        List<String> needClassPaths = new ArrayList<>();
        for (String classPath : getClassPath().split(splitSymbol)) {
            if (ignoreClassPathPredicate.test(classPath)) {
                needClassPaths.add(classPath);
            }
        }
        return needClassPaths.toArray(new String[0]);
    }

    /**
     * 获取java家路径
     *
     * @return java家路径值
     */
    public String getJavaHome() {
        return getProperties("java.home");
    }

    /**
     * 获取系统类路径
     *
     * @param path 路径
     * @return 系统类路径
     * @since 2.5.1
     */
    public String getSystemClassPath(String path) {
        if (Objects.equals(systemType, SystemType.WINDOWS)) {
            return path.replace("\\", "/");
        }
        return path;
    }

    /**
     * 获取系统类路径
     *
     * @param path 路径
     * @return 系统类路径
     * @since 2.5.1
     */
    public Path getSystemClassPath(Path path) {
        return Path.of(getSystemClassPath(path.toString()));
    }

    /**
     * 退出虚拟机
     */
    public void exit() {
        exit(9);
    }

    /**
     * 发送状态退出虚拟机
     *
     * @param status 状态
     */
    public void exit(int status) {
        System.exit(status);
    }

    /**
     * 报告消息
     *
     * @param message 消息
     */
    public void report(String message) {
        System.err.println("TraceRecorder: " + message);
    }

    /**
     * 报告异常消息
     *
     * @param message 消息
     * @param t 异常
     */
    public void report(String message, Throwable t) {
        System.err.println(message);
        System.err.println("reported exception:");
        t.printStackTrace();
    }

    /**
     * 获取系统类型
     *
     * @return 系统类型
     */
    public SystemType systemType() {
        return systemType;
    }

    /**
     * 系统类型
     *
     * <p>
     * 目前只支持Linux、Mac OS、Windows，OTHER为未匹配的其它操作系统类型
     * </p>
     */
    public enum SystemType {
        Linux("Linux"),
        MacOS("Mac OS"),
        WINDOWS("Windows"),
        OTHER(Strings.empty());

        /**
         * 系统名
         */
        private final String osName;

        /**
         * 基础构造
         *
         * @param osName 系统名
         */
        SystemType(String osName) {
            this.osName = osName;
        }

        /**
         * 获取对应系统类型
         *
         * @param osName 系统名
         * @return 系统类型
         * @throws NullPointerException if {@code osName} is null.
         */
        public static SystemType systemType(String osName) {
            Objects.requireNonNull(osName);

            for (SystemType systemType : values()) {
                if (osName.startsWith(systemType.osName)) {
                    return systemType;
                }
            }
            return OTHER;
        }
    }
}
