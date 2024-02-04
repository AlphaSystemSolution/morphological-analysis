package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package persistence
package cache

import munit.FunSuite

trait CacheFactorySpec extends BaseRepositorySpec {

  self: FunSuite =>

  import concurrent.ExecutionContext.Implicits.global

  private lazy val cacheFactory = CacheFactory(database)

  test("CacheFactory: get all chapters") {
    assertEquals(cacheFactory.chapters.synchronous().get(0).map(_.chapterNumber), 1 to 12)
  }

  test("CacheFactory: get verses by chapter") {
    val verses = cacheFactory.verses.synchronous().get(1).map(v => (v.chapterNumber, v.verseNumber))
    val expected = (1 to 10).map(i => (1, i))
    assertEquals(verses, expected)
  }

  test("CacheFactory: get tokens by chapter and verse") {
    val tokens = cacheFactory.tokens.synchronous().get(TokenRequest(1, 1))
    val expected = createTokens(1, 1, 1, 10)
    assertEquals(tokens, expected)
  }
}
