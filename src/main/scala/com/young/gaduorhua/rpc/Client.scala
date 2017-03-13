package com.young.gaduorhua.rpc

import java.net.InetSocketAddress
import java.util.concurrent.atomic.AtomicInteger

import io.netty.bootstrap.Bootstrap
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.channel.{Channel, ChannelFuture, ChannelInitializer}

import scala.language.postfixOps

/**
  * Created by Young on 16-9-1.
  */
abstract class Client(address: InetSocketAddress,
                      invoker: RpcInvocationHandler[_ <: Serializable],
                      workerThreadN: Int = 4)
  extends AutoCloseable {

  private val nextRequestId = new AtomicInteger

  private lazy val workerGroup = new NioEventLoopGroup(workerThreadN)

  lazy val client : Bootstrap = new Bootstrap() group
    workerGroup channel classOf[NioSocketChannel] handler getInitializer

  private var channelFuture : ChannelFuture = _

  private var ch : Channel = _

  def getInvoker = invoker

  def start = {
        channelFuture = client connect address sync()
        ch = channelFuture channel()
  }

  override def close(): Unit = {
    ch closeFuture() sync()
    workerGroup shutdownGracefully()
  }

  def send(msg: AnyRef) {
    ch.synchronized {
      ch writeAndFlush msg
    }
  }

  def getNextRequestId = nextRequestId.getAndIncrement

  def getInitializer : ChannelInitializer[SocketChannel]
}
