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
package cn.xusc.trace.chart.config;

import cn.xusc.trace.chart.AbstractChartConfig;
import cn.xusc.trace.chart.constant.Temporary;
import cn.xusc.trace.common.exception.TraceUnsupportedOperationException;
import java.nio.file.Path;
import java.util.Objects;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * 可生成的图表配置
 *
 * <p>
 * 此类的字段值都至关重要，实现类构建应主动调用{@link #buildCompleteVerify()}来完成构建完成的验证
 * </p>
 * <strong>ps: 调用{@link #buildCompleteVerify()}是一种兼容builder和new字段属性完整性的方式</strong>
 *
 * @author WangCai
 * @since 2.5.2
 */
@Setter
@Getter
@SuperBuilder
public abstract class GenerableChartConfig extends AbstractChartConfig {

    /**
     * 特定的图路径
     *
     * <p>
     * 该路径确保生成图文件都在chart路径下，用来划分图资源
     * </p>
     */
    private static final Path SPECIFIC_CHART_PATH = Path.of("chart");

    /**
     * 生成路径
     */
    private Path generatePath;

    /**
     * 家路径
     */
    private Path homePath;

    /**
     * 特定的生成图路径
     *
     * <p>
     * 该路径保证了各图之间生成存放文件的隔离性，需确保该路径有效
     * </p>
     */
    private Path specificGenerateChartPath;

    /**
     * json文件路径
     *
     * <p>
     * 该路径为相对{@link #generatePath}的相对路径
     * </p>
     * <strong>ps: 如果json文件路径存在于文件夹之下，那么文件夹需自己保证其存在</strong>
     *
     * <table border="1">
     * <caption>示例</caption>
     * <tr><th>generatePath</th><th>jsonFilePath</th><th>absolute JsonFilePath</th></tr>
     * <tr><td>/home/xusc</td><td>chart/trace-recorder.json</td><td>/home/xusc/chart/trace-recorder.json</td></tr>
     * </table>
     */
    private Path jsonFilePath;

    /**
     * 完整标识
     */
    private boolean complete;

    /**
     * 获取特定的生成路径
     *
     * @return 特定的生成路径
     */
    public Path getSpecificGeneratePath() {
        return generatePath.resolve(getSpecificGenerateChartPath());
    }

    /**
     * 获取特定的生成图路径
     *
     * @return 特定的生成图路径
     */
    public Path getSpecificGenerateChartPath() {
        return Temporary.GENERATE_PATH.resolve(SPECIFIC_CHART_PATH).resolve(specificGenerateChartPath);
    }

    /**
     * 构建完成验证
     *
     * @return 验证成功后的自身图配置
     * @param <T> 图配置类型
     */
    public <T> T buildCompleteVerify() {
        if (!complete) {
            /*
            验证有效的生成路径
             */
            if (Objects.isNull(generatePath) || generatePath.toString().isBlank()) {
                throw new TraceUnsupportedOperationException("generatePath must to not null and not blank");
            }
            /*
            验证有效的家路径
             */
            if (Objects.isNull(homePath) || homePath.toString().isBlank()) {
                throw new TraceUnsupportedOperationException("homePath must to not null and not blank");
            }
            /*
            验证有效的特定的生成图路径
             */
            if (Objects.isNull(specificGenerateChartPath) || specificGenerateChartPath.toString().isBlank()) {
                throw new TraceUnsupportedOperationException(
                    "specificGenerateChartPath must to not null and not blank"
                );
            }
            /*
            验证有效的特定的json文件路径
             */
            if (Objects.isNull(jsonFilePath) || jsonFilePath.toString().isBlank()) {
                throw new TraceUnsupportedOperationException("jsonFilePath must to not null and not blank");
            }
            complete = true;
        }
        return (T) this;
    }
}
