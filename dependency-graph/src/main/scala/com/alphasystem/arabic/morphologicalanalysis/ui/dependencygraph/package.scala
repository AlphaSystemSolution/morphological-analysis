package com.alphasystem
package arabic
package morphologicalanalysis
package ui

import morphology.model.{ ParticlePartOfSpeechType, PartOfSpeechType }
import model.ArabicLabel
import morphology.graph.model.DependencyGraph
import ui.dependencygraph.utils.DependencyGraphPreferences

package object dependencygraph {

  implicit val dependencyGraphPreferences: DependencyGraphPreferences = DependencyGraphPreferences()

  val HiddenPartOfSpeeches: Seq[PartOfSpeechType] = Seq(ParticlePartOfSpeechType.DefiniteArticle)

  extension (src: DependencyGraph) {
    def toArabicLabel: ArabicLabel[DependencyGraph] = ArabicLabel(userData = src, code = src.id, label = src.text)
  }
}
