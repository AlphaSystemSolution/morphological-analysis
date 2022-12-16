package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package dependencygraph
package control

import morphologicalanalysis.morphology.model.{ NounStatus, PhraseType }
import control.skin.CreatePhraseSkin
import javafx.beans.binding.Bindings
import javafx.scene.control.{ Control, Skin }
import scalafx.beans.property.{ BooleanProperty, ObjectProperty, StringProperty }
import scalafx.collections.ObservableBuffer

class CreatePhraseView extends Control {

  private[control] val invalidSelectionProperty = BooleanProperty(true)

  private[control] val displayTextProperty = StringProperty("")

  private[control] val selectedPhraseTypesProperty = ObservableBuffer.empty[PhraseType]

  private[control] val nounStatusProperty = ObjectProperty[Option[NounStatus]](this, "nounStatus", None)

  invalidSelectionProperty.bind(Bindings.isEmpty(selectedPhraseTypesProperty))
  setSkin(createDefaultSkin())

  def displayText: String = displayTextProperty.value
  def displayText_=(value: String): Unit = displayTextProperty.value = value

  def nounStatus: Option[NounStatus] = nounStatusProperty.value
  def nounStatus_=(value: Option[NounStatus]): Unit = nounStatusProperty.value = value

  def selectedPhraseTypes: Seq[PhraseType] = selectedPhraseTypesProperty.toSeq
  def selectedPhraseTypes_=(values: Seq[PhraseType]): Unit = {
    selectedPhraseTypesProperty.clear()
    selectedPhraseTypesProperty.addAll(values)
  }

  def invalidSelection: Boolean = invalidSelectionProperty.value
  def invalidSelection_=(value: Boolean): Unit = invalidSelectionProperty.value = value

  override def createDefaultSkin(): Skin[_] = CreatePhraseSkin(this)
}

object CreatePhraseView {
  def apply(): CreatePhraseView = new CreatePhraseView()
}
