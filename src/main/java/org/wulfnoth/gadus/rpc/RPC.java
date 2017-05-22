package org.wulfnoth.gadus.rpc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wulfnoth.gadus.rpc.kryo.KryoRpcEngine;

import java.net.InetSocketAddress;

public class RPC {

    private static Logger logger = LoggerFactory.getLogger(RPC.class);

    public static class RpcServerBuilder {

        private RpcServerBuilder(){}

        private int workerThreadN = 8;

        private Object instance = null;

        private InetSocketAddress address = null;

        private Class<? extends RpcEngine> clazz = null;

        private Class protocol = null;

        public RpcServerBuilder setWorkerThreadNum(int workerThreadN) {
            this.workerThreadN = workerThreadN;
            return this;
        }

        public RpcServerBuilder setInstance(Object instance) {
            this.instance = instance;
            return this;
        }

        public RpcServerBuilder setAddress(InetSocketAddress address) {
            this.address = address;
            return this;
        }

        public RpcServerBuilder setRpcEngineType(Class<? extends RpcEngine> clazz) {
            this.clazz = clazz;
            return this;
        }

        public RpcServer build() {
            if (clazz == null) clazz = KryoRpcEngine.class;
            if (protocol == null) throw new IllegalStateException("未指定RpcServer使用的协议类");
            if (instance == null) throw new IllegalStateException("未指定RpcServer使用的实例");
            if (address == null) throw new IllegalStateException("未指定RpcServer绑定的地址");

            if (!protocol.isAssignableFrom(instance.getClass()))
                throw new IllegalStateException("实例与协议类不匹配");

            return RPCInScala$.MODULE$.getRpcEngine(clazz).getServer(address, instance, workerThreadN);
        }

    }



    public static RpcServerBuilder getRpcServerBuilder() {
        return new RpcServerBuilder();
    }

    public static <T> T getProxy(Class<T> clazz, InetSocketAddress address) {
        return null;
    }

}
