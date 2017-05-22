package org.wulfnoth.gadus.rpc

import java.net.InetSocketAddress

import net.vidageek.mirror.dsl.Mirror
import org.wulfnoth.gadus.rpc.kryo.KryoRpcEngine

import scala.collection.mutable._

/**
  * Created by Young on 16-8-30.
  */
private[rpc] object RPCInScala {

	private val PROTOCOL_ENGINES = new WeakHashMap[Class[_ <: RpcEngine], RpcEngine]

	private val SERVER_CACHE = new HashMap[Class[_ <: RpcEngine], Map[InetSocketAddress, RpcServer]]

	def getRpcEngine(clazz: Class[_ <: RpcEngine]) =
		PROTOCOL_ENGINES.synchronized {
			PROTOCOL_ENGINES.getOrElseUpdate(clazz,
			new Mirror().on(clazz).reflect.constructor.withoutArgs.newInstance())
		}

  def getServer(address: InetSocketAddress, instance: AnyRef, workerThreadN: Int) = {
    val maps = SERVER_CACHE getOrElseUpdate(classOf[KryoRpcEngine], new HashMap[InetSocketAddress, RpcServer])
    maps getOrElseUpdate(address, getRpcEngine(classOf[KryoRpcEngine]).getServer(address, instance, workerThreadN))
  }

  def getProxy[T](clazz : Class[T],
                  address : InetSocketAddress,
                  timeout: Long = 10000) : T =
    getRpcEngine(classOf[KryoRpcEngine]) getProxy(clazz, address, timeout)
}
