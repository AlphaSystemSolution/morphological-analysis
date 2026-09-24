package com.alphasystem
package arabic
package morphologicalengine
package ui

import morphologicalengine.asciidoc_generator.{RootInfo, RootTitle}
import morphologicalengine.conjugation.model.{NamedTemplate, RootLetters}

case class ErrorStatus(header: String, errorMessage: String)

case class RootRequest(rootLetters: RootLetters, family: NamedTemplate)

case class RootInfos(currentRootInfo: Option[RootInfo], rootTitles: Seq[RootTitle])
