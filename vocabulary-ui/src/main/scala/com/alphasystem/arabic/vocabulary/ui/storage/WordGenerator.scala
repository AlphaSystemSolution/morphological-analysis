package com.alphasystem
package arabic
package vocabulary
package ui
package storage

import morphologicalengine.conjugation.model.{ NamedTemplate, RootLetters }
import io.circe.generic.auto.*
import io.circe.syntax.*
import io.circe.yaml.v12.*
import io.circe.yaml.v12.syntax.*
import io.circe.yaml.common.Printer.StringStyle

import java.nio.file.{ Files, Path, Paths }

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
      println(s"[vocabulary-ui] Migrated legacy file '${oldFile.getFileName}' to '${newFile.getFileName}'.")
      Some(wordList)
    } else None
  }

  def saveWord(root: RootLetters, family: NamedTemplate, translation: String, word: String): Unit = {
    val file = encodedFile(root)
    val newWord = Word(word, family, translation)

    val wordList =
      readWithMigration(root) match {
        case Some(existingWordList) =>
        val filteredWords = existingWordList.words.filterNot(_.family == family)
        val updatedWords = filteredWords match {
          case words if words.size != existingWordList.words.size => (words :+ newWord).sorted
          case words                                              => (words :+ newWord).sorted
        }
        existingWordList.copy(words = updatedWords)
        case None => WordList(root = root.rawString, words = Seq(newWord))
      }

    writeWordList(file, wordList)
  }

  def findWords(root: RootLetters): WordList = readWithMigration(root).getOrElse(WordList(root = root.rawString, words = Seq.empty))

  def findWord(root: RootLetters, family: NamedTemplate): Option[Word] =
    findWords(root).words.find(_.family == family)
}
