package com.alphasystem
package arabic
package model

import ArabicLetterType.*
import DiacriticType.*

object ArabicLetters {

  val LetterForwardSlash: ArabicLetter = ArabicLetter(ForwardSlash)
  val LetterTatweel: ArabicLetter = ArabicLetter(Tatweel)
  val LetterSpace: ArabicLetter = ArabicLetter(Space)
  val LetterComma: ArabicLetter = ArabicLetter(Comma)
  val LetterAlif: ArabicLetter = ArabicLetter(Alif)
  val LetterAlifMaddah: ArabicLetter = ArabicLetter(AlifMaddah)
  val LetterYa: ArabicLetter = ArabicLetter(Ya)
  val LetterWaw: ArabicLetter = ArabicLetter(Waw)
  val LetterKaf: ArabicLetter = ArabicLetter(Kaf)
  val LetterNoon: ArabicLetter = ArabicLetter(Noon)
  val LetterTa: ArabicLetter = ArabicLetter(Ta)
  val LetterMeem: ArabicLetter = ArabicLetter(Meem)
  val LetterSeen: ArabicLetter = ArabicLetter(Seen)
  val LetterAlifMaksura: ArabicLetter = ArabicLetter(AlifMaksura)
  val AlifHamzaBelowWithKasra: ArabicLetter = ArabicLetter(AlifHamzaBelow, Kasra)
  val AlifHamzaBelowWithDamma: ArabicLetter = ArabicLetter(AlifHamzaBelow, Damma)
  val AlifHamzaAboveWithDamma: ArabicLetter = ArabicLetter(AlifHamzaAbove, Damma)
  val AlifHamzaAboveWithFatha: ArabicLetter = ArabicLetter(AlifHamzaAbove, Fatha)
  val AlifHamzaAboveWithSukun: ArabicLetter = ArabicLetter(AlifHamzaAbove, Sukun)
  val YaHamzaAboveWithKasra: ArabicLetter = ArabicLetter(YaHamzaAbove, Kasra)
  val YaHamzaAboveWithFatha: ArabicLetter = ArabicLetter(YaHamzaAbove, Fatha)
  val WawHamzaAboveWithSukun: ArabicLetter = ArabicLetter(WawHamzaAbove, Sukun)
  val YaWithSukun: ArabicLetter = ArabicLetter(Ya, Sukun)
  val YaWithDamma: ArabicLetter = ArabicLetter(Ya, Damma)
  val YaWithDammatan: ArabicLetter = ArabicLetter(Ya, Dammatan)
  val YaWithFatha: ArabicLetter = ArabicLetter(Ya, Fatha)
  val YaWithFathatan: ArabicLetter = ArabicLetter(Ya, Fathatan)
  val YaWithKasra: ArabicLetter = ArabicLetter(Ya, Kasra)
  val BaWithFatha = ArabicLetter(Ba, Fatha)
  val BaWithDamma = ArabicLetter(Ba, Damma)
  val BaWithKasra = ArabicLetter(Ba, Kasra)
  val BaWithSukun = ArabicLetter(Ba, Sukun)
  val BaWithFathatan = ArabicLetter(Ba, Fathatan)
  val BaWithDammatan = ArabicLetter(Ba, Dammatan)
  val BaWithKasratan = ArabicLetter(Ba, Kasratan)
  val BaWithShaddaAndFatha = ArabicLetter(Ba, Shadda, Fatha)
  val BaWithShaddaAndDamma = ArabicLetter(Ba, Shadda, Damma)
  val BaWithShaddaAndKasra = ArabicLetter(Ba, Shadda, Kasra)
  val TaWithFatha: ArabicLetter = ArabicLetter(Ta, Fatha)
  val TaWithDamma: ArabicLetter = ArabicLetter(Ta, Damma)
  val TaWithKasra: ArabicLetter = ArabicLetter(Ta, Kasra)
  val TaWithSukun: ArabicLetter = ArabicLetter(Ta, Sukun)
  val TaWithFathatan = ArabicLetter(Ta, Fathatan)
  val TaWithDammatan: ArabicLetter = ArabicLetter(Ta, Dammatan)
  val TaWithKasratan: ArabicLetter = ArabicLetter(Ta, Kasratan)
  val TaWithShaddaAndFatha = ArabicLetter(Ta, Shadda, Fatha)
  val TaWithShaddaAndDamma = ArabicLetter(Ta, Shadda, Damma)
  val TaWithShaddaAndKasra = ArabicLetter(Ta, Shadda, Kasra)
  val ThaWithFatha = ArabicLetter(Tha, Fatha)
  val ThaWithDamma = ArabicLetter(Tha, Damma)
  val ThaWithKasra = ArabicLetter(Tha, Kasra)
  val ThaWithSukun = ArabicLetter(Tha, Sukun)
  val ThaWithFathatan = ArabicLetter(Tha, Fathatan)
  val ThaWithDammatan = ArabicLetter(Tha, Dammatan)
  val ThaWithKasratan = ArabicLetter(Tha, Kasratan)
  val ThaWithShaddaAndFatha = ArabicLetter(Tha, Shadda, Fatha)
  val ThaWithShaddaAndDamma = ArabicLetter(Tha, Shadda, Damma)
  val ThaWithShaddaAndKasra = ArabicLetter(Tha, Shadda, Kasra)
  val JeemWithFatha = ArabicLetter(Jeem, Fatha)
  val JeemWithDamma = ArabicLetter(Jeem, Damma)
  val JeemWithKasra = ArabicLetter(Jeem, Kasra)
  val JeemWithSukun = ArabicLetter(Jeem, Sukun)
  val JeemWithFathatan = ArabicLetter(Jeem, Fathatan)
  val JeemWithDammatan = ArabicLetter(Jeem, Dammatan)
  val JeemWithKasratan = ArabicLetter(Jeem, Kasratan)
  val JeemWithShaddaAndFatha = ArabicLetter(Jeem, Shadda, Fatha)
  val JeemWithShaddaAndDamma = ArabicLetter(Jeem, Shadda, Damma)
  val JeemWithShaddaAndKasra = ArabicLetter(Jeem, Shadda, Kasra)
  val HhaWithFatha = ArabicLetter(Hha, Fatha)
  val HhaWithDamma = ArabicLetter(Hha, Damma)
  val HhaWithKasra = ArabicLetter(Hha, Kasra)
  val HhaWithSukun = ArabicLetter(Hha, Sukun)
  val HhaWithFathatan = ArabicLetter(Hha, Fathatan)
  val HhaWithDammatan = ArabicLetter(Hha, Dammatan)
  val HhaWithKasratan = ArabicLetter(Hha, Kasratan)
  val HhaWithShaddaAndFatha = ArabicLetter(Hha, Shadda, Fatha)
  val HhaWithShaddaAndDamma = ArabicLetter(Hha, Shadda, Damma)
  val HhaWithShaddaAndKasra = ArabicLetter(Hha, Shadda, Kasra)
  val HaWithFatha = ArabicLetter(Ha, Fatha)
  val HaWithDamma = ArabicLetter(Ha, Damma)
  val KhaWithFatha = ArabicLetter(Kha, Fatha)
  val KhaWithDamma = ArabicLetter(Kha, Damma)
  val KhaWithKasra = ArabicLetter(Kha, Kasra)
  val KhaWithSukun = ArabicLetter(Kha, Sukun)
  val KhaWithFathatan = ArabicLetter(Kha, Fathatan)
  val KhaWithDammatan = ArabicLetter(Kha, Dammatan)
  val KhaWithKasratan = ArabicLetter(Kha, Kasratan)
  val KhaWithShaddaAndFatha = ArabicLetter(Kha, Shadda, Fatha)
  val KhaWithShaddaAndDamma = ArabicLetter(Kha, Shadda, Damma)
  val KhaWithShaddaAndKasra = ArabicLetter(Kha, Shadda, Kasra)
  val DalWithFatha = ArabicLetter(Dal, Fatha)
  val DalWithDamma = ArabicLetter(Dal, Damma)
  val DalWithKasra = ArabicLetter(Dal, Kasra)
  val DalWithSukun = ArabicLetter(Dal, Sukun)
  val DalWithFathatan = ArabicLetter(Dal, Fathatan)
  val DalWithDammatan = ArabicLetter(Dal, Dammatan)
  val DalWithKasratan = ArabicLetter(Dal, Kasratan)
  val DalWithShaddaAndFatha = ArabicLetter(Dal, Shadda, Fatha)
  val DalWithShaddaAndDamma = ArabicLetter(Dal, Shadda, Damma)
  val DalWithShaddaAndKasra = ArabicLetter(Dal, Shadda, Kasra)
  val ThalWithFatha = ArabicLetter(Thal, Fatha)
  val ThalWithDamma = ArabicLetter(Thal, Damma)
  val ThalWithKasra = ArabicLetter(Thal, Kasra)
  val ThalWithSukun = ArabicLetter(Thal, Sukun)
  val ThalWithFathatan = ArabicLetter(Thal, Fathatan)
  val ThalWithDammatan = ArabicLetter(Thal, Dammatan)
  val ThalWithKasratan = ArabicLetter(Thal, Kasratan)
  val ThalWithShaddaAndFatha = ArabicLetter(Thal, Shadda, Fatha)
  val ThalWithShaddaAndDamma = ArabicLetter(Thal, Shadda, Damma)
  val ThalWithShaddaAndKasra = ArabicLetter(Thal, Shadda, Kasra)
  val RaWithFatha = ArabicLetter(Ra, Fatha)
  val RaWithDamma = ArabicLetter(Ra, Damma)
  val RaWithKasra = ArabicLetter(Ra, Kasra)
  val RaWithSukun = ArabicLetter(Ra, Sukun)
  val RaWithFathatan = ArabicLetter(Ra, Fathatan)
  val RaWithDammatan = ArabicLetter(Ra, Dammatan)
  val RaWithKasratan = ArabicLetter(Ra, Kasratan)
  val RaWithShaddaAndFatha = ArabicLetter(Ra, Shadda, Fatha)
  val RaWithShaddaAndDamma = ArabicLetter(Ra, Shadda, Damma)
  val RaWithShaddaAndKasra = ArabicLetter(Ra, Shadda, Kasra)
  val ZainWithFatha = ArabicLetter(Zain, Fatha)
  val ZainWithDamma = ArabicLetter(Zain, Damma)
  val ZainWithKasra = ArabicLetter(Zain, Kasra)
  val ZainWithSukun = ArabicLetter(Zain, Sukun)
  val ZainWithFathatan = ArabicLetter(Zain, Fathatan)
  val ZainWithDammatan = ArabicLetter(Zain, Dammatan)
  val ZainWithKasratan = ArabicLetter(Zain, Kasratan)
  val ZainWithShaddaAndFatha = ArabicLetter(Zain, Shadda, Fatha)
  val ZainWithShaddaAndDamma = ArabicLetter(Zain, Shadda, Damma)
  val ZainWithShaddaAndKasra = ArabicLetter(Zain, Shadda, Kasra)
  val SeenWithFatha = ArabicLetter(Seen, Fatha)
  val SeenWithDamma = ArabicLetter(Seen, Damma)
  val SeenWithKasra = ArabicLetter(Seen, Kasra)
  val SeenWithSukun = ArabicLetter(Seen, Sukun)
  val SeenWithFathatan = ArabicLetter(Seen, Fathatan)
  val SeenWithDammatan = ArabicLetter(Seen, Dammatan)
  val SeenWithKasratan = ArabicLetter(Seen, Kasratan)
  val SeenWithShaddaAndFatha = ArabicLetter(Seen, Shadda, Fatha)
  val SeenWithShaddaAndDamma = ArabicLetter(Seen, Shadda, Damma)
  val SeenWithShaddaAndKasra = ArabicLetter(Seen, Shadda, Kasra)
  val SheenWithFatha = ArabicLetter(Sheen, Fatha)
  val SheenWithDamma = ArabicLetter(Sheen, Damma)
  val SheenWithKasra = ArabicLetter(Sheen, Kasra)
  val SheenWithSukun = ArabicLetter(Sheen, Sukun)
  val SheenWithFathatan = ArabicLetter(Sheen, Fathatan)
  val SheenWithDammatan = ArabicLetter(Sheen, Dammatan)
  val SheenWithKasratan = ArabicLetter(Sheen, Kasratan)
  val SheenWithShaddaAndFatha = ArabicLetter(Sheen, Shadda, Fatha)
  val SheenWithShaddaAndDamma = ArabicLetter(Sheen, Shadda, Damma)
  val SheenWithShaddaAndKasra = ArabicLetter(Sheen, Shadda, Kasra)
  val SadWithFatha = ArabicLetter(Sad, Fatha)
  val SadWithDamma = ArabicLetter(Sad, Damma)
  val SadWithKasra = ArabicLetter(Sad, Kasra)
  val SadWithSukun = ArabicLetter(Sad, Sukun)
  val SadWithFathatan = ArabicLetter(Sad, Fathatan)
  val SadWithDammatan = ArabicLetter(Sad, Dammatan)
  val SadWithKasratan = ArabicLetter(Sad, Kasratan)
  val SadWithShaddaAndFatha = ArabicLetter(Sad, Shadda, Fatha)
  val SadWithShaddaAndDamma = ArabicLetter(Sad, Shadda, Damma)
  val SadWithShaddaAndKasra = ArabicLetter(Sad, Shadda, Kasra)
  val DdadWithFatha = ArabicLetter(Ddad, Fatha)
  val DdadWithDamma = ArabicLetter(Ddad, Damma)
  val DdadWithKasra = ArabicLetter(Ddad, Kasra)
  val DdadWithSukun = ArabicLetter(Ddad, Sukun)
  val DdadWithFathatan = ArabicLetter(Ddad, Fathatan)
  val DdadWithDammatan = ArabicLetter(Ddad, Dammatan)
  val DdadWithKasratan = ArabicLetter(Ddad, Kasratan)
  val DdadWithShaddaAndFatha = ArabicLetter(Ddad, Shadda, Fatha)
  val DdadWithShaddaAndDamma = ArabicLetter(Ddad, Shadda, Damma)
  val DdadWithShaddaAndKasra = ArabicLetter(Ddad, Shadda, Kasra)
  val TtaWithFatha = ArabicLetter(Tta, Fatha)
  val TtaWithDamma = ArabicLetter(Tta, Damma)
  val TtaWithKasra = ArabicLetter(Tta, Kasra)
  val TtaWithSukun = ArabicLetter(Tta, Sukun)
  val TtaWithFathatan = ArabicLetter(Tta, Fathatan)
  val TtaWithDammatan = ArabicLetter(Tta, Dammatan)
  val TtaWithKasratan = ArabicLetter(Tta, Kasratan)
  val TtaWithShaddaAndFatha = ArabicLetter(Tta, Shadda, Fatha)
  val TtaWithShaddaAndDamma = ArabicLetter(Tta, Shadda, Damma)
  val TtaWithShaddaAndKasra = ArabicLetter(Tta, Shadda, Kasra)
  val DthaWithFatha = ArabicLetter(Dtha, Fatha)
  val DthaWithDamma = ArabicLetter(Dtha, Damma)
  val DthaWithKasra = ArabicLetter(Dtha, Kasra)
  val DthaWithSukun = ArabicLetter(Dtha, Sukun)
  val DthaWithFathatan = ArabicLetter(Dtha, Fathatan)
  val DthaWithDammatan = ArabicLetter(Dtha, Dammatan)
  val DthaWithKasratan = ArabicLetter(Dtha, Kasratan)
  val DthaWithShaddaAndFatha = ArabicLetter(Dtha, Shadda, Fatha)
  val DthaWithShaddaAndDamma = ArabicLetter(Dtha, Shadda, Damma)
  val DthaWithShaddaAndKasra = ArabicLetter(Dtha, Shadda, Kasra)
  val AinWithFatha = ArabicLetter(Ain, Fatha)
  val AinWithDamma = ArabicLetter(Ain, Damma)
  val AinWithKasra = ArabicLetter(Ain, Kasra)
  val AinWithSukun = ArabicLetter(Ain, Sukun)
  val AinWithFathatan = ArabicLetter(Ain, Fathatan)
  val AinWithDammatan = ArabicLetter(Ain, Dammatan)
  val AinWithKasratan = ArabicLetter(Ain, Kasratan)
  val AinWithShaddaAndFatha = ArabicLetter(Ain, Shadda, Fatha)
  val AinWithShaddaAndDamma = ArabicLetter(Ain, Shadda, Damma)
  val AinWithShaddaAndKasra = ArabicLetter(Ain, Shadda, Kasra)
  val GhainWithFatha = ArabicLetter(Ghain, Fatha)
  val GhainWithDamma = ArabicLetter(Ghain, Damma)
  val GhainWithKasra = ArabicLetter(Ghain, Kasra)
  val GhainWithSukun = ArabicLetter(Ghain, Sukun)
  val GhainWithFathatan = ArabicLetter(Ghain, Fathatan)
  val GhainWithDammatan = ArabicLetter(Ghain, Dammatan)
  val GhainWithKasratan = ArabicLetter(Ghain, Kasratan)
  val GhainWithShaddaAndFatha = ArabicLetter(Ghain, Shadda, Fatha)
  val GhainWithShaddaAndDamma = ArabicLetter(Ghain, Shadda, Damma)
  val GhainWithShaddaAndKasra = ArabicLetter(Ghain, Shadda, Kasra)
  val FaWithFatha = ArabicLetter(Fa, Fatha)
  val FaWithDamma = ArabicLetter(Fa, Damma)
  val FaWithKasra = ArabicLetter(Fa, Kasra)
  val FaWithSukun = ArabicLetter(Fa, Sukun)
  val FaWithFathatan = ArabicLetter(Fa, Fathatan)
  val FaWithDammatan = ArabicLetter(Fa, Dammatan)
  val FaWithKasratan = ArabicLetter(Fa, Kasratan)
  val FaWithShaddaAndFatha = ArabicLetter(Fa, Shadda, Fatha)
  val FaWithShaddaAndDamma = ArabicLetter(Fa, Shadda, Damma)
  val FaWithShaddaAndKasra = ArabicLetter(Fa, Shadda, Kasra)
  val QafWithFatha = ArabicLetter(Qaf, Fatha)
  val QafWithDamma = ArabicLetter(Qaf, Damma)
  val QafWithKasra = ArabicLetter(Qaf, Kasra)
  val QafWithSukun = ArabicLetter(Qaf, Sukun)
  val QafWithFathatan = ArabicLetter(Qaf, Fathatan)
  val QafWithDammatan = ArabicLetter(Qaf, Dammatan)
  val QafWithKasratan = ArabicLetter(Qaf, Kasratan)
  val QafWithShaddaAndFatha = ArabicLetter(Qaf, Shadda, Fatha)
  val QafWithShaddaAndDamma = ArabicLetter(Qaf, Shadda, Damma)
  val QafWithShaddaAndKasra = ArabicLetter(Qaf, Shadda, Kasra)
  val KafWithFatha = ArabicLetter(Kaf, Fatha)
  val KafWithDamma = ArabicLetter(Kaf, Damma)
  val KafWithKasra = ArabicLetter(Kaf, Kasra)
  val KafWithSukun = ArabicLetter(Kaf, Sukun)
  val KafWithFathatan = ArabicLetter(Kaf, Fathatan)
  val KafWithDammatan = ArabicLetter(Kaf, Dammatan)
  val KafWithKasratan = ArabicLetter(Kaf, Kasratan)
  val KafWithShaddaAndFatha = ArabicLetter(Kaf, Shadda, Fatha)
  val KafWithShaddaAndDamma = ArabicLetter(Kaf, Shadda, Damma)
  val KafWithShaddaAndKasra = ArabicLetter(Kaf, Shadda, Kasra)
  val LamWithFatha = ArabicLetter(Lam, Fatha)
  val LamWithDamma = ArabicLetter(Lam, Damma)
  val LamWithKasra = ArabicLetter(Lam, Kasra)
  val LamWithSukun = ArabicLetter(Lam, Sukun)
  val LamWithFathatan = ArabicLetter(Lam, Fathatan)
  val LamWithDammatan = ArabicLetter(Lam, Dammatan)
  val LamWithKasratan = ArabicLetter(Lam, Kasratan)
  val LamWithShaddaAndFatha = ArabicLetter(Lam, Shadda, Fatha)
  val LamWithShaddaAndDamma = ArabicLetter(Lam, Shadda, Damma)
  val LamWithShaddaAndKasra = ArabicLetter(Lam, Shadda, Kasra)
  val LamWithAlifKhanJareeya = ArabicLetter(Lam, AlifKhanJareeya)
  val MeemWithFatha = ArabicLetter(Meem, Fatha)
  val MeemWithDamma = ArabicLetter(Meem, Damma)
  val MeemWithKasra = ArabicLetter(Meem, Kasra)
  val MeemWithSukun = ArabicLetter(Meem, Sukun)
  val MeemWithFathatan = ArabicLetter(Meem, Fathatan)
  val MeemWithDammatan = ArabicLetter(Meem, Dammatan)
  val MeemWithKasratan = ArabicLetter(Meem, Kasratan)
  val MeemWithShaddaAndFatha = ArabicLetter(Meem, Shadda, Fatha)
  val MeemWithShaddaAndDamma = ArabicLetter(Meem, Shadda, Damma)
  val MeemWithShaddaAndKasra = ArabicLetter(Meem, Shadda, Kasra)
  val NoonWithFatha = ArabicLetter(Noon, Fatha)
  val NoonWithDamma = ArabicLetter(Noon, Damma)
  val NoonWithKasra = ArabicLetter(Noon, Kasra)
  val NoonWithSukun = ArabicLetter(Noon, Sukun)
  val NoonWithFathatan = ArabicLetter(Noon, Fathatan)
  val NoonWithDammatan = ArabicLetter(Noon, Dammatan)
  val NoonWithKasratan = ArabicLetter(Noon, Kasratan)
  val NoonWithShaddaAndFatha = ArabicLetter(Noon, Shadda, Fatha)
  val NoonWithShaddaAndDamma = ArabicLetter(Noon, Shadda, Damma)
  val NoonWithShaddaAndKasra = ArabicLetter(Noon, Shadda, Kasra)
  val WawWithFatha = ArabicLetter(Waw, Fatha)
  val WawWithDamma = ArabicLetter(Waw, Damma)
  val WawWithKasra = ArabicLetter(Waw, Kasra)
  val WawWithSukun = ArabicLetter(Waw, Sukun)
  val WawWithFathatan = ArabicLetter(Waw, Fathatan)
  val WawWithDammatan = ArabicLetter(Waw, Dammatan)
  val WawWithKasratan = ArabicLetter(Waw, Kasratan)
  val WawWithShaddaAndFatha = ArabicLetter(Waw, Shadda, Fatha)
  val WawWithShaddaAndDamma = ArabicLetter(Waw, Shadda, Damma)
  val WawWithShaddaAndKasra = ArabicLetter(Waw, Shadda, Kasra)
  val WawWithShaddaAndDammatan = ArabicLetter(Waw, Shadda, Dammatan)
  val TaMarbutaWithDammatan = ArabicLetter(TaMarbuta, Dammatan)
  val TaMarbutaWithFathatan = ArabicLetter(TaMarbuta, Fathatan)
  val TaMarbutaWithKasratan = ArabicLetter(TaMarbuta, Kasratan)
  val HamzaWithKasra = ArabicLetter(Hamza, Kasra)
  val HamzaWithFatha = ArabicLetter(Hamza, Fatha)
  val HamzaWithDamma = ArabicLetter(Hamza, Damma)
  val HamzaWithSukun = ArabicLetter(Hamza, Sukun)

  val WordSpace: ArabicWord = ArabicWord(Space)
  val WordComma: ArabicWord = ArabicWord(Comma)
  val WordTatweel: ArabicWord = ArabicWord(LetterTatweel)
  val WordNewLine: ArabicWord = ArabicWord(NewLine)
  val InPlaceOf: ArabicWord = ArabicWord(Fa, Ya).concatWithSpace(ArabicWord(Meem, Hha, Lam))
  val WeightLabel: ArabicWord = ArabicWord(Waw, Zain, Noon)
}
