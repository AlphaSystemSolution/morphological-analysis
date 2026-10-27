package com.alphasystem
package arabic
package cli
package examples

import arabic.examples_generator.examples.DocumentGenerator
import arabic.examples_generator.model.toExampleData

class GenerateExampleDocument extends BaseCommand("examples") {

  banner("Generate examples document from yml file")

  override def buildDocument(): Unit =
    DocumentGenerator.generateDocument(
      toExampleData(srcPath()),
      destPath(),
      false,
      readAsciidocAttributes(attributesPath.toOption)
    )
}

object GenerateExampleDocument {
  def apply(): GenerateExampleDocument = new GenerateExampleDocument()
}
