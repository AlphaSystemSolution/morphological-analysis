package com.alphasystem
package arabic
package morphologicalengine
package generator
package docx

//com.alphasystem.arabic.morphologicalengine.generator.docx.GeneratorTest

import com.alphasystem.arabic.model.ArabicLetterType
import com.alphasystem.arabic.morphologicalengine.conjugation.forms.noun.VerbalNoun
import com.alphasystem.arabic.morphologicalengine.conjugation.model.{
  ConjugationConfiguration,
  ConjugationInput,
  NamedTemplate,
  RootLetters
}
import com.alphasystem.arabic.morphologicalengine.generator.model.{ ChartConfiguration, DocumentFormat }

import java.nio.file.Paths

object GeneratorTest {

  def main(args: Array[String]): Unit = {
    val conjugationConfiguration = ConjugationConfiguration()
    val inputs = Seq(
      ConjugationInput(
        namedTemplate = NamedTemplate.FormICategoryAGroupUTemplate,
        conjugationConfiguration = conjugationConfiguration,
        rootLetters = RootLetters(
          firstRadical = ArabicLetterType.Noon,
          secondRadical = ArabicLetterType.Sad,
          thirdRadical = ArabicLetterType.Ra
        ),
        verbalNounCodes = Seq(VerbalNoun.FormIV1.code),
        translation = Some("To Help")
      ),
      ConjugationInput(
        namedTemplate = NamedTemplate.FormIITemplate,
        conjugationConfiguration = conjugationConfiguration,
        rootLetters = RootLetters(
          firstRadical = ArabicLetterType.Ain,
          secondRadical = ArabicLetterType.Lam,
          thirdRadical = ArabicLetterType.Meem
        ),
        translation = Some("To Teach")
      ),
      ConjugationInput(
        namedTemplate = NamedTemplate.FormIVTemplate,
        conjugationConfiguration = conjugationConfiguration,
        rootLetters = RootLetters(
          firstRadical = ArabicLetterType.Seen,
          secondRadical = ArabicLetterType.Lam,
          thirdRadical = ArabicLetterType.Meem
        ),
        translation = Some("To Submit")
      ),
      ConjugationInput(
        namedTemplate = NamedTemplate.FormICategoryAGroupUTemplate,
        conjugationConfiguration = conjugationConfiguration,
        rootLetters = RootLetters(
          firstRadical = ArabicLetterType.Dal,
          secondRadical = ArabicLetterType.Ain,
          thirdRadical = ArabicLetterType.Waw
        ),
        translation = Some("To Submit")
      ),
      ConjugationInput(
        namedTemplate = NamedTemplate.FormICategoryIGroupATemplate,
        conjugationConfiguration = conjugationConfiguration,
        rootLetters = RootLetters(
          firstRadical = ArabicLetterType.Hha,
          secondRadical = ArabicLetterType.Meem,
          thirdRadical = ArabicLetterType.Dal
        ),
        translation = Some("To Praise")
      ),
      ConjugationInput(
        namedTemplate = NamedTemplate.FormICategoryUTemplate,
        conjugationConfiguration = conjugationConfiguration,
        rootLetters = RootLetters(
          firstRadical = ArabicLetterType.Kaf,
          secondRadical = ArabicLetterType.Ra,
          thirdRadical = ArabicLetterType.Meem
        ),
        translation = Some("To be noble")
      ),
      ConjugationInput(
        namedTemplate = NamedTemplate.FormIVTemplate,
        conjugationConfiguration = conjugationConfiguration,
        rootLetters = RootLetters(
          firstRadical = ArabicLetterType.Kaf,
          secondRadical = ArabicLetterType.Ra,
          thirdRadical = ArabicLetterType.Meem
        ),
        translation = Some("To call noble")
      )
    )
    buildDocument(inputs, "classic.docx")
    buildDocument(
      inputs,
      "abbreviated.docx",
      ChartConfiguration(format = DocumentFormat.AbbreviateConjugationSingleRow, removeAdverbs = true)
    )
  }

  private def buildDocument(
    inputs: Seq[ConjugationInput],
    fileName: String,
    chartConfiguration: ChartConfiguration = ChartConfiguration()
  ): Unit = {
    val builder = DocumentBuilder(
      chartConfiguration,
      Paths.get("target", fileName),
      inputs*
    )

    builder.generateDocument()
  }
}
