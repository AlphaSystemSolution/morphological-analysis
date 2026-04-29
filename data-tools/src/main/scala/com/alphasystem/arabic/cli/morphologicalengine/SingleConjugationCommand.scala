package com.alphasystem
package arabic
package cli
package morphologicalengine

class SingleConjugationCommand extends BaseCommand("single-conjugation") {

  banner("Given root letters and template and conjugate single type")

  override def buildDocument(): Unit =
    SingleConjugationRequestGenerator.buildDocument(srcPath(), destPath(), attributesPath.toOption)
}
