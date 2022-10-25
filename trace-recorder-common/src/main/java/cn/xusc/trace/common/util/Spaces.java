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
package cn.xusc.trace.common.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.UtilityClass;

/**
 * 空间工具类
 *
 * <p>
 * 通过lombok组件{@link UtilityClass}确保工具类的使用
 * </p>
 *
 * @author WangCai
 * @since 2.5
 */
@UtilityClass
public class Spaces {

    /**
     * 创建坐标轴
     *
     * @param x 横轴值
     * @return 坐标轴
     */
    public CoordinateAxis create(double x) {
        return create(x, 0.0d, 0.0d);
    }

    /**
     * 创建坐标轴
     *
     * @param x 横轴值
     * @param y 纵轴值
     * @return 坐标轴
     */
    public CoordinateAxis create(double x, double y) {
        return create(x, y, 0.0d);
    }

    /**
     * 创建坐标轴
     *
     * @param x 横轴值
     * @param y 纵轴值
     * @param z 竖轴值
     * @return 坐标轴
     */
    public CoordinateAxis create(double x, double y, double z) {
        return new CoordinateAxis(x, y, z);
    }

    /**
     * 坐标轴
     */
    @Setter
    @Getter
    @ToString
    @AllArgsConstructor
    public class CoordinateAxis {

        /**
         * 横轴值
         */
        double x;
        /**
         * 纵轴值
         */
        double y;
        /**
         * 竖轴值
         */
        double z;
    }
}
