package com.alphasystem
package arabic
package cli
package vocabulary
package command

import arabic.model.ArabicLetterType
import arabic.morphologicalengine.conjugation.model.{ NamedTemplate, RootLetters }
import io.circe.generic.auto.*
import io.circe.syntax.*
import io.circe.yaml.v12.*
import io.circe.yaml.v12.syntax.*
import org.rogach.scallop.charConverter
import org.rogach.scallop.Subcommand
import org.rogach.scallop.stringConverter

import java.nio.file.{ Path, Paths }
import scala.util.{ Failure, Success, Try }

class WordGeneratorCommand extends Subcommand("word") {

  banner("Save and find words by root letters")

  private val dataDir = opt[Path](
    name = "data-dir",
    descr = "Path to vocabulary data directory",
    default = Some(Paths.get("data")),
    required = false
  )

  object SaveCommand extends Subcommand("save") {
    banner("Save word for a root and template")

    val firstRadical = opt[Char](name = "first-radical", required = true)
    val secondRadical = opt[Char](name = "second-radical", required = true)
    val thirdRadical = opt[Char](name = "third-radical", required = true)
    val fourthRadical = opt[Char](name = "fourth-radical", required = false)
    val family = opt[String](name = "family", required = true)
    val translation = opt[String](name = "translation", required = true)
  }

  object FindWordsCommand extends Subcommand("findWords") {
    banner("Find all words for a root")

    val firstRadical = opt[Char](name = "first-radical", required = true)
    val secondRadical = opt[Char](name = "second-radical", required = true)
    val thirdRadical = opt[Char](name = "third-radical", required = true)
    val fourthRadical = opt[Char](name = "fourth-radical", required = false)
  }

  object FindWordCommand extends Subcommand("findWord") {
    banner("Find a word for a root and template")

    val firstRadical = opt[Char](name = "first-radical", required = true)
    val secondRadical = opt[Char](name = "second-radical", required = true)
    val thirdRadical = opt[Char](name = "third-radical", required = true)
    val fourthRadical = opt[Char](name = "fourth-radical", required = false)
    val family = opt[String](name = "family", required = true)
  }

  addSubcommand(SaveCommand)
  addSubcommand(FindWordsCommand)
  addSubcommand(FindWordCommand)

  def execute(): Unit = {
    val generator = new WordGenerator(dataDir())

    subcommand match {
      case Some(SaveCommand) =>
        val root = toRootLetters(
          SaveCommand.firstRadical(),
          SaveCommand.secondRadical(),
          SaveCommand.thirdRadical(),
          SaveCommand.fourthRadical.toOption
        )
        val template = toNamedTemplate(SaveCommand.family())
        generator.saveWord(root, template, SaveCommand.translation())
      case Some(FindWordsCommand) =>
        val root = toRootLetters(
          FindWordsCommand.firstRadical(),
          FindWordsCommand.secondRadical(),
          FindWordsCommand.thirdRadical(),
          FindWordsCommand.fourthRadical.toOption
        )
        Try(generator.findWords(root)) match {
          case Success(words) => println(words.asJson.asYaml.spaces2)
          case Failure(e) => Console.err.println(e.getMessage)
        }
      case Some(FindWordCommand) =>
        val root = toRootLetters(
          FindWordCommand.firstRadical(),
          FindWordCommand.secondRadical(),
          FindWordCommand.thirdRadical(),
          FindWordCommand.fourthRadical.toOption
        )
        Try {
          val template = toNamedTemplate(FindWordCommand.family())
          generator.findWord(root, template)
        } match {
          case Success(word) => println(word.asJson.asYaml.spaces2)
          case Failure(e) => Console.err.println(e.getMessage)
        }
      case _ =>  printHelp()
    }
  }

  private def toRootLetters(
    first: Char,
    second: Char,
    third: Char,
    fourth: Option[Char]
  ): RootLetters =
    RootLetters(
      firstRadical = toArabicLetterType(first, "first-radical"),
      secondRadical = toArabicLetterType(second, "second-radical"),
      thirdRadical = toArabicLetterType(third, "third-radical"),
      fourthRadical = fourth.map(value => toArabicLetterType(value, "fourth-radical"))
    )

  private def toNamedTemplate(value: String): NamedTemplate = NamedTemplate.getByAlias(value)

  private def toArabicLetterType(value: Char, optionName: String): ArabicLetterType =
    Try(ArabicLetterType.UnicodesMap(value)).getOrElse {
      throw new IllegalArgumentException(s"Invalid $optionName: $value")
    }
}

object WordGeneratorCommand {
  def apply(): WordGeneratorCommand = new WordGeneratorCommand()
}
