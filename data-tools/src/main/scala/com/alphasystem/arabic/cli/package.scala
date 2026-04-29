package com.alphasystem
package arabic

import org.rogach.scallop.{ ValueConverter, singleArgConverter }

import java.nio.file.{ Path, Paths }
import scala.io.Source
import scala.util.Using

package object cli {

  implicit val pathConverter: ValueConverter[Path] = singleArgConverter[Path](arg => Paths.get(arg))

  def readAsciidocAttributes(attributesPath: Option[Path]): String =
    attributesPath match
      case Some(path) =>
        Using(Source.fromFile(path.toFile))(_.mkString).toOption.getOrElse("")
      case None => ""
}
