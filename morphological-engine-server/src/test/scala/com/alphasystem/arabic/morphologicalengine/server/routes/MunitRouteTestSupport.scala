package com.alphasystem
package arabic
package morphologicalengine
package server
package routes

import munit.FunSuite
import org.apache.pekko.http.scaladsl.server.ExceptionHandler
import org.apache.pekko.http.scaladsl.testkit.TestFrameworkInterface

trait MunitRouteTestSupport extends FunSuite with TestFrameworkInterface {

  override def failTest(msg: String): Nothing = fail(msg)

  override val testExceptionHandler: ExceptionHandler =
    ExceptionHandler { case e: java.lang.AssertionError =>
      throw e
    }

  override def afterAll(): Unit = {
    cleanUp()
    super.afterAll()
  }
}
