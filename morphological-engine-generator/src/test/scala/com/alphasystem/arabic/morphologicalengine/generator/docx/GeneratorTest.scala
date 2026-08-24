package com.alphasystem
package arabic
package morphologicalengine
package generator
package docx

import com.alphasystem.arabic.model.ArabicLetterType
import com.alphasystem.arabic.morphologicalengine.asciidoc_generator.ConjugationUtil
import com.alphasystem.arabic.morphologicalengine.conjugation.forms.noun.VerbalNoun
import com.alphasystem.arabic.morphologicalengine.conjugation.model.{
  ConjugationConfiguration,
  ConjugationInput,
  NamedTemplate,
  RootLetters
}

import java.nio.file.Paths

object GeneratorTest {

  def main(args: Array[String]): Unit = {
    val conjugationConfiguration = ConjugationConfiguration()

    val conjugationInput =
      ConjugationInput(
        namedTemplate = NamedTemplate.FormICategoryAGroupATemplate,
        conjugationConfiguration = conjugationConfiguration,
        rootLetters = RootLetters(
          firstRadical = ArabicLetterType.Fa,
          secondRadical = ArabicLetterType.Ain,
          thirdRadical = ArabicLetterType.Lam
        ),
        verbalNounCodes = Seq(VerbalNoun.FormIV1.code),
        translation = Some("To Do")
      )

    ConjugationUtil.generateDocuments(
      conjugationInput,
      Paths.get("/Users/sfali/Documents/Arabic/morphological-engine")
    )
  }
}
