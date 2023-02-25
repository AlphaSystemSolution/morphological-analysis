package com.alphasystem
package arabic
package morphologicalengine
package generator
package docx

import arabic.model.ArabicLetterType
import morphologicalengine.conjugation.forms.noun.VerbalNoun
import generator.model.{ ChartConfiguration, ConjugationInput, DocumentFormat, SortDirective }
import morphologicalengine.conjugation.model.{ ConjugationConfiguration, NamedTemplate, OutputFormat }

import java.nio.file.Paths

object GeneratorTest {

  def main(args: Array[String]): Unit = {
    val inputs = Seq(
      ConjugationInput(
        namedTemplate = NamedTemplate.FormICategoryAGroupUTemplate,
        conjugationConfiguration = ConjugationConfiguration(),
        outputFormat = OutputFormat.Unicode,
        firstRadical = ArabicLetterType.Noon,
        secondRadical = ArabicLetterType.Sad,
        thirdRadical = ArabicLetterType.Ra,
        verbalNounCodes = Seq(VerbalNoun.FormIV1.code),
        translation = Some("To Help")
      ),
      ConjugationInput(
        namedTemplate = NamedTemplate.FormIITemplate,
        conjugationConfiguration = ConjugationConfiguration(),
        outputFormat = OutputFormat.Unicode,
        firstRadical = ArabicLetterType.Ain,
        secondRadical = ArabicLetterType.Lam,
        thirdRadical = ArabicLetterType.Meem,
        translation = Some("To Teach")
      ),
      ConjugationInput(
        namedTemplate = NamedTemplate.FormIVTemplate,
        conjugationConfiguration = ConjugationConfiguration(),
        outputFormat = OutputFormat.Unicode,
        firstRadical = ArabicLetterType.Seen,
        secondRadical = ArabicLetterType.Lam,
        thirdRadical = ArabicLetterType.Meem,
        translation = Some("To Submit")
      )
    )
    val builder = DocumentBuilder(
      ChartConfiguration(format = DocumentFormat.AbbreviateConjugationSingleRow, sortDirective = SortDirective.Type),
      Paths.get("target", "test.docx"),
      inputs*
    )

    builder.generateDocument()
  }
}
