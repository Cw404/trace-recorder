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

import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * 画家测试
 *
 * @author wangcai
 */
public class PainterTest {

    /**
     * 画家
     */
    private Painter painter;

    @BeforeEach
    @DisplayName("init Environment")
    public void initEnvironment() {
        painter = new Painter();
    }

    @Test
    @DisplayName("draw frame")
    public void drawFrameTest() {
        painter.addContent("this is first content");
        painter.addContent("this is second content");
        painter.addContent("this is third content");
        assertNotEquals(Strings.empty(), painter.drawFrame());
    }

    @Test
    @DisplayName("draw table")
    public void drawTableTest() {
        painter.addContent("id|name|age");
        painter.addContent("1|xusc|22");
        painter.addContent("2|tom|16");
        painter.addContent("3|Cw404|23");
        assertNotEquals(Strings.empty(), painter.drawTable());
    }
}
