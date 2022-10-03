package com.alphasystem.arabic.morphologicalanalysis.ui.tokeneditor.control

import com.alphasystem.arabic.morphologicalanalysis.morphology.model.ParticleProperties
import com.alphasystem.arabic.morphologicalanalysis.ui.tokeneditor.control.skin.ParticlePropertiesSkin
import com.alphasystem.morphologicalanalysis.morphology.model.*
import javafx.scene.control.Skin
import scalafx.beans.property.ObjectProperty

class ParticlePropertiesView
    extends BasePropertiesView[
      ParticlePartOfSpeechType,
      ParticleProperties
    ] {

  override protected val initialProperties: ParticleProperties =
    ParticleProperties(
      partOfSpeech = ParticlePartOfSpeechType.DEFINITE_ARTICLE
    )

  properties = initialProperties
  setSkin(createDefaultSkin())

  override protected def update(
    partOfSpeechType: ParticlePartOfSpeechType,
    properties: ParticleProperties
  ): ParticleProperties = properties.copy(partOfSpeech = partOfSpeechType)

  override def createDefaultSkin(): Skin[_] = ParticlePropertiesSkin(this)
}

object ParticlePropertiesView {

  def apply(): ParticlePropertiesView = new ParticlePropertiesView()
}
