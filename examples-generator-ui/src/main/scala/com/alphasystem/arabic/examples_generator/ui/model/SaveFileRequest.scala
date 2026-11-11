package com.alphasystem
package arabic
package examples_generator
package ui
package model

import examples_generator.examples.ExampleFileGenerator
import examples_generator.model.{ ExampleData, fromExampleData, toExampleData }
import examples_generator.morphologicalchart.{
  Conjugations,
  ConjugationsFileGenerator,
  fromConjugations,
  toConjugations
}

import java.nio.charset.StandardCharsets
import java.nio.file.{ Files, Path, Paths, StandardCopyOption }

case class SaveFileRequest(
  path: Path,
  exampleData: Option[ExampleData] = None,
  conjugations: Option[Conjugations] = None,
  newFileSaved: Boolean = false) {

  def generateDocument(): Unit =
    (exampleData, conjugations) match {
      case (Some(exampleData), _)  => generateExampleDocument(exampleData)
      case (_, Some(conjugations)) => generateConjugationDocument(conjugations)
      case _                       => throw new IllegalArgumentException("Invalid request")
    }

  private def generateExampleDocument(exampleData: ExampleData): Unit = {
    val value = fromExampleData(exampleData)
    val backupFilePath = toBackupFile(path)
    Files.writeString(backupFilePath, value, StandardCharsets.UTF_8)
    toExampleData(backupFilePath)
    Files.copy(backupFilePath, path, StandardCopyOption.REPLACE_EXISTING)
    ExampleFileGenerator.generateDocument(exampleData = exampleData, destPath = toAsciidocPath(path))
  }

  private def generateConjugationDocument(conjugations: Conjugations): Unit = {
    val value = fromConjugations(conjugations)
    val backupFilePath = toBackupFile(path)
    Files.writeString(backupFilePath, value, StandardCharsets.UTF_8)
    toConjugations(backupFilePath)
    Files.copy(backupFilePath, path, StandardCopyOption.REPLACE_EXISTING)
    ConjugationsFileGenerator.generateDocument(conjugations = conjugations, destPath = toAsciidocPath(path))
  }

  private def toAsciidocPath(path: Path): Path = {
    val parent = path.getParent
    val fileName = path.getFileName.toString
    val indexOfExtension = fileName.lastIndexOf('.')
    val fileNameWithoutExtension = fileName.substring(0, indexOfExtension)
    Paths.get(parent.toString, s"$fileNameWithoutExtension.adoc")
  }
}
