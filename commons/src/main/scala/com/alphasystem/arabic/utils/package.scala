package com.alphasystem
package arabic

import com.typesafe.config.Config

import java.nio.file.{Files, Path, Paths}
import scala.annotation.targetName

package object utils {

  extension (src: Config) {
    def getOptionalString(path: String): Option[String] = if src.hasPath(path) then Some(src.getString(path)) else None
  }

  extension (src: String) {
    def asResourceUrl: String = Thread.currentThread().getContextClassLoader.getResource(src).toExternalForm
  }

  extension (src: Path) {
    @targetName("appendPaths")
    def /(others: Seq[String]): Path = Paths.get(src.toString, others*)

    @targetName("appendAsDirectory")
    def +(others: Seq[String]): Path = {
      val path = src / others
      createDirectories(path)
      path
    }

    @targetName("appendAsFile")
    def ->(others: String*): Path = Paths.get(src.toString, others*)
  }

  extension (src: String) {
    def toPath: Path = Paths.get(src)
  }

  private def createDirectories(path: Path): Unit = if Files.notExists(path) then Files.createDirectories(path)

}
