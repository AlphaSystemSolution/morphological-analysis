package com.alphasystem
package arabic
package cli

import arabic.model.ArabicLetterType.*
import arabic.morphologicalengine.conjugation.model.MorphologicalTermType.*
import arabic.morphologicalengine.conjugation.model.NamedTemplate.{ FormICategoryAGroupUTemplate, FormIVTemplate }
import arabic.morphologicalengine.conjugation.model.{ ConjugationConfiguration, ConjugationInput, RootLetters }
import arabic.morphologicalengine.generator.model.{ ChartConfiguration, ConjugationTemplate }
import arabic.model.{ ArabicLetterType, ArabicWord }
import arabic.utils.VerseSearch
import arabic.examples_generator.morphologicalchart.{
  ConjugationRequest,
  Conjugations,
  MorphologicalChartGenerator,
  PairedConjugation,
  PairedConjugationRequestGenerator,
  Settings,
  SingleConjugation,
  SingleConjugationRequestGenerator
}
import io.circe.generic.auto.*
import io.circe.syntax.*
import io.circe.yaml.v12.*
import io.circe.yaml.v12.syntax.*
import munit.FunSuite

import java.nio.file.Paths
import java.util.UUID

class ToolsTest extends FunSuite {

  private val attributes =
    """// THIS FILE IS AUTO-GENERATED, DO NOT EDIT
      |:encoding: utf-8
      |:lang: en
      |:last-update-label!:
      |
      |include::ref.adoc[]
      |
      |//
      |""".stripMargin

  test("testConjugationRequest".ignore) {
    val request = PairedConjugation(
      tag = "test",
      settings = Settings(
        showPronouns = Some(true),
        showNumbers = Some(true),
        showGenders = Some(true)
      ),
      right = Some(
        ConjugationRequest(
          morphologicalTermType = PastTense,
          namedTemplate = FormICategoryAGroupUTemplate,
          rootLetters = RootLetters(firstRadical = Noon, secondRadical = Sad, thirdRadical = Ra)
        )
      ),
      left = Some(
        ConjugationRequest(
          morphologicalTermType = PresentTense,
          namedTemplate = FormICategoryAGroupUTemplate,
          rootLetters = RootLetters(firstRadical = Noon, secondRadical = Sad, thirdRadical = Ra),
          verbalNouns = Some(Seq("FormIV"))
        )
      )
    )

    println(request.asJson.asYaml.spaces2)
  }

  test("testConjugations".ignore) {
    val conjugations = Conjugations(
      single = Some(
        Set(
          SingleConjugation(
            tag = "test",
            settings = Settings(
              showPronouns = Some(true),
              showNumbers = Some(true),
              showGenders = Some(true)
            ),
            request = ConjugationRequest(
              morphologicalTermType = PastTense,
              namedTemplate = FormICategoryAGroupUTemplate,
              rootLetters = RootLetters(firstRadical = Noon, secondRadical = Sad, thirdRadical = Ra)
            )
          )
        )
      ),
      paired = Some(
        Set(
          PairedConjugation(
            tag = "test",
            settings = Settings(
              showPronouns = Some(true),
              showNumbers = Some(true),
              showGenders = Some(true)
            ),
            right = Some(
              ConjugationRequest(
                morphologicalTermType = PastTense,
                namedTemplate = FormICategoryAGroupUTemplate,
                rootLetters = RootLetters(firstRadical = Noon, secondRadical = Sad, thirdRadical = Ra)
              )
            ),
            left = Some(
              ConjugationRequest(
                morphologicalTermType = PresentTense,
                namedTemplate = FormICategoryAGroupUTemplate,
                rootLetters = RootLetters(firstRadical = Noon, secondRadical = Sad, thirdRadical = Ra),
                verbalNouns = Some(Seq("FormIV"))
              )
            )
          )
        )
      ),
      full = Some(
        Set(
          ConjugationTemplate(
            id = "test",
            chartConfiguration = ChartConfiguration(),
            inputs = Seq(
              ConjugationInput(
                id = UUID.randomUUID(),
                namedTemplate = FormIVTemplate,
                conjugationConfiguration = ConjugationConfiguration(),
                rootLetters = RootLetters(firstRadical = Seen, secondRadical = Lam, thirdRadical = Meem),
                translation = Some("To submit"),
                verbalNounCodes = Seq.empty
              )
            )
          )
        )
      )
    )

    println(conjugations.asJson.asYaml.spaces2)
  }

  test("testConjugationTemplate".ignore) {
    val template = ConjugationTemplate(
      id = "test",
      chartConfiguration = ChartConfiguration(),
      inputs = Seq(
        ConjugationInput(
          id = UUID.randomUUID(),
          namedTemplate = FormIVTemplate,
          conjugationConfiguration = ConjugationConfiguration(),
          rootLetters = RootLetters(firstRadical = Seen, secondRadical = Lam, thirdRadical = Meem),
          translation = Some("To submit"),
          verbalNounCodes = Seq.empty
        )
      )
    )

    println(template.asJson.asYaml.spaces2)
  }

  test("singleConjugationTest".ignore) {
    SingleConjugationRequestGenerator
      .buildDocument(
        Paths.get("/Users/sfali/Documents/Arabic/GrammarLessons/test/conjugations.yml"),
        Paths.get("/Users/sfali/Documents/Arabic/GrammarLessons/test/conjugations.adoc"),
        attributes
      )
  }

  test("pairedConjugationTest".ignore) {
    PairedConjugationRequestGenerator
      .buildDocument(
        Paths.get("/Users/sfali/Documents/Arabic/GrammarLessons/test/paired_conjugations.yml"),
        Paths.get("/Users/sfali/Documents/Arabic/GrammarLessons/test/paired_conjugations.adoc"),
        attributes
      )
  }

  test("morphologicalChartTest".ignore) {
    MorphologicalChartGenerator
      .buildDocument(
        Paths.get("/Users/sfali/Documents/Arabic/GrammarLessons/conjugations/full_chart.yml"),
        Paths.get("/Users/sfali/Documents/Arabic/GrammarLessons/conjugations/full_chart.adoc"),
        attributes
      )
  }

  test("search".ignore) {
    val verseSearch = new VerseSearch()
    val text = verseSearch.searchVerse(48, 29, Some(1), Some(3))
    println(text)
    println("_" * 25)
    println()
    verseSearch
      .getChapters
      .foreach(chapterInfo =>
        println(s"${chapterInfo.chapterName} (${chapterInfo.chapterNumber}) - ${chapterInfo.verseCount}")
      )
  }

  private def printHtmlCode(aw: ArabicWord): Unit = println(aw.htmlCode)
}
