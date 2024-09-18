package com.alphasystem
package arabic
package cli
package asciidoc

import arabic.morphologicalanalysis.morphology.persistence.cache.CacheFactory
import org.rogach.scallop.{ ScallopOption, Subcommand }

import java.nio.file.Path

class GenerateDocumentCommand(cacheFactory: CacheFactory) extends Subcommand("asciidoc") {

  // import concurrent.ExecutionContext.Implicits.global

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
    DocumentGenerator.buildDocument(cacheFactory, srcPath(), destPath(), attributesPath.toOption)

  private def sanitizeString(src: String) = if src.isBlank then "{nbsp}" else src

}

object GenerateDocumentCommand {
  def apply(cacheFactory: CacheFactory): GenerateDocumentCommand = new GenerateDocumentCommand(cacheFactory)
}
