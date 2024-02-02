package com.alphasystem
package arabic
package morphologicalengine
package server

import org.apache.pekko.actor.typed.ActorSystem
import org.apache.pekko.actor.typed.scaladsl.Behaviors

object Main {

  def main(args: Array[String]): Unit = {
    implicit val system: ActorSystem[Nothing] = ActorSystem[Nothing](Behaviors.empty, "MorphologicalEngineSystem")

    HttpServer().run()
  }
}
