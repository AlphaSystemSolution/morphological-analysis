package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package rule
package processors

import conjugation.rule.RuleProcessor
import conjugation.model.internal.RootWord
import arabic.model.SarfMemberType
import conjugation.model.MorphologicalTermType.*
import arabic.model.JussiveParticle
import arabic.model.HiddenPronounStatus.*

class JussiveParticleProcessor extends RuleProcessor {

  private val secondPersonTypes = Seq(
    SecondPersonMasculineSingular,
    SecondPersonFeminineSingular,
    SecondPersonMasculineDual,
    SecondPersonFeminineDual,
    SecondPersonMasculinePlural,
    SecondPersonFemininePlural
  )

  override def applyRules(
    memberType: SarfMemberType,
    baseRootWord: RootWord,
    processingContext: ProcessingContext
  ): RootWord =
    baseRootWord.`type` match {
      case Forbidden => prependJussiveParticle(baseRootWord, processingContext, JussiveParticle.LamOfProhibition)
      case PresentTenseJussive =>
        processingContext.jussiveParticle match {
          case Some(particle)
              if particle == JussiveParticle.LamOfCommand && validateHiddenPronounTypeMembers(
                memberType,
                secondPersonTypes
              ) =>
            baseRootWord // with LamOfCommand and any second person types no need to append
          case Some(particle) => prependJussiveParticle(baseRootWord, processingContext, particle)
          case None           => throw new IllegalStateException("Jussive particle is required for jussive tense")
        }
      case PresentPassiveTenseJussive =>
        processingContext.jussiveParticle match {
          case Some(particle) => prependJussiveParticle(baseRootWord, processingContext, particle)
          case None           => throw new IllegalStateException("Jussive particle is required for jussive tense")
        }
      case _ => baseRootWord
    }

  private def prependJussiveParticle(
    baseRootWord: RootWord,
    processingContext: ProcessingContext,
    jussiveParticle: JussiveParticle
  ) = {
    val updatedWord = jussiveParticle.word.concat(baseRootWord.derivedWord)
    if baseRootWord.derivedWord != updatedWord then processingContext.applyRule(getClass.getSimpleName)
    baseRootWord.copy(derivedWord = updatedWord)
  }
}
