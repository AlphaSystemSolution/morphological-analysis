package com.alphasystem.morphologicalengine.conjugation.model

import com.alphasystem.arabic.model.{
  NamedTemplate,
  RootType,
  VerbType,
  WeakVerbType
}

case class ChartMode(
  template: NamedTemplate,
  rootType: RootType,
  verbType: VerbType,
  weakVerbType: WeakVerbType)

enum OutputFormat {

  case UNICODE
  case HTML
  case BUCK_WALTER
  case STREAM
}
