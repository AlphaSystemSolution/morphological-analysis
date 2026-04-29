package com.alphasystem
package arabic
package cli
package morphologicalengine

import arabic.morphologicalengine.conjugation.model.{
  ConjugationConfiguration,
  ConjugationInput,
  ConjugationTuple,
  DetailedConjugation,
  MorphologicalTermType,
  NounConjugationGroup,
  OutputFormat,
  VerbConjugationGroup
}
import arabic.morphologicalengine.conjugation.builder.ConjugationBuilder
import arabic.morphologicalanalysis.morphology.model.{ ConversationType, GenderType, NounStatus, NumberType }
import arabic.model.ProNoun.*

import scala.collection.mutable.ListBuffer

class SingleConjugationGenerator(singleConjugation: SingleConjugation) {

  private val conjugationBuilder = ConjugationBuilder()
  private val showPronouns = singleConjugation.showPronouns.getOrElse(false)
  private val showNumbers = singleConjugation.showNumbers.getOrElse(false)
  private val showGenders = singleConjugation.showGenders.getOrElse(false)
  private val showConversationTypes = singleConjugation.showConversationTypes.getOrElse(false)
  private val showNounStatus = singleConjugation.showNounStatus.getOrElse(false)

  // Translations
  private val translations = singleConjugation.translations
  private lazy val thirdPersonMasculineTranslations = getTranslation(
    translations.get(ThirdPersonMasculineSingular),
    translations.get(ThirdPersonMasculineDual),
    translations.get(ThirdPersonMasculinePlural)
  )
  private lazy val thirdPersonFeminineTranslations = getTranslation(
    translations.get(ThirdPersonFeminineSingular),
    translations.get(ThirdPersonFeminineDual),
    translations.get(ThirdPersonFemininePlural)
  )
  private lazy val secondPersonMasculineTranslations = getTranslation(
    translations.get(SecondPersonMasculineSingular),
    translations.get(SecondPersonMasculineDual),
    translations.get(SecondPersonMasculinePlural)
  )
  private lazy val secondPersonFeminineTranslations = getTranslation(
    translations.get(SecondPersonFeminineSingular),
    translations.get(SecondPersonFeminineDual),
    translations.get(SecondPersonFemininePlural)
  )
  private lazy val firstPersonTranslations =
    getTranslation(translations.get(FirstPersonSingular), None, translations.get(FirstPersonPlural))

  // Pronouns
  private lazy val thirdPersonMasculinePronoun =
    if showPronouns then
      Map(NumberType.Singular -> "{dtpsmp}", NumberType.Dual -> "{dtpdmp}", NumberType.Plural -> "{dtppmp}")
    else Map.empty[NumberType, String]
  private lazy val thirdPersonFemininePronoun =
    if showPronouns then
      Map(NumberType.Singular -> "{dtpsfp}", NumberType.Dual -> "{dtpdfp}", NumberType.Plural -> "{dtppfp}")
    else Map.empty[NumberType, String]

  private lazy val secondPersonMasculinePronoun =
    if showPronouns then
      Map(NumberType.Singular -> "{dspsmp}", NumberType.Dual -> "{dspdmp}", NumberType.Plural -> "{dsppmp}")
    else Map.empty[NumberType, String]
  private lazy val secondPersonFemininePronoun =
    if showPronouns then
      Map(NumberType.Singular -> "{dspsfp}", NumberType.Dual -> "{dspdfp}", NumberType.Plural -> "{dsppfp}")
    else Map.empty[NumberType, String]

  private lazy val firstPersonPronoun =
    if showPronouns then Map(NumberType.Singular -> "{dfpsp}", NumberType.Dual -> "", NumberType.Plural -> "{dfppp}")
    else Map.empty[NumberType, String]

  private val buffer = ListBuffer[String]()

  def buildDocument: Seq[String] = {
    val tag = singleConjugation.tag
    buffer.addOne(s"// tag::$tag[]").addOne("[.CenteredTable]")
    val chart = conjugationBuilder.doConjugation(
      input = ConjugationInput(
        namedTemplate = singleConjugation.namedTemplate,
        conjugationConfiguration = ConjugationConfiguration(),
        rootLetters = singleConjugation.rootLetters,
        translation = None,
        verbalNounCodes = singleConjugation.verbalNoun.map(_.code).toSeq
      ),
      outputFormat = OutputFormat.Html,
      showAbbreviatedConjugation = false
    )

    chart.detailedConjugation match {
      case Some(detailedConjugation) => buildDocument(singleConjugation.morphologicalTermType, detailedConjugation)
      case None                      => throw new RuntimeException("Something went wrong to generate the chart")
    }

    buffer.addOne("|===").addOne(s"// end::$tag[]").addOne("").toSeq
  }

