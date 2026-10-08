package com.alphasystem
package arabic
package fx
package ui

import de.jensd.fx.glyphs.{ GlyphIcon, GlyphIcons }
import javafx.event.{ ActionEvent, EventHandler }
import scalafx.Includes.*
import scalafx.scene.{ Cursor, Node }
import scalafx.scene.control.{ Button, ContentDisplay, Label, MenuItem, Tooltip }
import scalafx.scene.input.KeyCodeCombination
import scalafx.scene.layout.Region

import java.nio.file.{ Path, Paths }
import scala.util.Try

package object util {

  private val UserDirName: String = System.getProperty("user.dir", ".")
  val UserHome: String = System.getProperty("user.home", UserDirName)
  val UserDir: Path = Paths.get(UserDirName)
  val UserHomeDir: Path = Paths.get(UserHome)

  def roundTo100(srcValue: Double): Double = ((srcValue.toInt + 99) / 100).toDouble * 100

  def createToolbarButton[T <: Enum[T] & GlyphIcons, V <: GlyphIcon[T]](
    icon: V,
    tooltipText: String,
    action: () => Unit
  ): Button =
    new Button() {
      graphic = icon
      contentDisplay = ContentDisplay.GraphicOnly
      tooltip = Tooltip(tooltipText)
      delegate.setOnAction((event: ActionEvent) => action())
    }

  def createMenuItem(label: String, keyAccelerator: KeyCodeCombination, action: () => Unit): MenuItem = {
    new MenuItem() {
      text = label
      accelerator = keyAccelerator
      delegate.setOnAction((event: ActionEvent) => action())
    }
  }

  def createMenuItem(label: String, action: () => Unit): MenuItem = {
    new MenuItem() {
      text = label
      delegate.setOnAction((event: ActionEvent) => action())
    }
  }

  def createLabel(label: String): Label =
    new Label {
      text = label
      style = "-fx-font-weight: bold;"
      minWidth = Region.USE_PREF_SIZE
    }

  extension (node: Node) {
    private def changeCursor(cursor: Cursor): Unit = Try(node.scene.value).foreach(_.setCursor(cursor))
    def waitCursor(): Unit = changeCursor(Cursor.Wait)
    def defaultCursor(): Unit = changeCursor(Cursor.Default)
  }
}
