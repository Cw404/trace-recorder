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

package cn.xusc.trace.recorder;

import cn.xusc.trace.TraceRecorder;
import cn.xusc.trace.enhance.InfoEnhancer;
import cn.xusc.trace.filter.InfoFilter;
import cn.xusc.trace.record.InfoRecorder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

/**
 * 跟踪记录仪测试
 *
 * @author wangcai
 */
public class TraceRecorderTest {
    
    /**
     * 跟踪记录仪
     */
    private static TraceRecorder recorder;
    
    @BeforeAll
    @DisplayName("Init Environment")
    public static void initEnv() {
        recorder = new TraceRecorder();
    }
    
    @Test
    @DisplayName("Base record")
    public void baseRecordTest() {
        recorder.log("base record");
    }
    
    @Test
    @DisplayName("hide record")
    public void hideRecordTest() {
        recorder.log("hide record", false);
    }
    
    @Test
    @DisplayName("add a infoFilter")
    public void addInfoFilterTest() {
        assertTrue(recorder.addInfoFilter(mock(InfoFilter.class)));
    }
    
    @Test
    @DisplayName("add a infoEnhancer")
    public void addInfoEnhancerTest() {
        assertTrue(recorder.addInfoEnhancer(mock(InfoEnhancer.class)));
    }
    
    @Test
    @DisplayName("add a infoRecorder")
    public void addInfoRecorderTest() {
        assertTrue(recorder.addInfoRecorder(mock(InfoRecorder.class)));
    }
    
    @Test
    @DisplayName("enable record all config, will show all info of record")
    public void recordALLConfigTest() {
        assertTrue(recorder.recordALL());
        recorder.log("show one");
        recorder.log("hide two", false);
    }
    
    @Test
    @DisplayName("enable hide all config, will hide all info of record")
    public void hideALLConfigTest() {
        assertTrue(recorder.hideALL());
        recorder.log("show one");
        recorder.log("hide two", false);
    }
    
    @Test
    @DisplayName("enable short class name config, just show class name of stack info, not package + class name")
    public void enableShortClassNameConfigTest() {
        recorder.log("show package + class name");
        assertTrue(recorder.enableShortClassName());
        recorder.log("only class name of show");
    }
    
    @Test
    @DisplayName("enable stack info config, will capture info of record point. default enable")
    public void enableStackInfoConfigTest() {
        assertTrue(recorder.enableStackInfo());
        recorder.log("enable stack info, default enable");
    }
    
    @Test
    @DisplayName("disable stack info config, not capture info of record point")
    public void disableStackInfoConfigTest() {
        assertTrue(recorder.disableStackInfo());
        recorder.log("disable capture stack info record point");
    }
    
}
