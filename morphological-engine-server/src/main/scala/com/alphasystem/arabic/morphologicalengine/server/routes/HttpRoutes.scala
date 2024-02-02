package com.alphasystem
package arabic
package morphologicalengine
package server
package routes

import com.alphasystem.arabic.morphologicalengine.generator.docx.DocumentBuilder
import com.alphasystem.arabic.morphologicalengine.generator.model.ConjugationTemplate
import io.circe.generic.auto.*
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

import java.nio.file.{ Files, Path }
import scala.concurrent.{ ExecutionContext, Future }
import scala.util.{ Failure, Success }

final class HttpRoutes(implicit ec: ExecutionContext) extends CustomMarshallers {

  import HttpRoutes.*

  private val logger = LoggerFactory.getLogger(classOf[HttpRoutes])

  lazy val routes: Route =
    concat(
      path(PathMatchers.separateOnSlashes(HealthPath))(healthRoute),
      path(PathMatchers.separateOnSlashes(GenerateDocumentPath))(buildDocument)
    )

  private lazy val healthRoute: Route =
    get {
      complete("Ok")
    }

  private lazy val buildDocument: Route =
    post {
      entity(as[ConjugationTemplate]) { conjugationTemplate =>
        val fileName = s"${conjugationTemplate.id}.docx"
        val path = Files.createTempFile(conjugationTemplate.id, ".docx")
        val eventualResult: Future[Response] =
          Future
            .successful(
              DocumentBuilder(
                chartConfiguration = conjugationTemplate.chartConfiguration,
                path = path,
                inputs = conjugationTemplate.inputs*
              )
                .generateDocument()
            )
            .recoverWith { case ex =>
              logger.error("Unable to process request.", ex)
              Future.successful(DocumentGenerationFailed(ex.getMessage))
            }
            .map(_ => DocumentGenerationCompleted)

        onComplete(eventualResult) {
          case Failure(ex) =>
            logger.error("Unable to process request.", ex)
            complete(GenericError("Unable to process request."))
          case Success(DocumentGenerationCompleted) => complete(createResponse(fileName, path))
          case Success(response)                    => complete(response)
        }
      }
    }

  private def createResponse(fileName: String, path: Path) = {
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
            Map("filename" -> fileName)
          ),
          `Cache-Control`(
            Seq(CacheDirectives.`no-cache`, CacheDirectives.`no-store`, CacheDirectives.`must-revalidate`)
          )
        )
      )
  }
}

object HttpRoutes {

  private[routes] sealed trait Response
  private[routes] final case class GenericError(message: String) extends Response
  private[routes] final case class DocumentGenerationFailed(message: String) extends Response
  private[routes] case object DocumentGenerationCompleted extends Response

  private[routes] val HealthPath = "health"
  private[routes] val GenerateDocumentPath = "morphological-chart/docx"
}
