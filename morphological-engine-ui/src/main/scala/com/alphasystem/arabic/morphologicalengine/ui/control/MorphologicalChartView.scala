package com.alphasystem
package arabic
package morphologicalengine
package ui
package control

import arabic.fx.ui.util.*
import morphologicalengine.generator.model.{ ChartConfiguration, ConjugationTemplate }
import skin.MorphologicalChartSkin
import javafx.scene.control.{ Control, Skin }
import scalafx.beans.property.{ ObjectProperty, ReadOnlyBooleanWrapper }

import java.nio.file.{ Path, Paths }

class MorphologicalChartView extends Control {

  private val defaultTemplate = ConjugationTemplate(ChartConfiguration(), Seq.empty)

  private val projectNameProperty = ObjectProperty[Option[String]](this, "projectName", None)
  private val projectDirectoryProperty = ObjectProperty[Path](this, "projectDirectory", UserDir)
  private[control] val conjugationTemplateProperty =
    ObjectProperty[ConjugationTemplate](this, "conjugationTemplate", defaultTemplate)
  private val transientProjectProperty = new ReadOnlyBooleanWrapper(this, "transientProject", true)
  private[control] val actionProperty = ObjectProperty[TableAction](this, "action", TableAction.None)

  projectNameProperty.onChange((_, _, nv) => transientProjectProperty.value = nv.isEmpty)

  setSkin(createDefaultSkin())

  def projectName: Option[String] = projectNameProperty.value
  def projectName_=(value: Option[String]): Unit = projectNameProperty.value = value

  def projectDirectory: Path = projectDirectoryProperty.value
  def projectDirectory_=(value: Path): Unit = projectDirectoryProperty.value = value

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
