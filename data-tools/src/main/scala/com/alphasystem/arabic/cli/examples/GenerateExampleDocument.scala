package com.alphasystem
package arabic
package cli
package examples

import arabic.examples_generator.examples.ExampleFileGenerator
import arabic.examples_generator.model.toExampleData

class GenerateExampleDocument extends BaseCommand("examples") {

  banner("Generate examples document from yml file")

  override def buildDocument(): Unit =
    ExampleFileGenerator.generateDocument(toExampleData(srcPath()), destPath())
}

object GenerateExampleDocument {
  def apply(): GenerateExampleDocument = new GenerateExampleDocument()
}
