package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package transformer
package verb

import arabic.model.{ ArabicWord, ProNoun }
import morphologicalanalysis.morphology.model.{ ConversationType, GenderType }
import conjugation.model.RootWord

abstract class AbstractVerbTransformer(genderType: GenderType, conversationType: ConversationType)
    extends AbstractTransformer {

  override protected def doPreProcess(rootWord: RootWord): RootWord = rootWord
}
