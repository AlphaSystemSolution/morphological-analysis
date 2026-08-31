package com.alphasystem
package arabic
package morphologicalengine
package persistence

import arabic.model.{ ArabicLetterType, JussiveParticle }
import morphologicalengine.asciidoc_generator.*
import morphologicalengine.conjugation.model.{
  ConjugationConfiguration,
  NamedTemplate,
  RootLetters
}
import org.dizitart.no2.collection.{ Document, DocumentCursor }

import scala.jdk.CollectionConverters.*

package object nitrite {

  val IdFieldName = "id"
  val FirstRadicalFieldName = "first_radical"
  val BuckWalterFieldName = "buck_walter"
  private val SecondRadicalFieldName = "second_radical"
  private val ThirdRadicalFieldName = "third_radical"
  private val FourthRadicalFieldName = "fourth_rRadical"
  private val FamilyFieldName = "family"
  private val BaseTranslationFieldName = "base_translation"
  private val ConjugationConfigurationFieldName = "conjugation_configuration"
  private val TranslationsFieldName = "translations"
  private val VerbalNounsFieldName = "verbal_nouns"
  private val ConjugationTitleFieldName = "conjugation_title"
  private val MorphologicalChartFieldName = "morphological_chart"
  private val SkipRuleProcessingFieldName = "skip_rule_processing"
  private val RemovePassiveLineFieldName = "remove_passive_line"
  private val JussiveParticleFieldName = "jussive_particle"

  extension (src: DocumentCursor) {
    def asScalaList: List[Document] = src.asScala.toList
  }

  extension (src: Document) {
    def toRootInfo: RootInfo =
      RootInfo(
        rootLetters = RootLetters(
          firstRadical = toArabicLetterType(src.get(FirstRadicalFieldName, classOf[String])).get,
          secondRadical = toArabicLetterType(src.get(SecondRadicalFieldName, classOf[String])).get,
          thirdRadical = toArabicLetterType(src.get(ThirdRadicalFieldName, classOf[String])).get,
          fourthRadical = toArabicLetterType(src.get(FourthRadicalFieldName, classOf[String]))
        ),
        family = NamedTemplate.valueOf(src.get(FamilyFieldName, classOf[String])),
        baseTranslation = src.get(BaseTranslationFieldName, classOf[String]),
        conjugationConfiguration =
          src.get(ConjugationConfigurationFieldName, classOf[Document]).toConjugationConfiguration,
        verbalNounCodes = {
          val verbalNouns = src.get(VerbalNounsFieldName, classOf[String])
          if verbalNouns.isEmpty then Seq.empty
          else verbalNouns.split(",").toSeq
        },
        translations = Option(src.get(TranslationsFieldName, classOf[String])),
        conjugationTitle = Option(src.get(ConjugationTitleFieldName, classOf[String])),
        morphologicalChart = Option(src.get(MorphologicalChartFieldName, classOf[String])).map(toMorphologicalChart)
      )

    private def toConjugationConfiguration: ConjugationConfiguration =
      ConjugationConfiguration(
        skipRuleProcessing = src.get(SkipRuleProcessingFieldName, classOf[String]).toBoolean,
        removePassiveLine = src.get(RemovePassiveLineFieldName, classOf[String]).toBoolean,
        jussiveParticle = Option(src.get(JussiveParticleFieldName, classOf[String])).map(JussiveParticle.valueOf)
      )
  }

  extension (src: RootInfo) {
    def toDocument: Document =
      Document
        .createDocument(IdFieldName, src.id)
        .put(FirstRadicalFieldName, src.rootLetters.firstRadical.label)
        .put(SecondRadicalFieldName, src.rootLetters.secondRadical.label)
        .put(ThirdRadicalFieldName, src.rootLetters.thirdRadical.label)
        .put(FourthRadicalFieldName, src.rootLetters.fourthRadical.map(_.label).orNull)
        .put(BuckWalterFieldName, src.rootLetters.buckWalterString)
        .put(FamilyFieldName, src.family.name())
        .put(BaseTranslationFieldName, src.baseTranslation)
        .put(VerbalNounsFieldName, src.verbalNounCodes.mkString(","))
        .put(TranslationsFieldName, src.translations.orNull)
        .put(ConjugationTitleFieldName, src.conjugationTitle.orNull)
        .put(MorphologicalChartFieldName, src.morphologicalChart.map(_.toYaml).orNull)
        .put(ConjugationConfigurationFieldName, src.conjugationConfiguration.toDocument)

    def updateDocument(document: Document): Document =
      document
        .put(BaseTranslationFieldName, src.baseTranslation)
        .put(TranslationsFieldName, src.translations.orNull)
        .put(VerbalNounsFieldName, src.verbalNounCodes.mkString(","))
        .put(ConjugationConfigurationFieldName, src.conjugationConfiguration.toDocument)
        .put(ConjugationTitleFieldName, src.conjugationTitle.orNull)
        .put(MorphologicalChartFieldName, src.morphologicalChart.map(_.toYaml).orNull)
  }

  extension (src: ConjugationConfiguration) {
    def toDocument: Document =
      Document
        .createDocument(SkipRuleProcessingFieldName, src.skipRuleProcessing.toString)
        .put(RemovePassiveLineFieldName, src.removePassiveLine.toString)
        .put(JussiveParticleFieldName, src.jussiveParticle.map(_.name()).orNull)
  }

  private def toArabicLetterType(label: String): Option[ArabicLetterType] =
    Option(label).flatMap(l => ArabicLetterType.fromUnicode(l.charAt(0)))
}
