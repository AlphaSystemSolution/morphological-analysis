package com.alphasystem
package arabic
package morphologicalengine
package server
package routes

import com.alphasystem.arabic.morphologicalengine.generator.docx.DocumentBuilder
import com.alphasystem.arabic.morphologicalengine.generator.model.ConjugationTemplate
import io.circe.generic.auto.*
import org.apache.pekko.Done
import org.apache.pekko.http.scaladsl.model.*
import org.apache.pekko.http.scaladsl.model.StatusCodes.OK
import org.apache.pekko.http.scaladsl.model.headers.{
  CacheDirectives,
  ContentDispositionTypes,
  `Cache-Control`,
  `Content-Disposition`
}
import org.apache.pekko.http.scaladsl.server.Directives.*
import org.apache.pekko.http.scaladsl.server.{ PathMatchers, Route }
import org.apache.pekko.stream.scaladsl.FileIO
import org.slf4j.LoggerFactory

import java.nio.file.Files
import scala.concurrent.Future
import scala.util.{ Failure, Success, Try }

final class HttpRoutes extends CustomMarshallers {

  import HttpRoutes.*

  private val logger = LoggerFactory.getLogger(classOf[HttpRoutes])

  lazy val routes: Route =
    concat(
      path(PathMatchers.separateOnSlashes(HealthPath))(healthRoute),
      path(PathMatchers.separateOnSlashes(GenerateDocumentPath))(buildDocument)
    )

  private lazy val healthRoute: Route =
    get {
      onComplete(Future.successful(Done))(response("Ok"))
    }

  private lazy val buildDocument: Route =
    post {
      entity(as[ConjugationTemplate]) { conjugationTemplate =>
        val path = Files.createTempFile(conjugationTemplate.id, ".docx")
        Try(
          DocumentBuilder(
            chartConfiguration = conjugationTemplate.chartConfiguration,
            path = path,
            inputs = conjugationTemplate.inputs*
          )
            .generateDocument()
        ) match
          case Failure(ex) =>
            logger.error("Unable to process request.", ex)
            complete(GenericError("Unable to process request."))

          case Success(_) =>
            complete(
              HttpResponse(OK)
                .withEntity(
                  HttpEntity(
                    ContentType(MediaTypes.`application/vnd.openxmlformats-officedocument.wordprocessingml.document`),
                    path.toFile.length(),
                    FileIO.fromPath(path)
                  )
                )
                .withHeaders(
                  Seq(
                    `Content-Disposition`(
                      ContentDispositionTypes.attachment,
                      Map("filename" -> s"${conjugationTemplate.id}.docx")
                    ),
                    `Cache-Control`(
                      Seq(CacheDirectives.`no-cache`, CacheDirectives.`no-store`, CacheDirectives.`must-revalidate`)
                    )
                  )
                )
            )
      }
    }

  private def response(msg: String): Try[Done] => Route = {
    case Success(_) => complete(StatusCodes.OK -> msg)
    case Failure(t) => complete(StatusCodes.InternalServerError -> s"$msg failed: ${t.getMessage}")
  }
}

object HttpRoutes {

  private[routes] final case class GenericError(message: String)
  private[routes] val HealthPath = "health"
  private[routes] val GenerateDocumentPath = "morphological-chart/docx"
}
