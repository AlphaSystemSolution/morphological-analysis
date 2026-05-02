package com.alphasystem
package arabic
package cli
package morphologicalengine

import arabic.model.ProNoun
import arabic.model.ProNoun.*
import arabic.morphologicalanalysis.morphology.model.{ ConversationType, GenderType, NounStatus, NumberType }
import arabic.morphologicalengine.conjugation.builder.ConjugationBuilder
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

import scala.collection.mutable.ListBuffer

class ConjugationGenerator(
  conjugationBuilder: ConjugationBuilder,
  settings: DisplaySettings,
  isNestedTable: Boolean,
  tableWidth: Option[Int]) {

  private val tableWidthValue = tableWidth.map(s => s""", width="$s%", """).getOrElse(" ")
  private val tableSeparator = if isNestedTable then "!" else "|"

  private val showPronouns = settings.showPronouns.getOrElse(false)
  private val showNumbers = settings.showNumbers.getOrElse(false)
  private val showGenders = settings.showGenders.getOrElse(false)
  private val showConversationTypes = settings.showConversationTypes.getOrElse(false)
  private val showNounStatus = settings.showNounStatus.getOrElse(false)

  // Translations
  private lazy val thirdPersonMasculineTranslations = (translations: Map[ProNoun, String]) =>
    getTranslation(
      translations.get(ThirdPersonMasculineSingular),
      translations.get(ThirdPersonMasculineDual),
      translations.get(ThirdPersonMasculinePlural)
    )
  private lazy val thirdPersonFeminineTranslations = (translations: Map[ProNoun, String]) =>
    getTranslation(
      translations.get(ThirdPersonFeminineSingular),
      translations.get(ThirdPersonFeminineDual),
      translations.get(ThirdPersonFemininePlural)
    )
  private lazy val secondPersonMasculineTranslations = (translations: Map[ProNoun, String]) =>
    getTranslation(
      translations.get(SecondPersonMasculineSingular),
      translations.get(SecondPersonMasculineDual),
      translations.get(SecondPersonMasculinePlural)
    )
  private lazy val secondPersonFeminineTranslations = (translations: Map[ProNoun, String]) =>
    getTranslation(
      translations.get(SecondPersonFeminineSingular),
      translations.get(SecondPersonFeminineDual),
      translations.get(SecondPersonFemininePlural)
    )
  private lazy val firstPersonTranslations = (translations: Map[ProNoun, String]) =>
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

  def runConjugation(conjugationRequest: ConjugationRequest): Seq[String] = {
    val buffer = ListBuffer[String]()
    val chart = conjugationBuilder.doConjugation(
      input = ConjugationInput(
        namedTemplate = conjugationRequest.namedTemplate,
        conjugationConfiguration = ConjugationConfiguration(),
        rootLetters = conjugationRequest.rootLetters,
        translation = None,
        verbalNounCodes = conjugationRequest.verbalNouns.getOrElse(Seq.empty)
      ),
      outputFormat = OutputFormat.Html,
      showAbbreviatedConjugation = false
    )

    val translations = conjugationRequest.translations.getOrElse(Map.empty)

    chart.detailedConjugation match {
      case Some(detailedConjugation) =>
        buffer.addAll(buildDocument(conjugationRequest.morphologicalTermType, detailedConjugation, translations))
      case None => throw new RuntimeException("Something went wrong to generate the chart")
    }

    buffer.addOne(s"$tableSeparator===").toSeq
  }

  private def buildDocument(
    morphologicalTermType: MorphologicalTermType,
    detailedConjugation: DetailedConjugation,
    translations: Map[ProNoun, String]
  ) = {
    morphologicalTermType match {
      case MorphologicalTermType.PastTense => buildVerbConjugationGroup(detailedConjugation.pastTense, translations)
      case MorphologicalTermType.PresentTense =>
        buildVerbConjugationGroup(detailedConjugation.presentTense, translations)
      case MorphologicalTermType.VerbalNoun =>
        val verbalNouns = detailedConjugation.verbalNouns
        if verbalNouns.isEmpty then throw new RuntimeException("Could not find verbal nouns conjugation")
        else buildNounConjugationGroup(verbalNouns.head)
      case MorphologicalTermType.ActiveParticipleMasculine =>
        buildNounConjugationGroup(detailedConjugation.masculineActiveParticiple)
      case MorphologicalTermType.ActiveParticipleFeminine =>
        buildNounConjugationGroup(detailedConjugation.feminineActiveParticiple)
      case MorphologicalTermType.PastPassiveTense =>
        detailedConjugation.pastPassiveTense match {
          case Some(value) => buildVerbConjugationGroup(value, translations)
          case None        => throw new RuntimeException("Could not find pastPassiveTense conjugation")
        }
      case MorphologicalTermType.PresentPassiveTense =>
        detailedConjugation.presentPassiveTense match {
          case Some(value) => buildVerbConjugationGroup(value, translations)
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
      case MorphologicalTermType.Imperative => buildVerbConjugationGroup(detailedConjugation.imperative, translations)
      case MorphologicalTermType.Forbidden  => buildVerbConjugationGroup(detailedConjugation.forbidden, translations)
      case MorphologicalTermType.NounOfPlaceAndTime =>
        throw new RuntimeException("Noun of place and time conjugation are not implemented yet!")
    }
  }

  private def buildVerbConjugationGroup(
    verbConjugationGroup: VerbConjugationGroup,
    translations: Map[ProNoun, String]
  ) = {
    val buffer = ListBuffer[String]()
    var numOfColumns = 3
    if showGenders then numOfColumns += 1
    if showConversationTypes then numOfColumns += 1

    var cols = "^.^50,^.^50,^.^50"
    if numOfColumns == 4 then cols = "^.^40,^.^40,^.^40,^.^50"
    if numOfColumns == 5 then cols = "^.^40,^.^40,^.^40,^.^50,^.^50"

    buffer
      .addOne(s"""[cols="$cols"${tableWidthValue}align="center", halign="center", valign="center"]""")
      .addOne(s"$tableSeparator===")
      .addOne("")

    if showNumbers then {
      val extraColumns =
        if numOfColumns == 4 then s" $tableSeparator{nbsp}"
        else if numOfColumns == 5 then s" 2+$tableSeparator{nbsp}"
        else ""
      buffer
        .addOne(
          s"$tableSeparator[arabicTableCaption]#{plural}# $tableSeparator[arabicTableCaption]#{dual}# $tableSeparator[arabicTableCaption]#{singular}#$extraColumns"
        )
        .addOne("")
    }

    buffer.addAll(handleThirdPersonConjugations(
      verbConjugationGroup.masculineThirdPerson,
      verbConjugationGroup.feminineThirdPerson,
      translations
    ))
    buffer.addAll(handleSecondPersonConjugations(
      verbConjugationGroup.masculineSecondPerson,
      verbConjugationGroup.feminineSecondPerson,
      translations
    ))
    buffer.addAll(handleFirstPersonConjugations(verbConjugationGroup.firstPerson, translations))

    buffer.toSeq
  }

  private def handleThirdPersonConjugations(
    masculineThirdPerson: Option[ConjugationTuple],
    feminineThirdPerson: Option[ConjugationTuple],
    translations: Map[ProNoun, String]
  ) = {
    val buffer = ListBuffer[String]()
    masculineThirdPerson
      .map(
        buildVerbConjugationTuple(
          GenderType.Masculine,
          ConversationType.ThirdPerson,
          thirdPersonMasculineTranslations(translations),
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
          thirdPersonFeminineTranslations(translations),
          thirdPersonFemininePronoun
        )
      )
      .foreach { result =>
        buffer.addOne("// 3rd person, feminine").addAll(result).addOne("")
      }
    buffer.toSeq
  }

  private def handleSecondPersonConjugations(
    masculineSecondPerson: ConjugationTuple,
    feminineSecondPerson: ConjugationTuple,
    translations: Map[ProNoun, String]
  ) = {
    val buffer = ListBuffer[String]()
    buffer
      .addOne("// 2nd person, masculine")
      .addAll(
        buildVerbConjugationTuple(
          GenderType.Masculine,
          ConversationType.SecondPerson,
          secondPersonMasculineTranslations(translations),
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
          secondPersonFeminineTranslations(translations),
          secondPersonFemininePronoun
        )(
          feminineSecondPerson
        )
      )
      .addOne("")

    buffer.toSeq
  }

  private def handleFirstPersonConjugations(
    firstPerson: Option[ConjugationTuple],
    translations: Map[ProNoun, String]
  ) = {
    val buffer = ListBuffer[String]()
    firstPerson
      .map(buildFirstPersonVerbConjugationTuple(firstPersonTranslations(translations), firstPersonPronoun))
      .foreach { result =>
        buffer.addOne("// 1st person").addAll(result).addOne("")
      }
    buffer.toSeq
  }

  private def buildVerbConjugationTuple(
    genderType: GenderType,
    conversationType: ConversationType,
    translations: Option[(String, String, String)],
    pronouns: Map[NumberType, String]
  )(conjugationTuple: ConjugationTuple
  ) = {
    val plural = s"""$tableSeparator[arabicNormal]#${conjugationTuple.plural}#${createPronoun(
      pronouns.get(NumberType.Plural)
    )} """
    val dual = conjugationTuple
      .dual
      .map(value => s"""$tableSeparator[arabicNormal]#$value#${createPronoun(pronouns.get(NumberType.Dual))} """)
      .getOrElse(s"$tableSeparator{nbsp}")
    val singular =
      s"""$tableSeparator[arabicNormal]#${conjugationTuple.singular}#${createPronoun(
        pronouns.get(NumberType.Singular)
      )} """

    // if translations are provided, then they will go as a separate row after arabic text, in this case gender column will span two rows
    val genderColumnPrefix = if translations.nonEmpty then ".2+" else ""

    // conversation type column will span two columns if translations are empty otherwise it will span 4 columns
    var conversationTypeColumnSpan = 0
    if showConversationTypes then conversationTypeColumnSpan = 2
    if translations.nonEmpty then conversationTypeColumnSpan += 2

    val genders =
      if showGenders then s"$genderColumnPrefix$tableSeparator[arabicTableCaption]#${genderType.toHtmlCode}# " else ""
    val conversationTypes =
      if showConversationTypes && genderType == GenderType.Masculine then
        s".$conversationTypeColumnSpan+$tableSeparator[arabicTableCaption]#${conversationType.toHtmlCode}# "
      else ""

    Seq(s"$plural$dual$singular$genders$conversationTypes") ++ translations.map(buildTranslationRow)
  }

  private def buildFirstPersonVerbConjugationTuple(
    translations: Option[(String, String, String)],
    pronouns: Map[NumberType, String]
  )(conjugationTuple: ConjugationTuple
  ) = {
    val plural = s"""2+$tableSeparator[arabicNormal]#${conjugationTuple.plural}#${createPronoun(
      pronouns.get(NumberType.Plural)
    )} """
    val singular =
      s"""$tableSeparator[arabicNormal]#${conjugationTuple.singular}#${createPronoun(
        pronouns.get(NumberType.Singular)
      )} """

    // if translations are provided then conversation type column will span two rows
    val rowspan = if translations.nonEmpty then ".2" else ""

    // if no gender column, then colspan will be 2, otherwise no colspan
    val colspan = if showGenders then "2" else ""
    val conversationTypeColumnPrefix = if rowspan.nonEmpty || colspan.nonEmpty then "+" else ""
    val conversationTypeColumn =
      if showConversationTypes then
        s"$colspan$rowspan$conversationTypeColumnPrefix$tableSeparator[arabicTableCaption]#${ConversationType.FirstPerson.toHtmlCode}# "
      else ""

    val genderColumnPrefix = if rowspan.nonEmpty then "+" else ""
    val genderColumn =
      if showGenders && !showConversationTypes then s"$rowspan$genderColumnPrefix$tableSeparator{nbsp}" else ""

    Seq(s"$plural$singular$genderColumn$conversationTypeColumn") ++ translations
      .map(t => (t._1, "", t._3))
      .map(buildTranslationRow)
  }

  private def buildNounConjugationGroup(nounConjugationGroup: NounConjugationGroup) = {
    val buffer = ListBuffer[String]()
    var numOfColumns = 3
    if showNounStatus then numOfColumns += 1

    var cols = "^.^1,^.^1,^.^1"
    if numOfColumns == 4 then cols = "^.^14,^.^14,^.^14,^.^15"

    buffer
      .addOne(s"""[cols="$cols"${tableWidthValue}align="center", halign="center", valign="center"]""")
      .addOne(s"$tableSeparator===")
      .addOne("")

    if showNumbers then {
      val extraColumns = if numOfColumns == 4 then s" $tableSeparator{nbsp}" else ""
      buffer
        .addOne(
          s"$tableSeparator[arabicTableCaption]#{plural}# $tableSeparator[arabicTableCaption]#{dual}# $tableSeparator[arabicTableCaption]#{singular}#$extraColumns"
        )
        .addOne("")
    }

    buffer
      .addOne(handleConjugationTuple(nounConjugationGroup.nominative, NounStatus.Nominative))
      .addOne(handleConjugationTuple(nounConjugationGroup.accusative, NounStatus.Accusative))
      .addOne(handleConjugationTuple(nounConjugationGroup.genitive, NounStatus.Genitive))
      .addOne("")

    buffer.toSeq
  }

  private def handleConjugationTuple(conjugationTuple: ConjugationTuple, nounStatus: NounStatus) = {
    val plural = s"""$tableSeparator[arabicNormal]#${conjugationTuple.plural}# """
    val dual = conjugationTuple
      .dual
      .map(value => s"""$tableSeparator[arabicNormal]#$value# """)
      .getOrElse(s"$tableSeparator{nbsp}")
    val singular = s"""$tableSeparator[arabicNormal]#${conjugationTuple.singular}# """
    val nounStatusColumn =
      if showNounStatus then s"$tableSeparator[arabicTableCaption]#${nounStatus.shortLabel.htmlCode}# " else ""
    s"$plural$dual$singular$nounStatusColumn"
  }

  private def buildTranslationRow(translations: (String, String, String)) = {
    val singular = translations._1
    val dual = translations._2
    val plural = translations._3

    val dualColumn = if dual.isBlank then "" else s"$tableSeparator[translation]#$dual# "
    val pluralColumnSpan = if dual.isBlank then "2+" else ""
    s"$pluralColumnSpan$tableSeparator[translation]#$plural# $dualColumn$tableSeparator[translation]#$singular#"
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

object ConjugationGenerator {
  def apply(
    conjugationBuilder: ConjugationBuilder,
    settings: DisplaySettings,
    isNestedTable: Boolean = false,
    tableWidth: Option[Int] = None
  ): ConjugationGenerator =
    new ConjugationGenerator(conjugationBuilder, settings, isNestedTable, tableWidth)
}
