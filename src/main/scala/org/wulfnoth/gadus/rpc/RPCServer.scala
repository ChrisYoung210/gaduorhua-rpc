package org.wulfnoth.gadus.rpc

import java.io.Closeable
import java.net.InetSocketAddress

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.channel.{ChannelFuture, ChannelInitializer}

import scala.language.postfixOps

/**
  * @author young
  */
abstract class RPCServer(address : InetSocketAddress,
                         workerThreadNumb: Int) extends Closeable {
	private var channelFuture : ChannelFuture = _

	private lazy val bossGroup = new NioEventLoopGroup

	private lazy val workerGroup = new NioEventLoopGroup(workerThreadNumb)

	private val server : ServerBootstrap =
		new ServerBootstrap() group
		(bossGroup, workerGroup) channel
		classOf[NioServerSocketChannel] childHandler getInitializer

	def start = {
		synchronized {
			channelFuture = server bind address
		}
		this
	}

	override def close(): Unit = {
		synchronized {
			if (channelFuture != null)
				channelFuture.channel.closeFuture
			bossGroup.shutdownGracefully()
			workerGroup.shutdownGracefully()
		}
	}

	def getInitializer : ChannelInitializer[SocketChannel]
}
