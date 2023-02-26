package com.alphasystem
package arabic
package morphologicalengine
package generator
package docx

import arabic.model.{ ArabicLetterType, ArabicWord }
import morphologicalengine.conjugation.ProcessingContext
import morphologicalengine.conjugation.forms.Form
import morphologicalengine.conjugation.rule.RuleEngine
import morphologicalengine.conjugation.builder.ConjugationBuilder
import morphologicalengine.conjugation.model.{ ConjugationConfiguration, NamedTemplate, OutputFormat }
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
  private val ruleProcessor = RuleEngine()

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

    val namedTemplate = sorted.head
    runConjugation(namedTemplate, mdp, inputsMap(namedTemplate))
    sorted.tail.foreach { namedTemplate =>
      mdp.addObject(WmlAdapter.getPageBreak)
      runConjugation(namedTemplate, mdp, inputsMap(namedTemplate))
    }
  }

  private def runConjugation(
    namedTemplate: NamedTemplate,
    mdp: MainDocumentPart,
    values: Seq[ConjugationInput]
  ): Unit = {
    val form = Form.fromNamedTemplate(namedTemplate)

    val processingContext = ProcessingContext(
      namedTemplate = namedTemplate,
      outputFormat = outputFormat,
      firstRadical = ArabicLetterType.Fa,
      secondRadical = ArabicLetterType.Ain,
      thirdRadical = ArabicLetterType.Lam,
      skipRuleProcessing = conjugationConfiguration.skipRuleProcessing
    )

    val header =
      chapterText
        .concatWithSpace(
          ArabicWord(form.pastTense.defaultValue(ruleProcessor, processingContext)),
          ArabicWord(form.presentTense.defaultValue(ruleProcessor, processingContext))
        )
        .unicode

    val abbreviatedConjugations = values.map(runConjugation).flatMap(_.abbreviatedConjugation)
    single_row.AbbreviatedConjugationGenerator(chartConfiguration, header, abbreviatedConjugations).buildDocument(mdp)
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
