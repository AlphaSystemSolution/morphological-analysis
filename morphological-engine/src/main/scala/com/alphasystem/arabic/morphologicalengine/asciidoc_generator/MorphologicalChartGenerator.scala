package com.alphasystem
package arabic
package morphologicalengine
package asciidoc_generator

import arabic.model.{ ArabicLetterType, ArabicWord }
import arabic.morphologicalengine.conjugation.builder.ConjugationBuilder
import arabic.morphologicalengine.conjugation.model.{
  AbbreviatedConjugation,
  ConjugationHeader,
  MorphologicalChart,
  OutputFormat
}
import arabic.morphologicalengine.generator.model.{ ChartConfiguration, ConjugationTemplate }

import java.nio.file.{ Files, Path }
import scala.collection.mutable.ListBuffer
import scala.jdk.CollectionConverters.*

object MorphologicalChartGenerator {

  private val ParticiplePrefix = ArabicWord(ArabicLetterType.Fa, ArabicLetterType.Ha, ArabicLetterType.Waw).htmlCode
  private val ParticiplePrefixAsciidoc = s"[arabicSmallGray]##$ParticiplePrefix##"

  private val ImperativePrefix = ArabicWord(
    ArabicLetterType.Alif,
    ArabicLetterType.Lam,
    ArabicLetterType.AlifHamzaAbove,
    ArabicLetterType.Meem,
    ArabicLetterType.Ra,
    ArabicLetterType.Space,
    ArabicLetterType.Meem,
    ArabicLetterType.Noon,
    ArabicLetterType.Ha
  ).htmlCode

  private val ImperativePrefixAsciidoc = s"[arabicSmallGray]##$ImperativePrefix##"

  private val ForbiddenPrefix = ArabicWord(
    ArabicLetterType.Waw,
    ArabicLetterType.Noon,
    ArabicLetterType.Ha,
    ArabicLetterType.Ya,
    ArabicLetterType.Space,
    ArabicLetterType.Ain,
    ArabicLetterType.Noon,
    ArabicLetterType.Ha
  ).htmlCode

  private val ForbiddenPrefixAsciidoc = s"[arabicSmallGray]##$ForbiddenPrefix##"

  private val AdverbPrefix =
    ArabicWord(
      ArabicLetterType.Waw,
      ArabicLetterType.Alif,
      ArabicLetterType.Lam,
      ArabicLetterType.Dtha,
      ArabicLetterType.Ra,
      ArabicLetterType.Fa,
      ArabicLetterType.Space,
      ArabicLetterType.Meem,
      ArabicLetterType.Noon,
      ArabicLetterType.Ha
    ).htmlCode

  private val AdverbsPrefixAsciidoc = s"[arabicSmallGray]##$AdverbPrefix##"

  def buildDocument(srcPath: Path, destPath: Path, attributes: String): Unit =
    buildDocument(toConjugationTemplate(srcPath), destPath, attributes)

  def buildDocument(conjugationTemplate: ConjugationTemplate, destPath: Path, attributes: String): Unit = {
    val conjugationBuilder = ConjugationBuilder()
    val buffer = ListBuffer(attributes)
    buffer.addAll(buildMorphologicalChart(conjugationBuilder, conjugationTemplate))
    Files.write(destPath, buffer.toSeq.asJava)
  }

  private def buildMorphologicalChart(
    conjugationBuilder: ConjugationBuilder,
    conjugationTemplate: ConjugationTemplate
  ): Seq[String] = {
    val conjugationGenerator = ConjugationGenerator(
      conjugationBuilder,
      Settings(showTermTypeCaption =
        Some(conjugationTemplate.chartConfiguration.showMorphologicalTermCaptionInDetailConjugation)
      ),
      isNestedTable = true
    )

    val buffer = ListBuffer[String]()
    val chartConfiguration = conjugationTemplate.chartConfiguration

    val inputs = conjugationTemplate.inputs
    inputs
      .zipWithIndex
      .foreach { (conjugationInput, index) =>

        val chart = conjugationBuilder.doConjugation(
          input = conjugationInput,
          outputFormat = OutputFormat.Html,
          removeAdverbs = chartConfiguration.removeAdverbs,
          showDetailedConjugation = chartConfiguration.showDetailedConjugation
        )

        val id = s"${conjugationInput.rootLetters.buckWalterString}_${conjugationInput.namedTemplate.name()}"

        buffer.addAll(
          buildMorphologicalChart(
            id = id,
            chart = chart,
            chartConfiguration = chartConfiguration,
            conjugationGenerator = conjugationGenerator,
            translation = conjugationInput.translation
          )
        )

        // do not add page break if detail conjugations are not shown
        if chartConfiguration.showDetailedConjugation then
          if index < inputs.length - 1 then buffer.addOne("<<<").addOne("")
      }

    buffer.toSeq
  }

