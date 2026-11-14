package com.alphasystem
package arabic
package cli
package morphologicalengine
package command

import arabic.examples_generator.morphologicalchart.{ ConjugationsFileGenerator, toConjugations }

class GenerateConjugationsCommand extends BaseCommand("conjugations") {

  banner("Given root letters and template and generate conjugation(s) based on given template")

  override def buildDocument(): Unit =
    ConjugationsFileGenerator.generateDocument(conjugations = toConjugations(srcPath()), destPath = destPath())

}
