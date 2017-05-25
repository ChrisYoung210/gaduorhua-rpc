package org.wulfnoth.gadus.rpc

import java.net.InetSocketAddress

import org.junit.{After, Before, Test}
import org.junit.Assert._

import scala.language.postfixOps
import scala.util.Random
/**
  */
class RPCTest {

	val server = RPC.getServerBuilder
		.address(new InetSocketAddress("0.0.0.0", 9999)).instance(new TestRPCImpl).build

	var startTime = 0L

	@Before
	def startServer(): Unit = {
		server.start
		startTime = System.currentTimeMillis
	}

	@Test
	def test(): Unit = {

		val testRPC = RPC.getProxy(classOf[TestRPC], new InetSocketAddress("0.0.0.0", 9999))
		0 until 1000000 foreach { x =>
			val v1 = Random.nextInt(100000000)
			val v2 = Random.nextInt(100000000)
			//println(v1 + v2)
			try {
				assertEquals(v1+v2, testRPC.add(v1, v2))
			} catch {
				case t: Throwable =>
					println(x)
					throw t
			}
		}

	}

	@After
	def close(): Unit = {
		server.close()
		println(System.currentTimeMillis - startTime)
	}

}

trait TestRPC {
	def add(a: Int, b: Int): Int
}

class TestRPCImpl extends TestRPC {
	override def add(a: Int, b: Int): Int = a+b
}