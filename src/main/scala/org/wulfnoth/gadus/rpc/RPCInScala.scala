package org.wulfnoth.gadus.rpc

import java.net.InetSocketAddress

import com.young.commons.collection.Reflections
import org.wulfnoth.gadus.rpc.kryo.KryoRpcEngine

import scala.collection.mutable._

/**
  * Created by Young on 16-8-30.
  */
private[rpc] object RPCInScala {

  private val PROTOCOL_ENGINES = new WeakHashMap[Class[_ <: RpcEngine], RpcEngine]

  private val SERVER_CACHE = new HashMap[Class[_ <: RpcEngine], Map[InetSocketAddress, RpcServer]]

  def getRpcEngine(clazz: Class[_ <: RpcEngine]) = {
    PROTOCOL_ENGINES.synchronized {
      PROTOCOL_ENGINES.getOrElseUpdate(clazz, Reflections newInstance clazz)
    }
  }

  def getServer(address: InetSocketAddress, instance: AnyRef) = {
    val maps = SERVER_CACHE getOrElseUpdate(classOf[KryoRpcEngine], new HashMap[InetSocketAddress, RpcServer])
    maps getOrElseUpdate(address, getRpcEngine(classOf[KryoRpcEngine]).getServer(address, instance))
  }

  def getProxy[T](clazz : Class[T],
                  address : InetSocketAddress,
                  timeout: Long = 10000) : T =
    getRpcEngine(classOf[KryoRpcEngine]) getProxy(clazz, address, timeout)
}
