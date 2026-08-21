package com.alphasystem
package arabic

import java.nio.file.{Files, Path, Paths}
import scala.annotation.targetName

package object utils {

  extension (src: String) {
    def asResourceUrl: String = Thread.currentThread().getContextClassLoader.getResource(src).toExternalForm
  }

  extension (src: Path) {
    @targetName("appendAsDirectory")
    def +(others: Seq[String]) = {
      val path = Paths.get(src.toString, others *)
      createDirectories(path)
      path
    }
    

    @targetName("appendAsFile")
    def ->(others: String*): Path = Paths.get(src.toString, others *)
  }

  extension (src: String) {
    def toPath: Path = Paths.get(src)
  }

  private def createDirectories(path: Path): Unit = if Files.notExists(path) then Files.createDirectories(path)

}
