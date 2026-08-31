package com.alphasystem
package arabic
package morphologicalengine
package persistence
package nitrite

import arabic.model.ArabicLetterType.*
import arabic.persistence.DatabaseSettings
import morphologicalengine.asciidoc_generator.RootInfo
import morphologicalengine.conjugation.forms.noun.VerbalNoun
import morphologicalengine.conjugation.model.NamedTemplate.{
  FormICategoryAGroupATemplate,
  FormICategoryIGroupATemplate,
  FormIIITemplate
}
import morphologicalengine.conjugation.model.RootLetters
import munit.FunSuite

import java.nio.file.Files

class NitriteDatabaseSpec extends FunSuite {

  private val db = NitriteDatabase()

  private val defaultRootLetters = RootLetters(firstRadical = Fa, secondRadical = Ain, thirdRadical = Lam)
  private val defaultRootInfo = RootInfo(
    rootLetters = defaultRootLetters,
    family = FormICategoryAGroupATemplate,
    baseTranslation = "To do",
    verbalNounCodes = Seq(VerbalNoun.FormIV1.code)
  )
  private val otherRootInfo = defaultRootInfo.copy(family = FormIIITemplate)
  private val thirdRootInfo = RootInfo(
    rootLetters = RootLetters(firstRadical = Fa, secondRadical = Ha, thirdRadical = Meem),
    family = FormICategoryIGroupATemplate,
    baseTranslation = "To understand",
    verbalNounCodes = Seq(VerbalNoun.FormIV1.code)
  )

  test("Insert new record") {
    db.rootInfoCollection.upsert(defaultRootInfo)
    val maybeRootInfo = db.rootInfoCollection.findById(defaultRootInfo.id)
    assert(maybeRootInfo.isDefined)
    assert(maybeRootInfo.get == defaultRootInfo)
  }

  test("Insert a different record") {
    db.rootInfoCollection.upsert(otherRootInfo)
    val maybeRootInfo = db.rootInfoCollection.findById(otherRootInfo.id)
    assert(maybeRootInfo.isDefined)
    assert(maybeRootInfo.get == otherRootInfo)
  }

  test("Insert record with same first radical") {
    db.rootInfoCollection.upsert(thirdRootInfo)
    val maybeRootInfo = db.rootInfoCollection.findById(thirdRootInfo.id)
    assert(maybeRootInfo.isDefined)
    assert(maybeRootInfo.get == thirdRootInfo)
  }

  test("Find by root letters") {
    val rootInfos = db.rootInfoCollection.findByRootLetters(defaultRootLetters)
    assert(rootInfos.size == 2)
    assert(rootInfos == Seq(defaultRootInfo, otherRootInfo))
  }

  test("Find by first radical") {
    val rootInfos = db.rootInfoCollection.findByFirstRadical(Fa)
    assert(rootInfos.size == 3)
    assert(rootInfos == Seq(defaultRootInfo, otherRootInfo, thirdRootInfo))
  }

  test("Open file-based NitriteDatabase") {
    val tempDir = Files.createTempDirectory("nitrite-test")
    val fileDb = NitriteDatabase(tempDir, DatabaseSettings("test.db", None, None))
    fileDb.rootInfoCollection.upsert(defaultRootInfo)
    val found = fileDb.rootInfoCollection.findById(defaultRootInfo.id)
    assert(found.isDefined)
    assert(found.get == defaultRootInfo)
    fileDb.close()
  }

  override def afterAll(): Unit = {
    db.close()
  }
}
