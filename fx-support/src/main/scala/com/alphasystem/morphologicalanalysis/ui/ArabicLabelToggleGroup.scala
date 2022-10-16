package com.alphasystem.morphologicalanalysis.ui

import scalafx.Includes.*
import scalafx.beans.property.{ BooleanProperty, DoubleProperty, ObjectProperty }
import scalafx.collections.ObservableBuffer
import scalafx.scene.paint.Paint
import scalafx.scene.text.Font

class ArabicLabelToggleGroup(val groupName: String) {

  val widthProperty: DoubleProperty = DoubleProperty(0)

  val heightProperty: DoubleProperty = DoubleProperty(0)

  val fontProperty: ObjectProperty[Font] = ObjectProperty[Font](null, "font")

  val strokeProperty: ObjectProperty[Paint] =
    ObjectProperty[Paint](null, "stroke", ArabicLabelView.DefaultStroke)

  val disableProperty: BooleanProperty = BooleanProperty(false)

  val multipleSelectProperty: BooleanProperty = BooleanProperty(true)

  val selectedLabelProperty: ObjectProperty[ArabicLabelView] =
    ObjectProperty[ArabicLabelView](this, "stroke")

  val selectedValues: ObservableBuffer[ArabicLabelView] =
    ObservableBuffer.empty[ArabicLabelView]

  val toggles: ObservableBuffer[ArabicLabelView] =
    ObservableBuffer.empty[ArabicLabelView]

  // initialization
  multipleSelect = true

  fontProperty.onChange((_, _, nv) => if Option(nv).isDefined then toggles.foreach(label => label.font = nv))

  widthProperty.onChange((_, _, nv) =>
    if Option(nv).isDefined then toggles.foreach(label => label.widthDelegate = nv.doubleValue())
  )

  heightProperty.onChange((_, _, nv) =>
    if Option(nv).isDefined then toggles.foreach(label => label.heightDelegate = nv.doubleValue())
  )

  toggles.onChange((_, changes) =>
    changes.foreach {
      case ObservableBuffer.Add(_, added) =>
        // A Toggle can only be in one group at any one time. If the
        // group is changed, then remove the toggle from the old group prior to
        // being added to the new group.
        added
          .filter(label => Option(label.group).isDefined)
          .filterNot(_.group.groupName == groupName)
          .foreach { label =>
            label.group.toggles.remove(label)
            label.group = this

            label
              .selectedProperty
              .onChange((_, _, nv) =>
                if nv then selectedValues.add(label)
                else selectedValues.remove(label)
              )

            if width > 0 then label.widthDelegate = width
            if height > 0 then label.heightDelegate = height
            if Option(font).isDefined then label.font = font
            if Option(stroke).isDefined then label.stroke = stroke
            label.setDisable(disable)
          }

      case ObservableBuffer.Remove(_, removed) =>
        // Look through the removed toggles, and if any of them was the
        // one and only selected toggle, then we will clear the selected
        // toggle property.
        removed
          .filter(_.selected)
          .foreach(label => setSelected(label, flag = false))

      case ObservableBuffer.Reorder(_, _, _) => ()
      case ObservableBuffer.Update(_, _)     => ()
    }
  )

  def width: Double = widthProperty.value

  def width_=(value: Double): Unit = widthProperty.value = value

  def height: Double = heightProperty.value

  def height_=(value: Double): Unit = heightProperty.value = value

  def font: Font = fontProperty.value

  def font_=(value: Font): Unit = fontProperty.value = value

  def stroke: Paint = strokeProperty.value

  def stroke_=(value: Paint): Unit = strokeProperty.value = value

  def disable: Boolean = disableProperty.value

  def disable_=(value: Boolean): Unit = disableProperty.value = value

  def multipleSelect: Boolean = multipleSelectProperty.value

  def multipleSelect_=(value: Boolean): Unit =
    multipleSelectProperty.value = value

  def selectedLabel: ArabicLabelView = selectedLabelProperty.value

  def selectedLabel_=(value: ArabicLabelView): Unit =
    selectedLabelProperty.value = value

  def setSelected(label: ArabicLabelView, flag: Boolean): Unit = {
    if Option(label).isDefined then {
      if flag && !multipleSelect then {
        if Option(selectedLabel).isDefined then selectedLabel.select = false
        selectedLabel = label
      }
      label.selected = flag
    }
  }

  def clearToggles(): Unit = toggles.clear()
}

object ArabicLabelToggleGroup {

  def apply(groupName: String): ArabicLabelToggleGroup =
    new ArabicLabelToggleGroup(groupName)
}
