package org.wulfnoth.gadus.rpc.kryo

import io.netty.channel.{ChannelHandlerContext, SimpleChannelInboundHandler}
import org.wulfnoth.gadus.rpc.RpcInvocationHandler

/**
  * Created by cloud on 16-9-4.
  */
class KryoRpcClientHandler(invoker : RpcInvocationHandler[KryoResponseWrapper])
	extends SimpleChannelInboundHandler[KryoResponseWrapper]{

	override def channelRead0(ctx: ChannelHandlerContext,
							  msg: KryoResponseWrapper) {
		KryoRpcEngine.logger debug s"Received ${msg.requestId} ${System.currentTimeMillis()}"
		invoker putResponse msg
	}
}
