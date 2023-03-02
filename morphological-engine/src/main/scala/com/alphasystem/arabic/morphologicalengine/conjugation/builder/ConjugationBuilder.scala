package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package builder

import arabic.model.ArabicLetters
import conjugation.forms.noun.VerbalNoun
import conjugation.model.NamedTemplate.*
import conjugation.rule.RuleEngine
import conjugation.forms.{ Form, NounSupport }
import conjugation.model.{
  AbbreviatedConjugation,
  ConjugationHeader,
  DetailedConjugation,
  MorphologicalChart,
  OutputFormat
}
import morphologicalengine.generator.model.{ ConjugationConfiguration, ConjugationInput }

class ConjugationBuilder {

  private val ruleProcessor = RuleEngine()

  def doConjugation(
    input: ConjugationInput,
    outputFormat: OutputFormat,
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

        val maybeDetailedConjugation =
          if showDetailedConjugation then
            Some(
              doDetailConjugation(
                form,
                processingContext,
                conjugationConfiguration.removePassiveLine,
                conjugationConfiguration.removeAdverbs,
                verbalNounInputs
              )
            )
          else None

        val maybeAbbreviatedConjugation =
          if showAbbreviatedConjugation then
            maybeDetailedConjugation
              .map(doAbbreviatedConjugation)
              .orElse(
                Some(
                  doAbbreviatedConjugation(
                    form,
                    processingContext,
                    conjugationConfiguration.removePassiveLine,
                    conjugationConfiguration.removeAdverbs,
                    verbalNounInputs
                  )
                )
              )
          else None

        MorphologicalChart(
          conjugationHeader = createConjugationHeader(form, processingContext),
          abbreviatedConjugation = maybeAbbreviatedConjugation,
          detailedConjugation = maybeDetailedConjugation
        )
      case None =>
        throw new RuntimeException(s"No template found for: ${namedTemplate.code}")
  }

  private def createConjugationHeader(form: Form, processingContext: ProcessingContext) = {
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
      title = getTitle(form, processingContext).toValue(outputFormat),
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

  private def doAbbreviatedConjugation(detailedConjugation: DetailedConjugation) = {
    AbbreviatedConjugation(
      pastTense = detailedConjugation.pastTense.masculineThirdPerson.map(_.singular).getOrElse(""),
      presentTense = detailedConjugation.presentTense.masculineThirdPerson.map(_.singular).getOrElse(""),
      activeParticiple = detailedConjugation.masculineActiveParticiple.nominative.singular,
      imperative = detailedConjugation.imperative.masculineSecondPerson.singular,
      forbidden = detailedConjugation.forbidden.masculineSecondPerson.singular,
      pastPassiveTense = detailedConjugation.pastPassiveTense.flatMap(_.masculineThirdPerson.map(_.singular)),
      presentPassiveTense = detailedConjugation.presentPassiveTense.flatMap(_.masculineThirdPerson.map(_.singular)),
      passiveParticiple = detailedConjugation.masculinePassiveParticiple.map(_.nominative.singular),
      verbalNouns = detailedConjugation.verbalNouns.map(_.accusative.singular),
      adverbs = detailedConjugation.adverbs.map(_.nominative.singular)
    )
  }

  private def getTitle(form: Form, processingContext: ProcessingContext) =
    form.template match
      case FormICategoryAGroupUTemplate | FormICategoryAGroupITemplate | FormICategoryAGroupATemplate |
          FormICategoryIGroupATemplate | FormICategoryIGroupITemplate | FormICategoryUTemplate =>
        val pastTense = form
          .pastTense
          .rootWord
          .transform(
            processingContext.firstRadical,
            processingContext.secondRadical,
            processingContext.thirdRadical,
            processingContext.fourthRadical
          )
          .derivedWord

        val presentTense = form
          .presentTense
          .rootWord
          .transform(
            processingContext.firstRadical,
            processingContext.secondRadical,
            processingContext.thirdRadical,
            processingContext.fourthRadical
          )
          .derivedWord

        pastTense.concatWithSpace(presentTense)

      case FormIITemplate | FormIIITemplate | FormIVTemplate | FormVTemplate | FormVITemplate | FormVIITemplate |
          FormVIIITemplate | FormIXTemplate | FormXTemplate =>
        form
          .verbalNouns
          .head
          .rootWord
          .transform(
            processingContext.firstRadical,
            processingContext.secondRadical,
            processingContext.thirdRadical,
            processingContext.fourthRadical
          )
          .derivedWord
}

object ConjugationBuilder {
  def apply(): ConjugationBuilder = new ConjugationBuilder()
}
