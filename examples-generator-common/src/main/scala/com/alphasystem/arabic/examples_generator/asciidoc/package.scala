package com.alphasystem
package arabic
package examples_generator

import arabic.utils.*

import java.nio.file.{ Files, Path }
import scala.annotation.tailrec
import scala.collection.mutable.ListBuffer
import scala.jdk.CollectionConverters.*

package object asciidoc {

  private val DefaultAttributes = {
    val buffer = ListBuffer.empty[String]
    buffer
      .addOne("// THIS FILE IS AUTO-GENERATED, DO NOT EDIT")
      .addOne(":encoding: utf-8")
      .addOne(":lang: en")
      .addOne(":last-update-label!:")
      .addOne("")
    buffer
  }

  def loadAttributes(path: Path): String = {
    val attributesFile = path / Seq(".attributes.txt")
    if Files.exists(attributesFile) then Files.readAllLines(attributesFile).asScala.mkString(System.lineSeparator())
    else {
      val ref = getRefDoc(Some(path), "")
      val attributes = if ref.isEmpty then DefaultAttributes.addOne("//").addOne("")
      else DefaultAttributes.addOne(s"include::$ref[]").addOne("").addOne("//").addOne("")
      Files.write(attributesFile, attributes.asJava)
      attributes.mkString(System.lineSeparator())
    }
  }

  @tailrec
  private def getRefDoc(maybePath: Option[Path], result: String): String =
    maybePath match {
      case Some(path) =>
        val refDocPath = path / Seq("ref.doc")
        if Files.exists(refDocPath) then s"${result}ref.aadoc"
        else getRefDoc(Option(path.getParent), s"../$result")
      case None => ""
    }
}
