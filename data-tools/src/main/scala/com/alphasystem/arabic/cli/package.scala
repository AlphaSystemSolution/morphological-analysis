package com.alphasystem
package arabic

import org.rogach.scallop.{ ValueConverter, singleArgConverter }

import java.nio.file.{ Path, Paths }

package object cli {

  implicit val pathConverter: ValueConverter[Path] = singleArgConverter[Path](arg => Paths.get(arg))
}
