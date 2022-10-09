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
package cn.xusc.trace.example.core.util.need.filter;

import cn.xusc.trace.core.constant.RecordLabel;
import cn.xusc.trace.core.filter.InfoFilter;

/**
 * 短信息过滤器
 *
 * @author wangcai
 */
public class ShortInfoFilterTest implements InfoFilter {

    /**
     * 范围
     * <table border="1">
     * <caption>信息长度描述</caption>
     * <tr><th>范围长度</th><th>范围大小</th></tr>
     * <tr><td>(0,4]</td><td>小（短）</td></tr>
     * <tr><td>(4,10]</td><td>中</td></tr>
     * <tr><td>(10,20]</td><td>大</td></tr>
     */
    private static final int[] SCOPE = { 4, 10, 20 };

    @Override
    public boolean isRecord(String info, RecordLabel label) {
        int infoLength = info.length();
        if (Boolean.logicalAnd(0 < infoLength, infoLength <= SCOPE[0])) {
            return true;
        }
        return false;
    }
}
