package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package rule
package processors

import com.alphasystem.arabic.model.{ ArabicLetterType, ArabicLetters, ArabicWord, HiddenNounStatus }
import com.alphasystem.arabic.morphologicalengine.conjugation.forms.Form
import com.alphasystem.arabic.morphologicalengine.conjugation.model.{ NamedTemplate, OutputFormat }

class Rule17ProcessorSpec extends BaseRuleProcessorSpec {

  test("Second radical Waw") {
    val processingContext =
      ProcessingContext(
        NamedTemplate.FormICategoryAGroupUTemplate,
        OutputFormat.Unicode,
        ArabicLetterType.Qaf,
        ArabicLetterType.Waw,
        ArabicLetterType.Lam
      )

    processingContext.pastTenseHasTransformed = true
    val baseWord = Form.fromNamedTemplate(processingContext.namedTemplate).activeParticipleMasculine.rootWord
    val expected = ArabicWord(
      ArabicLetters.QafWithFatha,
      ArabicLetters.LetterAlif,
      ArabicLetters.YaHamzaAboveWithKasra,
      ArabicLetters.LamWithDammatan
    )
    validate(baseWord, expected, HiddenNounStatus.NominativeSingular, processingContext)
  }

  test("Second radical Ya") {
    val processingContext =
      ProcessingContext(
        NamedTemplate.FormICategoryAGroupITemplate,
        OutputFormat.Unicode,
        ArabicLetterType.Ba,
        ArabicLetterType.Ya,
        ArabicLetterType.Ain
      )

    processingContext.pastTenseHasTransformed = true
    val baseWord = Form.fromNamedTemplate(processingContext.namedTemplate).activeParticipleMasculine.rootWord
    val expected = ArabicWord(
      ArabicLetters.BaWithFatha,
      ArabicLetters.LetterAlif,
      ArabicLetters.YaHamzaAboveWithKasra,
      ArabicLetters.AinWithDammatan
    )
    validate(baseWord, expected, HiddenNounStatus.NominativeSingular, processingContext)
  }
}
