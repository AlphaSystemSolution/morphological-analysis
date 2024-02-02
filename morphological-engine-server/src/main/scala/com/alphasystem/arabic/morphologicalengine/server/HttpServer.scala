package com.alphasystem
package arabic
package morphologicalengine
package server

import com.alphasystem.arabic.morphologicalengine.server.routes.HttpRoutes
import org.apache.pekko.actor.typed.ActorSystem
import org.apache.pekko.http.scaladsl.Http
import org.apache.pekko.http.scaladsl.server.Directives.pathPrefix
import org.apache.pekko.http.scaladsl.server.Route

import scala.util.{ Failure, Success }

class HttpServer(implicit system: ActorSystem[?]) {

  import system.executionContext
  private val httpRoutes = new HttpRoutes()

  def run(): Unit = startHttpServer(pathPrefix("morphological-engine")(httpRoutes.routes))

  private def startHttpServer(routes: Route): Unit = {
    import system.executionContext

    val futureBinding = Http().newServerAt("0.0.0.0", 8080).bind(routes)
    futureBinding.onComplete {
      case Success(binding) =>
        val address = binding.localAddress
        system.log.info("Server online at http://{}:{}/", address.getHostString, address.getPort)
      case Failure(ex) =>
        system.log.error("Failed to bind HTTP endpoint, terminating system", ex)
        system.terminate()
    }
  }
}

object HttpServer {
  def apply()(implicit system: ActorSystem[?]) = new HttpServer()
}
