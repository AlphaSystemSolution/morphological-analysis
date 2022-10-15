package com.alphasystem.morphologicalanalysis.ui

import com.alphasystem.arabic.model.ArabicSupport
import com.alphasystem.fx.ui.util.UIUserPreferences
import com.alphasystem.morphologicalanalysis.ui.skin.ArabicLabelViewSkin
import javafx.scene.control.{ Control, Skin }
import scalafx.beans.property.{
  BooleanProperty,
  ObjectProperty,
  ReadOnlyBooleanProperty,
  ReadOnlyBooleanWrapper,
  ReadOnlyStringWrapper
}
import scalafx.geometry.Pos
import scalafx.scene.paint.{ Color, Paint }
import scalafx.scene.text.{ Font, FontPosture, FontWeight }

import java.util.UUID

class ArabicLabelView(
  initialLabel: ArabicSupport
)(implicit pref: UIUserPreferences)
    extends Control {

  import ArabicLabelView.*

  private val selectedPropertyWrapper = ReadOnlyBooleanWrapper(false)

  val selectedProperty: ReadOnlyBooleanProperty =
    selectedPropertyWrapper.readOnlyProperty

  private[ui] val selectProperty = BooleanProperty(false)

  private[ui] val fontProperty = ObjectProperty[Font](this, "font")

  private[ui] val strokeProperty =
    ObjectProperty[Paint](this, "stroke", DefaultStroke)

  private[ui] val unselectedStrokeProperty =
    ObjectProperty[Paint](this, "unselectedStroke", Color.Black)

  private[ui] val selectedStrokeProperty =
    ObjectProperty[Paint](this, "selectedStroke", Color.Red)

  private[ui] val disabledStrokeProperty =
    ObjectProperty[Paint](this, "disabledStroke", Color.LightGray)

  private[ui] val alignmentProperty =
    ObjectProperty[Pos](this, "alignment", Pos.Center)

  private[ui] val labelProperty = ObjectProperty[ArabicSupport](this, "label")

  private val textPropertyWrapper = ReadOnlyStringWrapper("")

  private[ui] val textProperty = textPropertyWrapper.readOnlyProperty

  private[ui] val groupProperty =
    ObjectProperty[ArabicLabelToggleGroup](this, "group")

  // initialization
  setId(UUID.randomUUID().toString)
  labelProperty.onChange((_, _, nv) => {
    val label =
      if Option(nv).isDefined then nv.label
      else ""
    textPropertyWrapper.value = label
  })
  selectProperty.onChange((_, _, nv) => makeSelection(nv))
  label = initialLabel
  widthDelegate = DefaultWidth
  heightDelegate = DefaultHeight
  font = Font(pref.arabicFontName, FontWeight.Bold, FontPosture.Regular, 36.0)
  stroke = DefaultStroke
  unselectedStroke = Color.Black
  selectedStroke = Color.Red
  disabledStroke = Color.LightGray
  alignment = Pos.Center
  select = false

  setSkin(createDefaultSkin())

  def widthDelegate: Double = getWidth
  def widthDelegate_=(value: Double): Unit = setWidth(value)

  def heightDelegate: Double = getHeight
  def heightDelegate_=(value: Double): Unit = setHeight(value)

  def selected: Boolean = selectedProperty.value
  def selected_=(value: Boolean): Unit = selectedPropertyWrapper.value = value

  def select: Boolean = selectProperty.value
  def select_=(value: Boolean): Unit = selectProperty.value = value

  def font: Font = fontProperty.value
  def font_=(value: Font): Unit = fontProperty.value = value

  def stroke: Paint = strokeProperty.value
  def stroke_=(value: Paint): Unit = strokeProperty.value = value

  def unselectedStroke: Paint = unselectedStrokeProperty.value
  def unselectedStroke_=(value: Paint): Unit =
    unselectedStrokeProperty.value = value

  def selectedStroke: Paint = selectedStrokeProperty.value
  def selectedStroke_=(value: Paint): Unit =
    selectedStrokeProperty.value = value

  def disabledStroke: Paint = disabledStrokeProperty.value
  def disabledStroke_=(value: Paint): Unit =
    disabledStrokeProperty.value = value

  def alignment: Pos = alignmentProperty.value
  def alignment_=(value: Pos): Unit = alignmentProperty.value = value

  def label: ArabicSupport = labelProperty.value
  def label_=(value: ArabicSupport): Unit = labelProperty.value = value

  def group: ArabicLabelToggleGroup = groupProperty.value
  def group_=(value: ArabicLabelToggleGroup): Unit = {
    groupProperty.value = value
    group.toggles.addOne(this)
  }

  override def createDefaultSkin(): Skin[_] = ArabicLabelViewSkin(this)

  private def makeSelection(value: Boolean): Unit = {
    if isDisable then select = false
    else {
      if Option(group).isDefined then group.setSelected(this, value)
    }
  }
}

object ArabicLabelView {

  val DefaultWidth: Int = 64
  val DefaultHeight: Int = 64
  val DefaultStroke: Color = Color.Transparent

  def apply(
    initialLabel: ArabicSupport
  )(implicit pref: UIUserPreferences
  ): ArabicLabelView = new ArabicLabelView(initialLabel)

}
