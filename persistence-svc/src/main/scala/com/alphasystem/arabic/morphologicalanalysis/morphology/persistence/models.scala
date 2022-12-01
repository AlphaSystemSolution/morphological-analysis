package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package persistence

case class ChapterAlreadyExists(chapterNumber: Int) extends Exception(s"Chapter {$chapterNumber} already exists")

case class EntityNotFound[T](entityType: Class[T], id: String)
    extends Exception(s"${entityType.getSimpleName} with id {$id} does not exists")
