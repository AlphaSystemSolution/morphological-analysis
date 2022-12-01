package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package persistence

import com.alphasystem.arabic.morphologicalanalysis.morphology.graph.model.TerminalNode
import morphology.utils.*
import morphology.model.{ Chapter, Token, Verse }
import persistence.nitrite.DatabaseSettings
import persistence.nitrite.collections.{ ChapterCollection, TerminalNodeCollection, TokenCollection, VerseCollection }
import org.dizitart.no2.Nitrite

import java.nio.file.Path

class NitriteDatabase(rootPath: Path, dbSettings: DatabaseSettings) extends Database {

  private val db: Nitrite = {
    val _db = Nitrite
      .builder()
      .compressed()
      .filePath((rootPath -> dbSettings.fileName).toString)

    dbSettings.userName match {
      case Some(userName) => _db.openOrCreate(userName, dbSettings.password.getOrElse(userName))
      case None           => _db.openOrCreate()
    }
  }

  private val chapterCollection = ChapterCollection(db)
  private val verseCollection = VerseCollection(db)
  private val tokenCollection = TokenCollection(db)
  private val terminalNodeCollection = TerminalNodeCollection(db, tokenCollection)

  override def createChapter(chapter: Chapter): Unit = chapterCollection.createChapter(chapter)

  override def createVerses(verses: Seq[Verse]): Unit = verseCollection.createVerses(verses)

  override def createTokens(tokens: Seq[Token]): Unit = {
    tokenCollection.createTokens(tokens)
    terminalNodeCollection.upsertTerminalNodes(tokens)
  }

  override def updateToken(token: Token): Unit = {
    tokenCollection.update(token)
    terminalNodeCollection.upsertTerminalNode(token)
  }

  override def findChapterById(chapterNumber: Int): Option[Chapter] =
    chapterCollection.findByChapterNumber(chapterNumber)

  override def findAllChapters: List[Chapter] = chapterCollection.findAll

  override def findVerseById(verseId: Long): Option[Verse] = verseCollection.findById(verseId)

  override def findVersesByChapterNumber(chapterNumber: Int): List[Verse] =
    verseCollection.findByChapterNumber(chapterNumber)

  override def findTokenById(tokenId: Long): Option[Token] = tokenCollection.findById(tokenId)

  override def findTokensByVerseId(verseId: Long): Seq[Token] = tokenCollection.findByVerseId(verseId)

  override def findTerminalNodesByTokenIds(tokenIds: Seq[Long]): Seq[TerminalNode] =
    terminalNodeCollection.findByTokenIds(tokenIds)

  override def removeTokensByVerseId(verseId: Long): Unit = {
    tokenCollection.deleteByVerseId(verseId)
    terminalNodeCollection.deleteByVerseId(verseId)
  }

  override def close(): Unit = {
    chapterCollection.collection.close()
    verseCollection.collection.close()
    tokenCollection.collection.close()
    terminalNodeCollection.collection.close()
  }
}

object NitriteDatabase {
  def apply(rootPath: Path, dbSettings: DatabaseSettings): Database = new NitriteDatabase(rootPath, dbSettings)
}
