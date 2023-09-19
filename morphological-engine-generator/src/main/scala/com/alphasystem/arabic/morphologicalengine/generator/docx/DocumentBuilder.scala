package com.alphasystem
package arabic
package morphologicalengine
package generator
package docx

import arabic.model.{ ArabicLetterType, ArabicWord }
import generator.model.{ ChartConfiguration, DocumentFormat, SortDirection }
import morphologicalengine.conjugation.ProcessingContext
import morphologicalengine.conjugation.forms.Form
import morphologicalengine.conjugation.rule.RuleEngine
import morphologicalengine.conjugation.builder.ConjugationBuilder
import morphologicalengine.conjugation.model.{ ConjugationInput, NamedTemplate, OutputFormat }
import openxml.builder.wml.{ TocGenerator, WmlAdapter, WmlBuilderFactory }
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart

import java.nio.file.Path

class DocumentBuilder(
  override val chartConfiguration: ChartConfiguration,
  path: Path,
  inputs: ConjugationInput*)
    extends DocumentGenerator(chartConfiguration) {

  private val removeAdverbs = chartConfiguration.removeAdverbs
  private val conjugationBuilder = ConjugationBuilder()
  private val ruleProcessor = RuleEngine()

  import DocumentFormat.*
  override private[docx] def buildDocument(mdp: MainDocumentPart): Unit =
    chartConfiguration.format match
      case Classic                        => buildClassicDocument(mdp, removeAdverbs)
      case AbbreviateConjugationSingleRow => buildAbbreviatedSingleRowDocument(mdp)

  private def buildClassicDocument(mdp: MainDocumentPart, removeAdverbs: Boolean): Unit = {
    if inputs.nonEmpty then {
      var sortedInputs = inputs.sortBy(input => (input.namedTemplate, input.rootLettersTuple))
      sortedInputs =
        if chartConfiguration.sortDirection == SortDirection.Descending then sortedInputs.reverse else sortedInputs

      val addToc = chartConfiguration.showToc && chartConfiguration.showAbbreviatedConjugation
      val tocHeading = "Table of Contents"
      val bookmarkName = tocHeading.replaceAll(" ", "_").toLowerCase()
      if addToc then {
        new TocGenerator()
          .tocStyle(TocStyle)
          .tocHeading(tocHeading)
          .mainDocumentPart(mdp)
          .instruction(" TOC \\o \"1-3\" \\h \\z \\t \"Arabic-Heading1,1\" ")
          .generateToc()
      }

      buildDocument(mdp, sortedInputs.head, removeAdverbs)
      sortedInputs.tail.foreach { input =>
        if addToc then addBackLink(mdp, bookmarkName)
        if chartConfiguration.showDetailedConjugation then mdp.addObject(WmlAdapter.getPageBreak)
        buildDocument(mdp, input, removeAdverbs)
      }
    }
  }

  private def buildDocument(mdp: MainDocumentPart, input: ConjugationInput, removeAdverbs: Boolean): Unit = {
    val morphologicalChart = runConjugation(removeAdverbs)(input)
    val generator =
      MorphologicalChartGenerator(chartConfiguration, morphologicalChart.copy(translation = input.translation))
    generator.buildDocument(mdp)
  }

  private def buildAbbreviatedSingleRowDocument(mdp: MainDocumentPart): Unit = {
    val inputsMap =
      inputs
        .groupBy(_.namedTemplate)
        .map { case (namedTemplate, values) =>
          val sorted = values.sortBy(_.rootLettersTuple)
          if chartConfiguration.sortDirection == SortDirection.Descending then namedTemplate -> sorted.reverse
          else namedTemplate -> sorted
        }

    var sorted = inputsMap.keys.toSeq.sorted
    sorted = if chartConfiguration.sortDirection == SortDirection.Descending then sorted.reverse else sorted

    val namedTemplate = sorted.head
    runConjugation(namedTemplate, removeAdverbs, mdp, inputsMap(namedTemplate))
    sorted.tail.foreach { namedTemplate =>
      mdp.addObject(WmlAdapter.getPageBreak)
      runConjugation(namedTemplate, removeAdverbs, mdp, inputsMap(namedTemplate))
    }
  }

  private def runConjugation(
    namedTemplate: NamedTemplate,
    removeAdverbs: Boolean,
    mdp: MainDocumentPart,
    values: Seq[ConjugationInput]
  ): Unit = {
    val form = Form.fromNamedTemplate(namedTemplate)

    val processingContext = ProcessingContext(
      namedTemplate = namedTemplate,
      outputFormat = OutputFormat.Unicode,
      firstRadical = ArabicLetterType.Fa,
      secondRadical = ArabicLetterType.Ain,
      thirdRadical = ArabicLetterType.Lam,
      skipRuleProcessing = values.head.conjugationConfiguration.skipRuleProcessing
    )

    val header =
      chapterText
        .concatWithSpace(
          ArabicWord(form.pastTense.defaultValue(ruleProcessor, processingContext)),
          ArabicWord(form.presentTense.defaultValue(ruleProcessor, processingContext))
        )
        .unicode

    val abbreviatedConjugations = values.map(runConjugation(removeAdverbs)).flatMap(_.abbreviatedConjugation)
    single_row
      .AbbreviatedConjugationGenerator(chartConfiguration, header, abbreviatedConjugations)
      .buildDocument(mdp)
  }

  private def runConjugation(removeAdverbs: Boolean)(input: ConjugationInput) =
    conjugationBuilder.doConjugation(
      input = input,
      outputFormat = OutputFormat.Unicode,
      removeAdverbs,
      showAbbreviatedConjugation = chartConfiguration.showAbbreviatedConjugation,
      showDetailedConjugation = chartConfiguration.showDetailedConjugation
    )

  private def addBackLink(mdp: MainDocumentPart, bookmarkName: String): Unit = {
    val backLink = WmlAdapter.addHyperlink(bookmarkName, "Back to Top")
    mdp.addObject(WmlBuilderFactory.getPBuilder().addContent(backLink).getObject)
  }

  def generateDocument(): Unit = createDocument(path, this, removeAdverbs)
}

object DocumentBuilder {
  def apply(
    chartConfiguration: ChartConfiguration,
    path: Path,
    inputs: ConjugationInput*
  ): DocumentBuilder = new DocumentBuilder(chartConfiguration, path, inputs*)
}
