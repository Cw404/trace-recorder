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
package cn.xusc.trace.example.common;

import cn.xusc.trace.common.exception.TraceException;
import cn.xusc.trace.common.util.Formats;
import cn.xusc.trace.common.util.Killers;
import cn.xusc.trace.common.util.Painter;
import cn.xusc.trace.common.util.Process;
import cn.xusc.trace.common.util.Systems;
import java.io.PrintStream;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

/**
 * Killers工具类测试
 *
 * <p>
 * 采用main而非junit说明:
 * <b>当前操作应为交互式，而非自动化</b>
 * </p>
 *
 *
 * @author wangcai
 */
public class KillersTest {

    /**
     * 系统输入扫描仪
     */
    private static final Scanner scanner = new Scanner(System.in);
    /**
     * 系统输出
     */
    private static final PrintStream out = System.out;

    /**
     * 主入口
     *
     * @param args jvm参数
     */
    public static void main(String[] args) {
        FunctionMenu menu = new FunctionMenu();
        menu.show();
        String killMode = scanTrap("请输入kill模式: ");
        menu.applyModel(killMode);
    }

    /**
     * 功能菜单
     */
    private static class FunctionMenu {

        /**
         * 显示
         */
        public void show() {
            out.println("------------- kill掉进程的方式：-------------");
            out.println("1、pid(进程id)");
            out.println("2、processName(进程名)");
        }

        /**
         * 模式应用
         *
         * @param killMode kill模式
         */
        public void applyModel(String killMode) {
            switch (Integer.valueOf(killMode)) {
                case 1:
                    showEnableJps();
                    String enableJpsMode = scanTrap("是否启用jps模式: ");
                    while (true) {
                        select(1, Objects.equals(enableJpsMode, "yes") ? true : false);
                        String killObject = scanTrap("请输入kill对象: ");
                        exitTrap(killObject);
                        String killSignal = scanTrap("请输入kill信号: ");
                        kill(Integer.valueOf(killSignal), killObject, 1);
                        out.println();
                    }
                case 2:
                    while (true) {
                        out.println();
                        Painter painter = new Painter();
                        painter.addContent("pid|cmd");
                        painter.addContent("q|exit");
                        out.println(painter.drawTable());
                        String killObject = scanTrap("请输入kill对象: ");
                        exitTrap(killObject);
                        String killSignal = scanTrap("请输入kill信号: ");
                        kill(Integer.valueOf(killSignal), killObject, 2);
                    }
                default:
                    throw new TraceException("not support operation menu number");
            }
        }

        /**
         * 显示启动Jps
         */
        private void showEnableJps() {
            out.print("是否启用Jps(yes/no)");
        }

        /**
         * 选择
         *
         * @param menuNumber 菜单编号
         * @param enableJps 是否启用jsp
         */
        private void select(int menuNumber, boolean enableJps) {
            if (enableJps) {
                selectJps(menuNumber);
                return;
            }
            selectPs(menuNumber);
        }

        /**
         * 选择ps
         *
         * @param menuNumber 菜单编号
         */
        private void selectPs(int menuNumber) {
            switch (menuNumber) {
                case 1:
                    List<Process.Info> infos = Process.ps();
                    Painter painter = new Painter();
                    painter.addContent("pid|cmd");
                    infos.forEach(info -> painter.addContent(Formats.format("{}|{}", info.getPID(), info.getCMD())));
                    painter.addContent("q|exit");
                    out.println(painter.drawTable());
                    break;
                case 2:
                    break;
                default:
                    throw new TraceException("not support operation menu number");
            }
        }

        /**
         * 选择jps
         *
         * @param menuNumber 菜单编号
         */
        private void selectJps(int menuNumber) {
            switch (menuNumber) {
                case 1:
                    List<Process.Info> infos = Process.jps();
                    Painter painter = new Painter();
                    painter.addContent("pid|cmd");
                    infos.forEach(info -> painter.addContent(Formats.format("{}|{}", info.getPID(), info.getCMD())));
                    painter.addContent("q|exit");
                    out.println(painter.drawTable());
                    break;
                case 2:
                    break;
                default:
                    throw new TraceException("not support operation menu number");
            }
        }

        /**
         * kill
         *
         * @param signal 信号
         * @param killObject kill对象
         * @param menuNumber 菜单编号
         */
        private void kill(int signal, String killObject, int menuNumber) {
            switch (menuNumber) {
                case 1:
                    out.println(
                        Formats.format(
                            "kill情况: {}",
                            Killers.kill(signal, Integer.valueOf(killObject)) ? "success" : "failure"
                        )
                    );
                    break;
                case 2:
                    out.println(
                        Formats.format("kill情况: {}", Killers.kill(signal, killObject) ? "success" : "failure")
                    );
                    break;
                default:
                    throw new TraceException("not support operation menu number");
            }
        }
    }

    /**
     * 扫描陷阱
     *
     * @param promptInfo 提示信息
     * @return 扫描字符串
     */
    private static String scanTrap(String promptInfo) {
        out.print(promptInfo);
        return scanner.next();
    }

    /**
     * 退出陷阱
     *
     * @param option 选项
     */
    private static void exitTrap(String option) {
        if (Objects.equals(option, "q")) {
            Systems.exit(0);
        }
    }
}
