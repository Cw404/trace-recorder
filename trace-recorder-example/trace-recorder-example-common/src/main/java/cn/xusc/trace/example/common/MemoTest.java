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
package cn.xusc.trace.example.common;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import cn.xusc.trace.common.util.Memo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * 备忘录测试
 *
 * @author wangcai
 */
public final class MemoTest {

    /**
     * 备忘录
     */
    private Memo memo;

    /**
     * 初始化环境
     */
    @BeforeEach
    public void initEnvironment() {
        memo = new Memo<>();
    }

    /**
     * 存储值
     *
     * @param value 值
     */
    @ParameterizedTest
    @ValueSource(strings = { "1", "2", "3" })
    @DisplayName("Storage value")
    public void storageValue(String value) {
        assertNotNull(memo.storage(value));
    }

    /**
     * 根据存储标签（回忆点）读取值
     *
     * @param value 存储值
     */
    @ParameterizedTest
    @ValueSource(strings = { "1", "2", "3" })
    @DisplayName("Read value, by storage label")
    public void readValue(String value) {
        String label = memo.storage(value);
        assertNotNull(memo.read(label));
    }
}
