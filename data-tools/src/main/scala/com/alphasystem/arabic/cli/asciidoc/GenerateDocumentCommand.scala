package com.alphasystem
package arabic
package cli
package asciidoc

import asciidoc.v2.ExampleGenerator

class GenerateDocumentCommand extends BaseCommand("examples") {

  banner("Generate examples document from yml file")

  override def buildDocument(): Unit =
    ExampleGenerator.buildDocument(srcPath(), destPath(), attributesPath.toOption)

  private def sanitizeString(src: String) = if src.isBlank then "{nbsp}" else src

}

object GenerateDocumentCommand {
  def apply(): GenerateDocumentCommand = new GenerateDocumentCommand()
}
