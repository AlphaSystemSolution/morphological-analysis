package com.alphasystem
package arabic
package cli
package asciidoc

import asciidoc.v2.ExampleGenerator

class GenerateDocumentCommand extends BaseCommand("examples-old") {

  banner("Generate examples document from yml file")

  override def buildDocument(): Unit =
    ExampleGenerator.buildDocument(srcPath(), destPath(), attributesPath.toOption)
}

object GenerateDocumentCommand {
  def apply(): GenerateDocumentCommand = new GenerateDocumentCommand()
}
