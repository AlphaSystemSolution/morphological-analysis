package com.alphasystem
package arabic
package morphologicalanalysis
package ui

import arabic.model.{ ArabicSupport, ArabicWord }
import arabic.fx.ui.util.*
import arabic.utils.*
import javafx.scene.layout.VBox
import scalafx.Includes.*
import scalafx.collections.ObservableBuffer
import scalafx.scene.layout.{ FlowPane, Region }

import scala.collection.mutable.ListBuffer

class ArabicSupportGroupPane[T <: ArabicSupport](
  numberOfColumns: Int,
  values: Seq[T]
)(using preferences: UIUserPreferences)
    extends VBox {

  import ArabicSupportGroupPane.*

  private val _selectedValues = ObservableBuffer.empty[T]
  private val toggleGroup = new ArabicLabelToggleGroup("list-cell-group") {
    width = 92
    height = 48
    font = preferences.arabicFont(24)
  }

  setSpacing(Spacing)
  initializeValues(values)
  setMinWidth(Region.USE_PREF_SIZE)
  setMaxWidth(Region.USE_PREF_SIZE)
  setPrefWidth(computedWidth + 120)
  getStyleClass.addAll("popup")

  private val buffer = ObservableBuffer.from[T](values)
  buffer.onChange((_, changes) => {
    changes.foreach {
      case ObservableBuffer.Add(_, added) =>
        getChildren.clear()
        initializeValues(added.toSeq)
      case _ =>
    }
  })

  def selectedValues: Seq[T] = _selectedValues.toSeq

  def reset(values: Seq[T]): Unit = toggleGroup.reset(values*)

  override def getUserAgentStylesheet: String = "styles/ui-common.css".asResourceUrl

  private def initializeValues(values: Seq[T]): Unit = {
    val buffer = ListBuffer[Option[T]](values.map(Option(_))*)
    while buffer.size % numberOfColumns != 0 do buffer.addOne(None)

    buffer.sliding(numberOfColumns, numberOfColumns).toList.foreach { subValues =>
      val flowPane = new FlowPane() {
        hgap = Spacing
      }
      subValues.reverse.foreach { maybeValue =>
        val label: ArabicLabelView =
          maybeValue match
            case Some(value) =>
              new ArabicLabelView(value) {
                group = toggleGroup
                selectedProperty.onChange((_, _, nv) => {
                  if nv then _selectedValues.addOne(value)
                  else _selectedValues.remove(value)
                })
              }

            case None =>
              new ArabicLabelView(ArabicWord()) {
                group = toggleGroup
              }

        flowPane.children.addOne(label)
      }
      getChildren.addOne(flowPane)
    }
  }

  private def computedWidth = roundTo100((toggleGroup.width + Spacing) * numberOfColumns)
}

object ArabicSupportGroupPane {

  private val DefaultNumberOfColumns = 8
  private val Spacing = 10

  def apply[T <: ArabicSupport](
    numberOfColumns: Int = DefaultNumberOfColumns,
    values: Seq[T]
  )(using preferences: UIUserPreferences
  ): ArabicSupportGroupPane[T] = new ArabicSupportGroupPane[T](numberOfColumns, values)
}