  private def buildMorphologicalChart(
    id: String,
    chart: MorphologicalChart,
    chartConfiguration: ChartConfiguration,
    conjugationGenerator: ConjugationGenerator,
    translation: Option[String]
  ): Seq[String] = {
    val buffer = ListBuffer[String]()

    if chartConfiguration.showAbbreviatedConjugation then {
      chart.abbreviatedConjugation match {
        case Some(abbreviatedConjugation) =>
          buffer.addAll(
            handleAbbreviatedConjugation(
              id,
              chartConfiguration,
              chart.conjugationHeader,
              abbreviatedConjugation,
              translation
            )
          )
        case None => // do nothing
      }
    }

    if !chartConfiguration.showAbbreviatedConjugation then {
      chart.abbreviatedConjugation match {
        case Some(abbreviatedConjugation) =>
          buffer
            .addAll(createTitleRow(id, chart.conjugationHeader.title))
            .addOne("")
        case None => // do nothing
      }
    }

    val detailedConjugation = chart.detailedConjugation
    detailedConjugation match {
      case Some(detailedConjugation) =>
        buffer.addAll(
          conjugationGenerator.buildDetailedConjugation(
            id,
            chartConfiguration.showMorphologicalTermCaptionInDetailConjugation,
            detailedConjugation
          )
        )
      case None => // do nothing
    }

    buffer.toSeq
  }

  private def createTitleRow(id: String, title: String) = {
    val buffer = ListBuffer[String]()
    buffer.addOne(s"[[$id]]").addOne("[.text-center]").addOne(s"== [arabicHeading1]#$title#").addOne("").toSeq
  }

  private def handleAbbreviatedConjugation(
    id: String,
    chartConfiguration: ChartConfiguration,
    header: ConjugationHeader,
    abbreviatedConjugation: AbbreviatedConjugation,
    maybeTranslation: Option[String]
  ) = {
    val tag = s"${id}_abbreviatedConjugation"

    val buffer = ListBuffer[String]()
    buffer.addOne(s"// tag::$tag[]")

    if chartConfiguration.showTitle then buffer.addAll(createTitleRow(id, header.title))

    buffer
      .addOne("[%unbreakable]")
      .addOne("""[cols="2*", align="center", halign="center", valign="center"]""")
      .addOne("|===")
      .addOne("")
      .addAll(buildRootLettersAndLabelsRow(chartConfiguration.showLabels, header, maybeTranslation))
      .addOne(buildActiveLine(abbreviatedConjugation))
      .addOne(buildPassiveLine(abbreviatedConjugation))
      .addOne(buildImperativeLine(abbreviatedConjugation))
      .addOne("")
      .addOne("|===")
      .addOne(s"// end::$tag[]")
      .addOne("")
      .toSeq
  }

  private def buildRootLettersAndLabelsRow(
    showLabels: Boolean,
    header: ConjugationHeader,
    maybeTranslation: Option[String]
  ) = {
    val buffer = ListBuffer[String]()

    val rootLettersColumnPrefix = if showLabels then "^.^|" else s"2+^.^|"
    buffer
      .addOne(s"$rootLettersColumnPrefix[arabicNormal]#${header.rootLetters.arabicWord.htmlCode}#")
      .addOne("")
    maybeTranslation.foreach(translation => buffer.addOne(s"([translation]#$translation#)"))

    if showLabels then {
      buffer
        .addOne(s">.^|[arabicSmall]#${header.templateTypeLabel}#")
        .addOne("")
        .addOne(s"[arabicSmall]#${header.weightLabel}#")
        .addOne("")
        .addOne(s"[arabicSmall]#${header.verbTypeLabel}#")
    }

    buffer.toSeq
  }

  private def buildActiveLine(ac: AbbreviatedConjugation) = {
    val verbalNounsValues = buildNounValues(ac.verbalNouns)
    val line =
      s"${ac.pastTense}&#0032;${ac.presentTense}$verbalNounsValues$ParticiplePrefixAsciidoc&#0032;${ac.activeParticiple}"
    s"2+^.^|[arabicNormal]#$line#"
  }

  private def buildPassiveLine(ac: AbbreviatedConjugation) = {
    if ac.hasPassiveLine then {
      val verbalNounsValues = buildNounValues(ac.verbalNouns)
      val line =
        s"${ac.pastPassiveTense.getOrElse("")}&#0032;${ac.presentPassiveTense.getOrElse("")}$verbalNounsValues$ParticiplePrefixAsciidoc&#0032;${ac.passiveParticiple.getOrElse("")}"
      s"2+^.^|[arabicNormal]#$line#"
    } else ""
  }

  private def buildNounValues(values: Seq[String]) =
    if values.isEmpty then "&#0032;" else s"&#0032;${values.mkString("&#0032;{waw}")} "

  private def buildImperativeLine(ac: AbbreviatedConjugation) = {
    val line = s"$ImperativePrefixAsciidoc&#0032;${ac.imperative}&#0032;$ForbiddenPrefixAsciidoc&#0032;${ac.forbidden}"
    val adverbs = ac.adverbs
    val adverbValues =
      if adverbs.nonEmpty then s"&#0032;$AdverbsPrefixAsciidoc&#0032;${adverbs.mkString(" {waw}")}"
      else ""
    s"2+^.^|[arabicNormal]#{nbsp}$line$adverbValues#"
  }
}
