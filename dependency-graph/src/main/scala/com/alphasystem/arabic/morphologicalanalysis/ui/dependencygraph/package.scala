package com.alphasystem
package arabic
package morphologicalanalysis
package ui

import morphologicalanalysis.morphology.utils.*
import model.ArabicLabel
import morphology.graph.model.DependencyGraph
import ui.dependencygraph.utils.DependencyGraphPreferences

import java.nio.file.Path

package object dependencygraph {

  implicit val dependencyGraphPreferences: DependencyGraphPreferences = DependencyGraphPreferences()

  extension (src: DependencyGraph) {
    def toArabicLabel: ArabicLabel[DependencyGraph] =
      ArabicLabel(userData = src, code = src.id.toString, label = src.text)

    def toFileName(baseDir: Path, extension: String): Path = {
      val verseNumbers = src.verseNumbers
      val mainVerse = getPaddedFileName(verseNumbers.head)
      val subDir =
        if verseNumbers.size == 1 then mainVerse
        else s"$mainVerse-${getPaddedFileName(verseNumbers.last)}"
      baseDir + Seq(getPaddedFileName(src.chapterNumber), subDir) -> s"${src.id.toString}.$extension"
    }

    private def getPaddedFileName(n: Int): String = f"$n%03d"
  }
}
