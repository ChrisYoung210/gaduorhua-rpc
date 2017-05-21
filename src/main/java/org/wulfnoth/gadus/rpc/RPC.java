package org.wulfnoth.gadus.rpc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wulfnoth.gadus.rpc.kryo.KryoRpcEngine;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.WeakHashMap;

public class RPC {

    private static Logger logger = LoggerFactory.getLogger(RPC.class);

    public static class RpcServerBuilder<T> {

        private RpcServerBuilder(){}

        private T instance = null;

        private InetSocketAddress address = null;

        private Class<? extends RpcEngine> clazz = null;

        public RpcServerBuilder<T> setInstance(T instance) {
            this.instance = instance;
            return this;
        }

        public RpcServerBuilder<T> setAddress(InetSocketAddress address) {
            this.address = address;
            return this;
        }

        public RpcServerBuilder<T> setRpcEngineType(Class<? extends RpcEngine> clazz) {
            this.clazz = clazz;
            return this;
        }

        public RpcServer build() {
            if (clazz == null) clazz = KryoRpcEngine.class;
            if (instance == null) throw new IllegalStateException("未指定RpcServer使用的实例");
            if (address == null) throw new IllegalStateException("未指定RpcServer绑定的地址");

            return RPCInScala$.MODULE$.getRpcEngine(clazz).getServer(address, instance);
        }

    }



    public static <T> RpcServerBuilder getRpcServerBuilder() {
        return new RpcServerBuilder<T>();
    }

    public static <T> T getProxy(Class<T> clazz, InetSocketAddress address) {
        return null;
    }

}
