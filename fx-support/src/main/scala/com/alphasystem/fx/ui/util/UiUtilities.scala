package com.alphasystem
package fx
package ui
package util

import scalafx.scene.{ Cursor, Node }

import scala.util.Try

object UiUtilities {

  def changeCursor(node: Node, cursor: Cursor): Unit = Try(node.scene.value).foreach(_.setCursor(cursor))

  def toWaitCursor(node: Node): Unit = changeCursor(node, Cursor.Wait)

  def toDefaultCursor(node: Node): Unit = changeCursor(node, Cursor.Default)
}
