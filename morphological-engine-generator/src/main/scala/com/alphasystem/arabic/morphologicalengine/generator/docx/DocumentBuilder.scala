package com.alphasystem
package arabic
package morphologicalengine
package generator
package docx

import morphologicalengine.conjugation.builder.ConjugationBuilder
import morphologicalengine.conjugation.model.{ ConjugationConfiguration, OutputFormat }
import generator.model.*
import openxml.builder.wml.WmlAdapter
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart

import java.nio.file.Path

class DocumentBuilder(
  override val chartConfiguration: ChartConfiguration,
  conjugationConfiguration: ConjugationConfiguration,
  outputFormat: OutputFormat,
  path: Path,
  inputs: ConjugationInput*)
    extends DocumentGenerator(chartConfiguration) {

  private val conjugationBuilder = ConjugationBuilder()

  import DocumentFormat.*
  override private[docx] def buildDocument(mdp: MainDocumentPart): Unit =
    chartConfiguration.format match
      case Classic                        => buildClassicDocument(mdp)
      case AbbreviateConjugationSingleRow => buildAbbreviatedSingleRowDocument(mdp)

  private def buildClassicDocument(mdp: MainDocumentPart): Unit = {
    if inputs.nonEmpty then {
      var sortedInputs = inputs.sortBy(input => (input.namedTemplate, input.rootLetters))
      sortedInputs =
        if chartConfiguration.sortDirection == SortDirection.Descending then sortedInputs.reverse else sortedInputs
      buildDocument(mdp, sortedInputs.head)
      sortedInputs.tail.foreach { input =>
        if chartConfiguration.showMorphologicalTermCaptionInDetailConjugation then
          mdp.addObject(WmlAdapter.getPageBreak)
        buildDocument(mdp, input)
      }
    }
  }

  private def buildDocument(mdp: MainDocumentPart, input: ConjugationInput): Unit = {
    val morphologicalChart = runConjugation(input)
    val generator =
      MorphologicalChartGenerator(chartConfiguration, morphologicalChart.copy(translation = input.translation))
    generator.buildDocument(mdp)
  }

  private def buildAbbreviatedSingleRowDocument(mdp: MainDocumentPart): Unit = {
    val inputsMap =
      inputs
        .groupBy(_.namedTemplate)
        .map { case (namedTemplate, values) =>
          val sorted = values.sortBy(_.rootLetters)
          if chartConfiguration.sortDirection == SortDirection.Descending then namedTemplate -> sorted.reverse
          else namedTemplate -> sorted
        }

    var sorted = inputsMap.keys.toSeq.sorted
    sorted = if chartConfiguration.sortDirection == SortDirection.Descending then sorted.reverse else sorted

    sorted.foreach { namedTemplate =>
      val abbreviatedConjugations = inputsMap(namedTemplate).map(runConjugation).flatMap { charts =>
        // TODO: populate title
        charts.abbreviatedConjugation
      }
      single_row.AbbreviatedConjugationGenerator(chartConfiguration, abbreviatedConjugations).buildDocument(mdp)
    }
  }

  private def runConjugation(input: ConjugationInput) =
    conjugationBuilder.doConjugation(
      namedTemplate = input.namedTemplate,
      conjugationConfiguration = conjugationConfiguration,
      outputFormat = outputFormat,
      firstRadical = input.firstRadical,
      secondRadical = input.secondRadical,
      thirdRadical = input.thirdRadical,
      fourthRadical = input.fourthRadical,
      verbalNounCodes = input.verbalNounCodes
    )

  def generateDocument(): Unit = createDocument(path, this)
}

object DocumentBuilder {
  def apply(
    chartConfiguration: ChartConfiguration,
    conjugationConfiguration: ConjugationConfiguration,
    outputFormat: OutputFormat,
    path: Path,
    inputs: ConjugationInput*
  ): DocumentBuilder =
    new DocumentBuilder(chartConfiguration, conjugationConfiguration, outputFormat, path, inputs*)
}
