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
package cn.xusc.trace.core;

import cn.xusc.trace.common.util.Maps;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 增强信息
 *
 * @author WangCai
 * @since 1.0
 */
public class EnhanceInfo implements Serializable, Cloneable {

    private static final long serialVersionUID = -2646424027034058288L;

    /**
     * 记录的类名
     */
    private String className;
    /**
     * 记录的方法名
     */
    private String methodName;
    /**
     * 记录的行数
     */
    private int lineNumber;
    /**
     * 记录的信息
     */
    private String info;
    /**
     * 最终记录的信息
     */
    private String writeInfo;
    /**
     * 临时存储的值
     */
    private final Map<String, Object> temporaryValue = new HashMap<>();

    /**
     * 包含消息的构造
     *
     * @param info 消息
     */
    public EnhanceInfo(String info) {
        this.info = info;
    }

    /**
     * 获取类名
     *
     * @return 类名
     */
    public String getClassName() {
        return className;
    }

    /**
     * 设置类名
     *
     * @param className 类名
     */
    public void setClassName(String className) {
        this.className = className;
    }

    /**
     * 获取方法名
     *
     * @return 方法名
     */
    public String getMethodName() {
        return methodName;
    }

    /**
     * 设置方法名
     *
     * @param methodName 方法名
     */
    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    /**
     * 获取行号
     *
     * @return 行号
     */
    public int getLineNumber() {
        return lineNumber;
    }

    /**
     * 设置行号
     *
     * @param lineNumber 行号
     */
    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    /**
     * 获取信息
     *
     * @return 信息
     */
    public String getInfo() {
        return info;
    }

    /**
     * 设置消息
     *
     * @param info 消息
     */
    public void setInfo(String info) {
        this.info = info;
    }

    /**
     * 获取写消息
     *
     * @return 写消息
     */
    public String getWriteInfo() {
        return writeInfo;
    }

    /**
     * 设置写消息
     *
     * @param writeInfo 写消息
     */
    public void setWriteInfo(String writeInfo) {
        this.writeInfo = writeInfo;
    }

    /**
     * 获取临时值
     *
     * @param key 键
     * @return 临时值
     */
    public Object getTemporaryValue(String key) {
        return temporaryValue.get(key);
    }

    /**
     * 设置临时值
     *
     * @param key   键
     * @param value 临时值
     * @throws NullPointerException if {@code key} is null
     */
    public void setTemporaryValue(String key, Object value) {
        Objects.requireNonNull(key);
        this.temporaryValue.put(key, value);
    }

    /**
     * 克隆增强信息
     *
     * <p>
     * 保留一个增强信息主要的内容，因为其余数据是内部使用
     * </p>
     *
     * @return 克隆后的增强信息
     * @since 2.5.3
     */
    @Override
    public EnhanceInfo clone() {
        EnhanceInfo enhanceInfo = new EnhanceInfo(info);
        Maps.walkEnd(
            temporaryValue,
            (k, v) -> {
                enhanceInfo.setTemporaryValue(k, v);
            }
        );
        return enhanceInfo;
    }

    /**
     * 增强信息详情
     *
     * @return 详情
     */
    @Override
    public String toString() {
        return (
            "EnhanceInfo{" +
            "className='" +
            className +
            '\'' +
            ", methodName='" +
            methodName +
            '\'' +
            ", lineNumber=" +
            lineNumber +
            ", info='" +
            info +
            '\'' +
            ", writeInfo='" +
            writeInfo +
            '\'' +
            ", temporaryValue=" +
            temporaryValue +
            '}'
        );
    }
}
