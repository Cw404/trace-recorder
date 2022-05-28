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

package cn.xusc.trace.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * 备忘录测试
 *
 * @author wangcai
 */
public class MemoTest {
    
    /**
     * 备忘录
     */
    private Memo memo;
    
    @BeforeEach
    public void initEnvironment() {
        memo = new Memo<>();
    }
    
    @ParameterizedTest
    @ValueSource(strings = {"1", "2", "3"})
    @DisplayName("Storage value")
    public void storageValue(String value) {
        assertNotNull(memo.storage(value));
    }
    
    @ParameterizedTest
    @ValueSource(strings = {"1", "2", "3"})
    @DisplayName("Read value, by storage label")
    public void readValue(String value) {
        String label = memo.storage(value);
        assertNotNull(memo.read(label));
    }
}
