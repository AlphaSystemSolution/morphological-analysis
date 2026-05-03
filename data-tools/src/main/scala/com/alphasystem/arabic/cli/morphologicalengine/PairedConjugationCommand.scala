package com.alphasystem
package arabic
package cli
package morphologicalengine

class PairedConjugationCommand extends BaseCommand("pair") {

  banner("Given root letters and template for two types and conjugate")

  override def buildDocument(): Unit =
    PairedConjugationRequestGenerator.buildDocument(srcPath(), destPath(), attributesPath.toOption)
}

object PairedConjugationCommand {
  def apply(): PairedConjugationCommand = new PairedConjugationCommand()
}
