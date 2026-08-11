package com.alphasystem
package arabic
package cli
package morphologicalengine
package generator

import cli.morphologicalengine.{ ConjugationRequest, PairedConjugation, Settings }
import arabic.model.ProNoun
import arabic.model.ProNoun.*
import arabic.morphologicalanalysis.morphology.model.{ ConversationType, GenderType, NounStatus, NumberType }
import arabic.morphologicalengine.conjugation.builder.ConjugationBuilder
import arabic.morphologicalengine.conjugation.model.*
import arabic.morphologicalengine.conjugation.model.MorphologicalTermType.*

import scala.collection.mutable.ListBuffer

class ConjugationGenerator(
  conjugationBuilder: ConjugationBuilder,
  settings: Settings,
  isNestedTable: Boolean) {

  private val tableWidth = settings.tableWidth.map(w => s""", width="$w%", """).getOrElse(", ")
  private val tableSeparator = if isNestedTable then "!" else "|"

  private val showPronouns = settings.showPronouns.getOrElse(false)
  private val showNumbers = settings.showNumbers.getOrElse(false)
  private val showGenders = settings.showGenders.getOrElse(false)
  private val showConversationTypes = settings.showConversationTypes.getOrElse(false)
  private val showNounStatus = settings.showNounStatus.getOrElse(false)
  private val showTermTypeCaption = settings.showTermTypeCaption.getOrElse(false)

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

  def runConjugation(conjugationRequest: ConjugationRequest): Seq[String] = {
    val morphologicalTermType = conjugationRequest.morphologicalTermType
    val maybeTranslations = conjugationRequest.translations
    val buffer = ListBuffer[String]()
    val chart = doConjugation(conjugationRequest)
    val translations = maybeTranslations.getOrElse(Map.empty)
    chart.detailedConjugation match {
      case Some(detailedConjugation) =>
        buffer.addAll(buildDocument(morphologicalTermType, detailedConjugation, translations))
      case None => throw new RuntimeException("Something went wrong to generate the chart")
    }
    buffer.toSeq
  }

  def buildPairedConjugation(
    tag: String,
    left: Option[ConjugationRequest],
    right: Option[ConjugationRequest]
  ): Seq[String] = {
    import PairedConjugation.*

    val leftTerm = left.map(_.morphologicalTermType).getOrElse(MorphologicalTermType.VerbalNoun)
    val rightTerm = right.map(_.morphologicalTermType).getOrElse(MorphologicalTermType.PastTense)
    if !hasSimilarTypes(rightTerm, leftTerm) then {
      // this should not happen, since we have already verified it
      throw new RuntimeException(s"The left and right conjugation types are not the same: $leftTerm and $rightTerm")
    }

    lazy val maybeLeftConjugationGroup =
      left.map(doConjugation).flatMap(_.detailedConjugation).flatMap(dc => getConjugation(leftTerm, dc))
    lazy val maybeRightConjugationGroup =
      right.map(doConjugation).flatMap(_.detailedConjugation).flatMap(dc => getConjugation(leftTerm, dc))

    leftTerm match {
      case PastTense | PresentTense | PastPassiveTense | PresentPassiveTense | Imperative | Forbidden =>
        createVerbConjugationGroupTable(
          tag,
          showTermTypeCaption,
          leftTerm,
          maybeLeftConjugationGroup.map(_.asInstanceOf[VerbConjugationGroup]),
          rightTerm,
          maybeRightConjugationGroup.map(_.asInstanceOf[VerbConjugationGroup])
        )
      case ActiveParticipleMasculine | ActiveParticipleFeminine | PassiveParticipleMasculine |
          PassiveParticipleFeminine | VerbalNoun | NounOfPlaceAndTime =>
        createNounConjugationGroupTable(
          tag,
          showTermTypeCaption,
          leftTerm,
          maybeLeftConjugationGroup.map(_.asInstanceOf[NounConjugationGroup]),
          rightTerm,
          maybeRightConjugationGroup.map(_.asInstanceOf[NounConjugationGroup])
        )
    }
  }

  def buildDetailedConjugation(
    id: String,
    showCaption: Boolean,
    detailedConjugation: DetailedConjugation
  ): Seq[String] = {
    val buffer = ListBuffer[String]()

    val activeTenseTable = createVerbConjugationGroupTable(
      s"${id}_activeTense",
      showCaption,
      PresentTense,
      Some(detailedConjugation.presentTense),
      PastTense,
      Some(detailedConjugation.pastTense)
    )
    buffer.addAll(activeTenseTable).addOne("")

    detailedConjugation.verbalNouns.sliding(2, 2).toSeq.zipWithIndex.foreach { (pairs, index) =>
      val maybeLeft = if pairs.size <= 1 then None else pairs.headOption
      val maybeRight = pairs.lastOption

      val verbalNounTable =
        createNounConjugationGroupTable(
          s"${id}_verbalNoun$index",
          showCaption,
          VerbalNoun,
          maybeLeft,
          VerbalNoun,
          maybeRight
        )
      if verbalNounTable.nonEmpty then buffer.addAll(verbalNounTable).addOne("")
    }

    val activeParticipleTable = createNounConjugationGroupTable(
      s"${id}_activeParticiple",
      showCaption,
      ActiveParticipleFeminine,
      Some(detailedConjugation.feminineActiveParticiple),
      ActiveParticipleMasculine,
      Some(detailedConjugation.masculineActiveParticiple)
    )
    buffer.addAll(activeParticipleTable).addOne("")

    val passiveTenseTable = createVerbConjugationGroupTable(
      s"${id}_passiveTense",
      showCaption,
      PresentPassiveTense,
      detailedConjugation.presentPassiveTense,
      PastPassiveTense,
      detailedConjugation.pastPassiveTense,
      keepTableTogether = true
    )
    if passiveTenseTable.nonEmpty then buffer.addAll(passiveTenseTable).addOne("")

    val passiveParticipleTable = createNounConjugationGroupTable(
      s"${id}_passiveParticiple",
      showCaption,
      PassiveParticipleFeminine,
      detailedConjugation.femininePassiveParticiple,
      PassiveParticipleMasculine,
      detailedConjugation.masculinePassiveParticiple
    )
    if passiveParticipleTable.nonEmpty then buffer.addAll(passiveParticipleTable).addOne("")

    val imperativeAndForbiddenTable = createVerbConjugationGroupTable(
      s"${id}_imperativeAndForbidden",
      showCaption,
      Forbidden,
      Some(detailedConjugation.forbidden),
      Imperative,
      Some(detailedConjugation.imperative)
    )
    buffer.addAll(imperativeAndForbiddenTable).addOne("")

    detailedConjugation.adverbs.sliding(2, 2).toSeq.zipWithIndex.foreach { (pairs, index) =>
      val maybeLeft = if pairs.size <= 1 then None else pairs.headOption
      val maybeRight = pairs.lastOption

      val adverbsTable =
        createNounConjugationGroupTable(
          s"${id}_adverbs$index",
          showCaption,
          NounOfPlaceAndTime,
          maybeLeft,
          NounOfPlaceAndTime,
          maybeRight
        )
      if adverbsTable.nonEmpty then buffer.addAll(adverbsTable).addOne("")
    }
    buffer.toSeq
  }

  private def doConjugation(conjugationRequest: ConjugationRequest) =
    conjugationBuilder.doConjugation(
      input = conjugationRequest.toConjugationInput,
      outputFormat = OutputFormat.Html,
      showAbbreviatedConjugation = false
    )

  private def buildDocument(
    morphologicalTermType: MorphologicalTermType,
    detailedConjugation: DetailedConjugation,
    translations: Map[ProNoun, String]
  ) = {
    morphologicalTermType match {
      case PastTense => buildVerbConjugationGroup(morphologicalTermType, detailedConjugation.pastTense, translations)
      case PresentTense =>
        buildVerbConjugationGroup(morphologicalTermType, detailedConjugation.presentTense, translations)
      case VerbalNoun =>
        val verbalNouns = detailedConjugation.verbalNouns
        if verbalNouns.isEmpty then throw new RuntimeException("Could not find verbal nouns conjugation")
        else buildNounConjugationGroup(morphologicalTermType, verbalNouns.head)
      case ActiveParticipleMasculine =>
        buildNounConjugationGroup(morphologicalTermType, detailedConjugation.masculineActiveParticiple)
      case ActiveParticipleFeminine =>
        buildNounConjugationGroup(morphologicalTermType, detailedConjugation.feminineActiveParticiple)
      case PastPassiveTense =>
        detailedConjugation.pastPassiveTense match {
          case Some(value) => buildVerbConjugationGroup(morphologicalTermType, value, translations)
          case None        => throw new RuntimeException("Could not find pastPassiveTense conjugation")
        }
      case PresentPassiveTense =>
        detailedConjugation.presentPassiveTense match {
          case Some(value) => buildVerbConjugationGroup(morphologicalTermType, value, translations)
          case None        => throw new RuntimeException("Could not find presentPassiveTense conjugation")
        }
      case PassiveParticipleMasculine =>
        detailedConjugation.masculinePassiveParticiple match {
          case Some(value) => buildNounConjugationGroup(morphologicalTermType, value)
          case None        => throw new RuntimeException("Could not find passive participle conjugation")
        }
      case PassiveParticipleFeminine =>
        detailedConjugation.femininePassiveParticiple match {
          case Some(value) => buildNounConjugationGroup(morphologicalTermType, value)
          case None        => throw new RuntimeException("Could not find passive participle conjugation")
        }
      case Imperative => buildVerbConjugationGroup(morphologicalTermType, detailedConjugation.imperative, translations)
      case Forbidden  => buildVerbConjugationGroup(morphologicalTermType, detailedConjugation.forbidden, translations)
      case MorphologicalTermType.NounOfPlaceAndTime =>
        throw new RuntimeException("Noun of place and time conjugation are not implemented yet!")
    }
  }

  private def getConjugation(morphologicalTermType: MorphologicalTermType, detailedConjugation: DetailedConjugation) = {
    morphologicalTermType match {
      case PastTense                  => Some(detailedConjugation.pastTense)
      case PresentTense               => Some(detailedConjugation.presentTense)
      case VerbalNoun                 => detailedConjugation.verbalNouns.headOption
      case ActiveParticipleMasculine  => Some(detailedConjugation.masculineActiveParticiple)
      case ActiveParticipleFeminine   => Some(detailedConjugation.feminineActiveParticiple)
      case PastPassiveTense           => detailedConjugation.pastPassiveTense
      case PresentPassiveTense        => detailedConjugation.presentPassiveTense
      case PassiveParticipleMasculine => detailedConjugation.masculinePassiveParticiple
      case PassiveParticipleFeminine  => detailedConjugation.femininePassiveParticiple
      case Imperative                 => Some(detailedConjugation.imperative)
      case Forbidden                  => Some(detailedConjugation.forbidden)
      case NounOfPlaceAndTime         => detailedConjugation.adverbs.headOption
    }
  }

  private def buildVerbConjugationGroup(
    term: MorphologicalTermType,
    verbConjugationGroup: VerbConjugationGroup,
    translations: Map[ProNoun, String] = Map.empty
  ) = {
    val buffer = ListBuffer[String]()
    var numOfColumns = 3
    if showGenders then numOfColumns += 1
    if showConversationTypes then numOfColumns += 1

    var cols = "^.^50,^.^50,^.^50"
    if numOfColumns == 4 then cols += ",^.^45"
    if numOfColumns == 5 then cols += ",^.^45,^.^45"

    buffer
      .addOne(s"""[cols="$cols"${tableWidth}align="center", halign="center", valign="center"]""")
      .addOne(s"$tableSeparator===")
      .addOne("")

    if showTermTypeCaption then buffer.addOne(buildTermCaption(term)).addOne("")

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

    buffer.addAll(
      handleThirdPersonConjugations(
        verbConjugationGroup.masculineThirdPerson,
        verbConjugationGroup.feminineThirdPerson,
        translations
      )
    )
    buffer.addAll(
      handleSecondPersonConjugations(
        verbConjugationGroup.masculineSecondPerson,
        verbConjugationGroup.feminineSecondPerson,
        translations
      )
    )
    buffer.addAll(handleFirstPersonConjugations(verbConjugationGroup.firstPerson, translations))

    buffer.addOne(s"$tableSeparator===").toSeq
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
          thirdPersonMasculineTranslations(translations)
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
          thirdPersonFeminineTranslations(translations)
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
          secondPersonMasculineTranslations(translations)
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
          secondPersonFeminineTranslations(translations)
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
      .map(buildFirstPersonVerbConjugationTuple(firstPersonTranslations(translations)))
      .foreach { result =>
        buffer.addOne("// 1st person").addAll(result).addOne("")
      }
    buffer.toSeq
  }

  private def buildVerbConjugationTuple(
    genderType: GenderType,
    conversationType: ConversationType,
    translations: Option[(String, String, String)]
  )(conjugationTuple: ConjugationTuple
  ) = {
    var proNoun = createPronoun(NumberType.Plural, genderType, conversationType)
    val plural = s"""$tableSeparator[arabicNormal]#${conjugationTuple.plural}#$proNoun """

    proNoun = createPronoun(NumberType.Dual, genderType, conversationType)
    val dual = conjugationTuple
      .dual
      .map(value => s"""$tableSeparator[arabicNormal]#$value#$proNoun """)
      .getOrElse(s"$tableSeparator{nbsp}")

    proNoun = createPronoun(NumberType.Singular, genderType, conversationType)
    val singular = s"""$tableSeparator[arabicNormal]#${conjugationTuple.singular}#$proNoun """

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
    translations: Option[(String, String, String)]
  )(conjugationTuple: ConjugationTuple
  ) = {
    var proNoun = createPronoun(NumberType.Plural, GenderType.Masculine, ConversationType.FirstPerson)
    val plural = s"""2+$tableSeparator[arabicNormal]#${conjugationTuple.plural}#$proNoun """

    proNoun = createPronoun(NumberType.Singular, GenderType.Masculine, ConversationType.FirstPerson)
    val singular = s"""$tableSeparator[arabicNormal]#${conjugationTuple.singular}#$proNoun """

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

  private def buildNounConjugationGroup(term: MorphologicalTermType, nounConjugationGroup: NounConjugationGroup) = {
    val buffer = ListBuffer[String]()
    var numOfColumns = 3
    if showNounStatus then numOfColumns += 1

    var cols = "^.^50,^.^50,^.^50"
    if numOfColumns == 4 then cols = "^.^14,^.^14,^.^14,^.^15"

    buffer
      .addOne(s"""[cols="$cols"${tableWidth}align="center", halign="center", valign="center"]""")
      .addOne(s"$tableSeparator===")
      .addOne("")

    if showTermTypeCaption then {
      buffer.addOne(buildTermCaption(term)).addOne("")
    }

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

    buffer.addOne(s"$tableSeparator===").toSeq
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

  private def buildTermCaption(term: MorphologicalTermType) =
    s"3+$tableSeparator[arabicTableCaption]#${term.title.htmlCode}#"

  private def createVerbConjugationGroupTable(
    tag: String,
    showCaption: Boolean,
    leftTerm: MorphologicalTermType,
    maybeLeftConjugation: Option[VerbConjugationGroup],
    rightTerm: MorphologicalTermType,
    maybeRightConjugation: Option[VerbConjugationGroup],
    keepTableTogether: Boolean = false
  ) =
    (maybeLeftConjugation, maybeRightConjugation) match {
      case (Some(leftConjugation), Some(rightConjugation)) =>
        val leftTable = buildVerbConjugationGroup(leftTerm, leftConjugation)
        val rightTable = buildVerbConjugationGroup(rightTerm, rightConjugation)
        createConjugationPairTable(tag, leftTable, rightTable)

      case _ => Seq.empty
    }

  private def createNounConjugationGroupTable(
    tag: String,
    showCaption: Boolean,
    leftTerm: MorphologicalTermType,
    maybeLeftConjugation: Option[NounConjugationGroup],
    rightTerm: MorphologicalTermType,
    maybeRightConjugation: Option[NounConjugationGroup]
  ) = {
    val leftTable = maybeLeftConjugation.map(cg => buildNounConjugationGroup(leftTerm, cg)).getOrElse(Seq.empty)
    var rightTable = maybeRightConjugation.map(cg => buildNounConjugationGroup(rightTerm, cg)).getOrElse(Seq.empty)

    // if rightTable is empty the switch it with leftTable, since we will only fill the table on the right side
    rightTable = if rightTable.isEmpty then leftTable else rightTable
    if leftTable.isEmpty && rightTable.isEmpty then Seq.empty
    else createConjugationPairTable(tag, leftTable, rightTable)
  }

  private def createConjugationPairTable(tag: String, leftTable: Seq[String], rightTable: Seq[String]) = {
    val buffer = ListBuffer[String](s"// tag::$tag[]")
    buffer
      .addOne("[%unbreakable]")
      .addOne("[.MultiColumnNestedTable]")
      .addOne("""[cols="^.^1,^.^1", align="center", halign="center", valign="center"]""")
      .addOne("|===")
      .addOne("")
      .addAll(createOuterTableRow(leftTable))
      .addOne("")
      .addAll(createOuterTableRow(rightTable))
      .addOne("")
      .addOne("|===")
      .addOne(s"// end::$tag[]")
      .addOne("")
      .addOne("[.NoSpacing]")
      .addOne("{nbsp}")
      .toSeq
  }

  private def createOuterTableRow(table: Seq[String]) = {
    val buffer = ListBuffer[String]()
    if table.isEmpty then buffer.addOne("|{nbsp}")
    else buffer.addOne("a|").addAll(table)
    buffer.toSeq
  }

  private def buildTranslationRow(translations: (String, String, String)) = {
    val singular = translations._1
    val dual = translations._2
    val plural = translations._3

    val dualColumn = if dual.isBlank then "" else s"$tableSeparator[translation]#$dual# "
    val pluralColumnSpan = if dual.isBlank then "2+" else ""
    s"$pluralColumnSpan$tableSeparator[translation]#$plural# $dualColumn$tableSeparator[translation]#$singular#"
  }

  private def createPronoun(numberType: NumberType, gender: GenderType, conversationType: ConversationType) =
    if showPronouns then
      s"{nbsp}[arabicSmallGray]##${ProNoun.fromProperties(numberType, gender, conversationType).toHtmlCode}##"
    else ""

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
    settings: Settings,
    isNestedTable: Boolean = false
  ): ConjugationGenerator =
    new ConjugationGenerator(conjugationBuilder, settings, isNestedTable)
}
