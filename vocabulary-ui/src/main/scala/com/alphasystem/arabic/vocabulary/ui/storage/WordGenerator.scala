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

import java.nio.file.{ Files, Path, Paths }

class WordGenerator(dataDir: Path) {

  def saveWord(root: RootLetters, family: NamedTemplate, translation: String, word: String): Unit = {
    val file = Paths.get(dataDir.toString, s"${root.buckWalterString}.yaml")
    val newWord = Word(word, family, translation)

    val wordList =
      if file.toFile.exists() then {
        val existingWordList = toWordList(file)
        val filteredWords = existingWordList.words.filterNot(_.family == family)
        val updatedWords = filteredWords match {
          case words if words.size != existingWordList.words.size => (words :+ newWord).sorted
          case words                                              => (words :+ newWord).sorted
        }
        existingWordList.copy(words = updatedWords)
      } else {
        WordList(root = root.rawString, words = Seq(newWord))
      }

    Files.writeString(file, wordList.asJson.asYaml.spaces2)
  }

  def findWords(root: RootLetters): WordList = {
    val file = Paths.get(dataDir.toString, s"${root.buckWalterString}.yaml")
    if file.toFile.exists() then toWordList(file)
    else WordList(root = root.rawString, words = Seq.empty)
  }

  def findWord(root: RootLetters, family: NamedTemplate): Option[Word] =
    findWords(root).words.find(_.family == family)
}
