package com.alphasystem
package arabic
package morphologicalengine
package asciidoc_generator

import com.alphasystem.arabic.morphologicalengine.conjugation.model.ConjugationInput
import com.alphasystem.arabic.morphologicalengine.generator.model.{ ChartConfiguration, ConjugationTemplate }
import com.alphasystem.arabic.utils.*
import com.alphasystem.asciidoc.util.DocumentConverter

import java.nio.file.{ Files, Path, StandardCopyOption }

import scala.jdk.CollectionConverters.*

object ConjugationUtil {

  private val attributes =
    """// THIS FILE IS AUTO-GENERATED, DO NOT EDIT
      |:encoding: utf-8
      |:lang: en
      |:last-update-label!:
      |
      |//
      |""".stripMargin

  def generateDocuments(
    conjugationInput: ConjugationInput,
    srcDir: Path,
    otherTranslations: Seq[String] = Seq.empty
  ): Unit = {
    val rootPath = srcDir + Seq("data", conjugationInput.rootLetters.toDirectoryName)

    // saving actual input
    val rootInfo = conjugationInput.toRootInfo(otherTranslations)
    val family = conjugationInput.namedTemplate.name()
    val inputFilePath = rootPath -> s"$family.yaml"
    Files.writeString(inputFilePath, toYaml(rootInfo))

    // generate asciidoc and save
    val id = s"${conjugationInput.rootLetters.buckWalterString}_$family"
    val conjugationTemplate = ConjugationTemplate(id, ChartConfiguration(), Seq(conjugationInput))
    val generatedAsciidocFileName = s"${family}_generated.adoc"
    val asciidocFilePath = rootPath -> generatedAsciidocFileName
    MorphologicalChartGenerator.buildDocument(conjugationTemplate, asciidocFilePath, attributes)

    //copy main.adoc and include the generated asciidoc file
    val mainDocPath = srcDir -> "main.adoc"
    val destPath = rootPath -> s"$family.adoc"
    Files.copy(mainDocPath, destPath, StandardCopyOption.REPLACE_EXISTING)
    val lines = Files.readAllLines(destPath).asScala
    lines.append(s"include::${asciidocFilePath.getFileName}[]")
    Files.write(destPath, lines.asJava)

    // convert to HTML and save
    val asciiDocumentInfo = DocumentConverter.convertToHtml(destPath)
    val htmlFilePath = rootPath -> s"$family.html"
    Files.writeString(htmlFilePath, asciiDocumentInfo.getDocumentInfo.getContent)
  }
}
