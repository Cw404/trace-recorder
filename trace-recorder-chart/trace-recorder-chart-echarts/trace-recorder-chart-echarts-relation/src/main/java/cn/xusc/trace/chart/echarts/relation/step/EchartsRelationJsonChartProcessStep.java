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
package cn.xusc.trace.chart.echarts.relation.step;

import cn.xusc.trace.chart.AbstractChartConfig;
import cn.xusc.trace.chart.ChartProcessStep;
import cn.xusc.trace.chart.constant.Temporary;
import cn.xusc.trace.chart.echarts.relation.config.EchartsRelationChartConfig;
import cn.xusc.trace.chart.echarts.relation.mapping.EchartsRelation;
import cn.xusc.trace.common.exception.TraceException;
import cn.xusc.trace.common.util.Formats;
import cn.xusc.trace.common.util.Jsons;
import cn.xusc.trace.common.util.Systems;
import cn.xusc.trace.common.util.file.Finder;
import cn.xusc.trace.common.util.file.JarPath;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

/**
 * Echarts关系图json化图表处理步骤
 *
 * @author WangCai
 * @since 2.5
 */
public class EchartsRelationJsonChartProcessStep implements ChartProcessStep {

    /**
     * 生成关系图json文件路径
     */
    private Path generateRelationJsonFilePath;

    /**
     * 基础构造
     */
    public EchartsRelationJsonChartProcessStep() {}

    @Override
    public <T> void run(AbstractChartConfig config, ValuePipeline<T> valuePipeline) throws Exception {
        EchartsRelation relation = (EchartsRelation) valuePipeline.getValue();

        initRelationJsonFilePath((EchartsRelationChartConfig) config);

        Jsons.write(objectMapper -> {
            try {
                objectMapper.writeValue(generateRelationJsonFilePath.toFile(), relation);
            } catch (IOException e) {
                throw new TraceException(e);
            }
        });
        valuePipeline.setValue((T) generateRelationJsonFilePath);
    }

    /**
     * 初始化关系图json文件路径
     *
     * @param chartConfig 图表配置
     * @throws IOException if an I/O error has occurred.
     */
    private void initRelationJsonFilePath(EchartsRelationChartConfig chartConfig) throws IOException {
        Path generatePath = generateEnvironment(chartConfig);
        Path relationJsonFilePath = generatePath.resolve("echarts/relation/data/relation.json");
        if (Files.notExists(relationJsonFilePath)) {
            Files.createFile(relationJsonFilePath);
        }
        generateRelationJsonFilePath = relationJsonFilePath;
    }

    /**
     * 生成关系图文件环境
     *
     * @param chartConfig 图表配置
     * @return 生成目录的路径
     * @throws IOException if an I/O error has occurred.
     */
    private Path generateEnvironment(EchartsRelationChartConfig chartConfig) throws IOException {
        Path generatePath = chartConfig.getGeneratePath();

        if (generatePath.equals(Path.of(""))) {
            throw new TraceException(Formats.format("not found store directory -> {}", generatePath.toString()));
        }

        Path templatePath = Temporary.TEMPLATE_PATH;
        generatePath = generatePath.resolve(Temporary.GENERATE_PATH);
        String homePath = chartConfig.getHomePath().toString();
        List<Path> paths = Finder.find(ClassLoader.getSystemResource(Systems.getSystemClassPath(homePath)), "**");
        if (Files.notExists(generatePath)) {
            Files.createDirectory(generatePath);
            for (Path path : paths) {
                if (path instanceof JarPath) {
                    JarPath jarPath = (JarPath) path;
                    Path generateFilePath = generatePath.resolve(templatePath.relativize(jarPath.getFileName()));
                    if (jarPath.isDirectoryPath()) {
                        Files.createDirectories(generateFilePath);
                        continue;
                    }
                    try (InputStream jarPathInputStream = jarPath.getInputStream()) {
                        Files.write(
                            generateFilePath,
                            jarPathInputStream.readAllBytes(),
                            StandardOpenOption.CREATE_NEW,
                            StandardOpenOption.WRITE
                        );
                    }
                    continue;
                }

                /*
                处理源码项目使用
                 */
                String pathPath = path.toString();
                String homeFilePath = pathPath.substring(pathPath.lastIndexOf(homePath));
                Path generateFilePath = generatePath.resolve(templatePath.relativize(Path.of(homeFilePath)));

                if (Files.isDirectory(path)) {
                    Files.createDirectories(generateFilePath);
                    continue;
                }
                Files.write(
                    generateFilePath,
                    Files.readAllBytes(path),
                    StandardOpenOption.CREATE_NEW,
                    StandardOpenOption.WRITE
                );
            }
        }
        return generatePath;
    }
}
