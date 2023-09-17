package com.alphasystem
package arabic
package morphologicalengine
package generator
package docx

import arabic.model.ArabicLetterType
import morphologicalengine.conjugation.forms.noun.VerbalNoun
import morphologicalengine.conjugation.model.{ ConjugationConfiguration, ConjugationInput }
import generator.model.{ ChartConfiguration, DocumentFormat }
import morphologicalengine.conjugation.model.{ NamedTemplate, OutputFormat, RootLetters }

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
      )
    )
    buildDocument(inputs, "classic.docx", removeAdverbs = false)
    buildDocument(
      inputs,
      "abbreviated.docx",
      ChartConfiguration(format = DocumentFormat.AbbreviateConjugationSingleRow),
      removeAdverbs = true
    )
  }

  private def buildDocument(
    inputs: Seq[ConjugationInput],
    fileName: String,
    chartConfiguration: ChartConfiguration = ChartConfiguration(),
    removeAdverbs: Boolean
  ): Unit = {
    val builder = DocumentBuilder(
      chartConfiguration,
      removeAdverbs,
      OutputFormat.Unicode,
      Paths.get("target", fileName),
      inputs*
    )

    builder.generateDocument()
  }
}
