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
package cn.xusc.trace.server.annotation;

import java.lang.annotation.*;

/**
 * 输出流的服务资源注释
 *
 * <p>
 * 被注释的方法，并且{@link #value()} = true，资源就标记为输出流的服务资源，最终会以输出流进行返回
 * </p>
 * <strong>ps: 需要搭配{@link ServerResource}一起使用</strong>
 *
 * @author WangCai
 * @since 2.5
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
@Documented
public @interface OutputStreamServerResource {
    /**
     * 是否输出流的资源操作值
     *
     * @return 输出流的资源详情
     */
    boolean value() default true;
}
