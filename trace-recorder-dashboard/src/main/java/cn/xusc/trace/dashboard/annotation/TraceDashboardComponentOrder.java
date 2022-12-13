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
package cn.xusc.trace.dashboard.annotation;

import java.lang.annotation.*;

/**
 * 跟踪仪表盘组件顺序注释
 *
 * <p>
 * 声明: 标注此注释的系列跟踪仪表盘组件，将按{@link #value()}值从小到大顺序进行处理，不标注此注释将采用{@link Integer#MAX_VALUE}
 * </p>
 * <strong>ps: 赋予跟踪仪表盘组件显示提权的特性，同一排序值情况下不保证显示的先后顺序，非必要勿占用最先顺序</strong>
 *
 * @author WangCai
 * @since 2.5.3
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface TraceDashboardComponentOrder {
    /**
     * 排序值
     *
     * @return 排序的值大小
     */
    int value() default Integer.MAX_VALUE;
}
