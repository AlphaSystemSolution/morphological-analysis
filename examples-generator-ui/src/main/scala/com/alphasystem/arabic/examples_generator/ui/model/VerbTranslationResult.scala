package com.alphasystem
package arabic
package examples_generator
package ui
package model

import arabic.model.ProNoun

case class VerbTranslationResult(translations: Map[ProNoun, Seq[String]] = Map.empty) {
  lazy val sanitizedTranslations: Map[ProNoun, String] =
    if translations.isEmpty then ProNoun.values.map(_ -> "").toMap
    else
      translations.map { case (proNoun, translations) =>
        proNoun -> translations.mkString(" / ")
      }
}

object VerbTranslationResult {
  def fromStringValues(translations: Map[ProNoun, String]): VerbTranslationResult = {
    val sanitizedMap =
      translations.map { case (proNoun, translation) =>
        proNoun -> translation.split("/").map(_.trim).toList
      }
    VerbTranslationResult(sanitizedMap)
  }
}
