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

  case FormIIITemplate
      extends Form(
        template = NamedTemplate.FormIIITemplate,
        pastTense = FormIII.PastTense,
        presentTense = FormIII.PresentTense,
        imperative = FormIII.Imperative,
        forbidden = FormIII.Forbidden,
        activeParticipleMasculine = Noun.FormIIIMasculineActiveParticiple,
        activeParticipleFeminine = Noun.FormIIIFeminineActiveParticiple,
        pastPassiveTense = Some(FormIII.PastPassiveTense),
        presentPassiveTense = Some(FormIII.PresentPassiveTense),
        passiveParticipleMasculine = Some(Noun.FormIIIMasculinePassiveParticiple),
        passiveParticipleFeminine = Some(Noun.FormIIIFemininePassiveParticiple),
        verbalNouns = Seq(VerbalNoun.FormIIIV1, VerbalNoun.FormIIIV2),
        adverbs = Seq(Noun.FormIIIFemininePassiveParticiple)
      )

  case FormIVTemplate
      extends Form(
        template = NamedTemplate.FormIVTemplate,
        pastTense = FormIV.PastTense,
        presentTense = FormIV.PresentTense,
        imperative = FormIV.Imperative,
        forbidden = FormIV.Forbidden,
        activeParticipleMasculine = Noun.FormIVMasculineActiveParticiple,
        activeParticipleFeminine = Noun.FormIVFeminineActiveParticiple,
        pastPassiveTense = Some(FormIV.PastPassiveTense),
        presentPassiveTense = Some(FormIV.PresentPassiveTense),
        passiveParticipleMasculine = Some(Noun.FormIVMasculinePassiveParticiple),
        passiveParticipleFeminine = Some(Noun.FormIVFemininePassiveParticiple),
        verbalNouns = Seq(VerbalNoun.FormIV),
        adverbs = Seq(Noun.FormIVFemininePassiveParticiple)
      )

  case FormVTemplate
      extends Form(
        template = NamedTemplate.FormVTemplate,
        pastTense = FormV.PastTense,
        presentTense = FormV.PresentTense,
        imperative = FormV.Imperative,
        forbidden = FormV.Forbidden,
        activeParticipleMasculine = Noun.FormVMasculineActiveParticiple,
        activeParticipleFeminine = Noun.FormVFeminineActiveParticiple,
        pastPassiveTense = Some(FormV.PastPassiveTense),
        presentPassiveTense = Some(FormV.PresentPassiveTense),
        passiveParticipleMasculine = Some(Noun.FormVMasculinePassiveParticiple),
        passiveParticipleFeminine = Some(Noun.FormVFemininePassiveParticiple),
        verbalNouns = Seq(VerbalNoun.FormV),
        adverbs = Seq(Noun.FormVFemininePassiveParticiple)
      )

  case FormVITemplate
      extends Form(
        template = NamedTemplate.FormVITemplate,
        pastTense = FormVI.PastTense,
        presentTense = FormVI.PresentTense,
        imperative = FormVI.Imperative,
        forbidden = FormVI.Forbidden,
        activeParticipleMasculine = Noun.FormVIMasculineActiveParticiple,
        activeParticipleFeminine = Noun.FormVIFeminineActiveParticiple,
        pastPassiveTense = Some(FormVI.PastPassiveTense),
        presentPassiveTense = Some(FormVI.PresentPassiveTense),
        passiveParticipleMasculine = Some(Noun.FormVIMasculinePassiveParticiple),
        passiveParticipleFeminine = Some(Noun.FormVIFemininePassiveParticiple),
        verbalNouns = Seq(VerbalNoun.FormVI),
        adverbs = Seq(Noun.FormVIFemininePassiveParticiple)
      )

  case FormVIITemplate
      extends Form(
        template = NamedTemplate.FormVIITemplate,
        pastTense = FormVII.PastTense,
        presentTense = FormVII.PresentTense,
        imperative = FormVII.Imperative,
        forbidden = FormVII.Forbidden,
        activeParticipleMasculine = Noun.FormVIIMasculineActiveParticiple,
        activeParticipleFeminine = Noun.FormVIIFeminineActiveParticiple,
        pastPassiveTense = None,
        presentPassiveTense = None,
        passiveParticipleMasculine = None,
        passiveParticipleFeminine = None,
        verbalNouns = Seq(VerbalNoun.FormVII),
        adverbs = Seq(Noun.FormVIIFemininePassiveParticiple)
      )

  case FormVIIITemplate
      extends Form(
        template = NamedTemplate.FormVIIITemplate,
        pastTense = FormVIII.PastTense,
        presentTense = FormVIII.PresentTense,
        imperative = FormVIII.Imperative,
        forbidden = FormVIII.Forbidden,
        activeParticipleMasculine = Noun.FormVIIIMasculineActiveParticiple,
        activeParticipleFeminine = Noun.FormVIIIFeminineActiveParticiple,
        pastPassiveTense = Some(FormVIII.PastPassiveTense),
        presentPassiveTense = Some(FormVIII.PresentPassiveTense),
        passiveParticipleMasculine = Some(Noun.FormVIIIMasculinePassiveParticiple),
        passiveParticipleFeminine = Some(Noun.FormVIIIFemininePassiveParticiple),
        verbalNouns = Seq(VerbalNoun.FormVIII),
        adverbs = Seq(Noun.FormVIIIFemininePassiveParticiple)
      )

  case FormXTemplate
      extends Form(
        template = NamedTemplate.FormXTemplate,
        pastTense = FormX.PastTense,
        presentTense = FormX.PresentTense,
        imperative = FormX.Imperative,
        forbidden = FormX.Forbidden,
        activeParticipleMasculine = Noun.FormXMasculineActiveParticiple,
        activeParticipleFeminine = Noun.FormXFeminineActiveParticiple,
        pastPassiveTense = Some(FormX.PastPassiveTense),
        presentPassiveTense = Some(FormX.PresentPassiveTense),
        passiveParticipleMasculine = Some(Noun.FormXMasculinePassiveParticiple),
        passiveParticipleFeminine = Some(Noun.FormXFemininePassiveParticiple),
        verbalNouns = Seq(VerbalNoun.FormX),
        adverbs = Seq(Noun.FormXFemininePassiveParticiple)
      )
}
