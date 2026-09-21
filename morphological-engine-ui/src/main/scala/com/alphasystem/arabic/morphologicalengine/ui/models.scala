package com.alphasystem
package arabic
package morphologicalengine
package ui

import com.alphasystem.arabic.morphologicalengine.asciidoc_generator.RootInfo
import com.alphasystem.arabic.morphologicalengine.conjugation.model.{ NamedTemplate, RootLetters }

case class ErrorStatus(header: String, errorMessage: String)

case class RootRequest(rootLetters: RootLetters, family: NamedTemplate)

case class RootInfos(currentRootInfo: Option[RootInfo], rootInfos: Seq[RootInfo])
