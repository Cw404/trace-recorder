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
 * 禁用重写服务资源注释
 *
 * <p>
 * 被注释的方法，并且{@link  #value()} = true，会禁用该服务资源重写
 * </p>
 * <strong>ps: 需要搭配{@link ServerResource}一起使用，不能和{@link OverrideServerResource}一起使用</strong>
 *
 * @author WangCai
 * @since 2.5.3
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
@Documented
public @interface DisableOverrideServerResource {
    /**
     * 是否禁用重写操作值
     *
     * @return 禁用重写操作详情
     */
    boolean value() default true;
}
