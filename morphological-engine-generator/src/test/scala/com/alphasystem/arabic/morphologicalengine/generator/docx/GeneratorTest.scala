package com.alphasystem
package arabic
package morphologicalengine
package generator
package docx

import arabic.model.ArabicLetterType
import morphologicalengine.conjugation.forms.noun.VerbalNoun
import generator.model.{ ChartConfiguration, ConjugationInput, DocumentFormat }
import morphologicalengine.conjugation.model.{ ConjugationConfiguration, NamedTemplate, OutputFormat }

import java.nio.file.Paths

object GeneratorTest {

  def main(args: Array[String]): Unit = {
    val inputs = Seq(
      ConjugationInput(
        namedTemplate = NamedTemplate.FormICategoryAGroupUTemplate,
        firstRadical = ArabicLetterType.Noon,
        secondRadical = ArabicLetterType.Sad,
        thirdRadical = ArabicLetterType.Ra,
        verbalNounCodes = Seq(VerbalNoun.FormIV1.code),
        translation = Some("To Help")
      ),
      ConjugationInput(
        namedTemplate = NamedTemplate.FormIITemplate,
        firstRadical = ArabicLetterType.Ain,
        secondRadical = ArabicLetterType.Lam,
        thirdRadical = ArabicLetterType.Meem,
        translation = Some("To Teach")
      ),
      ConjugationInput(
        namedTemplate = NamedTemplate.FormIVTemplate,
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
      ConjugationConfiguration(removeAdverbs = true),
      ChartConfiguration(format = DocumentFormat.AbbreviateConjugationSingleRow)
    )
  }

  private def buildDocument(
    inputs: Seq[ConjugationInput],
    fileName: String,
    conjugationConfiguration: ConjugationConfiguration = ConjugationConfiguration(),
    chartConfiguration: ChartConfiguration = ChartConfiguration()
  ): Unit = {
    val builder = DocumentBuilder(
      chartConfiguration,
      conjugationConfiguration,
      OutputFormat.Unicode,
      Paths.get("target", fileName),
      inputs*
    )

    builder.generateDocument()
  }
}