  private def buildDocument(
    morphologicalTermType: MorphologicalTermType,
    detailedConjugation: DetailedConjugation
  ): Unit = {
    morphologicalTermType match {
      case MorphologicalTermType.PastTense    => buildVerbConjugationGroup(detailedConjugation.pastTense)
      case MorphologicalTermType.PresentTense => buildVerbConjugationGroup(detailedConjugation.presentTense)
      case MorphologicalTermType.VerbalNoun   =>
        val verbalNouns = detailedConjugation.verbalNouns
        if verbalNouns.isEmpty then throw new RuntimeException("Could not find verbal nouns conjugation")
        else buildNounConjugationGroup(verbalNouns.head)
      case MorphologicalTermType.ActiveParticipleMasculine =>
        buildNounConjugationGroup(detailedConjugation.masculineActiveParticiple)
      case MorphologicalTermType.ActiveParticipleFeminine =>
        buildNounConjugationGroup(detailedConjugation.feminineActiveParticiple)
      case MorphologicalTermType.PastPassiveTense =>
        detailedConjugation.pastPassiveTense match {
          case Some(value) => buildVerbConjugationGroup(value)
          case None        => throw new RuntimeException("Could not find pastPassiveTense conjugation")
        }
      case MorphologicalTermType.PresentPassiveTense =>
        detailedConjugation.presentPassiveTense match {
          case Some(value) => buildVerbConjugationGroup(value)
          case None        => throw new RuntimeException("Could not find presentPassiveTense conjugation")
        }
      case MorphologicalTermType.PassiveParticipleMasculine =>
        detailedConjugation.masculinePassiveParticiple match {
          case Some(value) => buildNounConjugationGroup(value)
          case None        => throw new RuntimeException("Could not find passive participle conjugation")
        }
      case MorphologicalTermType.PassiveParticipleFeminine =>
        detailedConjugation.femininePassiveParticiple match {
          case Some(value) => buildNounConjugationGroup(value)
          case None        => throw new RuntimeException("Could not find passive participle conjugation")
        }
      case MorphologicalTermType.Imperative         => buildVerbConjugationGroup(detailedConjugation.imperative)
      case MorphologicalTermType.Forbidden          => buildVerbConjugationGroup(detailedConjugation.forbidden)
      case MorphologicalTermType.NounOfPlaceAndTime => throw new RuntimeException("Noun of place and time conjugation are not implemented yet!")
    }
  }

  private def buildVerbConjugationGroup(verbConjugationGroup: VerbConjugationGroup): Unit = {
    var numOfColumns = 3
    if showGenders then numOfColumns += 1
    if showConversationTypes then numOfColumns += 1

    var cols = "^.^1,^.^1,^.^1"
    if numOfColumns == 4 then cols = "^.^14,^.^14,^.^14,^.^15"
    if numOfColumns == 5 then cols = "^.^19,^.^19,^.^19,^.^20,^.^20"

    buffer
      .addOne(s"""[cols="$cols", width="60%", align="center", halign="center", valign="center"]""")
      .addOne("|===")
      .addOne("")

    if showNumbers then {
      val extraColumns = if numOfColumns == 4 then " |{nbsp}" else if numOfColumns == 5 then " 2+|{nbsp}" else ""
      buffer
        .addOne(
          s"|[arabicTableCaption]#{plural}# |[arabicTableCaption]#{dual}# |[arabicTableCaption]#{singular}#$extraColumns"
        )
        .addOne("")
    }

    handleThirdPersonConjugations(verbConjugationGroup.masculineThirdPerson, verbConjugationGroup.feminineThirdPerson)
    handleSecondPersonConjugations(
      verbConjugationGroup.masculineSecondPerson,
      verbConjugationGroup.feminineSecondPerson
    )
    handleFirstPersonConjugations(verbConjugationGroup.firstPerson)
  }

