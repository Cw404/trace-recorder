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
package cn.xusc.trace.common.exception;

/**
 * 跟踪异常
 *
 * @author WangCai
 * @since 2.0
 */
public class TraceException extends RuntimeException {

    /**
     * 基础构造
     */
    public TraceException() {}

    /**
     * 异常信息的构造
     *
     * @param message 异常信息
     */
    public TraceException(String message) {
        super(message);
    }

    /**
     * 异常信息和原因的构造
     *
     * @param message 异常信息
     * @param cause   原因
     */
    public TraceException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 原因的构造
     *
     * @param cause 原因
     */
    public TraceException(Throwable cause) {
        super(cause);
    }

    /**
     * 详细的构造
     *
     * @param message            异常信息
     * @param cause              原因
     * @param enableSuppression  启用抑制
     * @param writableStackTrace 可写的栈跟踪
     */
    public TraceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
