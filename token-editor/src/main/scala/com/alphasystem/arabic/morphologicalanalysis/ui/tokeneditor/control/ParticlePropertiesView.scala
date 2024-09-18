package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package tokeneditor
package control

import morphology.model.{ ParticleProperties, defaultParticleProperties }
import skin.ParticlePropertiesSkin
import morphology.model.ParticlePartOfSpeechType
import javafx.scene.control.Skin
import scalafx.beans.property.ObjectProperty

class ParticlePropertiesView
    extends BasePropertiesView[
      ParticlePartOfSpeechType,
      ParticleProperties
    ] {

  override protected val initialProperties: ParticleProperties =
    defaultParticleProperties

  properties = initialProperties
  setSkin(createDefaultSkin())

  override protected def update(
    partOfSpeechType: ParticlePartOfSpeechType,
    properties: ParticleProperties
  ): ParticleProperties = properties.copy(partOfSpeech = partOfSpeechType)

  override def createDefaultSkin(): Skin[?] = ParticlePropertiesSkin(this)
}

object ParticlePropertiesView {

  def apply(): ParticlePropertiesView = new ParticlePropertiesView()
}
