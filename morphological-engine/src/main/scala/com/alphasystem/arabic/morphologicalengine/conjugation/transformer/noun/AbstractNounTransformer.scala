package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package transformer
package noun

import arabic.model.ArabicLetterType
import morphologicalanalysis.morphology.model.Flexibility
import conjugation.model.{ ConjugationTuple, RootWord }
import conjugation.rule.RuleProcessor
import noun.AbstractNounTransformer.{ PluralType, VariableIndexType }

abstract class AbstractNounTransformer(
  ruleProcessor: RuleProcessor,
  flexibility: Flexibility = Flexibility.FullyFlexible,
  pluralType: PluralType = PluralType.Default,
  variableIndexType: VariableIndexType = VariableIndexType.ThirdRadicalIndex)
    extends AbstractTransformer(ruleProcessor) {

  protected var variableIndex: Int = Int.MaxValue

  override protected def doPreProcess(rootWord: RootWord): RootWord = {
    import VariableIndexType.*
    variableIndex = variableIndexType match
      case ThirdRadicalIndex => rootWord.rootLetter.thirdRadical.index
      case LastLetter        => rootWord.derivedWord.letters.length - 1

    rootWord
  }
}

object AbstractNounTransformer {

  protected[noun] enum VariableIndexType {
    case ThirdRadicalIndex extends VariableIndexType
    case LastLetter extends VariableIndexType
  }

  enum PluralType {
    case Default extends PluralType
    case Feminine extends PluralType
  }
}
