package com.alphasystem
package arabic
package cli
package morphologicalengine
package generator

import morphologicalengine.toConjugationTemplate
import arabic.model.{ ArabicLetterType, ArabicWord }
import arabic.morphologicalengine.generator.Settings
import arabic.morphologicalengine.conjugation.builder.ConjugationBuilder
import arabic.morphologicalengine.conjugation.model.{ AbbreviatedConjugation, ConjugationHeader, OutputFormat }
import arabic.morphologicalengine.generator.model.{ ChartConfiguration, ConjugationTemplate }

import java.nio.file.{ Files, Path }
import scala.collection.mutable.ListBuffer
import scala.jdk.CollectionConverters.*

object MorphologicalChartGenerator {

  private val ParticiplePrefix = ArabicWord(ArabicLetterType.Fa, ArabicLetterType.Ha, ArabicLetterType.Waw).htmlCode
  private val ParticiplePrefixAsciidoc = s"[arabicSmallGray]#$ParticiplePrefix#"

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

  private val ImperativePrefixAsciidoc = s"[arabicSmallGray]#$ImperativePrefix#"

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

  private val ForbiddenPrefixAsciidoc = s"[arabicSmallGray]#$ForbiddenPrefix#"

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

  private val AdverbsPrefixAsciidoc = s"[arabicSmallGray]#$AdverbPrefix#"

  def buildDocument(srcPath: Path, destPath: Path, attributes: String): Unit = {
    val conjugationBuilder = ConjugationBuilder()
    val conjugationTemplate = toConjugationTemplate(srcPath)
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

        val id = conjugationInput.id.toString

        if chartConfiguration.showAbbreviatedConjugation then {
          chart.abbreviatedConjugation match {
            case Some(abbreviatedConjugation) =>
              buffer.addAll(
                handleAbbreviatedConjugation(
                  id,
                  chartConfiguration,
                  chart.conjugationHeader,
                  abbreviatedConjugation,
                  conjugationInput.translation
                )
              )
            case None => // do nothing
          }
        }

        if !chartConfiguration.showAbbreviatedConjugation then {
          chart.abbreviatedConjugation match {
            case Some(abbreviatedConjugation) =>
              buffer
                .addAll(createTitleRow(chart.conjugationHeader.title))
                .addOne("")
                .addOne("[.NoSpacing]")
                .addOne("{nbsp}")
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

        // do not add page break if detail conjugations are not shown
        if detailedConjugation.nonEmpty then if index < inputs.length - 1 then buffer.addOne("<<<").addOne("")
      }

    buffer.toSeq
  }

  private def createTitleRow(title: String) = {
    val buffer = ListBuffer[String]()
    buffer
      .addOne("[%unbreakable]")
      .addOne("""[cols="^.^1", align="center", halign="center", valign="center", frame="none", grid="none"]""")
      .addOne("|===")
      .addOne(
        s"|[arabicHeading1]#$title#"
      )
      .addOne("|===")
      .addOne("")
      .toSeq
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

    if chartConfiguration.showTitle then buffer.addAll(createTitleRow(header.title))

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

    buffer.addOne("|===").addOne(s"// end::$tag[]").addOne("").addOne("[.NoSpacing]").addOne("{nbsp}").addOne("").toSeq
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
    maybeTranslation.foreach(translation => buffer.addOne(s"($translation)"))

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
    val activeParticiple = s"[arabicNormal]#${ac.activeParticiple}# $ParticiplePrefixAsciidoc"
    val verbalNounsValues = buildNounValues(ac.verbalNouns)
    val presentTense = s"[arabicNormal]#${ac.presentTense}#"
    val pastTense = s"[arabicNormal]#${ac.pastTense}#"
    s"2+^.^|$activeParticiple$verbalNounsValues$presentTense $pastTense"
  }

  private def buildPassiveLine(ac: AbbreviatedConjugation) = {
    if ac.hasPassiveLine then {
      val passiveParticiple = s"[arabicNormal]#${ac.passiveParticiple.getOrElse("")}# $ParticiplePrefixAsciidoc"
      val verbalNounsValues = buildNounValues(ac.verbalNouns)
      val presentPassiveTense = s"[arabicNormal]#${ac.presentPassiveTense.getOrElse("")}#"
      val pastPassiveTense = s"[arabicNormal]#${ac.pastPassiveTense.getOrElse("")}#"
      s"2+^.^|$passiveParticiple$verbalNounsValues$presentPassiveTense $pastPassiveTense"
    } else ""
  }

  private def buildNounValues(values: Seq[String]) =
    if values.isEmpty then " " else s" [arabicNormal]#${values.mkString(" {waw}")}# "

  private def buildImperativeLine(ac: AbbreviatedConjugation) = {
    val imperative = s"[arabicNormal]#${ac.imperative}# $ImperativePrefixAsciidoc"
    val forbidden = s"[arabicNormal]#${ac.forbidden}# $ForbiddenPrefixAsciidoc"
    val adverbs = ac.adverbs
    val adverbValues =
      if adverbs.nonEmpty then s" [arabicNormal]#${adverbs.mkString(" {waw}")}# $AdverbsPrefixAsciidoc "
      else " "
    s"2+^.^|$adverbValues$forbidden $imperative"
  }
}
