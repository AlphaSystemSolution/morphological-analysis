package com.alphasystem
package arabic
package morphologicalanalysis

import com.alphasystem.arabic.morphologicalanalysis.morphology.model.RelationshipType
import fx.ui.util.FontUtilities
import morphology.graph.model.{
  DependencyGraph,
  FontMetaInfo,
  GraphMetaInfo,
  HiddenNode,
  PartOfSpeechNode,
  PhraseNode,
  ReferenceNode,
  RelationshipNode,
  TerminalNode
}
import scalafx.Includes.*
import scalafx.scene.Node
import scalafx.scene.control.TitledPane
import scalafx.scene.paint.Color
import scalafx.scene.text.{ Font, FontPosture, FontWeight }
import scalafx.stage.Screen

import java.util.UUID

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
      size = 20.0
    )

  lazy val defaultPosArabicFont: FontMetaInfo =
    FontMetaInfo(
      id = "",
      family = FontUtilities.ArabicPosFontName,
      weight = FontWeight.Normal.name(),
      posture = FontPosture.Regular.name(),
      size = 10.0
    )

  lazy val defaultEnglishFont: FontMetaInfo =
    FontMetaInfo(
      id = "",
      family = FontUtilities.EnglishFontName,
      weight = FontWeight.Normal.name(),
      posture = FontPosture.Regular.name(),
      size = 12.0
    )

  lazy val defaultGraphMetaInfo: GraphMetaInfo =
    GraphMetaInfo(
      width = (screenWidth * 0.80).toInt,
      terminalFont = defaultArabicFont,
      partOfSpeechFont = defaultArabicFont,
      translationFont = defaultEnglishFont
    )

  lazy val defaultDependencyGraph: DependencyGraph =
    DependencyGraph(chapterNumber = 0, metaInfo = defaultGraphMetaInfo)

  lazy val defaultHiddenNode: HiddenNode =
    HiddenNode(
      dependencyGraphId = defaultDependencyGraph.id,
      chapterNumber = 0,
      verseNumber = 0,
      tokenNumber = 0,
      version = 1,
      text = "",
      x = 0,
      y = 0,
      translateX = 0,
      translateY = 0,
      x1 = 0,
      y1 = 0,
      x2 = 0,
      y2 = 0,
      translationText = "",
      translationX = 0,
      translationY = 0,
      tokenId = "",
      font = defaultArabicFont,
      translationFont = defaultEnglishFont
    )

  lazy val defaultPartOfSpeechNode: PartOfSpeechNode =
    PartOfSpeechNode(
      dependencyGraphId = defaultDependencyGraph.id,
      chapterNumber = 0,
      verseNumber = 0,
      tokenNumber = 0,
      version = 0,
      text = "",
      x = 0,
      y = 0,
      translateX = 0,
      translateY = 0,
      x1 = 0,
      y1 = 0,
      x2 = 0,
      y2 = 0,
      cx = 0,
      cy = 0,
      font = defaultPosArabicFont,
      linkId = "",
      hidden = false
    )

  lazy val defaultPhraseNode: PhraseNode =
    PhraseNode(
      dependencyGraphId = defaultDependencyGraph.id,
      chapterNumber = 0,
      verseNumber = 0,
      tokenNumber = 0,
      version = 1,
      text = "",
      x = 0,
      y = 0,
      x1 = 0,
      y1 = 0,
      x2 = 0,
      y2 = 0,
      cx = 0,
      cy = 0,
      translateX = 0,
      translateY = 0,
      linkId = "",
      font = defaultArabicFont
    )

  lazy val defaultReferenceNode: ReferenceNode =
    ReferenceNode(
      dependencyGraphId = defaultDependencyGraph.id,
      chapterNumber = 0,
      verseNumber = 0,
      tokenNumber = 0,
      version = 1,
      text = "",
      x = 0,
      y = 0,
      translateX = 0,
      translateY = 0,
      x1 = 0,
      y1 = 0,
      x2 = 0,
      y2 = 0,
      translationText = "",
      translationX = 0,
      translationY = 0,
      tokenId = "",
      font = defaultArabicFont,
      translationFont = defaultEnglishFont
    )

  lazy val defaultRelationshipNode: RelationshipNode =
    RelationshipNode(
      dependencyGraphId = defaultDependencyGraph.id,
      relationshipType = RelationshipType.None,
      chapterNumber = 0,
      verseNumber = 0,
      tokenNumber = 0,
      version = 1,
      text = "",
      x = 0,
      y = 0,
      controlX1 = 0,
      controlY1 = 0,
      controlX2 = 0,
      controlY2 = 0,
      t1 = 0,
      t2 = 0,
      translateX = 0,
      translateY = 0,
      dependentId = "",
      ownerId = "",
      font = defaultArabicFont
    )

  lazy val defaultTerminalNode: TerminalNode =
    TerminalNode(
      dependencyGraphId = defaultDependencyGraph.id,
      chapterNumber = 0,
      verseNumber = 0,
      tokenNumber = 0,
      version = 1,
      text = "",
      x = 0,
      y = 0,
      translateX = 0,
      translateY = 0,
      x1 = 0,
      y1 = 0,
      x2 = 0,
      y2 = 0,
      translationText = "",
      translationX = 0,
      translationY = 0,
      tokenId = "",
      font = defaultArabicFont,
      translationFont = defaultEnglishFont
    )

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

  def createTitledPane(displayText: String, displayContent: Node): TitledPane =
    new TitledPane() {
      text = displayText
      content = displayContent
      expanded = true
      collapsible = true
      animated = true
    }
}
