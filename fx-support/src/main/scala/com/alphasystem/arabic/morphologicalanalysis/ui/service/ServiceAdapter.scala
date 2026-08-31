package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package service

import javafx.application.Platform
import javafx.concurrent.{ Task, Worker, WorkerStateEvent, Service as JService }
import javafx.event.EventHandler
import scalafx.concurrent.Service

class ServiceAdapter[IN, OUT] {

  def serviceInitializer(f: IN => OUT): IN => Service[OUT] = (in: IN) =>
    new Service[OUT](
      new JService[OUT] {
        override def createTask(): Task[OUT] =
          new Task[OUT]() {
            override def call(): OUT = f(in)
          }
      }
    ) {}

  def handleResponse(
    service: Service[OUT],
    onSucceeded: EventHandler[WorkerStateEvent],
    onFailed: EventHandler[WorkerStateEvent]
  ): Unit = {
    service.onSucceeded = onSucceeded
    service.onFailed = onFailed
  }

  def start(service: Service[OUT]): Unit =
    Platform.runLater { () =>
      if service.state.value == Worker.State.SUCCEEDED then service.restart()
      else service.start()
    }
}

object ServiceAdapter {
  def apply[IN, OUT](): ServiceAdapter[IN, OUT] = new ServiceAdapter[IN, OUT]()
}
