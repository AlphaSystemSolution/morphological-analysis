package com.alphasystem
package arabic
package morphologicalengine
package generator
package docx

import arabic.model.ArabicLetterType
import generator.model.{ ChartConfiguration, ConjugationInput }
import morphologicalengine.conjugation.model.{ ConjugationConfiguration, NamedTemplate, OutputFormat }

import java.nio.file.Paths

object GeneratorTest {

  def main(args: Array[String]): Unit = {
    val builder = DocumentBuilder(
      ChartConfiguration(),
      Paths.get("target", "test.docx"),
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

    builder.generateDocument()
  }
}
