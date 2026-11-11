package com.alphasystem
package arabic
package cli

import arabic.model.ArabicLetterType.*
import arabic.morphologicalengine.conjugation.model.MorphologicalTermType.*
import arabic.morphologicalengine.conjugation.model.NamedTemplate.{ FormICategoryAGroupUTemplate, FormIVTemplate }
import arabic.morphologicalengine.conjugation.model.{ ConjugationInput, RootLetters }
import arabic.morphologicalengine.generator.model.ConjugationTemplate
import arabic.model.ArabicWord
import arabic.utils.VerseSearch
import arabic.examples_generator.morphologicalchart.{
  ConjugationRequest,
  Conjugations,
  PairedConjugation,
  Settings,
  SingleConjugation,
  fromConjugations
}
import munit.FunSuite

class ToolsTest extends FunSuite {

  test("testConjugations") {
    val conjugations = Conjugations(
      single = Some(
        Seq(
          SingleConjugation(
            tag = "test",
            settings = Settings(
              showPronouns = Some(true),
              showNumbers = Some(true),
              showGenders = Some(true)
            ),
            request = ConjugationRequest(
              morphologicalTermType = PastTense,
              conjugationInput = ConjugationInput(
                namedTemplate = FormICategoryAGroupUTemplate,
                rootLetters = RootLetters(firstRadical = Noon, secondRadical = Sad, thirdRadical = Ra)
              )
            )
          )
        )
      ),
      paired = Some(
        Seq(
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
                conjugationInput = ConjugationInput(
                  namedTemplate = FormICategoryAGroupUTemplate,
                  rootLetters = RootLetters(firstRadical = Noon, secondRadical = Sad, thirdRadical = Ra)
                )
              )
            ),
            left = Some(
              ConjugationRequest(
                morphologicalTermType = PresentTense,
                conjugationInput = ConjugationInput(
                  namedTemplate = FormICategoryAGroupUTemplate,
                  rootLetters = RootLetters(firstRadical = Noon, secondRadical = Sad, thirdRadical = Ra),
                  verbalNounCodes = Seq("FormIV")
                )
              )
            )
          )
        )
      ),
      full = Some(
        Seq(
          ConjugationTemplate(
            tag = "test",
            inputs = Seq(
              ConjugationInput(
                namedTemplate = FormIVTemplate,
                rootLetters = RootLetters(firstRadical = Seen, secondRadical = Lam, thirdRadical = Meem),
                translation = Some("To submit"),
                verbalNounCodes = Seq.empty
              )
            )
          )
        )
      )
    )

    println(fromConjugations(conjugations))
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
