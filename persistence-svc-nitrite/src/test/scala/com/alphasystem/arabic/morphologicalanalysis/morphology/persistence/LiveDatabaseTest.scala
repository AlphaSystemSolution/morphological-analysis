package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package persistence

import morphology.model.*
import morphology.utils.*
import org.dizitart.no2.Nitrite
import org.dizitart.no2.mvstore.MVStoreModule

object LiveDatabaseTest extends DatabaseInit {

  def main(args: Array[String]): Unit = {

    /*val chapterNumber = 1
    val verseNumber = 1
    val verseId = verseNumber.toVerseId(chapterNumber)
    val tokens = database.findTokensByVerseId(verseId)
    val tokenIds = tokens.map(_.id)
    println(s"Tokens: ${tokenIds.mkString("[", ", ", "]")}")

    database.close()*/

    val db = {
      val storeModule = MVStoreModule
        .withConfig()
        .filePath((rootPath -> databaseSettings.fileName).toString)
        .compress(true)
        .build()
      val _db = Nitrite
        .builder()
        .loadModule(storeModule)

      databaseSettings.userName match {
        case Some(userName) => _db.openOrCreate(userName, databaseSettings.password.getOrElse(userName))
        case None           => _db.openOrCreate()
      }
    }

    val graphNodeCollection = db.getCollection("graph_node")
    graphNodeCollection.drop()

    val dependencyGraphCollection = db.getCollection("dependency_graph")
    dependencyGraphCollection.drop()
  }
}
