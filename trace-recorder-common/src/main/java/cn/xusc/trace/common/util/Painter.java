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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringTokenizer;

/**
 * 画家
 *
 * <p>
 * 暂不支持中文字符格式布局
 * </p>
 *
 * @author WangCai
 * @since 2.5
 */
public final class Painter {

    /**
     * 内容列表
     */
    private List<String> contents = new ArrayList<>();

    /**
     * 表格识别符
     */
    private String tableIdentifier = "|";

    /**
     * 最大内容长度
     */
    private int maxContentLength;

    /**
     * 默认构造
     */
    public Painter() {}

    /**
     * 添加内容
     *
     * @param content 内容
     * @throws NullPointerException if {@code content} is null.
     */
    public void addContent(String content) {
        Objects.requireNonNull(content);

        maxContentLength = Math.max(maxContentLength, content.length());
        contents.add(content);
    }

    /**
     * 内容画框
     *
     * @return 画框后的内容
     */
    public String drawFrame() {
        if (contents.isEmpty()) {
            return Strings.empty();
        }

        int maxDrawLength = maxContentLength + 4, spacePlaceholderLength;
        StringBuilder drawBuffer = new StringBuilder((contents.size() + 2) * maxDrawLength + 16);
        String head = Symbols.generate(Symbols.connective(), maxDrawLength).concat(Symbols.lineSeparator());
        drawBuffer.append(head);
        for (String content : contents) {
            drawBuffer.append(Symbols.connective().concat(Symbols.space()).concat(content));
            drawBuffer.append(
                (spacePlaceholderLength = maxDrawLength - 4 - content.length()) > 0
                    ? Symbols.generate(Symbols.space(), spacePlaceholderLength)
                    : Strings.empty()
            );
            drawBuffer.append(Symbols.space().concat(Symbols.connective()).concat(Symbols.lineSeparator()));
        }
        drawBuffer.append(head);

        return drawBuffer.toString();
    }

    /**
     * 内容画表格
     *
     * @return 画表格后的内容
     */
    public String drawTable() {
        if (contents.isEmpty()) {
            return Strings.empty();
        }

        String[] tHead = {}, tBody;
        String[][] tBodies = {};
        Integer[] columnMaxLengths = {};
        String token;
        StringTokenizer tokenizer;
        for (int i = 0, y = 0, size = contents.size(); i < size; i++, y = 0) {
            tokenizer = new StringTokenizer(contents.get(i), tableIdentifier);
            if (i > 0) {
                /*
               表格
                */
                tBody = tBodies[i - 1];
                while (tokenizer.hasMoreTokens()) {
                    token = tokenizer.nextToken();
                    tBody[y] = token;
                    columnMaxLengths[y] = Math.max(columnMaxLengths[y], token.length());
                    y++;
                }
                if (tBody.length != tHead.length) {
                    throw new TraceException("illegal table model");
                }
                continue;
            }
            /*
            表头
             */
            while (tokenizer.hasMoreTokens()) {
                token = tokenizer.nextToken();
                tHead = Arrays.merge(tHead, token);
                columnMaxLengths = Arrays.merge(columnMaxLengths, token.length());
            }
            tBodies = new String[size - 1][tHead.length];
        }

        int columnLengths = java.util.Arrays
            .stream(columnMaxLengths)
            .reduce(1, (x, y) -> x + y + 3), spacePlaceholderLength;

        StringBuilder drawBuffer = new StringBuilder(columnLengths * (columnMaxLengths.length + 1) + 16);
        String vertical = Symbols.generate("|", 1);
        String spaceVertical = Symbols.generate(Symbols.space().concat(vertical), 1);
        drawBuffer.append(vertical);
        for (int i = 0; i < tHead.length; i++) {
            drawBuffer.append(Symbols.space().concat(tHead[i]));
            drawBuffer.append(
                (spacePlaceholderLength = columnMaxLengths[i] - tHead[i].length()) > 0
                    ? Symbols.generate(Symbols.space(), spacePlaceholderLength)
                    : Strings.empty()
            );
            drawBuffer.append(spaceVertical);
        }
        drawBuffer.append(Symbols.lineSeparator());
        drawBuffer.append(Symbols.generate(Symbols.connective(), columnLengths));
        drawBuffer.append(Symbols.lineSeparator());
        for (String[] bodies : tBodies) {
            drawBuffer.append(vertical);
            for (int i = 0; i < bodies.length; i++) {
                drawBuffer.append(Symbols.space().concat(bodies[i]));
                drawBuffer.append(
                    (spacePlaceholderLength = columnMaxLengths[i] - bodies[i].length()) > 0
                        ? Symbols.generate(Symbols.space(), spacePlaceholderLength)
                        : Strings.empty()
                );
                drawBuffer.append(spaceVertical);
            }
            drawBuffer.append(Symbols.lineSeparator());
        }

        return drawBuffer.toString();
    }
}
