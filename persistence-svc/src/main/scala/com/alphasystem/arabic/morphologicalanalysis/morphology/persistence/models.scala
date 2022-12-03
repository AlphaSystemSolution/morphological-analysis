package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package persistence

case class EntityAlreadyExists[T](entityType: Class[T], id: String)
    extends Exception(s"${entityType.getSimpleName} with id {$id} already exists")
case class EntityNotFound[T](entityType: Class[T], id: String)
    extends Exception(s"${entityType.getSimpleName} with id {$id} does not exists")
