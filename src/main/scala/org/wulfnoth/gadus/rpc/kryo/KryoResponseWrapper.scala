package org.wulfnoth.gadus.rpc.kryo

/**
  * Created by Young on 16-9-2.
  */
case class KryoResponseWrapper(response : AnyRef,
                          requestId : Long,
                          throwable: Throwable)
  extends Serializable
