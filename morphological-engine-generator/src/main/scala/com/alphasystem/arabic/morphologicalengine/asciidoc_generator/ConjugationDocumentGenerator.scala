package com.alphasystem
package arabic
package morphologicalengine
package asciidoc_generator

import morphologicalengine.conjugation.model.{ConjugationInput, NamedTemplate}
import morphologicalengine.generator.model.{ChartConfiguration, ConjugationTemplate}
import arabic.utils.*
import com.alphasystem.asciidoc.util.DocumentConverter

import java.nio.file.{Files, Path, StandardOpenOption}
import scala.collection.mutable.ListBuffer
import scala.jdk.CollectionConverters.*
import scala.util.Using

object ConjugationDocumentGenerator {

  private val DataDirName = "data"

  private val MainAttributes =
    """
      |:encoding: utf-8
      |:lang: en
      |:linkcss:
      |:iconfont-remote!:
      |:compact:
      |:stylesdir: ../../include/
      |:stylesheet: arabic.css
      |:docinfodir: ../../include/
      |:docinfo2:
      |:sectids:
      |:toc:
      |:secnums:
      |:last-update-label!:
      |:includedir: ../../include/
      |
      |include::{includedir}/ref.adoc[]
      |
      |//
      |""".stripMargin.split(System.lineSeparator()).toBuffer

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
    val rootPath = srcDir + Seq(DataDirName, conjugationInput.rootLetters.toDirectoryName)
    val family = conjugationInput.namedTemplate.name()
    val inputFilePath = rootPath -> s"$family.yaml"
    val existingFamily = Files.exists(inputFilePath)

    // save actual input
    val rootInfo = conjugationInput.toRootInfo(otherTranslations)
    Files.writeString(inputFilePath, toYaml(rootInfo))

    // generate asciidoc and save
    val id = s"${conjugationInput.rootLetters.buckWalterString}_$family"
    val conjugationTemplate = ConjugationTemplate(id, ChartConfiguration(), Seq(conjugationInput))
    val generatedAsciidocFileName = s"$family.adoc"
    val generatedAsciidocFilePath = rootPath -> generatedAsciidocFileName
    MorphologicalChartGenerator.buildDocument(conjugationTemplate, generatedAsciidocFilePath, attributes)

    val destPath = rootPath -> "main.adoc"
    // if this family generated the first time, then add it `main.adoc`
    if !existingFamily then {
      // create copy of attributes; otherwise asciidoc will complain about duplicate ids, since same family will be included twice
      val buffer = ListBuffer[String]()
      buffer.addAll(MainAttributes)
      buffer.addOne("")
      val families = listFamilies(rootPath)
        .sortBy(_.index)
        .flatMap(family => Seq(s"include::$family.adoc[]", ""))

      buffer.addAll(families)
      Files.write(destPath, buffer.asJava, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)
    }

    // convert to HTML and save
    val asciiDocumentInfo = DocumentConverter.convertToHtml(destPath)
    Files.writeString(rootPath -> "main.html", asciiDocumentInfo.getDocumentInfo.getContent)
  }

  private def listFamilies(rootPath: Path) = {
    Using(Files.newDirectoryStream(rootPath, "*.{yaml,yml}")) { dirStream =>
      dirStream.asScala.map(_.getFileName.toString)
    }.toOption.map(_.toSeq).getOrElse(Seq.empty).map(_.stripSuffix(".yaml")).map(NamedTemplate.valueOf)
  }
}
