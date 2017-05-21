package org.wulfnoth.gadus.rpc.kryo

import java.util

import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryo.io.Input
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageDecoder
import org.apache.commons.pool2.impl.GenericObjectPool

/**
  * Created by Young on 16-9-2.
  */
class KryoRequestDecoder(kryoPool : GenericObjectPool[Kryo]) extends ByteToMessageDecoder {
  override def decode(ctx: ChannelHandlerContext,
                      in: ByteBuf,
                      out: util.List[AnyRef]) {
    //KryoRpcEngine.logger debug "Try to decode a msg from " + ctx.channel.remoteAddress()
    if (in.readableBytes >= 4) {
      val length = in getInt(in readerIndex())
      if (in.readableBytes >= 4+length) {
        in readInt()
        val buf = new Array[Byte](length)
        in readBytes buf
        val input = new Input(buf)
        val kryo = kryoPool borrowObject()
        val requestWrapper = kryo readObject(input, classOf[KryoRequestWrapper])
        kryoPool returnObject kryo
        out add requestWrapper
      }
    }
  }
}
