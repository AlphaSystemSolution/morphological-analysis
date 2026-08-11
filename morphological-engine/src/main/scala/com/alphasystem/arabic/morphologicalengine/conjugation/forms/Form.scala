package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package forms

import conjugation.model.NamedTemplate

enum Form(
  val template: NamedTemplate,
  val pastTense: VerbSupport,
  val presentTense: VerbSupport,
  val presentTenseJussive: VerbSupport,
  val imperative: VerbSupport,
  val forbidden: VerbSupport,
  val activeParticipleMasculine: NounSupport,
  val activeParticipleFeminine: NounSupport,
  val pastPassiveTense: Option[VerbSupport],
  val presentPassiveTense: Option[VerbSupport],
  val presentPassiveTenseJussive: Option[VerbSupport],
  val passiveParticipleMasculine: Option[NounSupport],
  val passiveParticipleFeminine: Option[NounSupport],
  val verbalNouns: Seq[NounSupport],
  val adverbs: Seq[NounSupport]) {

  case FormICategoryAGroupUTemplate
      extends Form(
        template = NamedTemplate.FormICategoryAGroupUTemplate,
        pastTense = verb.FormI.PastTenseV1,
        presentTense = verb.FormI.PresentTenseV1,
        presentTenseJussive = verb.FormI.PresentTenseJussiveModeV1,
        imperative = verb.FormI.ImperativeV1,
        forbidden = verb.FormI.ForbiddenV1,
        activeParticipleMasculine = noun.FormI.MasculineActiveParticiple,
        activeParticipleFeminine = noun.FormI.FeminineActiveParticiple,
        pastPassiveTense = Some(verb.FormI.PastPassiveTense),
        presentPassiveTense = Some(verb.FormI.PresentPassiveTense),
        presentPassiveTenseJussive = Some(verb.FormI.PresentPassiveTenseJussiveMode),
        passiveParticipleMasculine = Some(noun.FormI.MasculinePassiveParticiple),
        passiveParticipleFeminine = Some(noun.FormI.FemininePassiveParticiple),
        verbalNouns = Seq.empty,
        adverbs = Seq(noun.FormI.NounOfPlaceAndTimeV1, noun.FormI.NounOfPlaceAndTimeV2, noun.FormI.NounOfPlaceAndTimeV3)
      )

  case FormICategoryAGroupITemplate
      extends Form(
        template = NamedTemplate.FormICategoryAGroupITemplate,
        pastTense = verb.FormI.PastTenseV1,
        presentTense = verb.FormI.PresentTenseV2,
        presentTenseJussive = verb.FormI.PresentTenseJussiveModeV2,
        imperative = verb.FormI.ImperativeV2,
        forbidden = verb.FormI.ForbiddenV2,
        activeParticipleMasculine = noun.FormI.MasculineActiveParticiple,
        activeParticipleFeminine = noun.FormI.FeminineActiveParticiple,
        pastPassiveTense = Some(verb.FormI.PastPassiveTense),
        presentPassiveTense = Some(verb.FormI.PresentPassiveTense),
        presentPassiveTenseJussive = Some(verb.FormI.PresentPassiveTenseJussiveMode),
        passiveParticipleMasculine = Some(noun.FormI.MasculinePassiveParticiple),
        passiveParticipleFeminine = Some(noun.FormI.FemininePassiveParticiple),
        verbalNouns = Seq.empty,
        adverbs = Seq(noun.FormI.NounOfPlaceAndTimeV1, noun.FormI.NounOfPlaceAndTimeV2, noun.FormI.NounOfPlaceAndTimeV3)
      )

  case FormICategoryAGroupATemplate
      extends Form(
        template = NamedTemplate.FormICategoryAGroupATemplate,
        pastTense = verb.FormI.PastTenseV1,
        presentTense = verb.FormI.PresentTenseV3,
        presentTenseJussive = verb.FormI.PresentTenseJussiveModeV3,
        imperative = verb.FormI.ImperativeV3,
        forbidden = verb.FormI.ForbiddenV3,
        activeParticipleMasculine = noun.FormI.MasculineActiveParticiple,
        activeParticipleFeminine = noun.FormI.FeminineActiveParticiple,
        pastPassiveTense = Some(verb.FormI.PastPassiveTense),
        presentPassiveTense = Some(verb.FormI.PresentPassiveTense),
        presentPassiveTenseJussive = Some(verb.FormI.PresentPassiveTenseJussiveMode),
        passiveParticipleMasculine = Some(noun.FormI.MasculinePassiveParticiple),
        passiveParticipleFeminine = Some(noun.FormI.FemininePassiveParticiple),
        verbalNouns = Seq.empty,
        adverbs = Seq(noun.FormI.NounOfPlaceAndTimeV1, noun.FormI.NounOfPlaceAndTimeV2, noun.FormI.NounOfPlaceAndTimeV3)
      )

  case FormICategoryIGroupATemplate
      extends Form(
        template = NamedTemplate.FormICategoryIGroupATemplate,
        pastTense = verb.FormI.PastTenseV2,
        presentTense = verb.FormI.PresentTenseV3,
        presentTenseJussive = verb.FormI.PresentTenseJussiveModeV3,
        imperative = verb.FormI.ImperativeV3,
        forbidden = verb.FormI.ForbiddenV3,
        activeParticipleMasculine = noun.FormI.MasculineActiveParticiple,
        activeParticipleFeminine = noun.FormI.FeminineActiveParticiple,
        pastPassiveTense = Some(verb.FormI.PastPassiveTense),
        presentPassiveTense = Some(verb.FormI.PresentPassiveTense),
        presentPassiveTenseJussive = Some(verb.FormI.PresentPassiveTenseJussiveMode),
        passiveParticipleMasculine = Some(noun.FormI.MasculinePassiveParticiple),
        passiveParticipleFeminine = Some(noun.FormI.FemininePassiveParticiple),
        verbalNouns = Seq.empty,
        adverbs = Seq(noun.FormI.NounOfPlaceAndTimeV1, noun.FormI.NounOfPlaceAndTimeV2, noun.FormI.NounOfPlaceAndTimeV3)
      )

  case FormICategoryIGroupITemplate
      extends Form(
        template = NamedTemplate.FormICategoryIGroupITemplate,
        pastTense = verb.FormI.PastTenseV2,
        presentTense = verb.FormI.PresentTenseV2,
        presentTenseJussive = verb.FormI.PresentTenseJussiveModeV2,
        imperative = verb.FormI.ImperativeV2,
        forbidden = verb.FormI.ForbiddenV2,
        activeParticipleMasculine = noun.FormI.MasculineActiveParticiple,
        activeParticipleFeminine = noun.FormI.FeminineActiveParticiple,
        pastPassiveTense = Some(verb.FormI.PastPassiveTense),
        presentPassiveTense = Some(verb.FormI.PresentPassiveTense),
        presentPassiveTenseJussive = Some(verb.FormI.PresentPassiveTenseJussiveMode),
        passiveParticipleMasculine = Some(noun.FormI.MasculinePassiveParticiple),
        passiveParticipleFeminine = Some(noun.FormI.FemininePassiveParticiple),
        verbalNouns = Seq.empty,
        adverbs = Seq(noun.FormI.NounOfPlaceAndTimeV1, noun.FormI.NounOfPlaceAndTimeV2, noun.FormI.NounOfPlaceAndTimeV3)
      )

  case FormICategoryUTemplate
      extends Form(
        template = NamedTemplate.FormICategoryUTemplate,
        pastTense = verb.FormI.PastTenseV3,
        presentTense = verb.FormI.PresentTenseV1,
        presentTenseJussive = verb.FormI.PresentTenseJussiveModeV1,
        imperative = verb.FormI.ImperativeV1,
        forbidden = verb.FormI.ForbiddenV1,
        activeParticipleMasculine = noun.FormI.Category6MasculineActiveParticiple,
        activeParticipleFeminine = noun.FormI.Category6FeminineActiveParticiple,
        pastPassiveTense = None,
        presentPassiveTense = None,
        presentPassiveTenseJussive = None,
        passiveParticipleMasculine = None,
        passiveParticipleFeminine = None,
        verbalNouns = Seq.empty,
        adverbs = Seq(noun.FormI.NounOfPlaceAndTimeV1, noun.FormI.NounOfPlaceAndTimeV2, noun.FormI.NounOfPlaceAndTimeV3)
      )

  case FormIITemplate
      extends Form(
        template = NamedTemplate.FormIITemplate,
        pastTense = verb.FormII.PastTense,
        presentTense = verb.FormII.PresentTense,
        presentTenseJussive = verb.FormII.PresentTenseJussiveMode,
        imperative = verb.FormII.Imperative,
        forbidden = verb.FormII.Forbidden,
        activeParticipleMasculine = noun.FormII.MasculineActiveParticiple,
        activeParticipleFeminine = noun.FormII.FeminineActiveParticiple,
        pastPassiveTense = Some(verb.FormII.PastPassiveTense),
        presentPassiveTense = Some(verb.FormII.PresentPassiveTense),
        presentPassiveTenseJussive = Some(verb.FormII.PresentPassiveTenseJussiveMode),
        passiveParticipleMasculine = Some(noun.FormII.MasculinePassiveParticiple),
        passiveParticipleFeminine = Some(noun.FormII.FemininePassiveParticiple),
        verbalNouns = Seq(noun.VerbalNoun.FormII),
        adverbs = Seq(noun.FormII.NounOfPlaceAndTime)
      )

  case FormIIITemplate
      extends Form(
        template = NamedTemplate.FormIIITemplate,
        pastTense = verb.FormIII.PastTense,
        presentTense = verb.FormIII.PresentTense,
        presentTenseJussive = verb.FormIII.PresentTenseJussiveMode,
        imperative = verb.FormIII.Imperative,
        forbidden = verb.FormIII.Forbidden,
        activeParticipleMasculine = noun.FormIII.MasculineActiveParticiple,
        activeParticipleFeminine = noun.FormIII.FeminineActiveParticiple,
        pastPassiveTense = Some(verb.FormIII.PastPassiveTense),
        presentPassiveTense = Some(verb.FormIII.PresentPassiveTense),
        presentPassiveTenseJussive = Some(verb.FormIII.PresentPassiveTenseJussiveMode),
        passiveParticipleMasculine = Some(noun.FormIII.MasculinePassiveParticiple),
        passiveParticipleFeminine = Some(noun.FormIII.FemininePassiveParticiple),
        verbalNouns = Seq(noun.VerbalNoun.FormIIIV1, noun.VerbalNoun.FormIIIV2),
        adverbs = Seq(noun.FormIII.NounOfPlaceAndTime)
      )

  case FormIVTemplate
      extends Form(
        template = NamedTemplate.FormIVTemplate,
        pastTense = verb.FormIV.PastTense,
        presentTense = verb.FormIV.PresentTense,
        presentTenseJussive = verb.FormIV.PresentTenseJussiveMode,
        imperative = verb.FormIV.Imperative,
        forbidden = verb.FormIV.Forbidden,
        activeParticipleMasculine = noun.FormIV.MasculineActiveParticiple,
        activeParticipleFeminine = noun.FormIV.FeminineActiveParticiple,
        pastPassiveTense = Some(verb.FormIV.PastPassiveTense),
        presentPassiveTense = Some(verb.FormIV.PresentPassiveTense),
        presentPassiveTenseJussive = Some(verb.FormIV.PresentPassiveTenseJussiveMode),
        passiveParticipleMasculine = Some(noun.FormIV.MasculinePassiveParticiple),
        passiveParticipleFeminine = Some(noun.FormIV.FemininePassiveParticiple),
        verbalNouns = Seq(noun.VerbalNoun.FormIV),
        adverbs = Seq(noun.FormIV.NounOfPlaceAndTime)
      )

  case FormVTemplate
      extends Form(
        template = NamedTemplate.FormVTemplate,
        pastTense = verb.FormV.PastTense,
        presentTense = verb.FormV.PresentTense,
        presentTenseJussive = verb.FormV.PresentTenseJussiveMode,
        imperative = verb.FormV.Imperative,
        forbidden = verb.FormV.Forbidden,
        activeParticipleMasculine = noun.FormV.MasculineActiveParticiple,
        activeParticipleFeminine = noun.FormV.FeminineActiveParticiple,
        pastPassiveTense = Some(verb.FormV.PastPassiveTense),
        presentPassiveTense = Some(verb.FormV.PresentPassiveTense),
        presentPassiveTenseJussive = Some(verb.FormV.PresentPassiveTenseJussiveMode),
        passiveParticipleMasculine = Some(noun.FormV.MasculinePassiveParticiple),
        passiveParticipleFeminine = Some(noun.FormV.FemininePassiveParticiple),
        verbalNouns = Seq(noun.VerbalNoun.FormV),
        adverbs = Seq(noun.FormV.NounOfPlaceAndTime)
      )

  case FormVITemplate
      extends Form(
        template = NamedTemplate.FormVITemplate,
        pastTense = verb.FormVI.PastTense,
        presentTense = verb.FormVI.PresentTense,
        presentTenseJussive = verb.FormVI.PresentTenseJussiveMode,
        imperative = verb.FormVI.Imperative,
        forbidden = verb.FormVI.Forbidden,
        activeParticipleMasculine = noun.FormVI.MasculineActiveParticiple,
        activeParticipleFeminine = noun.FormVI.FeminineActiveParticiple,
        pastPassiveTense = Some(verb.FormVI.PastPassiveTense),
        presentPassiveTense = Some(verb.FormVI.PresentPassiveTense),
        presentPassiveTenseJussive = Some(verb.FormVI.PresentPassiveTenseJussiveMode),
        passiveParticipleMasculine = Some(noun.FormVI.MasculinePassiveParticiple),
        passiveParticipleFeminine = Some(noun.FormVI.FemininePassiveParticiple),
        verbalNouns = Seq(noun.VerbalNoun.FormVI),
        adverbs = Seq(noun.FormVI.NounOfPlaceAndTime)
      )

  case FormVIITemplate
      extends Form(
        template = NamedTemplate.FormVIITemplate,
        pastTense = verb.FormVII.PastTense,
        presentTense = verb.FormVII.PresentTense,
        presentTenseJussive = verb.FormVII.PresentTenseJussiveMode,
        imperative = verb.FormVII.Imperative,
        forbidden = verb.FormVII.Forbidden,
        activeParticipleMasculine = noun.FormVII.MasculineActiveParticiple,
        activeParticipleFeminine = noun.FormVII.FeminineActiveParticiple,
        pastPassiveTense = None,
        presentPassiveTense = None,
        presentPassiveTenseJussive = None,
        passiveParticipleMasculine = None,
        passiveParticipleFeminine = None,
        verbalNouns = Seq(noun.VerbalNoun.FormVII),
        adverbs = Seq(noun.FormVII.NounOfPlaceAndTime)
      )

  case FormVIIITemplate
      extends Form(
        template = NamedTemplate.FormVIIITemplate,
        pastTense = verb.FormVIII.PastTense,
        presentTense = verb.FormVIII.PresentTense,
        presentTenseJussive = verb.FormVIII.PresentTenseJussiveMode,
        imperative = verb.FormVIII.Imperative,
        forbidden = verb.FormVIII.Forbidden,
        activeParticipleMasculine = noun.FormVIII.MasculineActiveParticiple,
        activeParticipleFeminine = noun.FormVIII.FeminineActiveParticiple,
        pastPassiveTense = Some(verb.FormVIII.PastPassiveTense),
        presentPassiveTense = Some(verb.FormVIII.PresentPassiveTense),
        presentPassiveTenseJussive = Some(verb.FormVIII.PresentPassiveTenseJussiveMode),
        passiveParticipleMasculine = Some(noun.FormVIII.MasculinePassiveParticiple),
        passiveParticipleFeminine = Some(noun.FormVIII.FemininePassiveParticiple),
        verbalNouns = Seq(noun.VerbalNoun.FormVIII),
        adverbs = Seq(noun.FormVIII.NounOfPlaceAndTime)
      )

  case FormXTemplate
      extends Form(
        template = NamedTemplate.FormXTemplate,
        pastTense = verb.FormX.PastTense,
        presentTense = verb.FormX.PresentTense,
        presentTenseJussive = verb.FormX.PresentTenseJussiveMode,
        imperative = verb.FormX.Imperative,
        forbidden = verb.FormX.Forbidden,
        activeParticipleMasculine = noun.FormX.MasculineActiveParticiple,
        activeParticipleFeminine = noun.FormX.FeminineActiveParticiple,
        pastPassiveTense = Some(verb.FormX.PastPassiveTense),
        presentPassiveTense = Some(verb.FormX.PresentPassiveTense),
        presentPassiveTenseJussive = Some(verb.FormX.PresentPassiveTenseJussiveMode),
        passiveParticipleMasculine = Some(noun.FormX.MasculinePassiveParticiple),
        passiveParticipleFeminine = Some(noun.FormX.FemininePassiveParticiple),
        verbalNouns = Seq(noun.VerbalNoun.FormX),
        adverbs = Seq(noun.FormX.NounOfPlaceAndTime)
      )
}

