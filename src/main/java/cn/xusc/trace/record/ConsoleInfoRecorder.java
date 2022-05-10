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

package cn.xusc.trace.record;

import java.io.PrintStream;

/**
 * 控制台信息记录器
 *
 * @author WangCai
 * @since 1.0
 */
public class ConsoleInfoRecorder implements InfoRecorder {
    
    /**
     * 系统默认输出流
     */
    private static final PrintStream print = System.out;
    
    @Override
    public void record(String writeInfo) {
        print.print(writeInfo);
    }
    
}
