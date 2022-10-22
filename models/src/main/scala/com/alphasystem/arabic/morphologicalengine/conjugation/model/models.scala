package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package model

import arabic.model.{ NamedTemplate, RootType, VerbType, WeakVerbType }

import java.lang.Enum

case class ChartMode(
  template: NamedTemplate,
  rootType: RootType,
  verbType: VerbType,
  weakVerbType: WeakVerbType)

enum OutputFormat extends Enum[OutputFormat] {

  case Unicode
  case Html
  case BuckWalter
  case Stream
}
