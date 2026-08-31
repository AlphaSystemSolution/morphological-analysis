package com.alphasystem
package arabic
package morphologicalengine
package persistence
package nitrite

import arabic.utils.*
import arabic.persistence.DatabaseSettings
import org.dizitart.no2.Nitrite
import org.dizitart.no2.mvstore.MVStoreModule

import java.nio.file.Path
import scala.util.Try

class NitriteDatabase(rootPath: Path, dbSettings: DatabaseSettings) {

  private val db: Nitrite = {
    val _db =
      dbSettings
        .fileName
        .map { value =>
          val storeModule = MVStoreModule
            .withConfig()
            .filePath((rootPath -> value).toString)
            .compress(true)
            .build()
          Nitrite.builder().loadModule(storeModule)
        }
        .getOrElse(Nitrite.builder())

    dbSettings.userName match {
      case Some(userName) => _db.openOrCreate(userName, dbSettings.password.getOrElse(userName))
      case None           => _db.openOrCreate()
    }
  }

  val rootInfoCollection: RootInfoCollection = RootInfoCollection(db)

  def close(): Unit = {
    Try(rootInfoCollection.collection.close())
    Try(db.close())
  }
}

object NitriteDatabase {
  def apply(): NitriteDatabase = NitriteDatabase(Path.of("."))

  def apply(rootPath: Path, dbSettings: DatabaseSettings = DatabaseSettings()): NitriteDatabase =
    new NitriteDatabase(rootPath, dbSettings)
}
