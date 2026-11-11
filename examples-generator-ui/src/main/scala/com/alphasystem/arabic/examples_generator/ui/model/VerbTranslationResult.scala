package com.alphasystem
package arabic
package examples_generator
package ui
package model

import arabic.model.ProNoun

case class VerbTranslationResult(translations: Map[ProNoun, Seq[String]] = Map.empty) {
  lazy val normalizedTranslations: Map[ProNoun, Seq[String]] =
    translations.flatMap { case (proNoun, values) =>
      val nonBlankValues = values.map(_.trim).filter(_.nonEmpty)
      Option.when(nonBlankValues.nonEmpty)(proNoun -> nonBlankValues)
    }

  lazy val sanitizedTranslations: Map[ProNoun, String] =
    ProNoun
      .values
      .map { proNoun =>
        proNoun -> normalizedTranslations.getOrElse(proNoun, Seq.empty).mkString(" / ")
      }
      .toMap
}

object VerbTranslationResult {
  def fromStringValues(translations: Map[ProNoun, String]): VerbTranslationResult = {
    val sanitizedMap =
      translations.map { case (proNoun, translation) =>
        proNoun -> translation.split("/").toList
      }
    val result = VerbTranslationResult(sanitizedMap)
    VerbTranslationResult(result.normalizedTranslations)
  }
}
