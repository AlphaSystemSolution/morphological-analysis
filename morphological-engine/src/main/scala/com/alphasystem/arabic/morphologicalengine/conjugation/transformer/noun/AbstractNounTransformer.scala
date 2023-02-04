package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package transformer
package noun

import arabic.model.ArabicLetterType
import conjugation.model.{ ConjugationTuple, RootWord }
import conjugation.rule.RuleProcessor
import noun.AbstractNounTransformer.VariableIndexType

abstract class AbstractNounTransformer(
  ruleProcessor: RuleProcessor,
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
}
