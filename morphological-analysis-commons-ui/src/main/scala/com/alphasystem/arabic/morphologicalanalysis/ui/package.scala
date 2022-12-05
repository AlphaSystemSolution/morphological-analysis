package com.alphasystem
package arabic
package morphologicalanalysis

import com.alphasystem.arabic.morphologicalanalysis.morphology.model.{
  Location,
  NounPartOfSpeechType,
  RelationshipType,
  defaultLocation,
  defaultToken
}
import fx.ui.util.FontUtilities
import morphology.graph.model.*
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
      family = FontUtilities.ArabicFontName,
      weight = FontWeight.Normal.name(),
      posture = FontPosture.Regular.name(),
      size = 20.0
    )

  lazy val defaultPosArabicFont: FontMetaInfo =
    FontMetaInfo(
      family = FontUtilities.ArabicPosFontName,
      weight = FontWeight.Normal.name(),
      posture = FontPosture.Regular.name(),
      size = 10.0
    )

  lazy val defaultEnglishFont: FontMetaInfo =
    FontMetaInfo(
      family = FontUtilities.EnglishFontName,
      weight = FontWeight.Normal.name(),
      posture = FontPosture.Regular.name(),
      size = 12.0
    )

  lazy val defaultGraphMetaInfo: GraphMetaInfo =
    GraphMetaInfo(
      width = (roundToNearest20(screenWidth * 0.80)).toInt,
      terminalFont = defaultArabicFont,
      partOfSpeechFont = defaultArabicFont,
      translationFont = defaultEnglishFont
    )

  lazy val defaultDependencyGraph: DependencyGraph =
    DependencyGraph(
      chapterNumber = 0,
      verseNumber = 0,
      chapterName = "",
      metaInfo = defaultGraphMetaInfo,
      tokens = Seq.empty,
      nodes = Seq.empty
    )

  lazy val defaultPartOfSpeechNodeMetaInfo: PartOfSpeechNodeMetaInfo =
    PartOfSpeechNodeMetaInfo(
      dependencyGraphId = defaultDependencyGraph.id,
      textPoint = Point(0, 0),
      translate = Point(0, 0),
      circle = Point(0, 0),
      font = defaultPosArabicFont,
      partOfSpeechNode = PartOfSpeechNode(location = defaultLocation)
    )

  lazy val defaultTerminalNodeMetaInfo: TerminalNodeMetaInfo =
    TerminalNodeMetaInfo(
      dependencyGraphId = defaultDependencyGraph.id,
      textPoint = Point(0, 0),
      translate = Point(0, 0),
      line = Line(Point(0, 0), Point(0, 0)),
      translationPoint = Point(0, 0),
      font = defaultArabicFont,
      translationFont = defaultEnglishFont,
      terminalNode = TerminalNode.createTerminalNode(defaultToken),
      partOfSpeechNodes = Seq.empty
    )

  lazy val defaultPhraseNodeMetaInfo: PhraseNodeMetaInfo =
    PhraseNodeMetaInfo(
      dependencyGraphId = defaultDependencyGraph.id,
      textPoint = Point(0, 0),
      translate = Point(0, 0),
      line = Line(Point(0, 0), Point(0, 0)),
      circle = Point(0, 0),
      phraseNode = PhraseNode(text = "", relationshipType = RelationshipType.None, fragments = Seq.empty),
      font = defaultPosArabicFont
    )

  lazy val defaultRelationshipNodeMetaInfo: RelationshipNodeMetaInfo =
    RelationshipNodeMetaInfo(
      dependencyGraphId = defaultDependencyGraph.id,
      textPoint = Point(0, 0),
      translate = Point(0, 0),
      control1 = Point(0, 0),
      control2 = Point(0, 0),
      t = Point(0, 0),
      font = defaultPosArabicFont,
      relationshipNode = RelationshipNode(
        text = "",
        relationshipType = RelationshipType.None,
        owner = defaultPartOfSpeechNodeMetaInfo.partOfSpeechNode,
        dependent = defaultPhraseNodeMetaInfo.phraseNode
      )
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
      FontMetaInfo(src.family, weightNPosture._1.name(), weightNPosture._2.name(), src.size)
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

  def roundToNearest20(d: Double): Double = math.ceil(d / 20) * 20.0
}
