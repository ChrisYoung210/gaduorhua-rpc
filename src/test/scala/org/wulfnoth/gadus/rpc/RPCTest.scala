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

	val count = 1

	var startTime = 0L

	@Before
	def startServer(): Unit = {
		server.start
		startTime = System.currentTimeMillis
	}

	@Test
	def test(): Unit = {

		val testRPC = RPC.getProxy(classOf[TestRPC], new InetSocketAddress("0.0.0.0", 9999))
		0 until count foreach { x =>
			val arr = Array(1,2,3,4,5,6,7,8,9)
			/*val v1 = Random.nextInt(100000000)
			val v2 = Random.nextInt(100000000)*/
			//println(v1 + v2)
			try {
				assertEquals(testRPC.rd(arr), 45)
				//assertEquals(v1+v2, testRPC.add(v1, v2))
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
		val duration = (System.currentTimeMillis - startTime)/1000.0
		printf("总耗时%.2f秒\n平均%.2f条/秒\n", duration, count/duration)
		//println(String.format("总耗时%.2f\n平均%.2f条/秒", duration, count/duration))
		//println(s"总耗时${duration}s\n平均${count/duration}条/秒")
	}

}

trait TestRPC {
	def rd(list: Array[Int]): Int
}

class TestRPCImpl extends TestRPC {

	def rd(list: Array[Int]): Int = list.sum

}