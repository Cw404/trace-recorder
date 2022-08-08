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
package cn.xusc.trace.enhance;

import cn.xusc.trace.EnhanceInfo;

/**
 * 信息增强器
 *
 * <p>
 * 每个信息增强器都应该设置写出消息{@link EnhanceInfo#setWriteInfo(String)}，不清楚何时停止写出措施
 * </p>
 *
 * @author WangCai
 * @since 1.0
 */
public interface InfoEnhancer {
    /**
     * 信息增强
     *
     * @param eInfo 增强前信息
     * @return 增强后信息
     */
    EnhanceInfo enhance(EnhanceInfo eInfo);

    /**
     * 设置写出消息
     *
     * @param eInfo 增强信息
     * @return 增强信息
     */
    EnhanceInfo setWriteInfo(EnhanceInfo eInfo);
}
