package com.alphasystem
package arabic
package examples_generator
package asciidoc

import munit.FunSuite

import java.nio.file.{ Files, Path }

class AsciiDocAttributesSpec extends FunSuite {

  test("loadAttributes: should return default attributes when ref.adoc does not exist") {
    val root = Files.createTempDirectory("asciidoc-attributes")
    val path = Files.createDirectories(root.resolve("includes"))
    val attributesFile = path.resolve(".attributes.txt")

    try {
      val expected = Seq(
        "// THIS FILE IS AUTO-GENERATED, DO NOT EDIT",
        ":encoding: utf-8",
        ":lang: en",
        ":last-update-label!:",
        "",
        "//",
        ""
      ).mkString(System.lineSeparator())

      assertEquals(loadAttributes(path), expected)
    } finally {
      Files.deleteIfExists(attributesFile)
      Files.deleteIfExists(path)
      Files.deleteIfExists(root)
    }
  }

  test("loadAttributes: should include ref.adoc relative to its ancestor") {
    val root = Files.createTempDirectory("asciidoc-attributes")
    val path = Files.createDirectories(root.resolve(Path.of("arabic-bare-essentials", "hadith", "includes")))
    val refDoc = Files.writeString(root.resolve("ref.adoc"), "")
    val attributesFile = path.resolve(".attributes.txt")

    try {
      val expected = Seq(
        "// THIS FILE IS AUTO-GENERATED, DO NOT EDIT",
        ":encoding: utf-8",
        ":lang: en",
        ":last-update-label!:",
        "",
        "include::../../../ref.adoc[]",
        "",
        "//",
        ""
      ).mkString(System.lineSeparator())

      assertEquals(loadAttributes(path), expected)
    } finally {
      Files.deleteIfExists(attributesFile)
      Files.deleteIfExists(refDoc)
      Files.deleteIfExists(path)
      Files.deleteIfExists(path.getParent)
      Files.deleteIfExists(path.getParent.getParent)
      Files.deleteIfExists(root)
    }
  }
}
