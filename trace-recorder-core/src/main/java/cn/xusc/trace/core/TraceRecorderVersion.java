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

import cn.xusc.trace.common.util.Formats;
import java.util.Objects;
import lombok.Getter;

/**
 * 跟踪记录仪版本
 *
 * @author WangCai
 * @since 2.5
 */
@Getter
public enum TraceRecorderVersion {
    /**
     * 版本实例
     */
    INSTANCE;

    /**
     * 主版本号
     */
    private int major;
    /**
     * 次版本号
     */
    private int minor;
    /**
     * 修订版本号
     */
    private int revision;
    /**
     * 快照标识
     */
    private boolean snapshot;

    {
        /* 初始化版本 */
        major = 2;
        minor = 5;
        revision = 0;
        snapshot = true;
    }

    @Override
    public String toString() {
        return Formats.format(
            "{}.{}{}{}",
            major,
            minor,
            Objects.equals(revision, 0) ? "" : ".".concat(String.valueOf(revision)),
            snapshot ? "-SNAPSHOT" : ""
        );
    }
}
