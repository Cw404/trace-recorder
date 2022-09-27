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
package cn.xusc.trace.example.common.reflect.model;

/**
 * 反射模型
 *
 * @author wangcai
 */
@ValueModel(value = "reflect model")
public class ReflectModel {

    /**
     * 默认值
     */
    @ValueModel("default number")
    private int z = 1;

    /**
     * 默认构造
     */
    @ValueModel("default constructor")
    public ReflectModel() {
        // nop
    }

    /**
     * 计算两值相加
     *
     * @param x 第一个值
     * @param y 第二个值
     * @return 相加合计值
     */
    @ValueModel("calculate two number add")
    public int calculateAdd(@ValueModel("first number") int x, @ValueModel("second number") int y) {
        return Math.addExact(x, y);
    }

    /**
     * 获取默认值
     *
     * @return 默认值
     */
    @ValueModel("default number")
    public int defaultNumber() {
        return z;
    }
}
