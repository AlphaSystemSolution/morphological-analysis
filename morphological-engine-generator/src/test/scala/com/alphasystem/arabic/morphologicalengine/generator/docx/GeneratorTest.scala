package com.alphasystem
package arabic
package morphologicalengine
package generator
package docx

import com.alphasystem.arabic.model.ArabicLetterType
import com.alphasystem.arabic.morphologicalengine.asciidoc_generator.ConjugationDocumentGenerator
import com.alphasystem.arabic.morphologicalengine.conjugation.forms.noun.VerbalNoun
import com.alphasystem.arabic.morphologicalengine.conjugation.model.{
  ConjugationConfiguration,
  ConjugationInput,
  NamedTemplate,
  RootLetters
}

import java.nio.file.Paths

object GeneratorTest {

  private val SrcPath = Paths.get("/Users/sfali/Documents/Arabic/morphological-engine")

  private val BaseRootLetters = RootLetters(
    firstRadical = ArabicLetterType.Fa,
    secondRadical = ArabicLetterType.Ain,
    thirdRadical = ArabicLetterType.Lam
  )

  def main(args: Array[String]): Unit = {
    generateFamily(NamedTemplate.FormICategoryAGroupATemplate, BaseRootLetters, "To do")
    generateFamily(NamedTemplate.FormIITemplate, BaseRootLetters, "To scan")
    generateFamily(NamedTemplate.FormVIITemplate, BaseRootLetters, "To be done")
    generateFamily(NamedTemplate.FormVIIITemplate, BaseRootLetters, "To invent")
  }

  private def generateFamily(family: NamedTemplate, rootLetters: RootLetters, translation: String): Unit = {
    val conjugationInput =
      ConjugationInput(
        namedTemplate = family,
        conjugationConfiguration = ConjugationConfiguration(),
        rootLetters = rootLetters,
        translation = Some(translation)
      )
    ConjugationDocumentGenerator.generateDocuments(conjugationInput, SrcPath)
  }
}
