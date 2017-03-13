package com.young.gaduorhua.rpc

import java.net.InetSocketAddress

import org.junit.{Assert, Before, Test}

import scala.language.postfixOps
import scala.util.Random

/**
  * @author young
  */
class RPCTest {

  var server : Server = _
  var cal : Protocol = _
  @Before
  def startServer {
    server = RPC.getServer(8889, "localhost")
    server start;
    server.addProtocolAndInstance(classOf[Protocol], new ProtocolImpl)
    cal = RPC.getProxy(classOf[Protocol], new InetSocketAddress("localhost", 8889))
  }

  @Test
  def calculate {
    var v1 = 1
    var v2 = 2
    0 until 100 foreach (

      x => {
        v1 = Random.nextInt()/2
        v2 = Random.nextInt()/2
        Assert.assertEquals(cal add(v1, v2), v1+v2)
    })
  }
}

trait Protocol {
  def add(value1 : Int, value2 : Int) : Int
}

class ProtocolImpl extends Protocol{
  override def add(value1: Int, value2: Int): Int = value1 + value2
}