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

import cn.xusc.trace.TraceRecorder;
import cn.xusc.trace.constant.RecordLabel;
import cn.xusc.trace.exception.TraceException;

/**
 * 同步处理器
 *
 * @author WangCai
 * @since 2.0
 */
public class SyncTraceHandler extends BaseTraceHandler {

    /**
     * 基本构造
     *
     * @param recorder 跟踪记录仪
     */
    public SyncTraceHandler(TraceRecorder recorder) {
        super(recorder);
    }

    @Override
    public void doHandle(String info, RecordLabel label, Object... argArray) {
        handling(info, label, new TraceException(), argArray);
    }
}
