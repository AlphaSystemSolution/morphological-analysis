package com.alphasystem
package arabic
package examples_generator
package morphologicalchart

import arabic.morphologicalengine.conjugation.builder.ConjugationBuilder

import scala.collection.mutable.ListBuffer

object SingleConjugationRequestGenerator {

  private[morphologicalchart] def runConjugation(
    conjugationBuilder: ConjugationBuilder, singleConjugation: SingleConjugation): Seq[String] = {
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
