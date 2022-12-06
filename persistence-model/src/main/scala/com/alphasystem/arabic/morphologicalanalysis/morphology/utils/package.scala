package com.alphasystem
package arabic
package morphologicalanalysis
package morphology

import morphology.model.{ ParticlePartOfSpeechType, PartOfSpeechType }

import java.nio.file.{ Files, Path, Paths }
import scala.annotation.targetName

package object utils {

  val HiddenPartOfSpeeches: Seq[PartOfSpeechType] =
    Seq(
      ParticlePartOfSpeechType.DefiniteArticle,
      ParticlePartOfSpeechType.QuranicPunctuation,
      ParticlePartOfSpeechType.QuranicInitial
    )

  extension (src: String) {
    def toPath: Path = Paths.get(src)
  }

  extension (src: Path) {
    @targetName("appendAsDirectory")
    def +(others: Seq[String]) = {
      val path = Paths.get(src.toString, others*)
      createDirectories(path)
      path
    }

    @targetName("appendAsFile")
    def ->(others: String*): Path = Paths.get(src.toString, others*)
  }

  private def createDirectories(path: Path): Unit = if Files.notExists(path) then Files.createDirectories(path)
}
