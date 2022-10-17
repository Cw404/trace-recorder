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

import cn.xusc.trace.common.exception.TraceException;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Member;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import lombok.experimental.UtilityClass;
import ognl.MemberAccess;
import ognl.Ognl;
import ognl.OgnlException;

/**
 * OGNL工具类
 *
 * <p>
 * 通过lombok组件{@link UtilityClass}确保工具类的使用
 * </p>
 *
 * @author WangCai
 * @since 2.5
 */
@UtilityClass
public class Ognls {

    /**
     * 表达式缓存
     */
    private Map<String, Object> expressionCache = new ConcurrentHashMap<>();

    /**
     * 获取操作对象的表达式值
     *
     * @param expression 表达式
     * @param root 操作对象
     * @return 操作对象的表达式值
     * @throws TraceException if there is a pathological environmental problem.
     */
    public Object getValue(String expression, Object root) {
        try {
            Map context = Ognl.createDefaultContext(root, OgnlMemberAccess.INSTANCE);
            return Ognl.getValue(parseExpression(expression), context, root);
        } catch (OgnlException e) {
            throw new TraceException(Formats.format("Error evaluating expression '{}'", expression), e);
        }
    }

    /**
     * 解析表达式
     *
     * @param expression 表达式
     * @return 解析的表达式
     * @throws OgnlException if there is a pathological environmental problem.
     */
    private Object parseExpression(String expression) throws OgnlException {
        Object node = expressionCache.get(expression);
        if (Objects.isNull(node)) {
            node = Ognl.parseExpression(expression);
            expressionCache.put(expression, node);
        }
        return node;
    }

    /**
     * OGNL成员访问
     */
    private enum OgnlMemberAccess implements MemberAccess {
        /**
         * 实例
         */
        INSTANCE;

        @Override
        public Object setup(Map context, Object target, Member member, String propertyName) {
            Object result = null;
            if (isAccessible(context, target, member, propertyName)) {
                AccessibleObject accessible = (AccessibleObject) member;
                if (!accessible.canAccess(target)) {
                    result = Boolean.FALSE;
                    accessible.setAccessible(true);
                }
            }
            return result;
        }

        @Override
        public void restore(Map context, Object target, Member member, String propertyName, Object state) {
            // Flipping accessible flag is not thread safe.
        }

        @Override
        public boolean isAccessible(Map context, Object target, Member member, String propertyName) {
            return true;
        }
    }
}
