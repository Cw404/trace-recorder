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
import java.io.IOException;
import java.util.Objects;
import lombok.experimental.UtilityClass;

/**
 * 杀手工具类
 *
 * <p>
 * 通过lombok组件{@link UtilityClass}确保工具类的使用
 * </p>
 *
 * @author WangCai
 * @since 2.5
 */
@UtilityClass
public class Killers {

    /**
     * 干掉相应pid进程
     *
     * <p>
     * 不确保进程会对此进行响应
     * </p>
     *
     * @param pid 进程id
     * @return 详情
     * @throws TraceException If an I/O error occurs or the current thread is interrupted by another thread while it is waiting.
     */
    public boolean kill(int pid) {
        try {
            java.lang.Process process = new ProcessBuilder()
                .command(Arrays.merge(WhereIss.findKillCommand(), String.valueOf(pid)))
                .start();
            process.waitFor();
        } catch (IOException e) {
            throw new TraceException(e);
        } catch (InterruptedException e) {
            throw new TraceException(e);
        }
        return true;
    }

    /**
     * 直接干掉相应pid进程
     *
     * <p>
     * 不会给进程机会，直接干掉
     * </p>
     *
     * @param pid 进程id
     * @return 详情
     * @throws TraceException If an I/O error occurs or the current thread is interrupted by another thread while it is waiting.
     */
    public boolean kill9(int pid) {
        return kill(9, pid);
    }

    /**
     * 根据指定信号量干掉相应pid进程
     *
     * @param signal 信号量
     * @param pid 进程id
     * @return 详情
     * @throws TraceException If an I/O error occurs or the current thread is interrupted by another thread while it is waiting.
     */
    public boolean kill(int signal, int pid) {
        try {
            java.lang.Process process = new ProcessBuilder()
                .command(Arrays.merge(WhereIss.findKillCommand(signal), String.valueOf(pid)))
                .start();
            process.waitFor();
        } catch (IOException e) {
            throw new TraceException(e);
        } catch (InterruptedException e) {
            throw new TraceException(e);
        }
        return true;
    }

    /**
     * 干掉相应进程名的进程
     *
     * <p>
     * 不确保进程会对此进行响应
     * </p>
     *
     * @param processName 进程名
     * @return 详情
     * @throws TraceException If an I/O error occurs or the current thread is interrupted by another thread while it is waiting.
     */
    public boolean kill(String processName) {
        Objects.requireNonNull(processName);

        if (Objects.equals(Systems.SystemType.WINDOWS, Systems.systemType())) {
            if (!processName.endsWith(".exe")) {
                processName = processName.concat(".exe");
            }
        }
        try {
            java.lang.Process process = new ProcessBuilder()
                .command(Arrays.merge(WhereIss.findPkillCommand(), processName))
                .start();
            process.waitFor();
        } catch (IOException e) {
            throw new TraceException(e);
        } catch (InterruptedException e) {
            throw new TraceException(e);
        }
        return true;
    }

    /**
     * 直接干掉相应进程名的进程
     *
     * <p>
     * 不会给进程机会，直接干掉
     * </p>
     *
     * @param processName 进程名
     * @return 详情
     * @throws TraceException If an I/O error occurs or the current thread is interrupted by another thread while it is waiting.
     */
    public boolean kill9(String processName) {
        return kill(9, processName);
    }

    /**
     * 根据指定信号量干掉相应进程名的进程
     *
     * @param signal 信号量
     * @param processName 进程名
     * @return 详情
     * @throws TraceException If an I/O error occurs or the current thread is interrupted by another thread while it is waiting.
     */
    public boolean kill(int signal, String processName) {
        Objects.requireNonNull(processName);

        if (Objects.equals(Systems.SystemType.WINDOWS, Systems.systemType())) {
            if (!processName.endsWith(".exe")) {
                processName = processName.concat(".exe");
            }
        }
        try {
            java.lang.Process process = new ProcessBuilder()
                .command(Arrays.merge(WhereIss.findPkillCommand(signal), processName))
                .start();
            process.waitFor();
        } catch (IOException e) {
            throw new TraceException(e);
        } catch (InterruptedException e) {
            throw new TraceException(e);
        }
        return true;
    }
}
