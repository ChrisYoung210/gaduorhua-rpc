package org.wulfnoth.gadus.rpc

import java.net.InetSocketAddress

/**
  */
trait RPCEngine {

	/**
	  * 获取一个RPC客户端
	  * @param clazz	协议类
	  * @param address	RPC服务端地址
	  * @param timeout	timeout
	  * @tparam T	协议类型
	  * @return		RPC客户端实例
	  */
	def getProxy[T](clazz: Class[T], address: InetSocketAddress, timeout: Long): T

	/**
	  * 获取一个RPC服务端
	  * @param address 绑定的本地地址
	  * @param instance RPC服务调用的实例
	  * @param workerThreadNum RPC服务端工作的线程数
	  * @return		RPC服务端实例
	  */
	def getServer(address: InetSocketAddress, instance: Any, workerThreadNum: Int): RPCServer

}