  private def handleThirdPersonConjugations(
    masculineThirdPerson: Option[ConjugationTuple],
    feminineThirdPerson: Option[ConjugationTuple]
  ): Unit = {
    masculineThirdPerson
      .map(
        buildVerbConjugationTuple(
          GenderType.Masculine,
          ConversationType.ThirdPerson,
          thirdPersonMasculineTranslations,
          thirdPersonMasculinePronoun
        )
      )
      .foreach { result =>
        buffer.addOne("// 3rd person, masculine").addAll(result).addOne("")
      }

    feminineThirdPerson
      .map(
        buildVerbConjugationTuple(
          GenderType.Feminine,
          ConversationType.ThirdPerson,
          thirdPersonFeminineTranslations,
          thirdPersonFemininePronoun
        )
      )
      .foreach { result =>
        buffer.addOne("// 3rd person, feminine").addAll(result).addOne("")
      }
  }

  private def handleSecondPersonConjugations(
    masculineSecondPerson: ConjugationTuple,
    feminineSecondPerson: ConjugationTuple
  ): Unit = {
    buffer
      .addOne("// 2nd person, masculine")
      .addAll(
        buildVerbConjugationTuple(
          GenderType.Masculine,
          ConversationType.SecondPerson,
          secondPersonMasculineTranslations,
          secondPersonMasculinePronoun
        )(
          masculineSecondPerson
        )
      )
      .addOne("")

    buffer
      .addOne("// 2nd person, feminine")
      .addAll(
        buildVerbConjugationTuple(
          GenderType.Feminine,
          ConversationType.SecondPerson,
          secondPersonFeminineTranslations,
          secondPersonFemininePronoun
        )(
          feminineSecondPerson
        )
      )
      .addOne("")
  }

  private def handleFirstPersonConjugations(firstPerson: Option[ConjugationTuple]): Unit =
    firstPerson
      .map(buildFirstPersonVerbConjugationTuple(firstPersonTranslations, firstPersonPronoun))
      .foreach { result =>
        buffer.addOne("// 1st person").addAll(result).addOne("")
      }

  private def buildVerbConjugationTuple(
    genderType: GenderType,
    conversationType: ConversationType,
    translations: Option[(String, String, String)],
    pronouns: Map[NumberType, String]
  )(conjugationTuple: ConjugationTuple
  ) = {
    val plural = s"""|[arabicNormal]#${conjugationTuple.plural}#${createPronoun(pronouns.get(NumberType.Plural))} """
    val dual = conjugationTuple
      .dual
      .map(value => s"""|[arabicNormal]#$value#${createPronoun(pronouns.get(NumberType.Dual))} """)
      .getOrElse("|{nbsp}")
    val singular = s"""|[arabicNormal]#${conjugationTuple.singular}#${createPronoun(
      pronouns.get(NumberType.Singular)
    )} """

    // if translations are provided, then they will go as a separate row after arabic text, in this case gender column will span two rows
    val genderColumnPrefix = if translations.nonEmpty then ".2+" else ""

    // conversation type column will span two columns if translations are empty otherwise it will span 4 columns
    var conversationTypeColumnSpan = 0
    if showConversationTypes then conversationTypeColumnSpan = 2
    if translations.nonEmpty then conversationTypeColumnSpan += 2

    val genders = if showGenders then s"$genderColumnPrefix|[arabicTableCaption]#${genderType.toHtmlCode}# " else ""
    val conversationTypes =
      if showConversationTypes && genderType == GenderType.Masculine then
        s".$conversationTypeColumnSpan+|[arabicTableCaption]#${conversationType.toHtmlCode}# "
      else ""

    Seq(s"$plural$dual$singular$genders$conversationTypes") ++ translations.map(buildTranslationRow)
  }

