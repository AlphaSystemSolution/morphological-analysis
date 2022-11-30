package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package persistence
package nitrite
package collections

import java.util.UUID

case class ChapterAlreadyExists(chapterNumber: Int) extends Exception(s"Chapter {$chapterNumber} already exists")

case class EntityNotFound(entityType: String, id: String)
    extends Exception(s"$entityType with id {$id} does not exists")
