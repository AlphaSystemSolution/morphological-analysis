package com.alphasystem
package arabic
package fx
package ui
package util

import scalafx.scene.{ Cursor, Node }
import scalafx.stage.Popup

import java.nio.file.{ Files, Path }
import scala.util.{ Try, Using }

object UiUtilities {

  private def changeCursor(node: Node, cursor: Cursor): Unit = Try(node.scene.value).foreach(_.setCursor(cursor))

  def showPopupBelowOwner(popup: Popup, owner: javafx.scene.Node): Unit = {
    val bounds = owner.localToScreen(owner.getBoundsInLocal)
    popup.delegate.show(owner, bounds.getMinX, bounds.getMinY + bounds.getHeight)

    val screens =
      javafx.stage.Screen.getScreensForRectangle(bounds.getMinX, bounds.getMinY, bounds.getWidth, bounds.getHeight)
    val screenBounds =
      (if screens.isEmpty then javafx.stage.Screen.getPrimary else screens.get(0)).getVisualBounds
    val popupWidth = popup.width.value
    val maxX = math.max(screenBounds.getMinX, screenBounds.getMaxX - popupWidth)
    val alignedX = bounds.getMaxX - popupWidth
    popup.x = math.min(math.max(alignedX, screenBounds.getMinX), maxX)
  }

  def clearDirectory(rootPath: Path): Unit = {
    Using(Files.walk(rootPath)) { stream =>
      stream
        .filter(path => !path.equals(rootPath))
        .filter(path => Files.isRegularFile(path))
        .forEach(path => Try(Files.delete(path)))
    }
  }
}
