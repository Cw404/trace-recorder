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
package cn.xusc.trace.example.chart.echarts.bar;

import cn.xusc.trace.core.TraceRecorder;
import cn.xusc.trace.core.config.TraceRecorderConfig;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Echarts柱状图图表测试
 *
 * @author wangcai
 */
public class EchartsBarChartTest {

    /**
     * Echarts柱状图图表欢迎记录
     *
     * @param recorder 跟踪记录仪
     */
    @ParameterizedTest
    @MethodSource("generateTraceRecorder")
    @DisplayName("welcome record")
    public void welcomeTest(TraceRecorder recorder) {
        recorder.log("hello EchartsBarChart");
    }

    /**
     * 生成跟踪记录仪
     */
    @DisplayName("generate TraceRecorder")
    private static Stream<TraceRecorder> generateTraceRecorder() {
        return Stream.of(new TraceRecorder(TraceRecorderConfig.builder().enableStack(true).build()));
    }
}
