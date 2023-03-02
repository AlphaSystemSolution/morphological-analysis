package com.alphasystem
package arabic
package morphologicalengine
package generator
package docx

import arabic.model.ArabicLetterType
import morphologicalengine.conjugation.forms.noun.VerbalNoun
import generator.model.{ ChartConfiguration, ConjugationConfiguration, ConjugationInput, DocumentFormat }
import morphologicalengine.conjugation.model.{ NamedTemplate, OutputFormat }

import java.nio.file.Paths

object GeneratorTest {

  def main(args: Array[String]): Unit = {
    val conjugationConfiguration = ConjugationConfiguration(removeAdverbs = true)
    val inputs = Seq(
      ConjugationInput(
        namedTemplate = NamedTemplate.FormICategoryAGroupUTemplate,
        conjugationConfiguration = conjugationConfiguration,
        firstRadical = ArabicLetterType.Noon,
        secondRadical = ArabicLetterType.Sad,
        thirdRadical = ArabicLetterType.Ra,
        verbalNounCodes = Seq(VerbalNoun.FormIV1.code),
        translation = Some("To Help")
      ),
      ConjugationInput(
        namedTemplate = NamedTemplate.FormIITemplate,
        conjugationConfiguration = conjugationConfiguration,
        firstRadical = ArabicLetterType.Ain,
        secondRadical = ArabicLetterType.Lam,
        thirdRadical = ArabicLetterType.Meem,
        translation = Some("To Teach")
      ),
      ConjugationInput(
        namedTemplate = NamedTemplate.FormIVTemplate,
        conjugationConfiguration = conjugationConfiguration,
        firstRadical = ArabicLetterType.Seen,
        secondRadical = ArabicLetterType.Lam,
        thirdRadical = ArabicLetterType.Meem,
        translation = Some("To Submit")
      )
    )
    buildDocument(inputs, "classic.docx")
    buildDocument(
      inputs,
      "abbreviated.docx",
      ChartConfiguration(format = DocumentFormat.AbbreviateConjugationSingleRow)
    )
  }

  private def buildDocument(
    inputs: Seq[ConjugationInput],
    fileName: String,
    chartConfiguration: ChartConfiguration = ChartConfiguration()
  ): Unit = {
    val builder = DocumentBuilder(
      chartConfiguration,
      OutputFormat.Unicode,
      Paths.get("target", fileName),
      inputs*
    )

    builder.generateDocument()
  }
}
