package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package transformer
package noun

import arabic.model.ArabicLetterType
import conjugation.model.internal.RootWord
import morphologicalanalysis.morphology.model.Flexibility
import conjugation.model.ConjugationTuple
import noun.AbstractNounTransformer.{ PluralType, VariableIndexType }

abstract class AbstractNounTransformer(
  flexibility: Flexibility = Flexibility.FullyFlexible,
  pluralType: PluralType = PluralType.Default,
  variableIndexType: VariableIndexType = VariableIndexType.ThirdRadicalIndex)
    extends AbstractTransformer {

  protected var variableIndex: Int = Int.MaxValue

  override protected def doPreProcess(rootWord: RootWord): RootWord = {
    import VariableIndexType.*
    variableIndex = variableIndexType match
      case ThirdRadicalIndex => rootWord.thirdRadicalIndex
      case LastLetter        => rootWord.lastLetterIndex

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
