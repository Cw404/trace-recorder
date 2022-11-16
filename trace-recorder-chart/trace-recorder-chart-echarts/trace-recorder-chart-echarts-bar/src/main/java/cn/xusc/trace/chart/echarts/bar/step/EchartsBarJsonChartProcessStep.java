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
package cn.xusc.trace.chart.echarts.bar.step;

import cn.xusc.trace.chart.AbstractChartConfig;
import cn.xusc.trace.chart.ChartProcessStep;
import cn.xusc.trace.chart.constant.Temporary;
import cn.xusc.trace.chart.echarts.bar.config.EchartsBarChartConfig;
import cn.xusc.trace.chart.echarts.bar.mapping.EchartsBar;
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
 * Echarts柱状图json化图表处理步骤
 *
 * @author WangCai
 * @since 2.5.2
 */
public class EchartsBarJsonChartProcessStep implements ChartProcessStep {

    /**
     * 生成柱状图json文件路径
     */
    private Path generateBarJsonFilePath;

    /**
     * 基础构造
     */
    public EchartsBarJsonChartProcessStep() {}

    @Override
    public <T> void run(AbstractChartConfig config, ValuePipeline<T> valuePipeline) throws Exception {
        EchartsBar bar = (EchartsBar) valuePipeline.getValue();

        initBarJsonFilePath((EchartsBarChartConfig) config);

        Jsons.write(objectMapper -> {
            try {
                objectMapper.writeValue(generateBarJsonFilePath.toFile(), bar);
            } catch (IOException e) {
                throw new TraceException(e);
            }
        });
        valuePipeline.setValue((T) generateBarJsonFilePath);
    }

    /**
     * 初始化柱状图json文件路径
     *
     * @param chartConfig 图表配置
     * @throws IOException if an I/O error has occurred.
     */
    private void initBarJsonFilePath(EchartsBarChartConfig chartConfig) throws IOException {
        Path generatePath = generateEnvironment(chartConfig);
        Path barJsonFilePath = generatePath.resolve("echarts/bar/data/bar.json");
        if (Files.notExists(barJsonFilePath)) {
            Files.createFile(barJsonFilePath);
        }
        generateBarJsonFilePath = barJsonFilePath;
    }

    /**
     * 生成柱状图文件环境
     *
     * @param chartConfig 图表配置
     * @return 生成目录的路径
     * @throws IOException if an I/O error has occurred.
     */
    private Path generateEnvironment(EchartsBarChartConfig chartConfig) throws IOException {
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
