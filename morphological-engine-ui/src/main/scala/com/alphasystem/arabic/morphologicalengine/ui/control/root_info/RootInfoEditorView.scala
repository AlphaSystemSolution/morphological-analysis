package com.alphasystem
package arabic
package morphologicalengine
package ui
package control
package root_info

import arabic.model.ArabicLetterType
import morphologicalengine.asciidoc_generator.RootInfo
import morphologicalengine.conjugation.forms.noun.VerbalNoun
import morphologicalengine.conjugation.forms.{ Form, NounSupport }
import morphologicalengine.conjugation.model.{
  ConjugationConfiguration,
  MorphologicalChart,
  NamedTemplate,
  RootLetters
}
import ui.utils.{ GetRootInfoService, SaveRootInfoService }
import javafx.scene.control.{ Control, Skin }
import scalafx.beans.property.{ BooleanProperty, ObjectProperty, StringProperty }
import scalafx.collections.ObservableBuffer

class RootInfoEditorView extends Control {

  import RootInfoEditorView.*

  private val getRootInfoService = GetRootInfoService(this)
  private val saveRootInfoService = SaveRootInfoService(this)
  private[control] val rootLettersProperty = ObjectProperty[RootLetters](this, "rootLetters", DefaultRootLetters)
  private[root_info] val familyProperty =
    ObjectProperty[NamedTemplate](this, "family", NamedTemplate.FormICategoryAGroupATemplate)
  private[root_info] val baseTranslationProperty = new StringProperty(this, "baseTranslation")
  private[root_info] val translationsProperty = new StringProperty(this, "otherTranslation")
  private[root_info] val skipRuleProcessingProperty = new BooleanProperty(this, "skipRuleProcessing", false)
  private[root_info] val removePassiveLineProperty = new BooleanProperty(this, "removePassiveLineProperty", false)
  private[control] val morphologicalChartProperty =
    ObjectProperty[Option[MorphologicalChart]](this, "morphologicalChart")
  private[root_info] val verbalNounsProperty: ObservableBuffer[NounSupport] = ObservableBuffer.empty[NounSupport]
  private[root_info] val errorStatusProperty: ObjectProperty[ErrorStatus] =
    ObjectProperty[ErrorStatus](this, "errorStatus")

  setSkin(createDefaultSkin())

  def rootLetters: RootLetters = rootLettersProperty.value
  def rootLetters_=(value: RootLetters): Unit = rootLettersProperty.value = value

  def family: NamedTemplate = familyProperty.value
  private def family_=(value: NamedTemplate): Unit = familyProperty.value = value

  def baseTranslation: String = baseTranslationProperty.value
  private def baseTranslation_=(value: String): Unit = baseTranslationProperty.value = value

  def translations: String = translationsProperty.value
  private def translations_=(value: String): Unit = translationsProperty.value = value

  def skipRuleProcessing: Boolean = skipRuleProcessingProperty.value
  private def skipRuleProcessing_=(value: Boolean): Unit = skipRuleProcessingProperty.value = value

  def removePassiveLine: Boolean = removePassiveLineProperty.value
  private def removePassiveLine_=(value: Boolean): Unit = removePassiveLineProperty.value = value

  def morphologicalChart: Option[MorphologicalChart] = morphologicalChartProperty.value
  def morphologicalChart_=(value: Option[MorphologicalChart]): Unit = morphologicalChartProperty.value = value

  def errorStatus: ErrorStatus = errorStatusProperty.value
  def errorStatus_=(value: ErrorStatus): Unit = errorStatusProperty.value = value

  def verbalNouns: Seq[NounSupport] = verbalNounsProperty.toSeq

  familyProperty.onChange((_, _, nv) => updateVerbalNouns(nv, Seq.empty))

  def update(rootInfo: RootInfo): Unit = {
    rootLetters = rootInfo.rootLetters
    family = rootInfo.family
    baseTranslation = rootInfo.baseTranslation
    skipRuleProcessing = rootInfo.conjugationConfiguration.skipRuleProcessing
    removePassiveLine = rootInfo.conjugationConfiguration.removePassiveLine
    translations = rootInfo.translations.getOrElse("")
    updateVerbalNouns(family, rootInfo.verbalNounCodes.flatMap(VerbalNoun.getVerbalNouns))
    morphologicalChart = rootInfo.morphologicalChart
  }

  private def updateVerbalNouns(family: NamedTemplate, verbalNouns: Seq[NounSupport]) = {
    verbalNounsProperty.clear()
    var _verbalNouns = verbalNouns
    if _verbalNouns.isEmpty then _verbalNouns = Form.fromNamedTemplate(family).verbalNouns
    verbalNounsProperty.addAll(_verbalNouns)
  }

  def toRootInfo: RootInfo =
    RootInfo(
      rootLetters = rootLetters,
      family = family,
      baseTranslation = baseTranslation,
      conjugationConfiguration = ConjugationConfiguration().copy(
        skipRuleProcessing = skipRuleProcessing,
        removePassiveLine = removePassiveLine
      ),
      verbalNounCodes = verbalNouns.map(_.code),
      translations = if translations.trim.isBlank then None else Some(translations)
    )

  /*
   * Loads root info from the rootLetters and family. Called when the rootLetters or family changes.
   */
  private[root_info] def loadRootInfo(rootLetters: RootLetters, family: NamedTemplate): Unit =
    getRootInfoService.executeService(rootLetters, family)

  /*
   * Saves the root info to the database. Called when the user clicks the save button.
   */
  private[root_info] def saveRootInfo(): Unit = saveRootInfoService.executeService(toRootInfo)

  override def createDefaultSkin(): Skin[?] = skin.RootInfoEditorSkin(this)
}

object RootInfoEditorView {

  def apply(): RootInfoEditorView = new RootInfoEditorView()

  private[root_info] val DefaultRootLetters = RootLetters(ArabicLetterType.Fa, ArabicLetterType.Ain, ArabicLetterType.Lam)

  case class ErrorStatus(header: String, errorMessage: String)
}
