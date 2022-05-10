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

package cn.xusc.trace.filter;

import cn.xusc.trace.constant.RecordLabel;

/**
 * 记录标签信息过滤器
 *
 * @author WangCai
 * @since 1.0
 */
public class RecordLabelInfoFilter implements InfoFilter {
    
    @SuppressWarnings("unused")
    @Override
    public boolean isRecord(String info, RecordLabel label) {
        if (label == RecordLabel.ALL) {
            return true;
        } else return label != RecordLabel.HIDE;
    }
}
