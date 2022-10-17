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
package cn.xusc.trace.server.util;

import cn.xusc.trace.common.exception.TraceException;
import java.util.concurrent.TimeUnit;

/**
 * 服务关闭等候者
 *
 * <p>
 * 服务关闭响应，更精确的等待
 * </p>
 *
 * @author WangCai
 * @since 2.5
 */
public enum ServerClosedWaiter {
    /**
     * 实例
     */
    INSTANCE;

    /**
     * 可等标识
     */
    private volatile boolean canWait;

    /**
     * 唤醒标识
     */
    private boolean notify;

    /**
     * 设置标识为可等
     */
    public void canWait() {
        canWait = true;
    }

    /**
     * 根据可等标识进行等待
     *
     * <p>
     * 等待规则:
     * <ul>
     *     <li>不可等休眠指定秒数时间，直至可等</li>
     *     <li>可等并未唤醒，则持续等待，直至唤醒</li>
     * </ul>
     * </p>
     *
     * @param timeout 睡眠时间
     * @throws InterruptedException if interrupted while sleeping.
     * @throws TraceException if {@code timeout} is less 1.
     */
    public void doWait(long timeout) throws InterruptedException {
        if (timeout < 1) {
            throw new TraceException("timeout must to greater 0");
        }
        while (!canWait) {
            TimeUnit.SECONDS.sleep(timeout);
        }
        synchronized (this) {
            if (!notify) {
                wait();
            }
        }
    }

    /**
     * 唤醒等待调用
     */
    public void doNotify() {
        doNotifyAll();
    }

    /**
     * 唤醒所有等待调用
     */
    public void doNotifyAll() {
        synchronized (this) {
            notifyAll();
            notify = true;
        }
    }
}
