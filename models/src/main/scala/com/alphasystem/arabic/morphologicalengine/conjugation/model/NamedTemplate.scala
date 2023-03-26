package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package model

import arabic.model.{ ArabicLetterType, ArabicLetters, ArabicSupportEnum, ArabicWord }

import java.lang.Enum

enum NamedTemplate(
  val form: String,
  override val index: Int,
  val subIndex: Int,
  override val word: ArabicWord,
  val `type`: ArabicWord)
    extends Enum[NamedTemplate]
    with ArabicSupportEnum {

  case FormICategoryAGroupUTemplate
      extends NamedTemplate(
        form = "I",
        index = 1,
        subIndex = 1,
        word = ArabicWord(
          ArabicLetters.FaWithFatha,
          ArabicLetters.AinWithFatha,
          ArabicLetters.LamWithFatha,
          ArabicLetters.LetterSpace,
          ArabicLetters.YaWithFatha,
          ArabicLetters.FaWithSukun,
          ArabicLetters.AinWithDamma,
          ArabicLetters.LamWithDamma
        ),
        `type` = ArabicWord(
          ArabicLetterType.Fa,
          ArabicLetterType.Ain,
          ArabicLetterType.Lam,
          ArabicLetterType.Space,
          ArabicLetterType.Tha,
          ArabicLetterType.Lam,
          ArabicLetterType.Alif,
          ArabicLetterType.Tha,
          ArabicLetterType.Ya,
          ArabicLetterType.Space,
          ArabicLetterType.Meem,
          ArabicLetterType.Jeem,
          ArabicLetterType.Ra,
          ArabicLetterType.Dal
        )
      )

  case FormICategoryAGroupITemplate
      extends NamedTemplate(
        form = "I",
        index = 1,
        subIndex = 2,
        word = ArabicWord(
          ArabicLetters.FaWithFatha,
          ArabicLetters.AinWithFatha,
          ArabicLetters.LamWithFatha,
          ArabicLetters.LetterSpace,
          ArabicLetters.YaWithFatha,
          ArabicLetters.FaWithSukun,
          ArabicLetters.AinWithKasra,
          ArabicLetters.LamWithDamma
        ),
        `type` = ArabicWord(
          ArabicLetterType.Fa,
          ArabicLetterType.Ain,
          ArabicLetterType.Lam,
          ArabicLetterType.Space,
          ArabicLetterType.Tha,
          ArabicLetterType.Lam,
          ArabicLetterType.Alif,
          ArabicLetterType.Tha,
          ArabicLetterType.Ya,
          ArabicLetterType.Space,
          ArabicLetterType.Meem,
          ArabicLetterType.Jeem,
          ArabicLetterType.Ra,
          ArabicLetterType.Dal
        )
      )

  case FormICategoryAGroupATemplate
      extends NamedTemplate(
        form = "I",
        index = 1,
        subIndex = 3,
        word = ArabicWord(
          ArabicLetters.FaWithFatha,
          ArabicLetters.AinWithFatha,
          ArabicLetters.LamWithFatha,
          ArabicLetters.LetterSpace,
          ArabicLetters.YaWithFatha,
          ArabicLetters.FaWithSukun,
          ArabicLetters.AinWithFatha,
          ArabicLetters.LamWithDamma
        ),
        `type` = ArabicWord(
          ArabicLetterType.Fa,
          ArabicLetterType.Ain,
          ArabicLetterType.Lam,
          ArabicLetterType.Space,
          ArabicLetterType.Tha,
          ArabicLetterType.Lam,
          ArabicLetterType.Alif,
          ArabicLetterType.Tha,
          ArabicLetterType.Ya,
          ArabicLetterType.Space,
          ArabicLetterType.Meem,
          ArabicLetterType.Jeem,
          ArabicLetterType.Ra,
          ArabicLetterType.Dal
        )
      )

  case FormICategoryIGroupATemplate
      extends NamedTemplate(
        form = "I",
        index = 1,
        subIndex = 4,
        word = ArabicWord(
          ArabicLetters.FaWithFatha,
          ArabicLetters.AinWithKasra,
          ArabicLetters.LamWithFatha,
          ArabicLetters.LetterSpace,
          ArabicLetters.YaWithFatha,
          ArabicLetters.FaWithSukun,
          ArabicLetters.AinWithFatha,
          ArabicLetters.LamWithDamma
        ),
        `type` = ArabicWord(
          ArabicLetterType.Fa,
          ArabicLetterType.Ain,
          ArabicLetterType.Lam,
          ArabicLetterType.Space,
          ArabicLetterType.Tha,
          ArabicLetterType.Lam,
          ArabicLetterType.Alif,
          ArabicLetterType.Tha,
          ArabicLetterType.Ya,
          ArabicLetterType.Space,
          ArabicLetterType.Meem,
          ArabicLetterType.Jeem,
          ArabicLetterType.Ra,
          ArabicLetterType.Dal
        )
      )

  case FormICategoryIGroupITemplate
      extends NamedTemplate(
        form = "I",
        index = 1,
        subIndex = 5,
        word = ArabicWord(
          ArabicLetters.FaWithFatha,
          ArabicLetters.AinWithKasra,
          ArabicLetters.LamWithFatha,
          ArabicLetters.LetterSpace,
          ArabicLetters.YaWithFatha,
          ArabicLetters.FaWithSukun,
          ArabicLetters.AinWithKasra,
          ArabicLetters.LamWithDamma
        ),
        `type` = ArabicWord(
          ArabicLetterType.Fa,
          ArabicLetterType.Ain,
          ArabicLetterType.Lam,
          ArabicLetterType.Space,
          ArabicLetterType.Tha,
          ArabicLetterType.Lam,
          ArabicLetterType.Alif,
          ArabicLetterType.Tha,
          ArabicLetterType.Ya,
          ArabicLetterType.Space,
          ArabicLetterType.Meem,
          ArabicLetterType.Jeem,
          ArabicLetterType.Ra,
          ArabicLetterType.Dal
        )
      )

  case FormICategoryUTemplate
      extends NamedTemplate(
        form = "I",
        index = 1,
        subIndex = 6,
        word = ArabicWord(
          ArabicLetters.FaWithFatha,
          ArabicLetters.AinWithDamma,
          ArabicLetters.LamWithFatha,
          ArabicLetters.LetterSpace,
          ArabicLetters.YaWithFatha,
          ArabicLetters.FaWithSukun,
          ArabicLetters.AinWithDamma,
          ArabicLetters.LamWithDamma
        ),
        `type` = ArabicWord(
          ArabicLetterType.Fa,
          ArabicLetterType.Ain,
          ArabicLetterType.Lam,
          ArabicLetterType.Space,
          ArabicLetterType.Tha,
          ArabicLetterType.Lam,
          ArabicLetterType.Alif,
          ArabicLetterType.Tha,
          ArabicLetterType.Ya,
          ArabicLetterType.Space,
          ArabicLetterType.Meem,
          ArabicLetterType.Jeem,
          ArabicLetterType.Ra,
          ArabicLetterType.Dal
        )
      )

  case FormIITemplate
      extends NamedTemplate(
        form = "II",
        index = 2,
        subIndex = 0,
        word = ArabicWord(
          ArabicLetters.FaWithFatha,
          ArabicLetters.AinWithShaddaAndFatha,
          ArabicLetters.LamWithFatha,
          ArabicLetters.LetterSpace,
          ArabicLetters.YaWithDamma,
          ArabicLetters.FaWithFatha,
          ArabicLetters.AinWithShaddaAndKasra,
          ArabicLetters.LamWithDamma
        ),
        `type` = ArabicWord(
          ArabicLetterType.Fa,
          ArabicLetterType.Ain,
          ArabicLetterType.Lam,
          ArabicLetterType.Space,
          ArabicLetterType.Tha,
          ArabicLetterType.Lam,
          ArabicLetterType.Alif,
          ArabicLetterType.Tha,
          ArabicLetterType.Ya,
          ArabicLetterType.Space,
          ArabicLetterType.Meem,
          ArabicLetterType.Zain,
          ArabicLetterType.Ya,
          ArabicLetterType.Dal,
          ArabicLetterType.Space,
          ArabicLetterType.Fa,
          ArabicLetterType.Ya,
          ArabicLetterType.Ha
        )
      )

  case FormIIITemplate
      extends NamedTemplate(
        form = "III",
        index = 3,
        subIndex = 0,
        word = ArabicWord(
          ArabicLetters.FaWithFatha,
          ArabicLetters.LetterAlif,
          ArabicLetters.AinWithFatha,
          ArabicLetters.LamWithFatha,
          ArabicLetters.LetterSpace,
          ArabicLetters.YaWithDamma,
          ArabicLetters.FaWithFatha,
          ArabicLetters.LetterAlif,
          ArabicLetters.AinWithKasra,
          ArabicLetters.LamWithDamma
        ),
        `type` = ArabicWord(
          ArabicLetterType.Fa,
          ArabicLetterType.Ain,
          ArabicLetterType.Lam,
          ArabicLetterType.Space,
          ArabicLetterType.Tha,
          ArabicLetterType.Lam,
          ArabicLetterType.Alif,
          ArabicLetterType.Tha,
          ArabicLetterType.Ya,
          ArabicLetterType.Space,
          ArabicLetterType.Meem,
          ArabicLetterType.Zain,
          ArabicLetterType.Ya,
          ArabicLetterType.Dal,
          ArabicLetterType.Space,
          ArabicLetterType.Fa,
          ArabicLetterType.Ya,
          ArabicLetterType.Ha
        )
      )

  case FormIVTemplate
      extends NamedTemplate(
        form = "IV",
        index = 4,
        subIndex = 0,
        word = ArabicWord(
          ArabicLetters.AlifHamzaAboveWithFatha,
          ArabicLetters.FaWithSukun,
          ArabicLetters.AinWithFatha,
          ArabicLetters.LamWithFatha,
          ArabicLetters.LetterSpace,
          ArabicLetters.YaWithDamma,
          ArabicLetters.FaWithSukun,
          ArabicLetters.AinWithKasra,
          ArabicLetters.LamWithDamma
        ),
        `type` = ArabicWord(
          ArabicLetterType.Fa,
          ArabicLetterType.Ain,
          ArabicLetterType.Lam,
          ArabicLetterType.Space,
          ArabicLetterType.Tha,
          ArabicLetterType.Lam,
          ArabicLetterType.Alif,
          ArabicLetterType.Tha,
          ArabicLetterType.Ya,
          ArabicLetterType.Space,
          ArabicLetterType.Meem,
          ArabicLetterType.Zain,
          ArabicLetterType.Ya,
          ArabicLetterType.Dal,
          ArabicLetterType.Space,
          ArabicLetterType.Fa,
          ArabicLetterType.Ya,
          ArabicLetterType.Ha
        )
      )

  case FormVTemplate
      extends NamedTemplate(
        form = "V",
        index = 5,
        subIndex = 0,
        // label
        word = ArabicWord(
          ArabicLetters.TaWithFatha,
          ArabicLetters.FaWithFatha,
          ArabicLetters.AinWithShaddaAndFatha,
          ArabicLetters.LamWithFatha,
          ArabicLetters.LetterSpace,
          ArabicLetters.YaWithFatha,
          ArabicLetters.TaWithFatha,
          ArabicLetters.FaWithFatha,
          ArabicLetters.AinWithShaddaAndFatha,
          ArabicLetters.LamWithDamma
        ),
        `type` = ArabicWord(
          ArabicLetterType.Fa,
          ArabicLetterType.Ain,
          ArabicLetterType.Lam,
          ArabicLetterType.Space,
          ArabicLetterType.Tha,
          ArabicLetterType.Lam,
          ArabicLetterType.Alif,
          ArabicLetterType.Tha,
          ArabicLetterType.Ya,
          ArabicLetterType.Space,
          ArabicLetterType.Meem,
          ArabicLetterType.Zain,
          ArabicLetterType.Ya,
          ArabicLetterType.Dal,
          ArabicLetterType.Space,
          ArabicLetterType.Fa,
          ArabicLetterType.Ya,
          ArabicLetterType.Ha
        )
      )

  case FormVITemplate
      extends NamedTemplate(
        "VI",
        6,
        0,
        // label
        ArabicWord(
          ArabicLetters.TaWithFatha,
          ArabicLetters.FaWithFatha,
          ArabicLetters.LetterAlif,
          ArabicLetters.AinWithFatha,
          ArabicLetters.LamWithFatha,
          ArabicLetters.LetterSpace,
          ArabicLetters.YaWithFatha,
          ArabicLetters.TaWithFatha,
          ArabicLetters.FaWithFatha,
          ArabicLetters.LetterAlif,
          ArabicLetters.AinWithFatha,
          ArabicLetters.LamWithDamma
        ),
        // type
        `type` = ArabicWord(
          ArabicLetterType.Fa,
          ArabicLetterType.Ain,
          ArabicLetterType.Lam,
          ArabicLetterType.Space,
          ArabicLetterType.Tha,
          ArabicLetterType.Lam,
          ArabicLetterType.Alif,
          ArabicLetterType.Tha,
          ArabicLetterType.Ya,
          ArabicLetterType.Space,
          ArabicLetterType.Meem,
          ArabicLetterType.Zain,
          ArabicLetterType.Ya,
          ArabicLetterType.Dal,
          ArabicLetterType.Space,
          ArabicLetterType.Fa,
          ArabicLetterType.Ya,
          ArabicLetterType.Ha
        )
      )

  case FormVIITemplate
      extends NamedTemplate(
        form = "VII",
        index = 7,
        subIndex = 0,
        word = ArabicWord(
          ArabicLetters.AlifHamzaBelowWithKasra,
          ArabicLetters.NoonWithSukun,
          ArabicLetters.FaWithFatha,
          ArabicLetters.AinWithFatha,
          ArabicLetters.LamWithFatha,
          ArabicLetters.LetterSpace,
          ArabicLetters.YaWithFatha,
          ArabicLetters.NoonWithSukun,
          ArabicLetters.FaWithFatha,
          ArabicLetters.AinWithKasra,
          ArabicLetters.LamWithDamma
        ),
        `type` = ArabicWord(
          ArabicLetterType.Fa,
          ArabicLetterType.Ain,
          ArabicLetterType.Lam,
          ArabicLetterType.Space,
          ArabicLetterType.Tha,
          ArabicLetterType.Lam,
          ArabicLetterType.Alif,
          ArabicLetterType.Tha,
          ArabicLetterType.Ya,
          ArabicLetterType.Space,
          ArabicLetterType.Meem,
          ArabicLetterType.Zain,
          ArabicLetterType.Ya,
          ArabicLetterType.Dal,
          ArabicLetterType.Space,
          ArabicLetterType.Fa,
          ArabicLetterType.Ya,
          ArabicLetterType.Ha
        )
      )

  case FormVIIITemplate
      extends NamedTemplate(
        form = "VIII",
        index = 8,
        subIndex = 0,
        word = ArabicWord(
          ArabicLetters.AlifHamzaBelowWithKasra,
          ArabicLetters.FaWithSukun,
          ArabicLetters.TaWithFatha,
          ArabicLetters.AinWithFatha,
          ArabicLetters.LamWithFatha,
          ArabicLetters.LetterSpace,
          ArabicLetters.YaWithFatha,
          ArabicLetters.FaWithSukun,
          ArabicLetters.TaWithFatha,
          ArabicLetters.AinWithKasra,
          ArabicLetters.LamWithDamma
        ),
        `type` = ArabicWord(
          ArabicLetterType.Fa,
          ArabicLetterType.Ain,
          ArabicLetterType.Lam,
          ArabicLetterType.Space,
          ArabicLetterType.Tha,
          ArabicLetterType.Lam,
          ArabicLetterType.Alif,
          ArabicLetterType.Tha,
          ArabicLetterType.Ya,
          ArabicLetterType.Space,
          ArabicLetterType.Meem,
          ArabicLetterType.Zain,
          ArabicLetterType.Ya,
          ArabicLetterType.Dal,
          ArabicLetterType.Space,
          ArabicLetterType.Fa,
          ArabicLetterType.Ya,
          ArabicLetterType.Ha
        )
      )

  case FormIXTemplate
      extends NamedTemplate(
        form = "IX",
        index = 9,
        subIndex = 0,
        word = ArabicWord(
          ArabicLetters.AlifHamzaBelowWithKasra,
          ArabicLetters.FaWithSukun,
          ArabicLetters.AinWithFatha,
          ArabicLetters.LamWithShaddaAndFatha,
          ArabicLetters.LetterSpace,
          ArabicLetters.YaWithFatha,
          ArabicLetters.FaWithSukun,
          ArabicLetters.AinWithFatha,
          ArabicLetters.LamWithShaddaAndFatha
        ),
        `type` = ArabicWord(
          ArabicLetterType.Fa,
          ArabicLetterType.Ain,
          ArabicLetterType.Lam,
          ArabicLetterType.Space,
          ArabicLetterType.Tha,
          ArabicLetterType.Lam,
          ArabicLetterType.Alif,
          ArabicLetterType.Tha,
          ArabicLetterType.Ya,
          ArabicLetterType.Space,
          ArabicLetterType.Meem,
          ArabicLetterType.Zain,
          ArabicLetterType.Ya,
          ArabicLetterType.Dal,
          ArabicLetterType.Space,
          ArabicLetterType.Fa,
          ArabicLetterType.Ya,
          ArabicLetterType.Ha
        )
      )

  case FormXTemplate
      extends NamedTemplate(
        form = "X",
        index = 10,
        subIndex = 0,
        word = ArabicWord(
          ArabicLetters.AlifHamzaBelowWithKasra,
          ArabicLetters.SeenWithSukun,
          ArabicLetters.TaWithFatha,
          ArabicLetters.FaWithSukun,
          ArabicLetters.AinWithFatha,
          ArabicLetters.LamWithFatha,
          ArabicLetters.LetterSpace,
          ArabicLetters.YaWithFatha,
          ArabicLetters.SeenWithSukun,
          ArabicLetters.TaWithFatha,
          ArabicLetters.FaWithSukun,
          ArabicLetters.AinWithKasra,
          ArabicLetters.LamWithDamma
        ),
        `type` = ArabicWord(
          ArabicLetterType.Fa,
          ArabicLetterType.Ain,
          ArabicLetterType.Lam,
          ArabicLetterType.Space,
          ArabicLetterType.Tha,
          ArabicLetterType.Lam,
          ArabicLetterType.Alif,
          ArabicLetterType.Tha,
          ArabicLetterType.Ya,
          ArabicLetterType.Space,
          ArabicLetterType.Meem,
          ArabicLetterType.Zain,
          ArabicLetterType.Ya,
          ArabicLetterType.Dal,
          ArabicLetterType.Space,
          ArabicLetterType.Fa,
          ArabicLetterType.Ya,
          ArabicLetterType.Ha
        )
      )

  override val code: String = s"Family $form"

}

object NamedTemplate {
  given ordering: Ordering[NamedTemplate] = (x: NamedTemplate, y: NamedTemplate) => x.index.compare(y.index)
}
