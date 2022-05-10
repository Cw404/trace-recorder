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

package cn.xusc.trace.util;

import cn.xusc.trace.enhance.InfoEnhancer;
import cn.xusc.trace.filter.InfoFilter;
import cn.xusc.trace.record.InfoRecorder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

/**
 * 记录器工具类测试
 *
 * @author wangcai
 */
public class RecordersTest {
    
    @Test
    @DisplayName("Base record, more see TraceRecorderTest")
    public void baseRecordTest() {
        Recorders.log("base record");
        Recorders.log("hide record", false);
        assertTrue(Recorders.addInfoFilter(mock(InfoFilter.class)));
        assertTrue(Recorders.addInfoEnhancer(mock(InfoEnhancer.class)));
        assertTrue(Recorders.addInfoRecorder(mock(InfoRecorder.class)));
        assertTrue(Recorders.recordALL());
        assertTrue(Recorders.hideALL());
        assertTrue(Recorders.enableShortClassName());
        assertTrue(Recorders.enableStackInfo());
        assertTrue(Recorders.disableStackInfo());
    }
    
    @Test
    @DisplayName("add fileInfoRecorder, will write all not hide info to specified file")
    public void addFileInfoRecorderTest() throws IOException {
        /*
          for better experience
         */
        File tempFile = File.createTempFile(String.valueOf(System.currentTimeMillis()), ".txt");
        assertNotNull(tempFile, "create temp file failure");
        Recorders.log("create temp file path: " + tempFile.getAbsolutePath());
        tempFile.deleteOnExit();
        Recorders.log("register JVM exit hook, will delete file" + tempFile.getAbsolutePath());
        
        
        assertTrue(Recorders.addFileInfoRecorder(tempFile));
        /*
          or use assertTrue(Recorders.addFileInfoRecorder(fileName));
         */
        Recorders.log("base record");
        Recorders.log("hide record", false);
        
        
        /*
          at 5s after for trigger JVM hook
         */
        LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(10));
    }
    
    @Test
    @DisplayName("add CommonStatisticsInfoEnhancer, you can open temp file to show")
    public void addCommonStatisticsInfoEnhancerTest() throws IOException {
        /*
          for better experience
         */
        File tempFile = File.createTempFile(String.valueOf(System.currentTimeMillis()), ".txt");
        assertNotNull(tempFile, "create temp file failure");
        Recorders.log("create temp file path: " + tempFile.getAbsolutePath());
        /*
          you should comment it, if you want to watch.
         */
        tempFile.deleteOnExit();
        Recorders.log("register JVM exit hook, will delete file" + tempFile.getAbsolutePath());
        assertTrue(Recorders.addFileInfoRecorder(tempFile));
        
        
        /*
          test mechanism question, console not show statistics info, but you can open temp file to show
         */
        assertTrue(Recorders.addCommonStatisticsInfoEnhancer());
        Recorders.log("base record");
        Recorders.log("hide record", false);
        
    }
    
}
