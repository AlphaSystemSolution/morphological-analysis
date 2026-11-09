package com.alphasystem
package arabic
package examples_generator

import examples_generator.morphologicalchart.{ ConjugationRequest, Settings, SingleConjugation }
import examples_generator.ui.utils.ExampleGeneratorPreferences
import arabic.fx.ui.util.UserHomeDir
import arabic.model.{ ArabicLabel, ArabicLetters, ArabicWord }
import arabic.morphologicalanalysis.ui.RootLettersKeyBoardView.DefaultRootLetters
import arabic.morphologicalengine.conjugation.model.{ ConjugationInput, MorphologicalTermType, NamedTemplate }
import arabic.morphologicalengine.generator.model.ConjugationTemplate
import arabic.utils.*

import java.nio.file.{ Files, Path, StandardCopyOption }
import java.util.UUID
import scala.util.Try

package object ui {

  private val generatorRoot: Path = UserHomeDir + Seq(".examples-generator")

  given preferences: ExampleGeneratorPreferences = ExampleGeneratorPreferences()

  val DefaultConjugationTemplate = ConjugationTemplate(tag = "", inputs = Seq.empty)

  val DefaultSettings = Settings()

  def DefaultConjugationInput = ConjugationInput(
    namedTemplate = NamedTemplate.FormICategoryAGroupATemplate,
    rootLetters = DefaultRootLetters
  )

  def DefaultConjugationRequest = ConjugationRequest(
    morphologicalTermType = MorphologicalTermType.PastTense,
    conjugationInput = DefaultConjugationInput
  )

  def DefaultSingleConjugation =
    SingleConjugation(tag = "", settings = DefaultSettings, request = DefaultConjugationRequest)

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
