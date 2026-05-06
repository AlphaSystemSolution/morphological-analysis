package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package builder

import arabic.model.ArabicLetters
import conjugation.forms.noun.VerbalNoun
import conjugation.rule.RuleEngine
import conjugation.forms.{ Form, NounSupport }
import conjugation.model.{
  AbbreviatedConjugation,
  ConjugationHeader,
  ConjugationInput,
  DetailedConjugation,
  MorphologicalChart,
  OutputFormat
}

class ConjugationBuilder {

  private val ruleProcessor = RuleEngine()

  def doConjugation(
    input: ConjugationInput,
    outputFormat: OutputFormat,
    removeAdverbs: Boolean = false,
    showAbbreviatedConjugation: Boolean = true,
    showDetailedConjugation: Boolean = true
  ): MorphologicalChart = {
    val namedTemplate = input.namedTemplate
    val conjugationConfiguration = input.conjugationConfiguration
    val verbalNounCodes = input.verbalNounCodes
    Form.fromNamedTemplate.get(namedTemplate) match
      case Some(form) =>
        val processingContext = ProcessingContext(
          namedTemplate = namedTemplate,
          outputFormat = outputFormat,
          firstRadical = input.rootLetters.firstRadical,
          secondRadical = input.rootLetters.secondRadical,
          thirdRadical = input.rootLetters.thirdRadical,
          fourthRadical = input.rootLetters.fourthRadical,
          skipRuleProcessing = conjugationConfiguration.skipRuleProcessing
        )

        val verbalNounInputs =
          if verbalNounCodes.nonEmpty then verbalNounCodes.flatMap(code => VerbalNoun.byCode.get(code))
          else form.verbalNouns

        val abbreviatedConjugation = doAbbreviatedConjugation(
          form,
          processingContext,
          conjugationConfiguration.removePassiveLine,
          removeAdverbs,
          verbalNounInputs
        )

        val maybeDetailedConjugation =
          if showDetailedConjugation then
            Some(
              doDetailConjugation(
                form,
                processingContext,
                conjugationConfiguration.removePassiveLine,
                removeAdverbs,
                verbalNounInputs
              )
            )
          else None

        MorphologicalChart(
          conjugationHeader = createConjugationHeader(form, processingContext, getTitle(abbreviatedConjugation)),
          abbreviatedConjugation = if showAbbreviatedConjugation then Some(abbreviatedConjugation) else None,
          detailedConjugation = maybeDetailedConjugation
        )
      case None =>
        throw new RuntimeException(s"No template found for: ${namedTemplate.code}")
  }

  private def createConjugationHeader(form: Form, processingContext: ProcessingContext, title: String) = {
    val outputFormat = processingContext.outputFormat
    val namedTemplate = processingContext.namedTemplate

    val chartMode = processingContext.wordStatus.toChartMode(namedTemplate)

    val verbTypeLabel =
      chartMode.weakVerbType match
        case Some(value) => chartMode.verbType.label.concat(ArabicLetters.WordComma, value.label).toValue(outputFormat)
        case None        => chartMode.verbType.label.toValue(outputFormat)

    ConjugationHeader(
      rootLetters = processingContext.toRootLetters,
      chartMode = chartMode,
      title = title,
      templateTypeLabel = namedTemplate.`type`.toValue(outputFormat),
      weightLabel = ArabicLetters.WeightLabel.concatWithSpace(namedTemplate.word).toValue(outputFormat),
      verbTypeLabel = verbTypeLabel
    )
  }

