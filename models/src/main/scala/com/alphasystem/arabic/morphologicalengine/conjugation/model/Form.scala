package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package model

import verb.*
import noun.*

import java.lang.Enum

enum Form(
  val template: NamedTemplate,
  val pastTense: VerbSupport,
  val presentTense: VerbSupport,
  val imperative: VerbSupport,
  val forbidden: VerbSupport,
  val activeParticipleMasculine: NounSupport,
  val activeParticipleFeminine: NounSupport,
  val pastPassiveTense: Option[VerbSupport],
  val presentPassiveTense: Option[VerbSupport],
  val passiveParticipleMasculine: Option[NounSupport],
  val passiveParticipleFeminine: Option[NounSupport],
  val verbalNouns: Seq[NounSupport],
  val adverbs: Seq[NounSupport])
    extends Enum[Form] {

  case FormICategoryAGroupUTemplate
      extends Form(
        template = NamedTemplate.FormICategoryAGroupUTemplate,
        pastTense = FormI.PastTenseV1,
        presentTense = FormI.PresentTenseV1,
        imperative = FormI.ImperativeV1,
        forbidden = FormI.ForbiddenV1,
        activeParticipleMasculine = Noun.FormIMasculineActiveParticiple,
        activeParticipleFeminine = Noun.FormIFeminineActiveParticiple,
        pastPassiveTense = Some(FormI.PastPassiveTense),
        presentPassiveTense = Some(FormI.PresentPassiveTense),
        passiveParticipleMasculine = Some(Noun.FormIMasculinePassiveParticiple),
        passiveParticipleFeminine = Some(Noun.FormIFemininePassiveParticiple),
        verbalNouns = Seq.empty,
        adverbs = Seq(Noun.NounOfPlaceAndTimeV1, Noun.NounOfPlaceAndTimeV2, Noun.NounOfPlaceAndTimeV3)
      )

  case FormICategoryAGroupITemplate
      extends Form(
        template = NamedTemplate.FormICategoryAGroupITemplate,
        pastTense = FormI.PastTenseV1,
        presentTense = FormI.PresentTenseV2,
        imperative = FormI.ImperativeV2,
        forbidden = FormI.ForbiddenV2,
        activeParticipleMasculine = Noun.FormIMasculineActiveParticiple,
        activeParticipleFeminine = Noun.FormIFeminineActiveParticiple,
        pastPassiveTense = Some(FormI.PastPassiveTense),
        presentPassiveTense = Some(FormI.PresentPassiveTense),
        passiveParticipleMasculine = Some(Noun.FormIMasculinePassiveParticiple),
        passiveParticipleFeminine = Some(Noun.FormIFemininePassiveParticiple),
        verbalNouns = Seq.empty,
        adverbs = Seq(Noun.NounOfPlaceAndTimeV1, Noun.NounOfPlaceAndTimeV2, Noun.NounOfPlaceAndTimeV3)
      )

  case FormICategoryAGroupATemplate
      extends Form(
        template = NamedTemplate.FormICategoryAGroupATemplate,
        pastTense = FormI.PastTenseV1,
        presentTense = FormI.PresentTenseV3,
        imperative = FormI.ImperativeV3,
        forbidden = FormI.ForbiddenV3,
        activeParticipleMasculine = Noun.FormIMasculineActiveParticiple,
        activeParticipleFeminine = Noun.FormIFeminineActiveParticiple,
        pastPassiveTense = Some(FormI.PastPassiveTense),
        presentPassiveTense = Some(FormI.PresentPassiveTense),
        passiveParticipleMasculine = Some(Noun.FormIMasculinePassiveParticiple),
        passiveParticipleFeminine = Some(Noun.FormIFemininePassiveParticiple),
        verbalNouns = Seq.empty,
        adverbs = Seq(Noun.NounOfPlaceAndTimeV1, Noun.NounOfPlaceAndTimeV2, Noun.NounOfPlaceAndTimeV3)
      )

  case FormICategoryIGroupATemplate
      extends Form(
        template = NamedTemplate.FormICategoryIGroupATemplate,
        pastTense = FormI.PastTenseV2,
        presentTense = FormI.PresentTenseV3,
        imperative = FormI.ImperativeV3,
        forbidden = FormI.ForbiddenV3,
        activeParticipleMasculine = Noun.FormIMasculineActiveParticiple,
        activeParticipleFeminine = Noun.FormIFeminineActiveParticiple,
        pastPassiveTense = Some(FormI.PastPassiveTense),
        presentPassiveTense = Some(FormI.PresentPassiveTense),
        passiveParticipleMasculine = Some(Noun.FormIMasculinePassiveParticiple),
        passiveParticipleFeminine = Some(Noun.FormIFemininePassiveParticiple),
        verbalNouns = Seq.empty,
        adverbs = Seq(Noun.NounOfPlaceAndTimeV1, Noun.NounOfPlaceAndTimeV2, Noun.NounOfPlaceAndTimeV3)
      )

  case FormICategoryIGroupITemplate
      extends Form(
        template = NamedTemplate.FormICategoryIGroupITemplate,
        pastTense = FormI.PastTenseV2,
        presentTense = FormI.PresentTenseV2,
        imperative = FormI.ImperativeV2,
        forbidden = FormI.ForbiddenV2,
        activeParticipleMasculine = Noun.FormIMasculineActiveParticiple,
        activeParticipleFeminine = Noun.FormIFeminineActiveParticiple,
        pastPassiveTense = Some(FormI.PastPassiveTense),
        presentPassiveTense = Some(FormI.PresentPassiveTense),
        passiveParticipleMasculine = Some(Noun.FormIMasculinePassiveParticiple),
        passiveParticipleFeminine = Some(Noun.FormIFemininePassiveParticiple),
        verbalNouns = Seq.empty,
        adverbs = Seq(Noun.NounOfPlaceAndTimeV1, Noun.NounOfPlaceAndTimeV2, Noun.NounOfPlaceAndTimeV3)
      )

  case FormICategoryUTemplate
      extends Form(
        template = NamedTemplate.FormICategoryUTemplate,
        pastTense = FormI.PastTenseV3,
        presentTense = FormI.PresentTenseV1,
        imperative = FormI.ImperativeV1,
        forbidden = FormI.ForbiddenV1,
        activeParticipleMasculine = Noun.FormICategory6MasculineActiveParticiple,
        activeParticipleFeminine = Noun.FormICategory6FeminineActiveParticiple,
        pastPassiveTense = None,
        presentPassiveTense = None,
        passiveParticipleMasculine = None,
        passiveParticipleFeminine = None,
        verbalNouns = Seq.empty,
        adverbs = Seq(Noun.NounOfPlaceAndTimeV1, Noun.NounOfPlaceAndTimeV2, Noun.NounOfPlaceAndTimeV3)
      )

  case FormIITemplate
      extends Form(
        template = NamedTemplate.FormIITemplate,
        pastTense = FormII.PastTense,
        presentTense = FormII.PresentTense,
        imperative = FormII.Imperative,
        forbidden = FormII.Forbidden,
        activeParticipleMasculine = Noun.FormIIMasculineActiveParticiple,
        activeParticipleFeminine = Noun.FormIIFeminineActiveParticiple,
        pastPassiveTense = Some(FormII.PastPassiveTense),
        presentPassiveTense = Some(FormII.PresentPassiveTense),
        passiveParticipleMasculine = Some(Noun.FormIIMasculinePassiveParticiple),
        passiveParticipleFeminine = Some(Noun.FormIIFemininePassiveParticiple),
        verbalNouns = Seq(VerbalNoun.FormII),
        adverbs = Seq(Noun.FormIIFemininePassiveParticiple)
      )
}
