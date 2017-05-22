package org.wulfnoth.gadus.rpc

import java.net.InetSocketAddress

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.channel.{ChannelFuture, ChannelInitializer}

/**
  * @author young
  */
abstract class RpcServerInScala(address : InetSocketAddress,
                      workerThreadN: Int) extends RpcServer {

  private var channelFuture : ChannelFuture = _

  private lazy val bossGroup = new NioEventLoopGroup

  private lazy val workerGroup = new NioEventLoopGroup(workerThreadN)

  private val server : ServerBootstrap =
    new ServerBootstrap() group
      (bossGroup, workerGroup) channel
      classOf[NioServerSocketChannel] childHandler getInitializer

  override def start = {
    synchronized {
      channelFuture = server bind address sync()
    }
    this
  }

  override def close() = {
    synchronized {
      if (channelFuture != null) channelFuture channel() closeFuture() sync()
      bossGroup.shutdownGracefully()
      workerGroup.shutdownGracefully()
    }
  }

  def getInitializer : ChannelInitializer[SocketChannel]
}
