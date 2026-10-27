package com.alphasystem
package arabic
package examples_generator
package ui
package control
package table

import com.alphasystem.arabic.examples_generator.ui.model.{
  Frame,
  Grid,
  HorizontalAlignment,
  TableColumnInfo,
  TableProperties,
  VerticalAlignment
}
import javafx.scene.control.{ Control, Skin }
import scalafx.beans.binding.Bindings
import scalafx.beans.property.{ ObjectProperty, ReadOnlyBooleanProperty, ReadOnlyBooleanWrapper, StringProperty }
import scalafx.collections.ObservableBuffer

import java.util.UUID
import scala.util.Try

class TablePropertiesEditorView extends Control {

  private[table] val tagProperty = StringProperty("")
  private[table] val tableWidthProperty = StringProperty("100")
  private[table] val frameProperty = ObjectProperty[Frame](this, "frame")
  private[table] val gridProperty = ObjectProperty[Grid](this, "grid")
  private[table] val roleProperty = StringProperty("")
  private[table] val columnWidthProperty = StringProperty("")
  private[table] val horizontalAlignmentProperty = ObjectProperty[HorizontalAlignment](this, "horizontalAlignment")
  private[table] val verticalAlignmentProperty = ObjectProperty[VerticalAlignment](this, "verticalAlignment")
  private[table] val columnInfosProperty = ObservableBuffer[TableColumnInfo]()
  private val enableWrapperProperty = new ReadOnlyBooleanWrapper(this, "enable")
  private var originalColumnIds: Set[UUID] = Set.empty

  setSkin(createDefaultSkin())
  reset()
  enableWrapperProperty.bind(
    tagProperty.isNotEmpty.and(Bindings.createBooleanBinding(() => columnInfosProperty.nonEmpty, columnInfosProperty))
  )

  def tag: String = tagProperty.value
  private[table] def tag_=(value: String): Unit = tagProperty.value = value

  def tableWidth: String = tableWidthProperty.value
  private[table] def tableWidth_=(value: String): Unit = tableWidthProperty.value = value

  def frame: Frame = frameProperty.value
  private[table] def frame_=(value: Frame): Unit = frameProperty.value = value

  def grid: Grid = gridProperty.value
  private[table] def grid_=(value: Grid): Unit = gridProperty.value = value

  def role: String = roleProperty.value
  private[table] def role_=(value: String): Unit = roleProperty.value = value

  def columnWidth: String = columnWidthProperty.value
  private[table] def columnWidth_=(value: String): Unit = columnWidthProperty.value = value

  def horizontalAlignment: HorizontalAlignment = horizontalAlignmentProperty.value
  private[table] def horizontalAlignment_=(value: HorizontalAlignment): Unit = horizontalAlignmentProperty.value = value

  def verticalAlignment: VerticalAlignment = verticalAlignmentProperty.value
  private[table] def verticalAlignment_=(value: VerticalAlignment): Unit = verticalAlignmentProperty.value = value

  def enableProperty: ReadOnlyBooleanProperty = enableWrapperProperty.readOnlyProperty
  def enable: Boolean = enableWrapperProperty.value

  def columnInfos: Seq[TableColumnInfo] = columnInfosProperty.toSeq

  /** The columns added since the current properties were loaded, in the order they appear in the list. Empty when
    * creating a new table or when no columns were added.
    */
  def addedColumnInfos: Seq[TableColumnInfo] =
    if originalColumnIds.isEmpty then Seq.empty
    else columnInfosProperty.filterNot(column => originalColumnIds(column.id)).toSeq

  /** Reorders [[columnInfosProperty]] so that each newly added column lands at the requested 1-based position, where
    * `-1` appends at the end. Positions correspond to [[addedColumnInfos]] in order.
    */
  def applyInsertPositions(positions: Seq[Int]): Unit = {
    val added = addedColumnInfos
    if added.nonEmpty then {
      val reordered = added
        .zip(positions)
        .foldLeft(columnInfosProperty.filter(column => originalColumnIds(column.id)).toSeq) {
          case (columns, (info, -1))       => columns :+ info
          case (columns, (info, position)) => columns.patch(position - 1, Seq(info), 0)
        }
      columnInfosProperty.clear()
      columnInfosProperty.addAll(reordered)
    }
  }

  def tableProperties: TableProperties =
    TableProperties(
      tag = tag,
      columns = columnInfos,
      tableWidth = Try(tableWidth.toInt).getOrElse(100),
      frame = frame,
      grid = grid,
      role = if role.isEmpty then None else Some(role)
    )

  def tableProperties_=(value: TableProperties): Unit = {
    reset()
    tag = value.tag
    tableWidth = value.tableWidth.toString
    frame = value.frame
    grid = value.grid
    role = value.role.getOrElse("")
    columnInfosProperty.addAll(value.columns)
    originalColumnIds = value.columns.map(_.id).toSet
  }

  def reset(): Unit = {
    originalColumnIds = Set.empty
    tag = ""
    tableWidth = "100"
    frame = Frame.All
    grid = Grid.All
    role = ""
    horizontalAlignment = HorizontalAlignment.Center
    verticalAlignment = VerticalAlignment.Center
    columnInfosProperty.clear()
  }
  override def createDefaultSkin(): Skin[?] = skin.TablePropertiesEditorSkin(this)
}

object TablePropertiesEditorView {
  def apply(): TablePropertiesEditorView = new TablePropertiesEditorView()
}
