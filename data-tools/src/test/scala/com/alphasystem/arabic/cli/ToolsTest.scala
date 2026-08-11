package com.alphasystem
package arabic
package cli

import arabic.cli.morphologicalengine.{ ConjugationRequest, PairedConjugation, Settings }
import arabic.model.ArabicLetterType.*
import arabic.morphologicalengine.conjugation.model.MorphologicalTermType.*
import arabic.morphologicalengine.conjugation.model.NamedTemplate.{ FormICategoryAGroupUTemplate, FormIVTemplate }
import arabic.morphologicalengine.conjugation.model.{ ConjugationConfiguration, ConjugationInput, RootLetters }
import arabic.morphologicalengine.generator.model.{ ChartConfiguration, ConjugationTemplate }
import arabic.cli.examples.{ *, given }
import arabic.cli.morphologicalengine.generator.{
  MorphologicalChartGenerator,
  PairedConjugationRequestGenerator,
  SingleConjugationRequestGenerator
}
import com.alphasystem.arabic.cli.vocabulary.WordGenerator
import com.alphasystem.arabic.model.DiacriticType.Kasra
import com.alphasystem.arabic.model.{ ArabicLetter, ArabicLetterType, ArabicWord }
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

  test("testFindWordsByTranslation".ignore) {
    val generator = new WordGenerator(Paths.get("/Users/sfali/Documents/Arabic/vocab-data"))
    val words = generator.findWordsByTranslationFlat("HELP")
    println(words.asJson.spaces2)
  }

  test("testConjugationRequest") {
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

  test("testConjugationTemplate") {
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

  test("examplesTest".ignore) {
    val exampleRequest = ExampleRequest(
      Seq(
        TableRequest(
          tag = "ExampleTag",
          columns = "^.^6,^.^6,^.^2",
          rows = Seq(
            RowRequest(
              Seq(
                Input(
                  `type` = ColumnType.Translation,
                  text = Some(
                    Verse(
                      verseNumber = 3,
                      text = "{nbsp}##Surely Allah## knows best what is ˹hidden˺ in the heart. (3:119)"
                    )
                  )
                ),
                Input(
                  `type` = ColumnType.Arabic,
                  searchRequest = Some(
                    SearchRequest(
                      chapterNumber = 3,
                      verses = Seq(
                        VerseRequest(verseNumber = 119, tokenStart = Some(26))
                      )
                    )
                  ),
                  highlights = Some(
                    Seq(
                      VerseRange(
                        verseNumber = 119,
                        tokenRange = Some(
                          List(Highlight(tokenStart = Token(1), tokenEnd = Token(2)))
                        )
                      )
                    )
                  )
                )
              )
            )
          )
        )
      )
    )

    println(Seq(exampleRequest).asJson.asYaml.spaces2)
  }

  test("search".ignore) {
    val verseSearch = new VerseSearch()
    val text = verseSearch.searchVerse(48, 29, Some(1), Some(3))
    println(text)
    println("_" * 25)
    println()
  }

  private def printHtmlCode(aw: ArabicWord): Unit = println(s"${aw.unicode}: ${aw.htmlCode}")
}
