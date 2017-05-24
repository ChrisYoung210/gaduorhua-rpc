package org.wulfnoth.gadus.rpc

import java.net.InetSocketAddress

import scala.util.Random

/**
  * @author Young
  */
object Main {

  def help: Unit = {
    println(
      """使用方法：
        |server ip port
        |client ServerIP ServerPort count
        |
      """.stripMargin)
  }

  def main(args: Array[String]): Unit = {

    if (args.length < 3) {
      help
      System.exit(-1)
    } else if (args(0) == "server") {
      RPC.getServerBuilder.
        instance(new TestRPCImpl).
        address(new InetSocketAddress(args(1), args(2).toInt)).build.start
    } else if (args(0) == "client") {
      val testRPC = RPC.getProxy(classOf[TestRPC],
        new InetSocketAddress(args(1), args(2).toInt))
      var v1 = 0
      var v2 = 0
      0 until args(3).toInt foreach { x =>
        v1 = Random.nextInt(10000000)
        v2 = Random.nextInt(10000000)

      }
    }
  }

}


trait TestRPC {
  def add(a: Int, b: Int): Int
}

class TestRPCImpl extends TestRPC {
  override def add(a: Int, b: Int): Int = a+b
}
