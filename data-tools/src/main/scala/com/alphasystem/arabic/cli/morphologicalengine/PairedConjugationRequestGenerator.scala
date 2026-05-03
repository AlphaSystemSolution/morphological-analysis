package com.alphasystem
package arabic
package cli
package morphologicalengine

import arabic.morphologicalengine.conjugation.builder.ConjugationBuilder
import com.alphasystem.arabic.morphologicalengine.conjugation.model.MorphologicalTermType

import java.nio.file.{ Files, Path }
import scala.collection.mutable.ListBuffer
import scala.jdk.CollectionConverters.*

object PairedConjugationRequestGenerator {

  def buildDocument(srcPath: Path, destPath: Path, attributesPath: Option[Path]): Unit = {
    val conjugationBuilder = ConjugationBuilder()
    val pairedConjugationRequest = toPairedConjugationRequest(srcPath)
    val attributes = readAsciidocAttributes(attributesPath)
    val buffer = ListBuffer(attributes)

    pairedConjugationRequest.conjugations.map(runConjugation(conjugationBuilder)).foreach(buffer.addAll)
    Files.write(destPath, buffer.toSeq.asJava)
  }

  private def runConjugation(conjugationBuilder: ConjugationBuilder)(pairedConjugation: PairedConjugation) = {
    val userSettings = pairedConjugation.settings
    val overriddenSettings =
      DisplaySettings(showPronouns = userSettings.showPronouns, showNumbers = userSettings.showNumbers)
    val conjugationGenerator = ConjugationGenerator(conjugationBuilder, overriddenSettings, isNestedTable = true)

    val buffer = ListBuffer[String]()
    val tag = pairedConjugation.tag
    buffer.addOne(s"// tag::$tag[]").addOne("[.TwoColumnConjugationTable]")
    buffer.addOne("""[cols="^.^1,^.^1", align="center", halign="center", valign="center"]""").addOne("|===").addOne("")
    buildTable(buffer, conjugationGenerator, pairedConjugation.rightTerm, pairedConjugation.right)
    buildTable(buffer, conjugationGenerator, pairedConjugation.leftTerm, pairedConjugation.left)
    buffer.addOne("|===").addOne(s"// end::$tag[]").addOne("").toSeq
  }

  private def buildTable(
    buffer: ListBuffer[String],
    conjugationGenerator: ConjugationGenerator,
    maybeMorphologicalTermType: Option[MorphologicalTermType],
    maybeRequest: Option[ConjugationRequest]
  ) = {
    (maybeMorphologicalTermType, maybeRequest) match {
      case (Some(morphologicalTermType), Some(conjugationRequest)) =>
        val generatedTable = conjugationGenerator.runConjugation(morphologicalTermType, conjugationRequest)
        buffer.addOne("a|").addAll(generatedTable).addOne("")
      case (None, Some(_)) => throw new RuntimeException("")
      case (Some(_), None) => throw new RuntimeException("")
      case _               => ()
    }

  }
}
