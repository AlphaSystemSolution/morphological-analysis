package com.alphasystem
package arabic
package cli
package examples

class GenerateExampleDocument extends BaseCommand("examples") {

  banner("Generate examples document from yml file")

  override def buildDocument(): Unit =
    ExampleGenerator.buildDocument(srcPath(), destPath(), readAsciidocAttributes(attributesPath.toOption))
}

object GenerateExampleDocument {
  def apply(): GenerateExampleDocument = new GenerateExampleDocument()
}
