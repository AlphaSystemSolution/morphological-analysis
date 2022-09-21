package com.alphasystem.arabic.model

import java.lang.Enum
enum ArabicLetterType(val code: Char, val unicode: Char)
    extends Enum[ArabicLetterType]
    with ArabicCharacter(code, unicode)
    with ArabicSupport {

  case HAMZA extends ArabicLetterType('\'', '\u0621')

  case ALIF_MADDAH extends ArabicLetterType('|', '\u0622')

  case ALIF_HAMZA_ABOVE extends ArabicLetterType('>', '\u0623')

  case WAW_HAMZA_ABOVE extends ArabicLetterType('&', '\u0624')

  case ALIF_HAMZA_BELOW extends ArabicLetterType('<', '\u0625')

  case YA_HAMZA_ABOVE extends ArabicLetterType('}', '\u0626')

  case ALIF extends ArabicLetterType('A', '\u0627')

  case BA extends ArabicLetterType('b', '\u0628')

  case TA_MARBUTA extends ArabicLetterType('p', '\u0629')

  case TA extends ArabicLetterType('t', '\u062A')

  case THA extends ArabicLetterType('v', '\u062B')

  case JEEM extends ArabicLetterType('j', '\u062C')

  case HHA extends ArabicLetterType('H', '\u062D')

  case KHA extends ArabicLetterType('x', '\u062E')

  case DAL extends ArabicLetterType('d', '\u062F')

  case THAL extends ArabicLetterType('*', '\u0630')

  case RA extends ArabicLetterType('r', '\u0631')

  case ZAIN extends ArabicLetterType('z', '\u0632')

  case SEEN extends ArabicLetterType('s', '\u0633')

  case SHEEN extends ArabicLetterType('$', '\u0634')

  case SAD extends ArabicLetterType('S', '\u0635')

  case DDAD extends ArabicLetterType('D', '\u0636')

  case TTA extends ArabicLetterType('T', '\u0637')

  case DTHA extends ArabicLetterType('Z', '\u0638')

  case AIN extends ArabicLetterType('E', '\u0639')

  case GHAIN extends ArabicLetterType('g', '\u063A')

  case TATWEEL extends ArabicLetterType('_', '\u0640')

  case FA extends ArabicLetterType('f', '\u0641')

  case QAF extends ArabicLetterType('q', '\u0642')

  case KAF extends ArabicLetterType('k', '\u0643')

  case LAM extends ArabicLetterType('l', '\u0644')

  case MEEM extends ArabicLetterType('m', '\u0645')

  case NOON extends ArabicLetterType('n', '\u0646')

  case HA extends ArabicLetterType('h', '\u0647')

  case WAW extends ArabicLetterType('w', '\u0648')

  case ALIF_MAKSURA extends ArabicLetterType('Y', '\u0649')

  case YA extends ArabicLetterType('y', '\u064A')

  case ALIF_HAMZATWASL extends ArabicLetterType('{', '\u0671')

  case HAMZA_ABOVE extends ArabicLetterType('#', '\u0654')

  case SMALL_HIGH_LIGATURE_SAD extends ArabicLetterType('G', '\u06D6')

  case SMALL_HIGH_LIGATURE_QAF extends ArabicLetterType('Q', '\u06D7')

  case SMALL_HIGH_LIGATURE_MEEM extends ArabicLetterType('M', '\u06D8')

  case SMALL_HIGH_LIGATURE_LAM_ALIF extends ArabicLetterType('L', '\u06D9')

  case SMALL_HIGH_JEEM extends ArabicLetterType('J', '\u06DA')

  case SMALL_HIGH_THREE_DOTS extends ArabicLetterType('O', '\u06DB')

  case SMALL_HIGH_SEEN extends ArabicLetterType('C', '\u06DC')

  case END_OF_AYAH extends ArabicLetterType('V', '\u06DD')

  case START_OF_RUB_EL_HIZB extends ArabicLetterType('B', '\u06DE')

  case SMALL_HIGH_ROUNDED_ZERO extends ArabicLetterType('@', '\u06DF')

  case SMALL_HIGH_UPRIGHT_RECTANGULAR_ZERO
      extends ArabicLetterType('"', '\u06E0')

  case SMALL_HIGH_MEEM_ISOLATED_FORM extends ArabicLetterType('[', '\u06E2')

  case SMALL_LOW_SEEN extends ArabicLetterType(';', '\u06E3')

  case SMALL_WAW extends ArabicLetterType(',', '\u06E5')

  case SMALL_YA extends ArabicLetterType('.', '\u06E6')

  case SMALL_HIGH_YEH extends ArabicLetterType('\\', '\u06E7')

  case SMALL_HIGH_NOON extends ArabicLetterType('!', '\u06E8')

  case PLACE_OF_SAJDAH extends ArabicLetterType('P', '\u06E9')

  case EMPTY_CENTRE_LOW_STOP extends ArabicLetterType('-', '\u06EA')

  case EMPTY_CENTRE_HIGH_STOP extends ArabicLetterType('+', '\u06EB')

  case ROUNDED_HIGH_STOP_WITH_FILLED_CENTRE
      extends ArabicLetterType('%', '\u06EC')

  case SMALL_LOW_MEEM extends ArabicLetterType(']', '\u06ED')

  case SPACE extends ArabicLetterType(' ', '\u0020')

  case COMMA extends ArabicLetterType('=', '\u060C')

  case ORNATE_LEFT_PARENTHESIS extends ArabicLetterType('(', '\uFD3E')

  case ORNATE_RIGHT_PARENTHESIS extends ArabicLetterType(')', '\uFD3F')

  case ASTERISK extends ArabicLetterType('*', '\u002A')

  case FORWARD_SLASH extends ArabicLetterType('/', '\u002F')

  case LEFT_PARENTHESIS extends ArabicLetterType('(', '\u0028')

  case RIGHT_PARENTHESIS extends ArabicLetterType(')', '\u0029')

  case SEMI_COLON extends ArabicLetterType(':', '\u003A')

  case ZERO extends ArabicLetterType('0', '\u0660')

  case ONE extends ArabicLetterType('1', '\u0661')

  case TWO extends ArabicLetterType('2', '\u0662')

  case THREE extends ArabicLetterType('3', '\u0663')

  case FOUR extends ArabicLetterType('4', '\u0664')

  case FIVE extends ArabicLetterType('5', '\u0665')

  case SIX extends ArabicLetterType('6', '\u0666')

  case SEVEN extends ArabicLetterType('7', '\u0667')

  case EIGHT extends ArabicLetterType('8', '\u0668')

  case NINE extends ArabicLetterType('9', '\u0669')

  case NEW_LINE extends ArabicLetterType('\n', '\n')

  override val label: ArabicWord = ArabicWord(ArabicLetter(this))

  override def htmlCode: String = toHtmlCodeString(unicode)
}
