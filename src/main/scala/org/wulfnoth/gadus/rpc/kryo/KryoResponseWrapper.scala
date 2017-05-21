package org.wulfnoth.gadus.rpc.kryo

/**
  * Created by Young on 16-9-2.
  */
class KryoResponseWrapper(response : AnyRef,
                          requestId : Long,
                          exception : java.lang.Throwable)
  extends Serializable {

  def getResponse = response

  def getRequestId = requestId

  def hasException = exception != null

  def getException = exception

}
