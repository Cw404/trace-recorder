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
package cn.xusc.trace.chart;

import lombok.Setter;

/**
 * 抽象图表数据
 *
 * @author WangCai
 * @since 2.5
 */
@Setter
public abstract class AbstractChartData implements ChartData {

    /**
     * 线程名
     */
    protected String threadName;

    /**
     * 类名
     */
    protected String className;

    /**
     * 方法名
     */
    protected String methodName;

    /**
     * 行号
     */
    protected int lineNumber;

    /**
     * 信息
     */
    protected String info;

    /**
     * 堆栈节点
     */
    protected StackTraceElement[] stackTraceElements;

    /**
     * 下一个图表数据
     */
    protected ChartData nextChartData;

    @Override
    public String threadName() {
        return threadName;
    }

    @Override
    public String className() {
        return className;
    }

    @Override
    public String methodName() {
        return methodName;
    }

    @Override
    public int lineNumber() {
        return lineNumber;
    }

    @Override
    public String info() {
        return info;
    }

    @Override
    public StackTraceElement[] stackTraceElements() {
        return stackTraceElements;
    }

    @Override
    public ChartData nextChartData() {
        return nextChartData;
    }

    @Override
    public String basicChartData() {
        return (
            "{" +
            "threadName='" +
            threadName +
            '\'' +
            ", className='" +
            className +
            '\'' +
            ", methodName='" +
            methodName +
            '\'' +
            ", lineNumber=" +
            lineNumber +
            ", info='" +
            info +
            '\'' +
            '}'
        );
    }
}
