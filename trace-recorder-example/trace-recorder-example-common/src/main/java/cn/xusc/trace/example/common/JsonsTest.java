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
package cn.xusc.trace.example.common;

import cn.xusc.trace.common.exception.TraceException;
import cn.xusc.trace.common.util.Jsons;
import cn.xusc.trace.core.TraceRecorder;
import java.io.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * 对象映射器测试
 *
 * @author wangcai
 */
public final class JsonsTest {

    /**
     * 写值并读取值
     */
    @SneakyThrows
    @Test
    @DisplayName("Write value and read value")
    public void writeReadValue() {
        /*
        write Data to bytesOutputStream
         */
        ByteArrayOutputStream bytesOutputStream = new ByteArrayOutputStream();
        Jsons.write(objectMapper -> {
            try {
                objectMapper.writeValue(bytesOutputStream, new Data());
            } catch (IOException e) {
                throw new TraceException(e);
            }
        });

        /*
        read bytesOutputStream to Data
         */
        Data data = Jsons.read(objectMapper -> {
            try {
                return objectMapper.readValue(new ByteArrayInputStream(bytesOutputStream.toByteArray()), Data.class);
            } catch (IOException e) {
                throw new TraceException(e);
            }
        });

        /*
        log Data
         */
        new TraceRecorder().log("{}", data);
    }

    /**
     * 数据
     */
    @ToString
    @NoArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    private static class Data {

        /**
         * 名称
         */
        String name = "trace-recorder";

        /**
         * 年龄
         */
        int age = 1;
    }
}
