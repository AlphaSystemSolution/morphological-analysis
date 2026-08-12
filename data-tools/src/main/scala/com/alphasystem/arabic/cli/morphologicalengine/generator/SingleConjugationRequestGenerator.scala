package com.alphasystem
package arabic
package cli
package morphologicalengine
package generator

import morphologicalengine.generator.ConjugationGenerator
import morphologicalengine.toSingleConjugationRequest
import arabic.morphologicalengine.conjugation.builder.ConjugationBuilder
import arabic.morphologicalengine.generator.SingleConjugation

import java.nio.file.{ Files, Path }
import scala.collection.mutable.ListBuffer
import scala.jdk.CollectionConverters.*

object SingleConjugationRequestGenerator {

  def buildDocument(srcPath: Path, destPath: Path, attributes: String): Unit = {
    val conjugationBuilder = ConjugationBuilder()
    val singleConjugationRequest = toSingleConjugationRequest(srcPath)
    val buffer = ListBuffer(attributes)
    singleConjugationRequest.conjugations.map(runConjugation(conjugationBuilder)).foreach(buffer.addAll)
    Files.write(destPath, buffer.toSeq.asJava)
  }

  private def runConjugation(
    conjugationBuilder: ConjugationBuilder
  )(singleConjugation: SingleConjugation
  ): Seq[String] = {
    val settings = singleConjugation.settings
    val tableWidth = settings.tableWidth.getOrElse(60)
    val conjugationGenerator =
      ConjugationGenerator(conjugationBuilder, settings.copy(tableWidth = Some(tableWidth)))
    val buffer = ListBuffer[String]()
    val tag = singleConjugation.tag
    buffer.addOne(s"// tag::$tag[]").addOne("[.CenteredTable]")
    val table = conjugationGenerator.runConjugation(singleConjugation.request)
    buffer.addAll(table).addOne(s"// end::$tag[]").addOne("").toSeq
  }
}
