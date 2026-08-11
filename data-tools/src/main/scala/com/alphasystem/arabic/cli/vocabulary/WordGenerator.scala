package com.alphasystem
package arabic
package cli
package vocabulary

import arabic.morphologicalengine.conjugation.model.{ NamedTemplate, RootLetters }
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.Locale

import io.circe.generic.auto.*
import io.circe.syntax.*
import io.circe.yaml.v12.*
import io.circe.yaml.v12.syntax.*
import io.circe.yaml.common.Printer.StringStyle
import scala.jdk.CollectionConverters.*
import scala.util.{ Failure, Success, Try, Using }

class WordGenerator(dataDir: Path) {

  private val yamlPrinter =
    Printer
      .builder
      .withStringStyle(StringStyle.DoubleQuoted)
      .build()

  private def encodedKey(root: RootLetters): String = root.buckWalterString.map(_.toInt.toString).mkString("_")

  private def encodedFile(root: RootLetters): Path = Paths.get(dataDir.toString, s"${encodedKey(root)}.yaml")

  private def legacyFile(root: RootLetters): Path = Paths.get(dataDir.toString, s"${root.buckWalterString}.yaml")

  private def writeWordList(path: Path, wordList: WordList): Unit =
    Files.writeString(path, yamlPrinter.pretty(wordList.asJson))

  private def readWithMigration(root: RootLetters): Option[WordList] = {
    val newFile = encodedFile(root)
    val oldFile = legacyFile(root)

    if newFile.toFile.exists() then Some(toWordList(newFile))
    else if oldFile.toFile.exists() then {
      val wordList = toWordList(oldFile)
      writeWordList(newFile, wordList)
      Files.deleteIfExists(oldFile)
      println(s"[data-tools] Migrated legacy file '${oldFile.getFileName}' to '${newFile.getFileName}'.")
      Some(wordList)
    } else None
  }

  def findWords(root: RootLetters): WordList =
    readWithMigration(root).getOrElse(
      throw new IllegalStateException(s"Word list not found for root: ${root.rawString}")
    )

  def findWordsByTranslation(translation: String): Seq[WordList] =
    val searchPhrase = translation.toLowerCase(Locale.ROOT)
    if !dataDir.toFile.exists() then Seq.empty
    else
      Using.resource(Files.list(dataDir)) { stream =>
        stream
          .iterator()
          .asScala
          .filter(path => Files.isRegularFile(path) && path.getFileName.toString.endsWith(".yaml"))
          .flatMap(path => Try(toWordList(path)).toOption)
          .map(wordList =>
            wordList.copy(words = wordList.words.filter(_.translation.toLowerCase(Locale.ROOT).contains(searchPhrase)))
          )
          .filter(_.words.nonEmpty)
          .toSeq
          .sorted
      }

  def findWordsByTranslationFlat(translation: String): Seq[TranslationSearchResult] =
    findWordsByTranslation(translation)
      .flatMap(wordList =>
        wordList
          .words
          .map(word =>
            TranslationSearchResult(
              root = wordList.root,
              text = word.text,
              family = word.family,
              translation = word.translation
            )
          )
      )
      .sortBy(result => (result.root, result.family.toString, result.text, result.translation))

  def findWord(root: RootLetters, family: NamedTemplate): Word =
    Try(findWords(root).words) match {
      case Success(words) =>
        words.find(_.family == family) match {
          case Some(word) => word
          case None =>
            throw new IllegalStateException(s"Word not found for root: ${root.rawString}, family: $family")
        }
      case Failure(ex: IllegalStateException) =>
        throw new IllegalStateException(s"Word not found for root: ${root.rawString}, family: $family", ex)
      case Failure(ex) => throw ex
    }
}
