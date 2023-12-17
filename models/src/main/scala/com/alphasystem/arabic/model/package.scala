package com.alphasystem
package arabic

package object model {

  def toHtmlCodeString(unicode: Char): String = f"&#${unicode.toInt}%04d;"

  extension (src: SarfMemberType) {

    def isThirdPersonMasculineDual: Boolean =
      src match
        case status: HiddenPronounStatus => status == HiddenPronounStatus.ThirdPersonMasculineDual
        case _                           => false

    def isDualType: Boolean =
      src match
        case status: HiddenPronounStatus => SarfMemberType.DualsTypes.contains(status)
        case _                           => false
  }
}
