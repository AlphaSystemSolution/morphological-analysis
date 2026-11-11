package com.alphasystem
package arabic
package examples_generator
package morphologicalchart

import arabic.morphologicalengine.conjugation.builder.ConjugationBuilder

object PairedConjugationRequestGenerator {

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
