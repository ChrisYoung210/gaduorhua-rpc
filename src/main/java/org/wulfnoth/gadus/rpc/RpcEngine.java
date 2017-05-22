package org.wulfnoth.gadus.rpc;

import java.net.InetSocketAddress;

/**
 * @author Young
 */
public interface RpcEngine {

    /**
     * 获取一个RPC客户端
     * @param clazz 协议类
     * @param address RPC服务端地址
     * @param timeout timeout
     * @param <T> 协议类型
     * @return RPC客户端实例
     */
    <T> T getProxy(Class<T> clazz, InetSocketAddress address,
                   long timeout);

    /**
     * 获取一个RPC服务端
     * @param address 绑定的本地地址
     * @return a RPC Server instance
     */
    RpcServer getServer(InetSocketAddress address, Object instance, int workerThreadN);

}
