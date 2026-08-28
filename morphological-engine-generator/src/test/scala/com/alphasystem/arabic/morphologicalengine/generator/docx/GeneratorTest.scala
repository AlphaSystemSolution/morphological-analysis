package com.alphasystem
package arabic
package morphologicalengine
package generator
package docx

import arabic.utils.*
import arabic.model.ArabicLetterType
import morphologicalengine.asciidoc_generator.{ ConjugationDocumentGenerator, toDirectoryName }
import morphologicalengine.conjugation.forms.noun.VerbalNoun
import morphologicalengine.conjugation.model.{ ConjugationConfiguration, ConjugationInput, NamedTemplate, RootLetters }

import java.nio.file.Paths

object GeneratorTest {

  private val BaseRootLetters = RootLetters(
    firstRadical = ArabicLetterType.Fa,
    secondRadical = ArabicLetterType.Ain,
    thirdRadical = ArabicLetterType.Lam
  )

  private val SrcPath =
    Paths.get("/Users/sfali/Documents/Arabic/morphological-engine") / Seq("data", BaseRootLetters.toDirectoryName)

  def main(args: Array[String]): Unit = {
    generateFamily(NamedTemplate.FormICategoryAGroupATemplate, BaseRootLetters, "To do", Seq(VerbalNoun.FormIV1.code))
    generateFamily(NamedTemplate.FormIITemplate, BaseRootLetters, "To scan")
    generateFamily(NamedTemplate.FormVIITemplate, BaseRootLetters, "To be done")
    generateFamily(NamedTemplate.FormVIIITemplate, BaseRootLetters, "To invent")
  }

  private def generateFamily(
    family: NamedTemplate,
    rootLetters: RootLetters,
    translation: String,
    verbalNouns: Seq[String] = Seq.empty
  ): Unit = {
    val conjugationInput =
      ConjugationInput(
        namedTemplate = family,
        conjugationConfiguration = ConjugationConfiguration(),
        rootLetters = rootLetters,
        translation = Some(translation),
        verbalNounCodes = verbalNouns
      )
    ConjugationDocumentGenerator.generateDocuments(conjugationInput, SrcPath)
  }
}
