package com.alphasystem
package arabic
package morphologicalengine
package server
package routes

import cats.effect.*
import morphologicalengine.generator.docx.DocumentBuilder
import morphologicalengine.generator.model.ConjugationTemplate
import fs2.*
import fs2.io.file.{ Path, Files as Fs2Files }
import org.http4s.*
import org.http4s.CacheDirective.*
import org.http4s.dsl.io.*
import org.http4s.headers.*
import org.typelevel.ci.CIString

import java.nio.file.Files

object Routes {

  lazy val healthRoute: HttpRoutes[IO] = HttpRoutes.of[IO] { case GET -> Root / "health" =>
    Ok()
  }

  lazy val morphologicalEngineService: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case req @ POST -> Root / "morphological-chart" / "docx" =>
      req.as[ConjugationTemplate].flatMap { conjugationTemplate =>
        val path = Files.createTempFile(conjugationTemplate.id, ".docx")
        DocumentBuilder(
          chartConfiguration = conjugationTemplate.chartConfiguration,
          path = path,
          inputs = conjugationTemplate.inputs*
        )
          .generateDocument()

        Ok(Fs2Files[IO].readAll(Path.fromNioPath(path)))
          .map(
            _.putHeaders(
              `Content-Disposition`(
                dispositionType = "attachment",
                parameters = Map(CIString("filename") -> s"${conjugationTemplate.id}.docx")
              ),
              `Cache-Control`(head = `no-cache`(), tail = `no-store`, `must-revalidate`),
              `Access-Control-Allow-Methods`(Set(Method.POST)),
              `Access-Control-Allow-Headers`(head = CIString("Content-Type"), tail = Seq.empty[CIString]: _*),
              `Content-Type`(mediaType = MediaType.forExtension("docx").getOrElse(MediaType.`text/event-stream`)),
              `Content-Length`(path.toFile.length())
            )
          )
      }
  }

}
