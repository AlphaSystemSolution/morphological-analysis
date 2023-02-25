package com.alphasystem
package arabic
package morphologicalengine
package generator
package docx

import openxml.builder.wml.WmlAdapter
import morphologicalengine.conjugation.builder.ConjugationBuilder
import generator.model.{ ChartConfiguration, ConjugationInput }
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart

import java.nio.file.Path

class DocumentBuilder(override val chartConfiguration: ChartConfiguration, path: Path, inputs: ConjugationInput*)
    extends DocumentGenerator(chartConfiguration) {

  private val conjugationBuilder = ConjugationBuilder()

  override private[docx] def buildDocument(mdp: MainDocumentPart): Unit =
    if inputs.nonEmpty then {
      buildDocument(mdp, inputs.head)
      inputs.tail.foreach { input =>
        if chartConfiguration.showMorphologicalTermCaptionInDetailConjugation then
          mdp.addObject(WmlAdapter.getPageBreak)
        buildDocument(mdp, input)
      }
    }

  private def buildDocument(mdp: MainDocumentPart, input: ConjugationInput): Unit = {
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

    val generator =
      MorphologicalChartGenerator(chartConfiguration, morphologicalChart.copy(translation = input.translation))
    generator.buildDocument(mdp)
  }

  def generateDocument(): Unit = createDocument(path, this)
}

object DocumentBuilder {
  def apply(chartConfiguration: ChartConfiguration, path: Path, inputs: ConjugationInput*): DocumentBuilder =
    new DocumentBuilder(chartConfiguration, path, inputs*)
}
