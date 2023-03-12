package com.alphasystem
package arabic
package morphologicalengine
package ui
package control

import arabic.fx.ui.util.*
import morphologicalengine.generator.model.{ ChartConfiguration, ConjugationTemplate }
import skin.MorphologicalChartSkin
import javafx.scene.control.{ Control, Skin }
import scalafx.beans.property.{ ObjectProperty, ReadOnlyBooleanWrapper, ReadOnlyObjectProperty, ReadOnlyObjectWrapper }

import java.nio.file.{ Path, Paths }

class MorphologicalChartView extends Control {

  private val defaultTemplate = ConjugationTemplate(ChartConfiguration(), Seq.empty)

  private val projectFileProperty = ObjectProperty[Option[Path]](this, "projectFile", None)
  private val projectNameWrapperProperty = ReadOnlyObjectWrapper[String](this, "projectName", "Untitled")
  private[control] val conjugationTemplateProperty =
    ObjectProperty[ConjugationTemplate](this, "conjugationTemplate", defaultTemplate)
  private val transientProjectProperty = new ReadOnlyBooleanWrapper(this, "transientProject", true)
  private[control] val actionProperty = ObjectProperty[TableAction](this, "action", TableAction.None)

  projectFileProperty.onChange((_, _, nv) => {
    transientProjectProperty.value = nv.isEmpty
    projectNameWrapperProperty.value = nv.map(getBaseName).getOrElse("Untitled")
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

  def transientProject: Boolean = transientProjectProperty.value

  override def createDefaultSkin(): Skin[_] = MorphologicalChartSkin(this)
}

object MorphologicalChartView {
  def apply(): MorphologicalChartView = new MorphologicalChartView()
}
