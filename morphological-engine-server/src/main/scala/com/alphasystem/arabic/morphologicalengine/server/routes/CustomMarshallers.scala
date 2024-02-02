package com.alphasystem
package arabic
package morphologicalengine
package server
package routes

import morphologicalengine.server.routes.HttpRoutes.*
import com.github.pjfanning.pekkohttpcirce.ErrorAccumulatingCirceSupport
import io.circe.generic.auto.*
import org.apache.pekko.http.scaladsl.marshalling.Marshaller.fromToEntityMarshaller
import org.apache.pekko.http.scaladsl.marshalling.ToResponseMarshaller
import org.apache.pekko.http.scaladsl.model.StatusCodes.{ InternalServerError, OK, ServiceUnavailable }

trait CustomMarshallers extends ErrorAccumulatingCirceSupport {

  implicit val GenericErrorResponseMarshaller: ToResponseMarshaller[GenericError] =
    fromToEntityMarshaller[GenericError](InternalServerError)

  implicit val DocumentGenerationFailedResponseMarshaller: ToResponseMarshaller[DocumentGenerationFailed] =
    fromToEntityMarshaller[DocumentGenerationFailed](ServiceUnavailable)
}
