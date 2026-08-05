package com.alphasystem
package arabic
package cli
package vocabulary

import arabic.model.ArabicWord
import arabic.morphologicalengine.conjugation.builder.ConjugationBuilder
import arabic.morphologicalengine.conjugation.model.{ ConjugationConfiguration, ConjugationInput, NamedTemplate, RootLetters }
import arabic.morphologicalengine.conjugation.model.OutputFormat
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import io.circe.generic.auto.*
import io.circe.syntax.*
import io.circe.yaml.v12.*
import io.circe.yaml.v12.syntax.*
import scala.util.{ Failure, Success, Try }

class WordGenerator(dataDir: Path) {

  private val conjugationBuilder = new ConjugationBuilder()
  
  def generateWord(root: RootLetters, family: NamedTemplate, translation: String): Word = {
    val morphologicalChart = conjugationBuilder.doConjugation(
        input=ConjugationInput(
        rootLetters = root,
        namedTemplate = family,
        conjugationConfiguration = ConjugationConfiguration(),
        translation = Some(translation)
        ),
        outputFormat = OutputFormat.Unicode,
        showDetailedConjugation = false
    )

    morphologicalChart.abbreviatedConjugation match {
      case Some(abbreviatedConjugation) => Word(abbreviatedConjugation.pastTense, family, translation)
      case None => throw new IllegalStateException("Abbreviated conjugation should not be empty")
    }
  }

  def saveWord(root: RootLetters, family: NamedTemplate, translation: String): Unit = {
    val file = Paths.get(dataDir.toString, s"${root.buckWalterString}.yaml")
    val word = generateWord(root, family, translation)

    val wordList =
      if file.toFile.exists() then {
        val existingWordList = toWordList(file)
        val filteredWords = existingWordList.words.filterNot(_.family == family)
        val updatedWords = filteredWords match {
          case words if words.size != existingWordList.words.size => (words :+ word).sorted
          case words                                              => (words :+ word).sorted
        }
        existingWordList.copy(words = updatedWords)
      } else {
        WordList(root = root.rawString, words = Seq(word))
      }
    
    Files.writeString(file, wordList.asJson.asYaml.spaces2)
  }

    def findWords(root: RootLetters): WordList = {
    val file = Paths.get(dataDir.toString, s"${root.buckWalterString}.yaml")
    if file.toFile.exists() then toWordList(file)
    else throw new IllegalStateException(s"Word list not found for root: ${root.rawString}")
  }

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
