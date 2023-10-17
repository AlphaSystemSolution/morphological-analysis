package com.alphasystem
package arabic
package morphologicalengine
package conjugation

package object model {

  extension (src: MorphologicalTermType) {
    def isPerfectVerb: Boolean = src == MorphologicalTermType.PastTense || src == MorphologicalTermType.PastPassiveTense

    def isImperfectVerb: Boolean =
      src == MorphologicalTermType.PresentTense || src == MorphologicalTermType.PresentPassiveTense

    def isVerbBasedType: Boolean = MorphologicalTermType.VerbBasedTypes.contains(src)
  }
}
