package com.alphasystem
package arabic
package cli
package asciidoc
package v2

import java.nio.file.{ Files, Path }
import scala.collection.mutable.ListBuffer
import scala.io.Source
import scala.util.Using
import scala.jdk.CollectionConverters.*

object ExampleGenerator {

  def buildDocument(srcPath: Path, destPath: Path, attributesPath: Option[Path]): Path = {
    val data = toRequest(srcPath)

    val attributes =
      attributesPath match
        case Some(path) =>
          Using(Source.fromFile(path.toFile))(_.mkString).toOption.getOrElse("")
        case None => ""

    val buffer = ListBuffer(attributes)
    data.examples.foreach(example => buffer.addAll(TableGenerator.generateTable(example, example.tag)))
    Files.write(destPath, buffer.toSeq.asJava)
  }
}
