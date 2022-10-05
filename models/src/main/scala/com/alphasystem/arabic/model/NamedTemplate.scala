package com.alphasystem.arabic.model

import com.alphasystem.arabic.model.ArabicLetterType.*
import com.alphasystem.arabic.model.DiacriticType.*

import java.lang.Enum
enum NamedTemplate(
  val form: String,
  val index: Integer,
  val subIndex: Integer,
  override val word: ArabicWord,
  val `type`: ArabicWord)
    extends Enum[NamedTemplate]
    with ArabicSupportEnum {

  case FORM_I_CATEGORY_A_GROUP_U_TEMPLATE
      extends NamedTemplate(
        "I",
        1,
        1,
        // label
        ArabicWord(
          ArabicLetter(FA, FATHA),
          ArabicLetter(AIN, FATHA),
          ArabicLetter(LAM, FATHA),
          ArabicLetter(SPACE),
          ArabicLetter(YA, FATHA),
          ArabicLetter(FA, SUKUN),
          ArabicLetter(AIN, DAMMA),
          ArabicLetter(LAM, DAMMA)
        ),
        // type
        ArabicWord(
          FA,
          AIN,
          LAM,
          SPACE,
          THA,
          LAM,
          ALIF,
          THA,
          YA,
          SPACE,
          MEEM,
          JEEM,
          RA,
          DAL
        )
      )

  case FORM_I_CATEGORY_A_GROUP_I_TEMPLATE
      extends NamedTemplate(
        "I",
        1,
        2,
        // label
        ArabicWord(
          ArabicLetter(FA, FATHA),
          ArabicLetter(AIN, FATHA),
          ArabicLetter(LAM, FATHA),
          ArabicLetter(SPACE),
          ArabicLetter(YA, FATHA),
          ArabicLetter(FA, SUKUN),
          ArabicLetter(AIN, KASRA),
          ArabicLetter(LAM, DAMMA)
        ),
        // type
        ArabicWord(
          FA,
          AIN,
          LAM,
          SPACE,
          THA,
          LAM,
          ALIF,
          THA,
          YA,
          SPACE,
          MEEM,
          JEEM,
          RA,
          DAL
        )
      )

  case FORM_I_CATEGORY_A_GROUP_A_TEMPLATE
      extends NamedTemplate(
        "I",
        1,
        3,
        // label
        ArabicWord(
          ArabicLetter(FA, FATHA),
          ArabicLetter(AIN, FATHA),
          ArabicLetter(LAM, FATHA),
          ArabicLetter(SPACE),
          ArabicLetter(YA, FATHA),
          ArabicLetter(FA, SUKUN),
          ArabicLetter(AIN, FATHA),
          ArabicLetter(LAM, DAMMA)
        ),
        // type
        ArabicWord(
          FA,
          AIN,
          LAM,
          SPACE,
          THA,
          LAM,
          ALIF,
          THA,
          YA,
          SPACE,
          MEEM,
          JEEM,
          RA,
          DAL
        )
      )

  case FORM_I_CATEGORY_U_TEMPLATE
      extends NamedTemplate(
        "I",
        1,
        4,
        // label
        ArabicWord(
          ArabicLetter(FA, FATHA),
          ArabicLetter(AIN, DAMMA),
          ArabicLetter(LAM, FATHA),
          ArabicLetter(SPACE),
          ArabicLetter(YA, FATHA),
          ArabicLetter(FA, SUKUN),
          ArabicLetter(AIN, DAMMA),
          ArabicLetter(LAM, DAMMA)
        ),
        // type
        ArabicWord(
          FA,
          AIN,
          LAM,
          SPACE,
          THA,
          LAM,
          ALIF,
          THA,
          YA,
          SPACE,
          MEEM,
          JEEM,
          RA,
          DAL
        )
      )

  case FORM_I_CATEGORY_I_GROUP_A_TEMPLATE
      extends NamedTemplate(
        "I",
        1,
        5,
        // label
        ArabicWord(
          ArabicLetter(FA, FATHA),
          ArabicLetter(AIN, KASRA),
          ArabicLetter(LAM, FATHA),
          ArabicLetter(SPACE),
          ArabicLetter(YA, FATHA),
          ArabicLetter(FA, SUKUN),
          ArabicLetter(AIN, FATHA),
          ArabicLetter(LAM, DAMMA)
        ),
        // type
        ArabicWord(
          FA,
          AIN,
          LAM,
          SPACE,
          THA,
          LAM,
          ALIF,
          THA,
          YA,
          SPACE,
          MEEM,
          JEEM,
          RA,
          DAL
        )
      )

  case FORM_I_CATEGORY_I_GROUP_I_TEMPLATE
      extends NamedTemplate(
        "I",
        1,
        6,
        // label
        ArabicWord(
          ArabicLetter(FA, FATHA),
          ArabicLetter(AIN, KASRA),
          ArabicLetter(LAM, FATHA),
          ArabicLetter(SPACE),
          ArabicLetter(YA, FATHA),
          ArabicLetter(FA, SUKUN),
          ArabicLetter(AIN, KASRA),
          ArabicLetter(LAM, DAMMA)
        ),
        // type
        ArabicWord(
          FA,
          AIN,
          LAM,
          SPACE,
          THA,
          LAM,
          ALIF,
          THA,
          YA,
          SPACE,
          MEEM,
          JEEM,
          RA,
          DAL
        )
      )

  case FORM_II_TEMPLATE
      extends NamedTemplate(
        "II",
        2,
        0,
        // label
        ArabicWord(
          ArabicLetter(FA, FATHA),
          ArabicLetter(AIN, SHADDA, FATHA),
          ArabicLetter(LAM, FATHA),
          ArabicLetter(SPACE),
          ArabicLetter(YA, DAMMA),
          ArabicLetter(FA, FATHA),
          ArabicLetter(AIN, SHADDA, KASRA),
          ArabicLetter(LAM, DAMMA)
        ),
        // type
        ArabicWord(
          FA,
          AIN,
          LAM,
          SPACE,
          THA,
          LAM,
          ALIF,
          THA,
          YA,
          SPACE,
          MEEM,
          ZAIN,
          YA,
          DAL,
          SPACE,
          FA,
          YA,
          HA
        )
      )

  case FORM_III_TEMPLATE
      extends NamedTemplate(
        "III",
        2,
        1,
        // label
        ArabicWord(
          ArabicLetter(FA, FATHA),
          ArabicLetter(ALIF),
          ArabicLetter(AIN, FATHA),
          ArabicLetter(LAM, FATHA),
          ArabicLetter(SPACE),
          ArabicLetter(YA, DAMMA),
          ArabicLetter(FA, FATHA),
          ArabicLetter(ALIF),
          ArabicLetter(AIN, KASRA),
          ArabicLetter(LAM, DAMMA)
        ),
        // type
        ArabicWord(
          FA,
          AIN,
          LAM,
          SPACE,
          THA,
          LAM,
          ALIF,
          THA,
          YA,
          SPACE,
          MEEM,
          ZAIN,
          YA,
          DAL,
          SPACE,
          FA,
          YA,
          HA
        )
      )

  case FORM_IV_TEMPLATE
      extends NamedTemplate(
        "IV",
        2,
        2,
        // label
        ArabicWord(
          ArabicLetter(ALIF_HAMZA_ABOVE, FATHA),
          ArabicLetter(FA, SUKUN),
          ArabicLetter(AIN, FATHA),
          ArabicLetter(LAM, FATHA),
          ArabicLetter(SPACE),
          ArabicLetter(YA, DAMMA),
          ArabicLetter(FA, SUKUN),
          ArabicLetter(AIN, KASRA),
          ArabicLetter(LAM, DAMMA)
        ),
        // type
        ArabicWord(
          FA,
          AIN,
          LAM,
          SPACE,
          THA,
          LAM,
          ALIF,
          THA,
          YA,
          SPACE,
          MEEM,
          ZAIN,
          YA,
          DAL,
          SPACE,
          FA,
          YA,
          HA
        )
      )

  case FORM_V_TEMPLATE
      extends NamedTemplate(
        "V",
        2,
        3,
        // label
        ArabicWord(
          ArabicLetter(TA, FATHA),
          ArabicLetter(FA, FATHA),
          ArabicLetter(AIN, SHADDA, FATHA),
          ArabicLetter(LAM, FATHA),
          ArabicLetter(SPACE),
          ArabicLetter(YA, FATHA),
          ArabicLetter(TA, FATHA),
          ArabicLetter(FA, FATHA),
          ArabicLetter(AIN, SHADDA, FATHA),
          ArabicLetter(LAM, DAMMA)
        ),
        // type
        ArabicWord(
          FA,
          AIN,
          LAM,
          SPACE,
          THA,
          LAM,
          ALIF,
          THA,
          YA,
          SPACE,
          MEEM,
          ZAIN,
          YA,
          DAL,
          SPACE,
          FA,
          YA,
          HA
        )
      )

  case FORM_VI_TEMPLATE
      extends NamedTemplate(
        "VI",
        2,
        4,
        // label
        ArabicWord(
          ArabicLetter(TA, FATHA),
          ArabicLetter(FA, FATHA),
          ArabicLetter(ALIF),
          ArabicLetter(AIN, FATHA),
          ArabicLetter(LAM, FATHA),
          ArabicLetter(SPACE),
          ArabicLetter(YA, FATHA),
          ArabicLetter(TA, FATHA),
          ArabicLetter(FA, FATHA),
          ArabicLetter(ALIF),
          ArabicLetter(AIN, FATHA),
          ArabicLetter(LAM, DAMMA)
        ),
        // type
        ArabicWord(
          FA,
          AIN,
          LAM,
          SPACE,
          THA,
          LAM,
          ALIF,
          THA,
          YA,
          SPACE,
          MEEM,
          ZAIN,
          YA,
          DAL,
          SPACE,
          FA,
          YA,
          HA
        )
      )

  case FORM_VII_TEMPLATE
      extends NamedTemplate(
        "VII",
        2,
        5,
        // label
        ArabicWord(
          ArabicLetter(ALIF_HAMZA_BELOW, KASRA),
          ArabicLetter(NOON, SUKUN),
          ArabicLetter(FA, FATHA),
          ArabicLetter(AIN, FATHA),
          ArabicLetter(LAM, FATHA),
          ArabicLetter(SPACE),
          ArabicLetter(YA, FATHA),
          ArabicLetter(NOON, SUKUN),
          ArabicLetter(FA, FATHA),
          ArabicLetter(AIN, KASRA),
          ArabicLetter(LAM, DAMMA)
        ),
        // type
        ArabicWord(
          FA,
          AIN,
          LAM,
          SPACE,
          THA,
          LAM,
          ALIF,
          THA,
          YA,
          SPACE,
          MEEM,
          ZAIN,
          YA,
          DAL,
          SPACE,
          FA,
          YA,
          HA
        )
      )

  case FORM_VIII_TEMPLATE
      extends NamedTemplate(
        "VIII",
        2,
        6,
        // label
        ArabicWord(
          ArabicLetter(ALIF_HAMZA_BELOW, KASRA),
          ArabicLetter(FA, SUKUN),
          ArabicLetter(TA, FATHA),
          ArabicLetter(AIN, FATHA),
          ArabicLetter(LAM, FATHA),
          ArabicLetter(SPACE),
          ArabicLetter(YA, FATHA),
          ArabicLetter(FA, SUKUN),
          ArabicLetter(TA, FATHA),
          ArabicLetter(AIN, KASRA),
          ArabicLetter(LAM, DAMMA)
        ),
        // type
        ArabicWord(
          FA,
          AIN,
          LAM,
          SPACE,
          THA,
          LAM,
          ALIF,
          THA,
          YA,
          SPACE,
          MEEM,
          ZAIN,
          YA,
          DAL,
          SPACE,
          FA,
          YA,
          HA
        )
      )

  case FORM_IX_TEMPLATE
      extends NamedTemplate(
        "IX",
        2,
        7,
        // label
        ArabicWord(
          ArabicLetter(ALIF_HAMZA_BELOW, KASRA),
          ArabicLetter(FA, SUKUN),
          ArabicLetter(AIN, FATHA),
          ArabicLetter(LAM, SHADDA, FATHA),
          ArabicLetter(SPACE),
          ArabicLetter(YA, FATHA),
          ArabicLetter(FA, SUKUN),
          ArabicLetter(AIN, FATHA),
          ArabicLetter(LAM, SHADDA, FATHA)
        ),
        // type
        ArabicWord(
          FA,
          AIN,
          LAM,
          SPACE,
          THA,
          LAM,
          ALIF,
          THA,
          YA,
          SPACE,
          MEEM,
          ZAIN,
          YA,
          DAL,
          SPACE,
          FA,
          YA,
          HA
        )
      )

  case FORM_X_TEMPLATE
      extends NamedTemplate(
        "X",
        2,
        8,
        // label
        ArabicWord(
          ArabicLetter(ALIF_HAMZA_BELOW, KASRA),
          ArabicLetter(SEEN, SUKUN),
          ArabicLetter(TA, FATHA),
          ArabicLetter(FA, SUKUN),
          ArabicLetter(AIN, FATHA),
          ArabicLetter(LAM, FATHA),
          ArabicLetter(SPACE),
          ArabicLetter(YA, FATHA),
          ArabicLetter(SEEN, SUKUN),
          ArabicLetter(TA, FATHA),
          ArabicLetter(FA, SUKUN),
          ArabicLetter(AIN, KASRA),
          ArabicLetter(LAM, DAMMA)
        ),
        // type
        ArabicWord(
          FA,
          AIN,
          LAM,
          SPACE,
          THA,
          LAM,
          ALIF,
          THA,
          YA,
          SPACE,
          MEEM,
          ZAIN,
          YA,
          DAL,
          SPACE,
          FA,
          YA,
          HA
        )
      )

  override val code: String = s"Family $form"

}
