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
package cn.xusc.trace.util.reflect;

import cn.xusc.trace.exception.TraceException;
import java.util.List;

/**
 * 方法反射接口
 *
 * @author WangCai
 * @since 2.5
 */
public interface MethodReflect<T> {
    /**
     * 获取方法列表
     *
     * @return 方法列表
     */
    default List<Method<java.lang.reflect.Method>> methods() {
        throw new TraceException("not support operation");
    }

    /**
     * 获取参数列表
     *
     * @return 参数列表
     */
    default List<Parameter<T>> parameters() {
        throw new TraceException("not support operation");
    }

    /**
     * 获取返回类型
     *
     * @return 返回类型
     */
    default String returnType() {
        throw new TraceException("not support operation");
    }

    /**
     * 方法调用
     *
     * @param ts 参数
     * @return 调用结果
     */
    default T call(T... ts) {
        throw new TraceException("not support operation");
    }
}
