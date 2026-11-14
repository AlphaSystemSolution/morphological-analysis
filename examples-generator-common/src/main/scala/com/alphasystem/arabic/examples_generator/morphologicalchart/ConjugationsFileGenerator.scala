package com.alphasystem
package arabic
package examples_generator
package morphologicalchart

import arabic.morphologicalengine.conjugation.builder.ConjugationBuilder

import java.nio.file.{ Files, Path }
import scala.collection.mutable.ListBuffer
import scala.jdk.CollectionConverters.*

object ConjugationsFileGenerator {

  private val conjugationBuilder = ConjugationBuilder()

  def generateDocument(
    conjugations: Conjugations,
    destPath: Path,
  ): Unit = {
    val attributes = asciidoc.loadAttributes(destPath.getParent)
    val buffer = ListBuffer(attributes)

    buffer.addAll(
      conjugations
        .single
        .map(_.flatMap(SingleConjugationRequestGenerator.runConjugation(conjugationBuilder, _)))
        .getOrElse(Seq.empty)
    )

    buffer.addAll(
      conjugations
        .paired
        .map(_.flatMap(PairedConjugationRequestGenerator.runConjugation(conjugationBuilder, _)))
        .getOrElse(Seq.empty)
    )

    buffer.addAll(
      conjugations
        .full
        .map(_.flatMap(MorphologicalChartGenerator.buildMorphologicalChart(conjugationBuilder, _)))
        .getOrElse(Seq.empty)
    )

    Files.write(destPath, buffer.toSeq.asJava)
  }
}