  private def buildFirstPersonVerbConjugationTuple(
    translations: Option[(String, String, String)],
    pronouns: Map[NumberType, String]
  )(conjugationTuple: ConjugationTuple
  ) = {
    val plural = s"""2+|[arabicNormal]#${conjugationTuple.plural}#${createPronoun(pronouns.get(NumberType.Plural))} """
    val singular =
      s"""|[arabicNormal]#${conjugationTuple.singular}#${createPronoun(pronouns.get(NumberType.Singular))} """

    // if translations are provided then conversation type column will span two rows
    val rowspan = if translations.nonEmpty then ".2" else ""

    // if no gender column, then colspan will be 2, otherwise no colspan
    val colspan = if showGenders then "2" else ""
    val conversationTypeColumnPrefix = if rowspan.nonEmpty || colspan.nonEmpty then "+" else ""
    val conversationTypeColumn =
      if showConversationTypes then
        s"$colspan$rowspan$conversationTypeColumnPrefix|[arabicTableCaption]#${ConversationType.FirstPerson.toHtmlCode}# "
      else ""

    val genderColumnPrefix = if rowspan.nonEmpty then "+" else ""
    val genderColumn = if showGenders && !showConversationTypes then s"$rowspan$genderColumnPrefix|{nbsp}" else ""

    Seq(s"$plural$singular$genderColumn$conversationTypeColumn") ++ translations
      .map(t => (t._1, "", t._3))
      .map(buildTranslationRow)
  }

  private def buildNounConjugationGroup(nounConjugationGroup: NounConjugationGroup) = {
    var numOfColumns = 3
    if showNounStatus then numOfColumns += 1

    var cols = "^.^1,^.^1,^.^1"
    if numOfColumns == 4 then cols = "^.^14,^.^14,^.^14,^.^15"

    buffer
      .addOne(s"""[cols="$cols", width="60%", align="center", halign="center", valign="center"]""")
      .addOne("|===")
      .addOne("")

    if showNumbers then {
      val extraColumns = if numOfColumns == 4 then " |{nbsp}" else ""
      buffer
        .addOne(
          s"|[arabicTableCaption]#{plural}# |[arabicTableCaption]#{dual}# |[arabicTableCaption]#{singular}#$extraColumns"
        )
        .addOne("")
    }

    buffer
      .addOne(handleConjugationTuple(nounConjugationGroup.nominative, NounStatus.Nominative))
      .addOne(handleConjugationTuple(nounConjugationGroup.accusative, NounStatus.Accusative))
      .addOne(handleConjugationTuple(nounConjugationGroup.genitive, NounStatus.Genitive))
      .addOne("")
  }

  private def handleConjugationTuple(conjugationTuple: ConjugationTuple, nounStatus: NounStatus) = {
    val plural = s"""|[arabicNormal]#${conjugationTuple.plural}# """
    val dual = conjugationTuple
      .dual
      .map(value => s"""|[arabicNormal]#$value# """)
      .getOrElse("|{nbsp}")
    val singular = s"""|[arabicNormal]#${conjugationTuple.singular}# """
    val nounStatusColumn = if showNounStatus then s"|[arabicTableCaption]#${nounStatus.shortLabel.htmlCode}# " else ""
    s"$plural$dual$singular$nounStatusColumn"
  }

  private def buildTranslationRow(translations: (String, String, String)) = {
    val singular = translations._1
    val dual = translations._2
    val plural = translations._3

    val dualColumn = if dual.isBlank then "" else s"|[translation]#$dual# "
    val pluralColumnSpan = if dual.isBlank then "2+" else ""
    s"$pluralColumnSpan|[translation]#$plural# $dualColumn|[translation]#$singular#"
  }

  private def createPronoun(maybePronoun: Option[String]) = {
    maybePronoun match {
      case Some(pronoun) => s"{nbsp}[arabicSmall]##[grey]#$pronoun#{nbsp}##"
      case None          => ""
    }
  }

  private def getTranslation(maybeSingular: Option[String], maybeDual: Option[String], maybePlural: Option[String]) = {
    (maybeSingular, maybeDual, maybePlural) match {
      case (Some(singular), Some(dual), Some(plural)) => Some((singular, dual, plural))
      case (Some(singular), Some(dual), None)         => Some((singular, dual, "{nbsp}"))
      case (Some(singular), None, Some(plural))       => Some((singular, "{nbsp}", plural))
      case (Some(singular), None, None)               => Some((singular, "{nbsp}", "{nbsp}"))
      case (None, Some(dual), Some(plural))           => Some(("{nbsp}", dual, plural))
      case (None, None, Some(plural))                 => Some(("{nbsp}", "{nbsp}", plural))
      case (None, Some(dual), None)                   => Some(("{nbsp}", dual, "{nbsp}"))
      case _                                          => None
    }
  }
}
