package com.alphasystem
package arabic
package morphologicalanalysis
package morphology

import java.nio.file.{ Files, Path, Paths }
import scala.annotation.targetName

package object utils {
  extension (src: String) {
    def toPath: Path = Paths.get(src)
  }

  extension (src: Path) {
    @targetName("appendAsDirectory")
    def +(others: String*) = {
      val path = Paths.get(src.toString, others*)
      createDirectories(path)
      path
    }

    @targetName("appendAsFile")
    def ->(others: String*): Path = Paths.get(src.toString, others*)
  }

  private def createDirectories(path: Path): Unit = if Files.notExists(path) then Files.createDirectories(path)
}
