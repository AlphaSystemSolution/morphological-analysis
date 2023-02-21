package com.alphasystem
package arabic
package morphologicalengine
package generator
package docx

import morphologicalengine.conjugation.builder.ConjugationBuilder
import generator.model.{ ChartConfiguration, ConjugationInput }
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart

import java.nio.file.Path

class DocumentBuilder(override val chartConfiguration: ChartConfiguration, path: Path, inputs: ConjugationInput*)
    extends DocumentGenerator(chartConfiguration) {

  private val conjugationBuilder = ConjugationBuilder()

  override private[docx] def buildDocument(mdp: MainDocumentPart): Unit =
    inputs.foreach { input =>
      val morphologicalChart = conjugationBuilder.doConjugation(
        namedTemplate = input.namedTemplate,
        conjugationConfiguration = input.conjugationConfiguration,
        outputFormat = input.outputFormat,
        firstRadical = input.firstRadical,
        secondRadical = input.secondRadical,
        thirdRadical = input.thirdRadical,
        fourthRadical = input.fourthRadical,
        verbalNounCodes = input.verbalNounCodes
      )

      val generator = MorphologicalChartGenerator(chartConfiguration, morphologicalChart)
      generator.buildDocument(mdp)
    }

  def generateDocument(): Unit = createDocument(path, this)
}

object DocumentBuilder {
  def apply(chartConfiguration: ChartConfiguration, path: Path, inputs: ConjugationInput*): DocumentBuilder =
    new DocumentBuilder(chartConfiguration, path, inputs*)
}
