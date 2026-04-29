package com.alphasystem
package arabic
package cli
package morphologicalengine

import java.nio.file.{ Files, Path }
import scala.collection.mutable.ListBuffer
import scala.jdk.CollectionConverters.*

object SingleConjugationRequestGenerator {

  def buildDocument(srcPath: Path, destPath: Path, attributesPath: Option[Path]): Unit = {
    val singleConjugationRequest = toSingleConjugationRequest(srcPath)
    val attributes = readAsciidocAttributes(attributesPath)
    val buffer = ListBuffer(attributes)

    singleConjugationRequest
      .conjugations
      .map(new SingleConjugationGenerator(_))
      .map(_.buildDocument)
      .foreach(buffer.addAll)

    Files.write(destPath, buffer.toSeq.asJava)
  }
}
