package com.alphasystem
package arabic
package morphologicalengine
package ui
package control
package skin

import arabic.model.ArabicWord
import arabic.morphologicalanalysis.ui.ArabicLabelView
import fx.ui.util.UIUserPreferences
import morphologicalengine.conjugation.model.{ ConjugationGroup, ConjugationTuple }
import control.ConjugationGroupView
import javafx.scene.control.SkinBase
import scalafx.Includes.*
import scalafx.geometry.{ NodeOrientation, Pos }
import scalafx.scene.layout.{ BorderPane, GridPane }
import scalafx.scene.paint.Color

/** Shared base for [[NounConjugationGroupSkin]] and [[VerbConjugationGroupSkin]]: builds the term label +
  * `GridPane`/`BorderPane` scaffolding common to both, and factors out the `ArabicLabelView` cell construction and
  * `ConjugationTuple` -> `ArabicWord` conversion helpers.
  *
  * Subclasses lay out their own cells in [[buildRows]] (called once, from [[initialize]]) and push `control.group`'s
  * data into those cells in [[refreshRows]] (called whenever `control.group` changes, and once up-front).
  *
  * NOTE: subclasses must call `initialize()` themselves, after their own cell fields have been constructed, since
  * [[buildRows]] is expected to reference those fields.
  */
abstract class ConjugationGroupSkin[G <: ConjugationGroup, C <: ConjugationGroupView[G]](
  control: C
)(using
  preferences: UIUserPreferences)
    extends SkinBase[C](control) {

  import ConjugationGroupSkin.*

  protected val termLabel: ArabicLabelView = arabicLabel(control.term, TermLabelWidth, CellHeight)
  termLabel.stroke = Color.DodgerBlue

  protected val gridPane: GridPane = new GridPane() {
    alignment = Pos.BaselineCenter
    nodeOrientation = NodeOrientation.RightToLeft
  }
  gridPane.add(termLabel, 0, 0, NumOfColumns, 1)

  protected def buildRows(): Unit

  protected def refreshRows(): Unit

  protected def initialize(): Unit = {
    buildRows()
    getChildren.add(new BorderPane() {
      center = gridPane
      BorderPane.setAlignment(gridPane, Pos.Center)
    })
    control.termProperty.onChange((_, _, nv) => termLabel.label = toArabicWord(nv))
    control.groupProperty.onChange((_, _, _) => refresh())
    refresh()
  }

  private def refresh(): Unit = {
    termLabel.label = toArabicWord(control.term)
    refreshRows()
  }

  protected def arabicLabel(
    word: ArabicWord,
    width: Double = CellWidth,
    height: Double = CellHeight
  ): ArabicLabelView = {
    val view = ArabicLabelView(word)
    // `ArabicLabelViewSkin` only reads the control's width/height once, at skin-creation time, so pref/min/max
    // sizing needs to be set explicitly for the parent GridPane's layout negotiation to respect it.
    view.setPrefWidth(width)
    view.setPrefHeight(height)
    view.setMinWidth(width)
    view.setMinHeight(height)
    view.setMaxWidth(width)
    view.setMaxHeight(height)
    // `disabledStroke` must be set before `disable` is flipped to `true`, otherwise the border color update
    // is missed and the cell renders with no visible border.
    view.font = preferences.arabicFont(24)
    view.disabledStroke = Color.LightGray
    view.disable = true
    view
  }

  protected def toArabicWord(value: String): ArabicWord =
    if Option(value).exists(_.trim.nonEmpty) then ArabicWord(value) else ArabicWord()

  protected def toArabicWord(value: ArabicWord): ArabicWord =
    if Option(value).isDefined then value else ArabicWord()

  protected def tupleWords(tuple: Option[ConjugationTuple]): (ArabicWord, ArabicWord, ArabicWord) =
    tuple match {
      case Some(t) => (toArabicWord(t.singular), toArabicWord(t.dual.getOrElse("")), toArabicWord(t.plural))
      case None    => (ArabicWord(), ArabicWord(), ArabicWord())
    }
}

object ConjugationGroupSkin {

  val NumOfColumns: Int = 3
  val CellWidth: Double = 128.0
  val CellHeight: Double = 64.0
  val TermLabelWidth: Double = 384.0
}
