package com.alphasystem
package arabic
package cli
package asciidoc
package v2

import java.nio.file.{ Files, Path }
import scala.collection.mutable.ListBuffer
import scala.jdk.CollectionConverters.*

object ExampleGenerator {

  def buildDocument(srcPath: Path, destPath: Path, attributesPath: Option[Path]): Path = {
    val data = toRequest(srcPath)
    val attributes = readAsciidocAttributes(attributesPath)
    val buffer = ListBuffer(attributes)
    data.examples.foreach(example => buffer.addAll(TableGenerator.generateTable(example, example.tag)))
    Files.write(destPath, buffer.toSeq.asJava)
  }
}
