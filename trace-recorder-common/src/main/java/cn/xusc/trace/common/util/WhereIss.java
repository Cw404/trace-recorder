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

import cn.xusc.trace.common.exception.TraceException;
import java.io.File;
import java.util.function.Supplier;
import lombok.experimental.UtilityClass;

/**
 * 指令定位工具类
 *
 * <p>
 * 通过lombok组件{@link UtilityClass}确保工具类的使用
 * </p>
 *
 * @author WangCai
 * @since 2.5
 */
@UtilityClass
public class WhereIss {

    /**
     * kill9信号量
     */
    private final int NINE_KILL_SIGNAL = 9;

    /**
     * 查找ps指令
     *
     * <p>
     * 报告当前系统的进程状态命令
     * </p>
     *
     * @return ps特定相关指令
     */
    public String[] findPsCommand() {
        return commands(() -> new String[] { "ps", "-ef" }, () -> new String[] { "TASKLIST.exe", "/FO", "CSV" });
    }

    /**
     * 查找grep指令
     *
     * <p>
     * 文本搜索命令
     * </p>
     *
     * @return grep特定相关指令
     */
    public String findGrepCommand() {
        return commands(() -> new String[] { "grep" }, () -> new String[] { "FINDSTR.exe" })[0];
    }

    /**
     * 查找kill指令
     *
     * <p>
     * 发送信号到进程指令
     * </p>
     *
     * @return kill特定相关指令
     */
    public String[] findKillCommand() {
        return commands(() -> new String[] { "kill" }, () -> new String[] { "TASKKILL.exe", "/PID" });
    }

    /**
     * 查找kill信号指令
     *
     * <p>
     * 发送信号到进程指令
     * </p>
     *
     * @param signal 信号量
     * @return kill信号特定相关指令
     */
    public String[] findKillCommand(int signal) {
        if (signal == NINE_KILL_SIGNAL) {
            return findKill9Command();
        }
        return commands(
            () -> new String[] { "kill", "-".concat(String.valueOf(signal)) },
            () -> new String[] { "TASKKILL.exe", "/PID" }
        );
    }

    /**
     * 查找kill9信号指令
     *
     * <p>
     * 发送信号到进程指令
     * </p>
     *
     * @return kill特定相关指令
     */
    public String[] findKill9Command() {
        return commands(() -> new String[] { "kill", "-9" }, () -> new String[] { "TASKKILL.exe", "/F", "/PID" });
    }

    /**
     * 查找pkill指令
     *
     * <p>
     * 发送信号到进程指令
     * </p>
     *
     * @return pkill特定相关指令
     */
    public String[] findPkillCommand() {
        return commands(() -> new String[] { "pkill" }, () -> new String[] { "TASKKILL.exe", "/IM" });
    }

    /**
     * 查找pkill信号指令
     *
     * <p>
     * 发送信号到进程指令
     * </p>
     *
     * @param signal 信号量
     * @return pkill特定相关指令
     */
    public String[] findPkillCommand(int signal) {
        if (signal == NINE_KILL_SIGNAL) {
            return findPkill9Command();
        }
        return commands(
            () -> new String[] { "pkill", "-".concat(String.valueOf(signal)) },
            () -> new String[] { "TASKKILL.exe", "/IM" }
        );
    }

    /**
     * 查找pkill9信号指令
     *
     * <p>
     * 发送信号到进程指令
     * </p>
     *
     * @return pkill特定相关指令
     */
    public String[] findPkill9Command() {
        return commands(() -> new String[] { "pkill", "-9" }, () -> new String[] { "TASKKILL.exe", "/F", "/IM" });
    }

    /**
     * 查找jps指令
     *
     * @return java平台特定相关指令
     */
    public String[] findJpsCommand() {
        return commands(() -> new String[] { "jps", "-l" }, () -> new String[] { "jps.exe", "-l" });
    }

    /**
     * 查找绝对路径下的jps指令
     *
     * @return java平台特定相关指令
     */
    public String[] findJpsAbsoluteCommand() {
        String javaHome = Systems.getJavaHome();
        return commands(
            () -> new String[] { new File(javaHome, "bin/jps").getAbsolutePath(), "-l" },
            () -> new String[] { new File(javaHome, "bin/jps.exe").getAbsolutePath(), "-l" }
        );
    }

    /**
     * 获取平台相关指令集
     *
     * @param unixLikeSupplier 类unix指令提供者
     * @param windowsSupplier windows指令提供者
     * @return 平台相关指令集
     */
    private String[] commands(Supplier<String[]> unixLikeSupplier, Supplier<String[]> windowsSupplier) {
        String[] commands = {};
        switch (Systems.systemType()) {
            case Linux:
            case MacOS:
                commands = unixLikeSupplier.get();
                break;
            case WINDOWS:
                commands = windowsSupplier.get();
                break;
            case OTHER:
                throw new TraceException("not support operation");
        }
        return commands;
    }
}
