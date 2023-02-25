package com.alphasystem
package arabic
package morphologicalengine
package generator
package docx

import morphologicalengine.conjugation.builder.ConjugationBuilder
import morphologicalengine.conjugation.model.ConjugationConfiguration
import generator.model.*
import openxml.builder.wml.WmlAdapter
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart

import java.nio.file.Path

class DocumentBuilder(
  override val chartConfiguration: ChartConfiguration,
  conjugationConfiguration: ConjugationConfiguration,
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
      val sortedInputs = sortInputs
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
    if inputs.nonEmpty then {
      val sortedInputs = sortInputs
      val abbreviatedConjugations = sortedInputs.map(runConjugation).flatMap(_.abbreviatedConjugation)
      single_row.AbbreviatedConjugationGenerator(chartConfiguration, abbreviatedConjugations).buildDocument(mdp)
    }
  }

  private def sortInputs = {
    (chartConfiguration.sortDirective, chartConfiguration.sortDirection) match {
      case (SortDirective.Type, SortDirection.Ascending)          => inputs.sortBy(_.namedTemplate)
      case (SortDirective.Type, SortDirection.Descending)         => inputs.sortBy(_.namedTemplate).reverse
      case (SortDirective.Alphabetical, SortDirection.Ascending)  => inputs.sortBy(_.rootLetters)
      case (SortDirective.Alphabetical, SortDirection.Descending) => inputs.sortBy(_.rootLetters).reverse
      case (_, _)                                                 => inputs
    }
  }

  private def runConjugation(input: ConjugationInput) =
    conjugationBuilder.doConjugation(
      namedTemplate = input.namedTemplate,
      conjugationConfiguration = conjugationConfiguration,
      outputFormat = input.outputFormat,
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
    path: Path,
    inputs: ConjugationInput*
  ): DocumentBuilder =
    new DocumentBuilder(chartConfiguration, conjugationConfiguration, path, inputs*)
}
