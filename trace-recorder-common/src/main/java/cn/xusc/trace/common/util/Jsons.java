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

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.util.function.Consumer;
import java.util.function.Function;
import lombok.experimental.UtilityClass;

/**
 * 对象映射器工具类
 *
 * <p>
 * 通过lombok组件{@link UtilityClass}确保工具类的使用
 * </p>
 *
 * @author WangCai
 * @since 2.5
 */
@UtilityClass
public class Jsons {

    /**
     * 对象映射器
     */
    private final ObjectMapper OBJECT_MAPPER;

    static {
        OBJECT_MAPPER = new ObjectMapper();

        /* written配置 */
        OBJECT_MAPPER.enable(SerializationFeature.INDENT_OUTPUT);
        OBJECT_MAPPER.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        OBJECT_MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        /* read配置 */
        OBJECT_MAPPER.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        OBJECT_MAPPER.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        /* parsing配置 */
        OBJECT_MAPPER.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        OBJECT_MAPPER.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        OBJECT_MAPPER.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        /* generate配置 */
        OBJECT_MAPPER.configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
    }

    /**
     * 读取数据为对象
     *
     * @param objectMapperTFunction 对象映射器函数
     * @return 对象
     * @param <T> 对象类型
     */
    public <T> T read(Function<ObjectMapper, T> objectMapperTFunction) {
        return objectMapperTFunction.apply(OBJECT_MAPPER);
    }

    /**
     * 写出数据
     *
     * @param objectMapperConsumer 对象映射器消费
     */
    public void write(Consumer<ObjectMapper> objectMapperConsumer) {
        objectMapperConsumer.accept(OBJECT_MAPPER);
    }
}
