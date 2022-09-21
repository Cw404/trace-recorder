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
package cn.xusc.trace.util.reflect.model;

/**
 * 反射模型
 *
 * @author wangcai
 */
@ValueModel(value = "reflect model")
public class ReflectModel {

    @ValueModel("default number")
    private int z = 1;

    @ValueModel("default constructor")
    public ReflectModel() {
        // nop
    }

    @ValueModel("calculate two number add")
    public int calculateAdd(@ValueModel("first number") int x, @ValueModel("second number") int y) {
        return Math.addExact(x, y);
    }

    @ValueModel("default number")
    public int defaultNumber() {
        return z;
    }
}
