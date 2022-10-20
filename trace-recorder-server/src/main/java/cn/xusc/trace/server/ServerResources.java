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
package cn.xusc.trace.server;

import cn.xusc.trace.common.util.Maps;
import java.util.*;
import java.util.function.BiFunction;

/**
 * 服务资源点
 *
 * @author WangCai
 * @since 2.5
 */
class ServerResources {

    /**
     * 服务资源池
     */
    private Map<String, ServerResource> resources = new HashMap();

    /**
     * 关闭资源路径列表
     */
    private List<String> closeResourcePaths = new ArrayList<>();

    /**
     * 基础构造
     */
    public ServerResources() {}

    /**
     * 注册服务资源
     *
     * @param serverResource 服务资源
     * @return 注册详情
     */
    public boolean register(ServerResource serverResource) {
        return register(serverResource, false);
    }

    /**
     * 注册服务资源
     *
     * @param serverResource 服务资源
     * @return 注册详情
     */
    public boolean register(ServerResource serverResource, boolean isCloseResource) {
        resources.put(serverResource.path(), serverResource);
        if (isCloseResource) {
            closeResourcePaths.add(serverResource.path());
        }
        return true;
    }

    /**
     * 根据服务资源路径移除相应的服务资源
     *
     * @param serverResourcePath 服务资源路径
     * @return 移除详情
     */
    public boolean remove(String serverResourcePath) {
        resources.remove(serverResourcePath);
        closeResourcePaths.remove(serverResourcePath);
        return true;
    }

    /**
     * 是否存在服务资源路径相应的服务资源
     *
     * @param serverResourcePath 服务资源路径
     * @return 存在详情
     */
    public boolean exist(String serverResourcePath) {
        return serverResource(serverResourcePath).isPresent();
    }

    /**
     * 根据服务资源路径检索相应的可选服务资源
     *
     * @param serverResourcePath 服务资源路径
     * @return 可选服务资源
     * @throws NullPointerException if {@code serverResourcePath} is null.
     */
    public Optional<ServerResource> serverResource(String serverResourcePath) {
        return Optional.ofNullable(resources.get(serverResourcePath));
    }

    /**
     * 游走服务资源
     *
     * @param serverResourceFunction 服务资源调用函数
     */
    public void walkServerResource(BiFunction<String, ServerResource, Boolean> serverResourceFunction) {
        Maps.walk(resources, (path, resource) -> serverResourceFunction.apply(path, resource));
    }

    /**
     * 游走关闭服务资源
     *
     * @param serverResourceFunction 服务资源调用函数
     */
    public void walkCloseServerResource(BiFunction<String, ServerResource, Boolean> serverResourceFunction) {
        Maps.walk(
            resources,
            (path, resource) -> {
                if (closeResourcePaths.contains(path)) {
                    return serverResourceFunction.apply(path, resource);
                }
                return true;
            }
        );
    }
}
