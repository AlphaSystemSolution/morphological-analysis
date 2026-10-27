package com.alphasystem
package arabic
package examples_generator

import examples_generator.ui.utils.ExampleGeneratorPreferences
import arabic.model.{ ArabicLabel, ArabicLetters, ArabicWord }
import arabic.utils.*
import arabic.fx.ui.util.UserHomeDir

import java.nio.file.{ Files, Path, StandardCopyOption }
import java.util.UUID
import scala.util.{ Failure, Success, Try }

package object ui {

  private val generatorRoot: Path = UserHomeDir + Seq(".examples-generator")

  given preferences: ExampleGeneratorPreferences = ExampleGeneratorPreferences()

  extension (src: ChapterInfo) {
    def toArabicLabel: ArabicLabel[ChapterInfo] = {
      val label =
        ArabicWord(src.chapterName).concatWithSpace(ArabicLetters.NumberWordWithParenthesis(src.chapterNumber))
      ArabicLabel[ChapterInfo](src, src.chapterNumber.toString, label.unicode)
    }
  }

  def toBackupFile(path: Path): Path = {
    val fileNamePrefix = UUID.nameUUIDFromBytes(path.toString.getBytes).toString
    generatorRoot / Seq(s"$fileNamePrefix.yml")
  }

  def backupFile(path: Path): Try[Path] =
    Try(Files.copy(path, toBackupFile(path), StandardCopyOption.REPLACE_EXISTING))
}
