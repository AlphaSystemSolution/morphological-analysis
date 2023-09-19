package com.alphasystem
package arabic
package morphologicalengine
package server

import routes.Routes
import cats.effect.*
import cats.implicits.*
import com.comcast.ip4s.*
import fs2.io.net.Network
import org.http4s.HttpRoutes
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.implicits.*
import org.http4s.server.Router
import org.http4s.server.middleware.{ ErrorAction, ErrorHandling }
import org.typelevel.log4cats.LoggerFactory
import org.typelevel.log4cats.slf4j.Slf4jFactory

object Main extends IOApp {

  private implicit val loggerFactory: LoggerFactory[IO] = Slf4jFactory.create[IO]

  override def run(args: List[String]): IO[ExitCode] = {
    val httpApp =
      Router("/morphological-engine" -> Routes.morphologicalEngineService, "/" -> Routes.healthRoute).orNotFound

    val withErrorLogging = ErrorHandling
      .Recover
      .total(
        ErrorAction.log(
          httpApp,
          messageFailureLogAction = (t, msg) =>
            IO.println(msg) >>
              IO.println(t),
          serviceErrorLogAction = (t, msg) =>
            IO.println(msg) >>
              IO.println(t)
        )
      )

    EmberServerBuilder
      .default[IO]
      .withHost(ipv4"0.0.0.0")
      .withPort(port"8080")
      .withHttpApp(withErrorLogging)
      .build
      .use(_ => IO.never)
      .as(ExitCode.Success)
  }

}
