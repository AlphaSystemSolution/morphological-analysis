package com.alphasystem
package arabic
package morphologicalengine
package ui

import com.alphasystem.arabic.examples_generator.morphologicalchart.{ RootInfo, RootTitle }
import morphologicalengine.conjugation.model.{ NamedTemplate, RootLetters }

case class ErrorStatus(header: String, errorMessage: String)

case class RootRequest(rootLetters: RootLetters, family: NamedTemplate)

case class RootInfos(currentRootInfo: Option[RootInfo], rootTitles: Seq[RootTitle])
