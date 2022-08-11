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

import java.util.UUID;
import lombok.experimental.UtilityClass;

/**
 * 随机工具类
 *
 * <p>
 * 通过lombok组件{@link UtilityClass}确保工具类的使用
 * </p>
 *
 * @author WangCai
 * @since 2.2
 */
@UtilityClass
public class Randoms {

    /**
     * 获取一个唯一标识符字符串
     *
     * @return 唯一标识符字符串
     */
    public String uuid() {
        return UUID.randomUUID().toString();
    }

    /**
     * 获取一个破折号转化的唯一标识符字符串
     *
     * @param dashToNewStr 破折号转化的新字符串
     * @return 唯一标识符字符串
     */
    public String uuid(String dashToNewStr) {
        return uuid().replace("-", dashToNewStr);
    }
}