  private def doDetailConjugation(
    form: Form,
    processingContext: ProcessingContext,
    removePassiveLine: Boolean,
    removeAdverbs: Boolean,
    verbalNounInputs: Seq[NounSupport]
  ) = {
    val pastTense = form.pastTense.transform(ruleProcessor, processingContext)
    val presentTense = form.presentTense.transform(ruleProcessor, processingContext)
    val masculineActiveParticiple = form.activeParticipleMasculine.transform(ruleProcessor, processingContext)
    val feminineActiveParticiple = form.activeParticipleFeminine.transform(ruleProcessor, processingContext)
    val imperative = form.imperative.transform(ruleProcessor, processingContext)
    val forbidden = form.forbidden.transform(ruleProcessor, processingContext)

    val (pastPassiveTense, presentPassiveTense, masculinePassiveParticiple, passiveParticipleFeminine) =
      if removePassiveLine then {
        (None, None, None, None)
      } else {
        (
          form.pastPassiveTense.map(_.transform(ruleProcessor, processingContext)),
          form.presentPassiveTense.map(_.transform(ruleProcessor, processingContext)),
          form.passiveParticipleMasculine.map(_.transform(ruleProcessor, processingContext)),
          form.passiveParticipleFeminine.map(_.transform(ruleProcessor, processingContext))
        )
      }

    val verbalNouns = verbalNounInputs.map(_.transform(ruleProcessor, processingContext))

    val adverbs = if removeAdverbs then Seq.empty else form.adverbs.map(_.transform(ruleProcessor, processingContext))

    DetailedConjugation(
      pastTense = pastTense,
      presentTense = presentTense,
      masculineActiveParticiple = masculineActiveParticiple,
      feminineActiveParticiple = feminineActiveParticiple,
      imperative = imperative,
      forbidden = forbidden,
      pastPassiveTense = pastPassiveTense,
      presentPassiveTense = presentPassiveTense,
      masculinePassiveParticiple = masculinePassiveParticiple,
      femininePassiveParticiple = passiveParticipleFeminine,
      verbalNouns = verbalNouns,
      adverbs = adverbs
    )
  }

  private def doAbbreviatedConjugation(
    form: Form,
    processingContext: ProcessingContext,
    removePassiveLine: Boolean,
    removeAdverbs: Boolean,
    verbalNounInputs: Seq[NounSupport]
  ): AbbreviatedConjugation = {
    val pastTense = form.pastTense.defaultValue(ruleProcessor, processingContext)
    val presentTense = form.presentTense.defaultValue(ruleProcessor, processingContext)
    val activeParticiple = form.activeParticipleMasculine.defaultValue(ruleProcessor, processingContext)
    val imperative = form.imperative.defaultValue(ruleProcessor, processingContext)
    val forbidden = form.forbidden.defaultValue(ruleProcessor, processingContext)

    val (pastPassiveTense, presentPassiveTense, masculinePassiveParticiple) =
      if removePassiveLine then {
        (None, None, None)
      } else {
        (
          form.pastPassiveTense.map(_.defaultValue(ruleProcessor, processingContext)),
          form.presentPassiveTense.map(_.defaultValue(ruleProcessor, processingContext)),
          form.passiveParticipleMasculine.map(_.defaultValue(ruleProcessor, processingContext))
        )
      }

    val verbalNouns = verbalNounInputs.map(_.defaultValue(ruleProcessor, processingContext))

    val adverbs =
      if removeAdverbs then Seq.empty else form.adverbs.map(_.defaultValue(ruleProcessor, processingContext))

    AbbreviatedConjugation(
      pastTense = pastTense,
      presentTense = presentTense,
      activeParticiple = activeParticiple,
      imperative = imperative,
      forbidden = forbidden,
      pastPassiveTense = pastPassiveTense,
      presentPassiveTense = presentPassiveTense,
      passiveParticiple = masculinePassiveParticiple,
      verbalNouns = verbalNouns,
      adverbs = adverbs
    )
  }

  private def getTitle(abbreviatedConjugation: AbbreviatedConjugation) =
    s"${abbreviatedConjugation.pastTense} ${abbreviatedConjugation.presentTense}"

}

object ConjugationBuilder {
  def apply(): ConjugationBuilder = new ConjugationBuilder()
}
