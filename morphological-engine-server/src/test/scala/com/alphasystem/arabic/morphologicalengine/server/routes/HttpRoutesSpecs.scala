package com.alphasystem
package arabic
package morphologicalengine
package server
package routes

import com.alphasystem.arabic.model.ArabicLetterType
import fs2.io.file.Files
import morphologicalengine.conjugation.forms.noun.VerbalNoun
import morphologicalengine.conjugation.model.{ ConjugationConfiguration, ConjugationInput, NamedTemplate, RootLetters }
import morphologicalengine.generator.model.{ ChartConfiguration, ConjugationTemplate }
import io.circe.generic.auto.*
import org.apache.pekko.http.scaladsl.model.MediaTypes
import org.apache.pekko.http.scaladsl.model.StatusCodes.OK
import org.apache.pekko.http.scaladsl.testkit.{ MarshallingTestUtils, RouteTest }

class HttpRoutesSpecs extends MunitRouteTestSupport with RouteTest with MarshallingTestUtils {

  import com.github.pjfanning.pekkohttpcirce.ErrorAccumulatingCirceSupport.*
  import HttpRoutes.*

  private lazy val routes = (new HttpRoutes()).routes

  test("health route") {
    Get(s"/$HealthPath") ~> routes ~> check {
      assertEquals(status, OK)
      assertEquals(responseAs[String], "Ok")
    }
  }

  test("build document") {
    val conjugationTemplate = ConjugationTemplate(
      id = "test",
      chartConfiguration = ChartConfiguration(),
      inputs = Seq(
        ConjugationInput(
          namedTemplate = NamedTemplate.FormICategoryAGroupUTemplate,
          conjugationConfiguration = ConjugationConfiguration(),
          rootLetters = RootLetters(
            firstRadical = ArabicLetterType.Noon,
            secondRadical = ArabicLetterType.Sad,
            thirdRadical = ArabicLetterType.Ra
          ),
          verbalNounCodes = Seq(VerbalNoun.FormIV1.code),
          translation = Some("To Help")
        )
      )
    )
    Post(s"/$GenerateDocumentPath", conjugationTemplate) ~> routes ~> check {
      assertEquals(status, OK)
      assertEquals(
        responseEntity.contentType.mediaType,
        MediaTypes.`application/vnd.openxmlformats-officedocument.wordprocessingml.document`
      )
    }
  }
}
