package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package transformer
package verb

import conjugation.model.internal.RootWord
import morphologicalanalysis.morphology.model.{ ConversationType, GenderType }

abstract class AbstractVerbTransformer(genderType: GenderType, conversationType: ConversationType)
    extends AbstractTransformer {

  override protected def doPreProcess(rootWord: RootWord): RootWord = rootWord
}
