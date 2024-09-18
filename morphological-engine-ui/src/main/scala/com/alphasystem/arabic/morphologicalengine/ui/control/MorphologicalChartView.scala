package com.alphasystem
package arabic
package morphologicalengine
package ui
package control

import arabic.fx.ui.util.*
import morphologicalengine.conjugation.model.{ ConjugationInput, RootLetters }
import control.TableAction.{ Add, Duplicate, Remove }
import morphologicalengine.generator.model.{ ChartConfiguration, ConjugationTemplate }
import skin.MorphologicalChartSkin
import javafx.scene.control.{ Control, Skin }
import scalafx.beans.property.{
  BooleanProperty,
  ObjectProperty,
  ReadOnlyBooleanProperty,
  ReadOnlyBooleanWrapper,
  ReadOnlyObjectProperty,
  ReadOnlyObjectWrapper
}

import java.nio.file.{ Path, Paths }

class MorphologicalChartView extends Control {

  private val defaultTemplate = ConjugationTemplate("", ChartConfiguration(), Seq.empty)

  private val projectFileProperty = ObjectProperty[Option[Path]](this, "projectFile", None)
  private val projectNameWrapperProperty = ReadOnlyObjectWrapper[String](this, "projectName", "Untitled")
  private[control] val conjugationTemplateProperty =
    ObjectProperty[ConjugationTemplate](this, "conjugationTemplate", defaultTemplate)
  private val transientProjectWrapperProperty = new ReadOnlyBooleanWrapper(this, "transientProject", true)
  private val hasUnsavedChangesProperty = new BooleanProperty(this, "hasUnsavedChanges", false)
  private[control] val actionProperty = ObjectProperty[TableAction](this, "action", TableAction.None)
  private[control] val viewDictionaryProperty = new ObjectProperty[RootLetters](this, "viewDictionary")
  private[control] val duplicateRowProperty = new ObjectProperty[ConjugationInput](this, "duplicateRow")

  projectFileProperty.onChange((_, _, nv) => {
    transientProjectWrapperProperty.value = nv.isEmpty
    projectNameWrapperProperty.value = nv.map(getBaseName).getOrElse("Untitled")
  })
  actionProperty.onChange((_, _, nv) => {
    nv match
      case Add | Remove | Duplicate => hasUnsavedChanges = true
      case _                        =>
  })

  setSkin(createDefaultSkin())

  def projectFile: Option[Path] = projectFileProperty.value
  def projectFile_=(value: Option[Path]): Unit = projectFileProperty.value = value

  def projectName: String = projectNameWrapperProperty.value
  def projectNameProperty: ReadOnlyObjectProperty[String] = projectNameWrapperProperty.readOnlyProperty

  def conjugationTemplate: ConjugationTemplate = conjugationTemplateProperty.value
  def conjugationTemplate_=(value: ConjugationTemplate): Unit =
    conjugationTemplateProperty.value = if Option(value).isEmpty then defaultTemplate else value

  def action: TableAction = actionProperty.value
  def action_=(value: TableAction): Unit = actionProperty.value = value

  def transientProject: Boolean = transientProjectWrapperProperty.value

  def viewDictionary: RootLetters = viewDictionaryProperty.value
  def viewDictionary_=(value: RootLetters): Unit = viewDictionaryProperty.value = value

  def duplicateRow: ConjugationInput = duplicateRowProperty.value
  def duplicateRow_=(value: ConjugationInput): Unit = duplicateRowProperty.value = value

  def hasUnsavedChanges: Boolean = hasUnsavedChangesProperty.value
  def hasUnsavedChanges_=(value: Boolean): Unit = hasUnsavedChangesProperty.value = value

  override def createDefaultSkin(): Skin[?] = MorphologicalChartSkin(this)
}

object MorphologicalChartView {
  def apply(): MorphologicalChartView = new MorphologicalChartView()
}
