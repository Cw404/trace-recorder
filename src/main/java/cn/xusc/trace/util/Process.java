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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.UtilityClass;

/**
 * 进程工具类
 *
 * <p>
 * 通过lombok组件{@link UtilityClass}确保工具类的使用
 * </p>
 *
 * @author WangCai
 * @since 2.5
 */
@UtilityClass
public class Process {

    /**
     * 查找所有进程信息
     *
     * @return 进程信息列表
     * @throws TraceException If an I/O error occurs
     */
    public List<Info> ps() {
        try {
            java.lang.Process process = new ProcessBuilder().command(WhereIss.findPsCommand()).start();
            return transform(1, process, false);
        } catch (IOException e) {
            throw new TraceException(e);
        }
    }

    /**
     * 查找指定进程名的进程信息
     *
     * @param processName 进程名
     * @return 指定进程信息列表
     * @throws TraceException If an I/O error occurs
     */
    public List<Info> ps(String processName) {
        Objects.requireNonNull(processName);

        List<java.lang.Process> processes;
        try {
            processes =
                ProcessBuilder.startPipeline(
                    List.of(
                        new ProcessBuilder().command(WhereIss.findPsCommand()),
                        new ProcessBuilder().command(WhereIss.findGrepCommand(), processName)
                    )
                );
            java.lang.Process process = processes.get(1);
            return transform(process, false);
        } catch (IOException e) {
            throw new TraceException(e);
        }
    }

    /**
     * 查找所有java进程信息
     *
     * @return java进程信息列表
     * @throws TraceException If an I/O error occurs
     */
    public List<Info> jps() {
        try {
            java.lang.Process process = new ProcessBuilder().command(WhereIss.findJpsCommand()).start();
            return transform(0, process, true);
        } catch (IOException e) {
            throw new TraceException(e);
        }
    }

    /**
     * 查找指定进程名的java进程信息
     *
     * @param processName 进程名
     * @return java进程信息列表
     * @throws TraceException If an I/O error occurs
     */
    public List<Info> jps(String processName) {
        Objects.requireNonNull(processName);

        List<java.lang.Process> processes;
        try {
            processes =
                ProcessBuilder.startPipeline(
                    List.of(
                        new ProcessBuilder().command(WhereIss.findJpsCommand()),
                        new ProcessBuilder().command(WhereIss.findGrepCommand(), processName)
                    )
                );
            java.lang.Process process = processes.get(1);
            return transform(process, true);
        } catch (IOException e) {
            throw new TraceException(e);
        }
    }

    /**
     * 转换指令结果流为进程信息
     *
     * @param process 系统进程
     * @param isJpsCommand 是否为jps命令
     * @return 进程信息列表
     * @throws IOException If an I/O error occurs
     */
    private List<Info> transform(java.lang.Process process, boolean isJpsCommand) throws IOException {
        return transform(0, process, isJpsCommand);
    }

    /**
     * 转换指令结果流为进程信息
     *
     * @param discardLineCount 丢弃的进程信息行数（格式头信息）
     * @param process 系统进程
     * @param isJpsCommand 是否为jps命令
     * @return 进程信息列表
     * @throws IOException If an I/O error occurs
     */
    private List<Info> transform(int discardLineCount, java.lang.Process process, boolean isJpsCommand)
        throws IOException {
        List<Info> infos = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

        for (int i = 0; i < discardLineCount; i++) {
            reader.readLine();
        }
        StringTokenizer tokenizer;
        String content, uid, pid, ppid, c, stime, tty, time, cmd, sessionName, SZ;
        while (Objects.nonNull(content = reader.readLine())) {
            tokenizer = new StringTokenizer(content);
            /* jps解决了java进程平台差异性 */
            if (isJpsCommand) {
                pid = tokenizer.nextToken();
                cmd = tokenizer.nextToken("");
                if (cmd.isBlank()) {
                    continue;
                }
                infos.add(Info.builder().PID(Integer.valueOf(pid)).CMD(cmd).build());
                continue;
            }
            switch (Systems.systemType()) {
                case Linux:
                case MacOS:
                    uid = tokenizer.nextToken();
                    pid = tokenizer.nextToken();
                    ppid = tokenizer.nextToken();
                    c = tokenizer.nextToken();
                    stime = tokenizer.nextToken();
                    tty = tokenizer.nextToken();
                    time = tokenizer.nextToken();
                    /* 命令是特殊的一串指令，应读取后续所有 */
                    cmd = tokenizer.nextToken("");
                    infos.add(
                        Info
                            .builder()
                            .UID(uid)
                            .PID(Integer.valueOf(pid))
                            .PPID(Integer.valueOf(ppid))
                            .C(Float.valueOf(c))
                            .STIME(stime)
                            .TTY(tty)
                            .TIME(time)
                            .CMD(cmd)
                            .build()
                    );
                    break;
                case WINDOWS:
                    cmd = (cmd = tokenizer.nextToken(",")).substring(1, cmd.length() - 1);
                    pid = (pid = tokenizer.nextToken(",")).substring(1, pid.length() - 1);
                    sessionName = (sessionName = tokenizer.nextToken(",")).substring(1, sessionName.length() - 1);
                    tty = (tty = tokenizer.nextToken(",")).substring(1, tty.length() - 1);
                    SZ = (SZ = tokenizer.nextToken(",")).substring(1, SZ.length() - 1);

                    infos.add(Info.builder().PID(Integer.valueOf(pid)).TTY(tty).CMD(cmd).SZ(SZ).build());
                    break;
                case OTHER:
                    throw new TraceException("not support operation");
            }
        }

        return infos;
    }

    /**
     * 信息
     */
    @Setter
    @Getter
    @Builder
    public static class Info {

        /**
         * 用户id
         */
        String UID;
        /**
         * 进程id
         */
        int PID;
        /**
         * 父级进程id
         */
        int PPID;
        /**
         * CPU使用资源百分比
         */
        float C;
        /**
         * 启动时间
         */
        String STIME;
        /**
         * 终端机位置
         */
        String TTY;
        /**
         * 使用掉的CPU时间
         */
        String TIME;
        /**
         * 使用掉的内存大小
         */
        String SZ;
        /**
         * 执行指令
         */
        String CMD;
    }
}
