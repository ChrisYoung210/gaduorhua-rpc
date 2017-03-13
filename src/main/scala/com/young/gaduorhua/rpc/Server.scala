package com.young.gaduorhua.rpc

import java.net.InetSocketAddress

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.channel.{ChannelFuture, ChannelInitializer}

/**
  * @author young
  */
abstract class Server(address : InetSocketAddress,
                      workerThreadN: Int = 8) extends AutoCloseable {

  //def this(address: InetSocketAddress) = this(address, 8)

  private var channelFuture : ChannelFuture = _

  private lazy val bossGroup = new NioEventLoopGroup

  private lazy val workerGroup = new NioEventLoopGroup(workerThreadN)

  private val server : ServerBootstrap =
    new ServerBootstrap() group
      (bossGroup, workerGroup) channel
      classOf[NioServerSocketChannel] childHandler getInitializer

  def start = {
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

  def addProtocolAndInstance[T <: AnyRef](clazz : Class[T], instance : T) : Boolean

  def getInitializer : ChannelInitializer[SocketChannel]
}
