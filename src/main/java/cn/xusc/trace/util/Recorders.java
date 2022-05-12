/*
 * Copyright 20022 WangCai.
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

package cn.xusc.trace.util;

import cn.xusc.trace.EnhanceInfo;
import cn.xusc.trace.TraceRecorder;
import cn.xusc.trace.annotation.CloseOrder;
import cn.xusc.trace.enhance.InfoEnhancer;
import cn.xusc.trace.enhance.StatisticsInfoEnhancer;
import cn.xusc.trace.filter.InfoFilter;
import cn.xusc.trace.record.FileInfoRecorder;
import cn.xusc.trace.record.InfoRecorder;

import java.io.File;
import java.util.Objects;

/**
 * 记录器工具类
 *
 * <p>
 * 简化跟踪记录器的使用
 * </p>
 *
 * @author WangCai
 * @since 1.0
 */
public final class Recorders {
    
    /**
     * 禁止实例化
     */
    private Recorders() {
    }
    
    /**
     * 跟踪记录仪（全局）
     */
    private final static TraceRecorder TRACE_RECORDER = new TraceRecorder();
    
    /**
     * 添加信息过滤器
     *
     * @param filter 信息过滤器
     * @return 添加结果
     */
    public static boolean addInfoFilter(InfoFilter filter) {
        return TRACE_RECORDER.addInfoFilter(filter);
    }
    
    /**
     * 添加信息增强器
     *
     * @param enhancer 信息增强器
     * @return 添加结果
     */
    public static boolean addInfoEnhancer(InfoEnhancer enhancer) {
        return TRACE_RECORDER.addInfoEnhancer(enhancer);
    }
    
    /**
     * 添加信息记录器
     *
     * @param recorder 信息记录器
     * @return 添加结果
     */
    public static boolean addInfoRecorder(InfoRecorder recorder) {
        return TRACE_RECORDER.addInfoRecorder(recorder);
    }
    
    /**
     * 记录所有
     *
     * <p>启用记录所有标签</p>
     *
     * @return 配置结果
     */
    public static boolean recordALL() {
        return TRACE_RECORDER.recordALL();
    }
    
    /**
     * 隐藏所有
     *
     * <p>启用隐藏所有标签</p>
     *
     * @return 配置结果
     */
    public static boolean hideALL() {
        return TRACE_RECORDER.hideALL();
    }
    
    /**
     * 启用短类名
     *
     * <p>
     * {@link cn.xusc.trace.enhance.ShortClassNameInfoEnhancer}
     * </p>
     *
     * @return 配置结果
     */
    public static boolean enableShortClassName() {
        return TRACE_RECORDER.enableShortClassName();
    }
    
    /**
     * 启用堆栈
     *
     * @return 配置结果
     */
    public static boolean enableStackInfo() {
        return TRACE_RECORDER.enableStackInfo();
    }
    
    /**
     * 禁用堆栈信息
     *
     * @return 配置结果
     */
    public static boolean disableStackInfo() {
        return TRACE_RECORDER.disableStackInfo();
    }
    
    /**
     * 记录信息
     *
     * @param info 信息
     */
    public static void log(String info) {
        TRACE_RECORDER.log(info);
    }
    
    /**
     * 记录格式信息
     *
     * @param info     格式信息
     * @param argArray 参数列表
     * @since 1.1
     */
    @SuppressWarnings("unused")
    public void log(String info, Object... argArray) {
        TRACE_RECORDER.log(info, argArray);
    }
    
    /**
     * 不记录信息
     *
     * @param info 信息
     * @since 1.1
     */
    public static void nolog(String info) {
        TRACE_RECORDER.nolog(info);
    }
    
    /**
     * 不记录格式信息
     *
     * @param info     格式信息
     * @param argArray 参数列表
     * @since 1.1
     */
    @SuppressWarnings("unused")
    public void nolog(String info, Object... argArray) {
        TRACE_RECORDER.nolog(info, argArray);
    }
    
    /*
      =========================== extension ===========================
     */
    
    /**
     * 添加文件信息记录器
     *
     * @param fileName 记录文件名
     * @return 添加结果
     */
    @SuppressWarnings("unused")
    public static boolean addFileInfoRecorder(String fileName) {
        Objects.requireNonNull(fileName);
        return addFileInfoRecorder(new File(fileName));
    }
    
    /**
     * 添加文件信息记录器
     *
     * @param file 记录文件
     *             仅文件地址，需要具备可写权限，目标文件不存在会自动创建
     * @return 添加结果
     */
    public static boolean addFileInfoRecorder(File file) {
        Objects.requireNonNull(file);
        if (file.isDirectory()) {
            throw new IllegalArgumentException("file can't directory");
        }
        return TRACE_RECORDER.addInfoRecorder(new FileInfoRecorder(file));
    }
    
    /**
     * 添加通用统计信息增强器
     *
     * @return 添加结果
     */
    public static boolean addCommonStatisticsInfoEnhancer() {
        return TRACE_RECORDER.addInfoEnhancer(new CommonStatisticsInfoEnhancer());
    }
    
    /**
     * 通用统计信息增强器
     */
    @CloseOrder(1)
    private static class CommonStatisticsInfoEnhancer extends StatisticsInfoEnhancer {
        private TraceNode HEAD;
        
        @Override
        protected EnhanceInfo doEnhance(EnhanceInfo eInfo) {
            TraceNode head = HEAD;
            if (Objects.isNull(head)) {
                /*
                  first , initialize
                 */
                HEAD = new TraceNode();
                HEAD.setClassName(eInfo.getClassName());
                HEAD.setCount(1);
                return eInfo;
            }
            
            /*
              increment count
             */
            do {
                if (Objects.equals(head.getClassName(), eInfo.getClassName())) {
                    head.increment();
                    break;
                }
            } while (Objects.nonNull(head = head.getNext()));
            return eInfo;
        }
        
        @Override
        protected String showInfo() {
            TraceNode head = HEAD;
            if (Objects.isNull(head)) {
                /*
                  没有记录任何一条显示记录，head没有生成
                 */
                return "";
            }
            
            StringBuilder sb = new StringBuilder();
            String lineSeparator = Symbols.lineSeparator();
            sb.append("--------------------").append(lineSeparator);
            do {
                sb.append(String.format("%s - %d", head.getClassName(), head.getCount())).append(lineSeparator);
            } while (Objects.nonNull(head = head.getNext()));
            
            sb.append("--------------------").append(lineSeparator);
            return sb.toString();
        }
        
        /**
         * 跟踪锚点节点
         *
         * <p>类锚点总数跟踪</p>
         */
        @SuppressWarnings("InnerClassMayBeStatic")
        private class TraceNode {
            private String className;
            private long count;
            @SuppressWarnings("unused")
            private TraceNode next;
            
            public TraceNode() {
            }
            
            public String getClassName() {
                return className;
            }
            
            public void setClassName(String className) {
                this.className = className;
            }
            
            public long getCount() {
                return count;
            }
            
            public void setCount(long count) {
                this.count = count;
            }
            
            public void increment() {
                this.count++;
            }
            
            public TraceNode getNext() {
                return next;
            }
            
            @SuppressWarnings("unused")
            public boolean hasNext() {
                return next != null;
            }
        }
    }
    
    
}
