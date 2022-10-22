package com.alphasystem
package arabic
package model

import java.lang.Enum
enum ArabicLetterType(val code: Char, val unicode: Char)
    extends Enum[ArabicLetterType]
    with ArabicCharacter(code, unicode)
    with ArabicSupport {

  case Hamza extends ArabicLetterType('\'', '\u0621')

  case AlifMaddah extends ArabicLetterType('|', '\u0622')

  case AlifHamzaAbove extends ArabicLetterType('>', '\u0623')

  case WawHamzaAbove extends ArabicLetterType('&', '\u0624')

  case AlifHamzaBelow extends ArabicLetterType('<', '\u0625')

  case YaHamzaAbove extends ArabicLetterType('}', '\u0626')

  case Alif extends ArabicLetterType('A', '\u0627')

  case Ba extends ArabicLetterType('b', '\u0628')

  case TaMarbuta extends ArabicLetterType('p', '\u0629')

  case Ta extends ArabicLetterType('t', '\u062A')

  case Tha extends ArabicLetterType('v', '\u062B')

  case Jeem extends ArabicLetterType('j', '\u062C')

  case Hha extends ArabicLetterType('H', '\u062D')

  case Kha extends ArabicLetterType('x', '\u062E')

  case Dal extends ArabicLetterType('d', '\u062F')

  case Thal extends ArabicLetterType('*', '\u0630')

  case Ra extends ArabicLetterType('r', '\u0631')

  case Zain extends ArabicLetterType('z', '\u0632')

  case Seen extends ArabicLetterType('s', '\u0633')

  case Sheen extends ArabicLetterType('$', '\u0634')

  case Sad extends ArabicLetterType('S', '\u0635')

  case Ddad extends ArabicLetterType('D', '\u0636')

  case Tta extends ArabicLetterType('T', '\u0637')

  case Dtha extends ArabicLetterType('Z', '\u0638')

  case Ain extends ArabicLetterType('E', '\u0639')

  case Ghain extends ArabicLetterType('g', '\u063A')

  case Tatweel extends ArabicLetterType('_', '\u0640')

  case Fa extends ArabicLetterType('f', '\u0641')

  case Qaf extends ArabicLetterType('q', '\u0642')

  case Kaf extends ArabicLetterType('k', '\u0643')

  case Lam extends ArabicLetterType('l', '\u0644')

  case Meem extends ArabicLetterType('m', '\u0645')

  case Noon extends ArabicLetterType('n', '\u0646')

  case Ha extends ArabicLetterType('h', '\u0647')

  case Waw extends ArabicLetterType('w', '\u0648')

  case AlifMaksura extends ArabicLetterType('Y', '\u0649')

  case Ya extends ArabicLetterType('y', '\u064A')

  case AlifHamzatwasl extends ArabicLetterType('{', '\u0671')

  case HamzaAbove extends ArabicLetterType('#', '\u0654')

  case SmallHighLigatureSad extends ArabicLetterType('G', '\u06D6')

  case SmallHighLigatureQaf extends ArabicLetterType('Q', '\u06D7')

  case SmallHighLigatureMeem extends ArabicLetterType('M', '\u06D8')

  case SmallHighLigatureLamAlif extends ArabicLetterType('L', '\u06D9')

  case SmallHighJeem extends ArabicLetterType('J', '\u06DA')

  case SmallHighThreeDots extends ArabicLetterType('O', '\u06DB')

  case SmallHighSeen extends ArabicLetterType('C', '\u06DC')

  case EndOfAyah extends ArabicLetterType('V', '\u06DD')

  case StartOfRubElHizb extends ArabicLetterType('B', '\u06DE')

  case SmallHighRoundedZero extends ArabicLetterType('@', '\u06DF')

  case SmallHighUprightRectangularZero extends ArabicLetterType('"', '\u06E0')

  case SmallHighMeemIsolatedForm extends ArabicLetterType('[', '\u06E2')

  case SmallLowSeen extends ArabicLetterType(';', '\u06E3')

  case SmallWaw extends ArabicLetterType(',', '\u06E5')

  case SmallYa extends ArabicLetterType('.', '\u06E6')

  case SmallHighYeh extends ArabicLetterType('\\', '\u06E7')

  case SmallHighNoon extends ArabicLetterType('!', '\u06E8')

  case PlaceOfSajdah extends ArabicLetterType('P', '\u06E9')

  case EmptyCentreLowStop extends ArabicLetterType('-', '\u06EA')

  case EmptyCentreHighStop extends ArabicLetterType('+', '\u06EB')

  case RoundedHighStopWithFilledCentre extends ArabicLetterType('%', '\u06EC')

  case SmallLowMeem extends ArabicLetterType(']', '\u06ED')

  case Space extends ArabicLetterType(' ', '\u0020')

  case Comma extends ArabicLetterType('=', '\u060C')

  case OrnateLeftParenthesis extends ArabicLetterType('(', '\uFD3E')

  case OrnateRightParenthesis extends ArabicLetterType(')', '\uFD3F')

  case Asterisk extends ArabicLetterType('*', '\u002A')

  case ForwardSlash extends ArabicLetterType('/', '\u002F')

  case LeftParenthesis extends ArabicLetterType('(', '\u0028')

  case RightParenthesis extends ArabicLetterType(')', '\u0029')

  case SemiColon extends ArabicLetterType(':', '\u003A')

  case Zero extends ArabicLetterType('0', '\u0660')

  case One extends ArabicLetterType('1', '\u0661')

  case Two extends ArabicLetterType('2', '\u0662')

  case Three extends ArabicLetterType('3', '\u0663')

  case Four extends ArabicLetterType('4', '\u0664')

  case Five extends ArabicLetterType('5', '\u0665')

  case Six extends ArabicLetterType('6', '\u0666')

  case Seven extends ArabicLetterType('7', '\u0667')

  case Eight extends ArabicLetterType('8', '\u0668')

  case Nine extends ArabicLetterType('9', '\u0669')

  case NewLine extends ArabicLetterType('\n', '\n')

  override val label: String = ArabicWord(ArabicLetter(this)).unicode

  override def htmlCode: String = toHtmlCodeString(unicode)
}

object ArabicLetterType {
  lazy val CodesMap: Map[Char, ArabicLetterType] =
    ArabicLetterType.values.groupBy(_.code).map { case (c, types) =>
      c -> types.head
    }

  lazy val UnicodesMap: Map[Char, ArabicLetterType] =
    ArabicLetterType.values.groupBy(_.unicode).map { case (c, types) =>
      c -> types.head
    }
}
