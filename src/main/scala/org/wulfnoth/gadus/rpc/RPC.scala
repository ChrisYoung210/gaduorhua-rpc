package org.wulfnoth.gadus.rpc

import java.net.InetSocketAddress

import net.vidageek.mirror.dsl.Mirror
import org.wulfnoth.gadus.rpc.kryo.KryoRpcEngine

import scala.collection.mutable._

/**
  * Created by Young on 16-8-30.
  */
object RPC {

	class RPCServerBuilder private[rpc]() {
		private var rpcEngine: Class[_ <: RPCEngine] = _
		private var workerThreadNum = -1
		private var instance: Any = _
		private var address: InetSocketAddress = _

		def rpcEngine(clazz: Class[_ <: RPCEngine]): RPCServerBuilder = {
			this.rpcEngine = clazz
			this
		}

		def workerThreadNum(workerThreadNum: Int): RPCServerBuilder = {
			this.workerThreadNum = workerThreadNum
			this
		}

		def instance(instance: Any): RPCServerBuilder = {
			this.instance = instance
			this
		}

		def address(address: InetSocketAddress): RPCServerBuilder = {
			this.address = address
			this
		}

		def build: RPCServer = {
			if (rpcEngine == null) rpcEngine = classOf[KryoRpcEngine]
			if (workerThreadNum < 1) workerThreadNum = 8
			if (instance == null) throw new IllegalStateException("未指定RpcServer使用的实例")
			if (address == null) throw new IllegalStateException("未指定RpcServer绑定的地址")

			getRpcEngine(rpcEngine).getServer(address, instance, workerThreadNum)
		}

	}

	private val PROTOCOL_ENGINES = new WeakHashMap[Class[_ <: RPCEngine], RPCEngine]

	private val SERVER_CACHE = new HashMap[Class[_ <: RPCEngine], Map[InetSocketAddress, RPCServer]]

	private def getRpcEngine(clazz: Class[_ <: RPCEngine]) =
		PROTOCOL_ENGINES.synchronized {
			PROTOCOL_ENGINES.getOrElseUpdate(clazz,
			new Mirror().on(clazz).reflect.constructor.withoutArgs.newInstance())
		}

	def getServerBuilder = new RPCServerBuilder

	def getProxy[T](clazz : Class[T],
					address : InetSocketAddress,
					timeout: Long = 10000) : T =
		getRpcEngine(classOf[KryoRpcEngine]) getProxy(clazz, address, timeout)
}
