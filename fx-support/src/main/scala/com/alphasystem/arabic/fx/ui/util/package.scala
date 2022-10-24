package com.alphasystem
package arabic
package fx
package ui

import scalafx.scene.paint.Color

package object util {

  extension (c: Color) {
    def toHex: String = {
      val red = (c.red * 255).toInt.toHexString.toUpperCase
      val green = (c.green * 255).toInt.toHexString.toUpperCase
      val blue = (c.blue * 255).toInt.toHexString.toUpperCase
      s"$red$green$blue"
    }
  }

}
