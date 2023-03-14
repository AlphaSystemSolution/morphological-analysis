package com.alphasystem
package arabic
package fx
package ui

import de.jensd.fx.glyphs.{ GlyphIcon, GlyphIcons }
import scalafx.Includes.*
import scalafx.scene.control.{ Button, ContentDisplay, MenuItem, Tooltip }
import scalafx.scene.input.KeyCodeCombination

import java.nio.file.{ Path, Paths }

package object util {

  private val UserDirName: String = System.getProperty("user.dir", ".")
  val UserHome: String = System.getProperty("user.home", UserDirName)
  val UserDir: Path = Paths.get(UserDirName)
  val UserHomeDir: Path = Paths.get(UserHome)

  def roundTo100(srcValue: Double): Double = ((srcValue.toInt + 99) / 100).toDouble * 100

  def createToolbarButton[T <: Enum[T] with GlyphIcons, V <: GlyphIcon[T]](
    icon: V,
    tooltipText: String,
    action: () => Unit
  ): Button =
    new Button() {
      graphic = icon
      contentDisplay = ContentDisplay.GraphicOnly
      tooltip = Tooltip(tooltipText)
      onAction = event => {
        action()
        event.consume()
      }
    }

  def createMenuItem(label: String, keyAccelerator: KeyCodeCombination, action: () => Unit): MenuItem = {
    new MenuItem() {
      text = label
      accelerator = keyAccelerator
      onAction = event => {
        action()
        event.consume()
      }
    }
  }

  def createMenuItem(label: String, action: () => Unit): MenuItem = {
    new MenuItem() {
      text = label
      onAction = event => {
        action()
        event.consume()
      }
    }
  }
}
