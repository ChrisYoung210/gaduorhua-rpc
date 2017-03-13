package com.young.gaduorhua.rpc.kryo

import com.young.gaduorhua.rpc.RpcInvocationHandler
import io.netty.channel.{ChannelHandlerContext, SimpleChannelInboundHandler}

/**
  * Created by cloud on 16-9-4.
  */
class KryoRpcClientHandler(invoker : RpcInvocationHandler[KryoResponseWrapper]) extends
  SimpleChannelInboundHandler[KryoResponseWrapper]{
  override def channelRead0(ctx: ChannelHandlerContext,
                            msg: KryoResponseWrapper) {
    invoker putResponse msg
  }
}
