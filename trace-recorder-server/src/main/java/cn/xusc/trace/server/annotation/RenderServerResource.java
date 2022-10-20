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
 * 渲染服务资源注释
 *
 * <p>
 * 根据键进行值渲染
 * </p>
 * 值模式:
 * <ul>
 *     <li>{@link ValueModel#TEXT} 文本模式，不会对值进行操作</li>
 *     <li>{@link ValueModel#OGNL} OGNL模式，会对OGNL值表达式进行解析操作</li>
 * </ul>
 * <strong>ps: 键切勿重复，否则会引发不可控的渲染情况</strong>
 *
 * @author WangCai
 * @since 2.5
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
@Documented
@Repeatable(RenderServerResources.class)
public @interface RenderServerResource {
    /**
     * 键
     *
     * @return 键值
     */
    String key();

    /**
     * 值
     *
     * @return 值
     */
    String value();

    /**
     * 值模式
     *
     * <p>
     * 可通过{@code ValueModel.OGNL}采用OGNL表达式来构建动态值。
     * </p>
     * <table border="1">
     *     <caption>Details</caption>
     *     <tr><th>OGNL支持的类</th></tr>
     *     <tr><td>{@link jakarta.servlet.http.HttpServletRequest}</td></tr>
     * </table>
     *
     * @return 值模式
     */
    ValueModel model() default ValueModel.TEXT;

    /**
     * 值模式
     */
    enum ValueModel {
        /**
         * 文本模式
         */
        TEXT,
        /**
         * OGNL模式
         */
        OGNL,
    }
}
