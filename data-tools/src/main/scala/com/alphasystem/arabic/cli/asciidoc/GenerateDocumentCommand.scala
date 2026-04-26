package com.alphasystem
package arabic
package cli
package asciidoc

import asciidoc.v2.{ExampleGenerator, TableGenerator}
import org.rogach.scallop.{ScallopOption, Subcommand}

import java.nio.file.Path

class GenerateDocumentCommand extends Subcommand("asciidoc") {

  val srcPath: ScallopOption[Path] = opt[Path](
    descr = "Path to source json file",
    required = true
  )

  val destPath: ScallopOption[Path] = opt[Path](
    descr = "Path to dest adoc file",
    required = true
  )

  val attributesPath: ScallopOption[Path] = opt[Path](
    descr = "Path to header attributes file",
    default = None,
    required = false
  )

  def buildDocument(): Unit =
    ExampleGenerator.buildDocument(srcPath(), destPath(), attributesPath.toOption)

  private def sanitizeString(src: String) = if src.isBlank then "{nbsp}" else src

}

object GenerateDocumentCommand {
  def apply(): GenerateDocumentCommand = new GenerateDocumentCommand()
}
