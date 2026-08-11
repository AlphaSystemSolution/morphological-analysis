package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package rule
package processors

import com.alphasystem.arabic.morphologicalengine.conjugation.rule.RuleProcessor
import com.alphasystem.arabic.morphologicalengine.conjugation.model.internal.RootWord
import com.alphasystem.arabic.model.SarfMemberType
import com.alphasystem.arabic.morphologicalengine.conjugation.model.MorphologicalTermType

class JussiveParticleProcessor extends RuleProcessor {

  override def applyRules(
    memberType: SarfMemberType,
    baseRootWord: RootWord,
    processingContext: ProcessingContext
  ): RootWord = {
    if baseRootWord.`type` == MorphologicalTermType.PresentTenseJussive || baseRootWord.`type` == MorphologicalTermType.PresentPassiveTenseJussive then {
      processingContext.jussiveParticle match {
        case Some(particle) =>
          val updatedWord = particle.word.concat(baseRootWord.derivedWord)
          if baseRootWord.derivedWord != updatedWord then processingContext.applyRule(getClass.getSimpleName)
          baseRootWord.copy(derivedWord = updatedWord)
        case None => throw new IllegalStateException("Jussive particle is required for jussive tense")
      }
    } else baseRootWord
  }

}
