package com.young.gaduorhua.rpc

import java.lang.reflect.InvocationHandler

/**
  * Created by Young on 16-9-2.
  */
trait RpcInvocationHandler[T <: Serializable] extends InvocationHandler {
  def putResponse(response : T)
}
