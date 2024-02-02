package com.alphasystem
package arabic
package morphologicalengine
package server
package routes

import com.alphasystem.arabic.morphologicalengine.server.routes.HttpRoutes.*
import com.github.pjfanning.pekkohttpcirce.ErrorAccumulatingCirceSupport
import io.circe.generic.auto.*
import org.apache.pekko.http.scaladsl.marshalling.Marshaller.fromToEntityMarshaller
import org.apache.pekko.http.scaladsl.marshalling.ToResponseMarshaller
import org.apache.pekko.http.scaladsl.model.StatusCodes.InternalServerError

trait CustomMarshallers extends ErrorAccumulatingCirceSupport {

  implicit val GenericErrorResponseMarshaller: ToResponseMarshaller[GenericError] =
    fromToEntityMarshaller[GenericError](InternalServerError)
}
