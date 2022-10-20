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
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * 渲染引擎
 *
 * <p>
 * 通过EL表达式风格进行内容渲染
 * </p>
 *
 * @author WangCai
 * @since 2.2
 */
public class RenderEngine {

    /**
     * 内容
     */
    private String content;

    /**
     * 渲染词库
     */
    private Map<String, String> renderWords = new HashMap<>();

    /**
     * 已渲染标识
     */
    private boolean rendered;

    /**
     * EL表达式左符号
     */
    private static final String EL_LEFT_SYMBOL = "${";

    /**
     * EL表达式右符号
     */
    private static final String EL_RIGHT_SYMBOL = "}";

    /**
     * 基础构造
     */
    public RenderEngine() {}

    /**
     * 带内容的构造
     *
     * @param content 内容
     */
    public RenderEngine(String content) {
        registerContent(content);
    }

    /**
     * 注册渲染词
     *
     * @param key 渲染键
     * @param renderWord 渲染词
     * @return 注册详情
     */
    public boolean register(String key, String renderWord) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(renderWord);

        renderWords.put(EL_LEFT_SYMBOL.concat(key).concat(EL_RIGHT_SYMBOL), renderWord);
        return true;
    }

    /**
     * 注册内容
     *
     * @param content 内容
     * @return 注册详情
     * @throws NullPointerException if {@code content} is null.
     */
    public boolean registerContent(String content) {
        Objects.requireNonNull(content);

        this.content = content;
        this.rendered = false;
        return true;
    }

    /**
     * 获取渲染内容
     *
     * @return 渲染内容
     * @throws TraceException if {@code content} is null.
     */
    public String renderContent() {
        if (Objects.isNull(content)) {
            throw new TraceException("render content is null");
        }

        if (!rendered) {
            synchronized (this) {
                if (!rendered) {
                    rending();
                    rendered = true;
                }
            }
        }
        return content;
    }

    /**
     * 获取渲染内容并消费
     *
     * @param renderContentConsumer 渲染内容消费者
     */
    public void renderContent(Consumer<String> renderContentConsumer) {
        String renderContent = renderContent();
        renderContentConsumer.accept(renderContent);
    }

    /**
     * 清除
     */
    public void clear() {
        content = null;
        rendered = false;
        renderWords.clear();
    }

    /**
     * 进行渲染
     */
    private void rending() {
        StringBuilder rendingContent = new StringBuilder(content);
        Maps.walk(
            renderWords,
            (k, v) -> {
                String innerContent = rendingContent.toString();
                if (innerContent.indexOf(k) > -1) {
                    int contentLength = innerContent.length();
                    innerContent = innerContent.replace(k, v);
                    rendingContent.delete(0, contentLength);
                    rendingContent.append(innerContent);
                }
                return true;
            }
        );

        content = rendingContent.toString();
    }
}
