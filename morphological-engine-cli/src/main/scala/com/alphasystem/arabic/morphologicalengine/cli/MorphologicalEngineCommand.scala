package com.alphasystem
package arabic
package morphologicalengine
package cli

import morphologicalengine.conjugation.model.OutputFormat
import morphologicalengine.generator.docx.DocumentBuilder
import org.rogach.scallop.{ ScallopOption, Subcommand }

import java.nio.file.Path

class MorphologicalEngineCommand extends Subcommand("generate-doc") {

  banner("Conjugate given root letters and generate document")

  val inputFile: ScallopOption[Path] = opt[Path](
    descr = "File containing input data, must be in '.json' format",
    required = true
  )

  val outFile: ScallopOption[Path] = opt[Path](
    descr = "File name of resulting document, if not provided then 'inputFile.docx' will be used",
    required = false
  )

  def run(): Unit = {
    val inputPath = inputFile()

    val config = toBuilderConfig(inputPath)
    val documentBuilder = DocumentBuilder(
      chartConfiguration = config.chartConfiguration,
      conjugationConfiguration = config.conjugationConfiguration,
      outputFormat = OutputFormat.Unicode,
      path = outFile.toOption.getOrElse(inputPath),
      inputs = config.inputs*
    )

    documentBuilder.generateDocument()
  }
}
