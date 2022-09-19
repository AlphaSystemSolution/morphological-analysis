package com.alphasystem.arabic.morphologicalanalysis.morphology.persistence.model

import com.alphasystem.morphologicalanalysis.morphology.model.PartOfSpeechType

case class PropertiesLifted(
  id: String,
  locationId: String,
  document: String)

case class LocationLifted(
  id: String,
  tokenId: String,
  document: String)
