package com.alphasystem
package arabic
package model

import ArabicLetterType.*
import DiacriticType.*

import java.lang.Enum

enum NamedTemplate(
  val form: String,
  val index: Integer,
  val subIndex: Integer,
  override val word: ArabicWord,
  val `type`: ArabicWord)
    extends Enum[NamedTemplate]
    with ArabicSupportEnum {

  case FormICategoryAGroupUTemplate
      extends NamedTemplate(
        "I",
        1,
        1,
        // label
        ArabicWord(
          ArabicLetter(Fa, Fatha),
          ArabicLetter(Ain, Fatha),
          ArabicLetter(Lam, Fatha),
          ArabicLetter(Space),
          ArabicLetter(Ya, Fatha),
          ArabicLetter(Fa, Sukun),
          ArabicLetter(Ain, Damma),
          ArabicLetter(Lam, Damma)
        ),
        // type
        ArabicWord(
          Fa,
          Ain,
          Lam,
          Space,
          Tha,
          Lam,
          Alif,
          Tha,
          Ya,
          Space,
          Meem,
          Jeem,
          Ra,
          Dal
        )
      )

  case FormICategoryAGroupITemplate
      extends NamedTemplate(
        "I",
        1,
        2,
        // label
        ArabicWord(
          ArabicLetter(Fa, Fatha),
          ArabicLetter(Ain, Fatha),
          ArabicLetter(Lam, Fatha),
          ArabicLetter(Space),
          ArabicLetter(Ya, Fatha),
          ArabicLetter(Fa, Sukun),
          ArabicLetter(Ain, Kasra),
          ArabicLetter(Lam, Damma)
        ),
        // type
        ArabicWord(
          Fa,
          Ain,
          Lam,
          Space,
          Tha,
          Lam,
          Alif,
          Tha,
          Ya,
          Space,
          Meem,
          Jeem,
          Ra,
          Dal
        )
      )

  case FormICategoryAGroupATemplate
      extends NamedTemplate(
        "I",
        1,
        3,
        // label
        ArabicWord(
          ArabicLetter(Fa, Fatha),
          ArabicLetter(Ain, Fatha),
          ArabicLetter(Lam, Fatha),
          ArabicLetter(Space),
          ArabicLetter(Ya, Fatha),
          ArabicLetter(Fa, Sukun),
          ArabicLetter(Ain, Fatha),
          ArabicLetter(Lam, Damma)
        ),
        // type
        ArabicWord(
          Fa,
          Ain,
          Lam,
          Space,
          Tha,
          Lam,
          Alif,
          Tha,
          Ya,
          Space,
          Meem,
          Jeem,
          Ra,
          Dal
        )
      )

  case FormICategoryUTemplate
      extends NamedTemplate(
        "I",
        1,
        4,
        // label
        ArabicWord(
          ArabicLetter(Fa, Fatha),
          ArabicLetter(Ain, Damma),
          ArabicLetter(Lam, Fatha),
          ArabicLetter(Space),
          ArabicLetter(Ya, Fatha),
          ArabicLetter(Fa, Sukun),
          ArabicLetter(Ain, Damma),
          ArabicLetter(Lam, Damma)
        ),
        // type
        ArabicWord(
          Fa,
          Ain,
          Lam,
          Space,
          Tha,
          Lam,
          Alif,
          Tha,
          Ya,
          Space,
          Meem,
          Jeem,
          Ra,
          Dal
        )
      )

  case FormICategoryIGroupATemplate
      extends NamedTemplate(
        "I",
        1,
        5,
        // label
        ArabicWord(
          ArabicLetter(Fa, Fatha),
          ArabicLetter(Ain, Kasra),
          ArabicLetter(Lam, Fatha),
          ArabicLetter(Space),
          ArabicLetter(Ya, Fatha),
          ArabicLetter(Fa, Sukun),
          ArabicLetter(Ain, Fatha),
          ArabicLetter(Lam, Damma)
        ),
        // type
        ArabicWord(
          Fa,
          Ain,
          Lam,
          Space,
          Tha,
          Lam,
          Alif,
          Tha,
          Ya,
          Space,
          Meem,
          Jeem,
          Ra,
          Dal
        )
      )

  case FormICategoryIGroupITemplate
      extends NamedTemplate(
        "I",
        1,
        6,
        // label
        ArabicWord(
          ArabicLetter(Fa, Fatha),
          ArabicLetter(Ain, Kasra),
          ArabicLetter(Lam, Fatha),
          ArabicLetter(Space),
          ArabicLetter(Ya, Fatha),
          ArabicLetter(Fa, Sukun),
          ArabicLetter(Ain, Kasra),
          ArabicLetter(Lam, Damma)
        ),
        // type
        ArabicWord(
          Fa,
          Ain,
          Lam,
          Space,
          Tha,
          Lam,
          Alif,
          Tha,
          Ya,
          Space,
          Meem,
          Jeem,
          Ra,
          Dal
        )
      )

  case FormIITemplate
      extends NamedTemplate(
        "II",
        2,
        0,
        // label
        ArabicWord(
          ArabicLetter(Fa, Fatha),
          ArabicLetter(Ain, Shadda, Fatha),
          ArabicLetter(Lam, Fatha),
          ArabicLetter(Space),
          ArabicLetter(Ya, Damma),
          ArabicLetter(Fa, Fatha),
          ArabicLetter(Ain, Shadda, Kasra),
          ArabicLetter(Lam, Damma)
        ),
        // type
        ArabicWord(
          Fa,
          Ain,
          Lam,
          Space,
          Tha,
          Lam,
          Alif,
          Tha,
          Ya,
          Space,
          Meem,
          Zain,
          Ya,
          Dal,
          Space,
          Fa,
          Ya,
          Ha
        )
      )

  case FormIIITemplate
      extends NamedTemplate(
        "III",
        2,
        1,
        // label
        ArabicWord(
          ArabicLetter(Fa, Fatha),
          ArabicLetter(Alif),
          ArabicLetter(Ain, Fatha),
          ArabicLetter(Lam, Fatha),
          ArabicLetter(Space),
          ArabicLetter(Ya, Damma),
          ArabicLetter(Fa, Fatha),
          ArabicLetter(Alif),
          ArabicLetter(Ain, Kasra),
          ArabicLetter(Lam, Damma)
        ),
        // type
        ArabicWord(
          Fa,
          Ain,
          Lam,
          Space,
          Tha,
          Lam,
          Alif,
          Tha,
          Ya,
          Space,
          Meem,
          Zain,
          Ya,
          Dal,
          Space,
          Fa,
          Ya,
          Ha
        )
      )

  case FormIVTemplate
      extends NamedTemplate(
        "IV",
        2,
        2,
        // label
        ArabicWord(
          ArabicLetter(AlifHamzaAbove, Fatha),
          ArabicLetter(Fa, Sukun),
          ArabicLetter(Ain, Fatha),
          ArabicLetter(Lam, Fatha),
          ArabicLetter(Space),
          ArabicLetter(Ya, Damma),
          ArabicLetter(Fa, Sukun),
          ArabicLetter(Ain, Kasra),
          ArabicLetter(Lam, Damma)
        ),
        // type
        ArabicWord(
          Fa,
          Ain,
          Lam,
          Space,
          Tha,
          Lam,
          Alif,
          Tha,
          Ya,
          Space,
          Meem,
          Zain,
          Ya,
          Dal,
          Space,
          Fa,
          Ya,
          Ha
        )
      )

  case FormVTemplate
      extends NamedTemplate(
        "V",
        2,
        3,
        // label
        ArabicWord(
          ArabicLetter(Ta, Fatha),
          ArabicLetter(Fa, Fatha),
          ArabicLetter(Ain, Shadda, Fatha),
          ArabicLetter(Lam, Fatha),
          ArabicLetter(Space),
          ArabicLetter(Ya, Fatha),
          ArabicLetter(Ta, Fatha),
          ArabicLetter(Fa, Fatha),
          ArabicLetter(Ain, Shadda, Fatha),
          ArabicLetter(Lam, Damma)
        ),
        // type
        ArabicWord(
          Fa,
          Ain,
          Lam,
          Space,
          Tha,
          Lam,
          Alif,
          Tha,
          Ya,
          Space,
          Meem,
          Zain,
          Ya,
          Dal,
          Space,
          Fa,
          Ya,
          Ha
        )
      )

  case FormVITemplate
      extends NamedTemplate(
        "VI",
        2,
        4,
        // label
        ArabicWord(
          ArabicLetter(Ta, Fatha),
          ArabicLetter(Fa, Fatha),
          ArabicLetter(Alif),
          ArabicLetter(Ain, Fatha),
          ArabicLetter(Lam, Fatha),
          ArabicLetter(Space),
          ArabicLetter(Ya, Fatha),
          ArabicLetter(Ta, Fatha),
          ArabicLetter(Fa, Fatha),
          ArabicLetter(Alif),
          ArabicLetter(Ain, Fatha),
          ArabicLetter(Lam, Damma)
        ),
        // type
        ArabicWord(
          Fa,
          Ain,
          Lam,
          Space,
          Tha,
          Lam,
          Alif,
          Tha,
          Ya,
          Space,
          Meem,
          Zain,
          Ya,
          Dal,
          Space,
          Fa,
          Ya,
          Ha
        )
      )

  case FormVIITemplate
      extends NamedTemplate(
        "VII",
        2,
        5,
        // label
        ArabicWord(
          ArabicLetter(AlifHamzaBelow, Kasra),
          ArabicLetter(Noon, Sukun),
          ArabicLetter(Fa, Fatha),
          ArabicLetter(Ain, Fatha),
          ArabicLetter(Lam, Fatha),
          ArabicLetter(Space),
          ArabicLetter(Ya, Fatha),
          ArabicLetter(Noon, Sukun),
          ArabicLetter(Fa, Fatha),
          ArabicLetter(Ain, Kasra),
          ArabicLetter(Lam, Damma)
        ),
        // type
        ArabicWord(
          Fa,
          Ain,
          Lam,
          Space,
          Tha,
          Lam,
          Alif,
          Tha,
          Ya,
          Space,
          Meem,
          Zain,
          Ya,
          Dal,
          Space,
          Fa,
          Ya,
          Ha
        )
      )

  case FormVIIITemplate
      extends NamedTemplate(
        "VIII",
        2,
        6,
        // label
        ArabicWord(
          ArabicLetter(AlifHamzaBelow, Kasra),
          ArabicLetter(Fa, Sukun),
          ArabicLetter(Ta, Fatha),
          ArabicLetter(Ain, Fatha),
          ArabicLetter(Lam, Fatha),
          ArabicLetter(Space),
          ArabicLetter(Ya, Fatha),
          ArabicLetter(Fa, Sukun),
          ArabicLetter(Ta, Fatha),
          ArabicLetter(Ain, Kasra),
          ArabicLetter(Lam, Damma)
        ),
        // type
        ArabicWord(
          Fa,
          Ain,
          Lam,
          Space,
          Tha,
          Lam,
          Alif,
          Tha,
          Ya,
          Space,
          Meem,
          Zain,
          Ya,
          Dal,
          Space,
          Fa,
          Ya,
          Ha
        )
      )

  case FormIXTemplate
      extends NamedTemplate(
        "IX",
        2,
        7,
        // label
        ArabicWord(
          ArabicLetter(AlifHamzaBelow, Kasra),
          ArabicLetter(Fa, Sukun),
          ArabicLetter(Ain, Fatha),
          ArabicLetter(Lam, Shadda, Fatha),
          ArabicLetter(Space),
          ArabicLetter(Ya, Fatha),
          ArabicLetter(Fa, Sukun),
          ArabicLetter(Ain, Fatha),
          ArabicLetter(Lam, Shadda, Fatha)
        ),
        // type
        ArabicWord(
          Fa,
          Ain,
          Lam,
          Space,
          Tha,
          Lam,
          Alif,
          Tha,
          Ya,
          Space,
          Meem,
          Zain,
          Ya,
          Dal,
          Space,
          Fa,
          Ya,
          Ha
        )
      )

  case FormXTemplate
      extends NamedTemplate(
        "X",
        2,
        8,
        // label
        ArabicWord(
          ArabicLetter(AlifHamzaBelow, Kasra),
          ArabicLetter(Seen, Sukun),
          ArabicLetter(Ta, Fatha),
          ArabicLetter(Fa, Sukun),
          ArabicLetter(Ain, Fatha),
          ArabicLetter(Lam, Fatha),
          ArabicLetter(Space),
          ArabicLetter(Ya, Fatha),
          ArabicLetter(Seen, Sukun),
          ArabicLetter(Ta, Fatha),
          ArabicLetter(Fa, Sukun),
          ArabicLetter(Ain, Kasra),
          ArabicLetter(Lam, Damma)
        ),
        // type
        ArabicWord(
          Fa,
          Ain,
          Lam,
          Space,
          Tha,
          Lam,
          Alif,
          Tha,
          Ya,
          Space,
          Meem,
          Zain,
          Ya,
          Dal,
          Space,
          Fa,
          Ya,
          Ha
        )
      )

  override val code: String = s"Family $form"

}
