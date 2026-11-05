package com.alphasystem
package arabic
package examples_generator
package morphologicalchart

import arabic.morphologicalengine.conjugation.builder.ConjugationBuilder

import java.nio.file.{ Files, Path }
import scala.collection.mutable.ListBuffer
import scala.jdk.CollectionConverters.*

object PairedConjugationRequestGenerator {

  @deprecated
  def buildDocument(srcPath: Path, destPath: Path, attributes: String): Unit = {
    val conjugationBuilder = ConjugationBuilder()
    val pairedConjugationRequest = toPairedConjugationRequest(srcPath)
    val buffer = ListBuffer(attributes)
    pairedConjugationRequest.conjugations.map(runConjugation(conjugationBuilder, _)).foreach(buffer.addAll)
    Files.write(destPath, buffer.toSeq.asJava)
  }

  private[morphologicalchart] def runConjugation(conjugationBuilder: ConjugationBuilder, pairedConjugation: PairedConjugation) = {
    val userSettings = pairedConjugation.settings
    val overriddenSettings =
      Settings(
        showPronouns = userSettings.showPronouns,
        showNumbers = userSettings.showNumbers,
        showTermTypeCaption = userSettings.showTermTypeCaption,
        jussiveParticle = userSettings.jussiveParticle
      )
    val conjugationGenerator = ConjugationGenerator(conjugationBuilder, overriddenSettings, isNestedTable = true)
    conjugationGenerator.buildPairedConjugation(pairedConjugation.tag, pairedConjugation.left, pairedConjugation.right)
  }
}
