package org.wulfnoth.gadus.rpc.kryo

import java.io.ByteArrayOutputStream

import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryo.io.Output
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder
import org.apache.commons.pool2.impl.GenericObjectPool

import scala.language.postfixOps

/**
  * Created by Young on 16-9-4.
  */
class KryoRequestEncoder(kryoPool : GenericObjectPool[Kryo]) extends
  MessageToByteEncoder[KryoRequestWrapper]{

  override def encode(ctx: ChannelHandlerContext,
                      msg: KryoRequestWrapper,
                      out: ByteBuf) {
    val stream = new ByteArrayOutputStream
    val output = new Output(stream)
    val kryo = kryoPool borrowObject()
    kryo writeObject(output, msg)
    kryoPool returnObject kryo
    output flush()
    out writeInt stream.size
    out writeBytes stream.toByteArray
  }

}
