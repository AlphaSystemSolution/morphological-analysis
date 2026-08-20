package com.alphasystem
package arabic
package cli
package morphologicalengine
package command

import arabic.morphologicalengine.asciidoc_generator.SingleConjugationRequestGenerator

class SingleConjugationCommand extends BaseCommand("single") {

  banner("Given root letters and template and conjugate single type")

  override def buildDocument(): Unit =
    SingleConjugationRequestGenerator.buildDocument(
      srcPath(),
      destPath(),
      readAsciidocAttributes(attributesPath.toOption)
    )
}

object SingleConjugationCommand {
  def apply(): SingleConjugationCommand = new SingleConjugationCommand
}
