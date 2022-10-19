package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package tokeneditor
package control

import morphology.model.AbstractNounProperties
import com.alphasystem.morphologicalanalysis.morphology.model.{ NounStatus, PartOfSpeechType }
import scalafx.beans.property.ObjectProperty

abstract class AbstractNounPropertiesView[P <: PartOfSpeechType, AP <: AbstractNounProperties[P]]
    extends AbstractPropertiesView[P, AP] {

  val nounStatusProperty: ObjectProperty[NounStatus] =
    ObjectProperty[NounStatus](this, "nounStatus")

  nounStatusProperty.onChange { (_, oldValue, newValue) =>
    if oldValue != newValue then properties = update(newValue, properties)
  }

  def nounStatus: NounStatus = nounStatusProperty.value

  def nounStatus_=(value: NounStatus): Unit = nounStatusProperty.value = value

  def update(nounStatus: NounStatus, properties: AP): AP

  override protected def update(properties: AP): Unit = {
    super.update(properties)
    nounStatus = properties.status
  }
}
