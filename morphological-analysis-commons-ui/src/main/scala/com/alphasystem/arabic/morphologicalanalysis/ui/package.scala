package com.alphasystem
package arabic
package morphologicalanalysis

import fx.ui.util.FontUtilities
import morphology.graph.model.{ DependencyGraph, FontMetaInfo, GraphMetaInfo }
import scalafx.Includes.*
import scalafx.scene.paint.Color
import scalafx.scene.text.{ Font, FontPosture, FontWeight }
import scalafx.stage.Screen

package object ui {

  val DefaultGap: Double = 10.0

  private lazy val screenBounds = Screen.primary.visualBounds
  lazy val screenWidth: Double = screenBounds.width
  lazy val screenHeight: Double = screenBounds.height

  lazy val defaultArabicFont: FontMetaInfo =
    FontMetaInfo(
      id = "",
      family = FontUtilities.ArabicFontName,
      weight = FontWeight.Normal.name(),
      posture = FontPosture.Regular.name(),
      size = 14.0
    )

  lazy val defaultEnglishFont: FontMetaInfo =
    FontMetaInfo(
      id = "",
      family = FontUtilities.EnglishFontName,
      weight = FontWeight.Normal.name(),
      posture = FontPosture.Regular.name(),
      size = 10.0
    )

  lazy val defaultGraphMetaInfo: GraphMetaInfo =
    GraphMetaInfo(
      width = screenWidth * 0.80,
      terminalFont = defaultArabicFont,
      partOfSpeechFont = defaultArabicFont,
      translationFont = defaultEnglishFont
    )

  lazy val defaultDependencyGraph: DependencyGraph =
    DependencyGraph(chapterNumber = 0, metaInfo = defaultGraphMetaInfo)

  extension (src: GraphMetaInfo) {
    def toColor: Color = Color.web(src.backgroundColor)
  }

  extension (src: FontMetaInfo) {
    def toFont: Font =
      Font(src.family, FontWeight.findByName(src.weight), FontPosture.findByName(src.posture), src.size)
  }

  extension (src: Font) {

    private def toFontWeightNPosture(style: String): (FontWeight, FontPosture) = {
      val maybeWeight: Option[FontWeight] = None
      val maybePosture: Option[FontPosture] = None
      val value =
        style.split("").foldLeft((maybeWeight, maybePosture)) { case ((w, p), s) =>
          (w, p) match
            case (None, None)         => (Option(FontWeight.findByName(s)).orElse(Some(FontWeight.Normal)), None)
            case (Some(w1), None)     => (Some(w1), Option(FontPosture.findByName(s)).orElse(Some(FontPosture.Regular)))
            case (None, Some(p1))     => (Option(FontWeight.findByName(s)).orElse(Some(FontWeight.Normal)), Some(p1))
            case (Some(w1), Some(p1)) => (Some(w1), Some(p1))
        }

      (value._1.get, value._2.get)
    }
    def toFontMetaInfo: FontMetaInfo = {
      val weightNPosture = toFontWeightNPosture(src.style)
      FontMetaInfo("", src.family, weightNPosture._1.name(), weightNPosture._2.name(), src.size)
    }

    def toDisplayText: String = s"${src.family}, ${src.style}, ${src.size}"
  }

  extension (c: Color) {
    def toHex: String = {
      def toPaddedHexString(value: Double) = {
        val i = (value * 255).toInt
        if i <= 0 then "00" else i.toHexString.toUpperCase
      }

      s"#${toPaddedHexString(c.red)}${toPaddedHexString(c.green)}${toPaddedHexString(c.blue)}"
    }
  }
}
