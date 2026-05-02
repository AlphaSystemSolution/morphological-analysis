package com.alphasystem
package arabic
package cli
package morphologicalengine

import arabic.morphologicalengine.conjugation.builder.ConjugationBuilder

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
    val overriddenSettings = DisplaySettings(showPronouns = userSettings.showPronouns, showNumbers = userSettings.showNumbers)
    val conjugationGenerator = ConjugationGenerator(conjugationBuilder, overriddenSettings, isNestedTable = true)

    val buffer = ListBuffer[String]()
    val tag = pairedConjugation.tag
    buffer.addOne(s"// tag::$tag[]").addOne("[.TwoColumnConjugationTable]")
    buffer.addOne("""[cols="^.^1,^.^1", align="center", halign="center", valign="center"]""").addOne("|===").addOne("")

    val maybeRightTable = pairedConjugation.right.map(conjugationGenerator.runConjugation)
    val maybeLeftTable = pairedConjugation.left.map(conjugationGenerator.runConjugation)

    (maybeLeftTable, maybeRightTable) match {
      case (Some(leftTable), Some(rightTable)) =>
        buffer.addOne("a|").addAll(leftTable).addOne("").addOne("a|").addAll(rightTable).addOne("")
      case (None, Some(rightTable)) => buffer.addOne("|{nbsp}").addOne("").addOne("a|").addAll(rightTable).addOne("")
      case (Some(leftTable), None)  => buffer.addOne("|{nbsp}").addOne("").addOne("a|").addAll(leftTable).addOne("")
      case _                        =>
        // this should not happen
        throw new RuntimeException("Conjugations not found")
    }

    buffer.addOne("|===").addOne(s"// end::$tag[]").addOne("").toSeq
  }
}