object Form {

  lazy val fromNamedTemplate: Map[NamedTemplate, Form] = Map(
    NamedTemplate.FormICategoryAGroupUTemplate -> Form.FormICategoryAGroupUTemplate,
    NamedTemplate.FormICategoryAGroupITemplate -> Form.FormICategoryAGroupITemplate,
    NamedTemplate.FormICategoryAGroupATemplate -> Form.FormICategoryAGroupATemplate,
    NamedTemplate.FormICategoryIGroupATemplate -> Form.FormICategoryIGroupATemplate,
    NamedTemplate.FormICategoryIGroupITemplate -> Form.FormICategoryIGroupITemplate,
    NamedTemplate.FormICategoryUTemplate -> Form.FormICategoryUTemplate,
    NamedTemplate.FormIITemplate -> Form.FormIITemplate,
    NamedTemplate.FormIIITemplate -> Form.FormIIITemplate,
    NamedTemplate.FormIVTemplate -> Form.FormIVTemplate,
    NamedTemplate.FormVTemplate -> Form.FormVTemplate,
    NamedTemplate.FormVITemplate -> Form.FormVITemplate,
    NamedTemplate.FormVIITemplate -> Form.FormVIITemplate,
    NamedTemplate.FormVIIITemplate -> Form.FormVIIITemplate,
    NamedTemplate.FormXTemplate -> Form.FormXTemplate
  )
}
