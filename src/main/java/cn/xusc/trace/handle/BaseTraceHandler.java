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

package cn.xusc.trace.handle;

import cn.xusc.trace.EnhanceInfo;
import cn.xusc.trace.TraceRecorder;
import cn.xusc.trace.constant.RecordLabel;
import cn.xusc.trace.constant.Temporary;
import cn.xusc.trace.enhance.InfoEnhancer;
import cn.xusc.trace.exception.TraceTimeoutException;
import cn.xusc.trace.filter.InfoFilter;
import cn.xusc.trace.record.InfoRecorder;
import cn.xusc.trace.util.Formats;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 基础处理器
 *
 * @author WangCai
 * @since 2.0
 */
public abstract class BaseTraceHandler implements TraceHandler {
    
    /**
     * 跟踪记录仪
     */
    protected final TraceRecorder RECORDER;
    
    /**
     * 基本构造
     *
     * @param recorder 跟踪记录仪
     * @throws NullPointerException if {@code recorder} is null
     */
    public BaseTraceHandler(TraceRecorder recorder) {
        Objects.requireNonNull(recorder);
        
        this.RECORDER = recorder;
    }
    
    @Override
    public void handle(String info, RecordLabel label, Object... argArray) {
        doHandle(info, label, argArray);
    }
    
    @Override
    public void shutdown() {
        // nop
    }
    
    @Override
    public void shutdown(long timeout, TimeUnit timeUnit) throws TraceTimeoutException {
        // nop
    }
    
    /**
     * 信息处理
     *
     * @param info     信息
     * @param label    记录标签
     * @param argArray 参数列表
     */
    protected abstract void doHandle(String info, RecordLabel label, Object... argArray);
    
    /**
     * 处理信息流转
     *
     * @param info      信息
     * @param label     记录标签
     * @param exception 异常信息,{@link cn.xusc.trace.enhance.StackInfoEnhancer}进行解析
     * @param argArray  参数列表
     */
    protected void handling(String info, RecordLabel label, Exception exception, Object... argArray) {
        boolean isRecord = true;
        /*
          过滤信息
         */
        for (InfoFilter infoFilter : RECORDER.getInfoFilters()) {
            if (!infoFilter.isRecord(info, label)) {
                /*
                  此刻，在某个过滤器中过滤掉了
                 */
                isRecord = false;
                break;
            }
        }
        
        if (isRecord) {
            if (Objects.nonNull(argArray) && argArray.length > 0) {
                /*
                  格式化信息
                 */
                info = Formats.format(info, argArray);
            }
            EnhanceInfo enhanceInfo = new EnhanceInfo(info);
            enhanceInfo.setTemporaryValue(Temporary.ENABLE_STACK, RECORDER.isEnableStackInfo());
            enhanceInfo.setTemporaryValue(Temporary.ENABLE_SHORT_CLASS_NAME, RECORDER.isEnableShortClassName());
            enhanceInfo.setTemporaryValue(Temporary.ENABLE_THREAD_NAME, RECORDER.isEnableThreadName());
            enhanceInfo.setTemporaryValue(Temporary.EXCEPTION, exception);
            /*
              信息增强
             */
            for (InfoEnhancer infoEnhancer : RECORDER.getInfoEnhancers()) {
                enhanceInfo = infoEnhancer.enhance(enhanceInfo);
                if (Objects.isNull(enhanceInfo)) return;
                enhanceInfo = infoEnhancer.setWriteInfo(enhanceInfo);
                if (Objects.isNull(enhanceInfo)) return;
            }
            
            String writeInfo = enhanceInfo.getWriteInfo();
            if (Objects.nonNull(writeInfo)) {
                /*
                  信息记录
                 */
                for (InfoRecorder infoRecorder : RECORDER.getInfoRecorders()) {
                    infoRecorder.record(writeInfo);
                }
            }
            
        }
    }
}
