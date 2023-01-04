package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package persistence
package nitrite
package collections
package listeners

import morphologicalanalysis.graph.model.GraphNodeType
import morphologicalanalysis.morphology.utils.*
import org.dizitart.no2.Nitrite
import org.dizitart.no2.event.{ ChangeInfo, ChangeListener, ChangeType }
import org.slf4j.LoggerFactory

import scala.jdk.CollectionConverters.*

class TokenChangeListener private (db: Nitrite) extends ChangeListener {

  private val logger = LoggerFactory.getLogger(classOf[TokenChangeListener])
  private val graphInfoCollection = GraphInfoCollection(db)
  override def onChange(changeInfo: ChangeInfo): Unit = {

    changeInfo.getChangedItems.asScala.toList.foreach { changedItem =>
      val changeType = changedItem.getChangeType
      logger.debug("Action = {}, OriginatingThread = {}", changeType, changeInfo.getOriginatingThread)
      val token = changedItem.getDocument.toToken
      changeType match
        case ChangeType.INSERT | ChangeType.UPDATE =>
          graphInfoCollection.upsertTerminalInfo(GraphNodeType.Terminal, token)
          token.locations.foreach(graphInfoCollection.upsertPartOfSpeechInfo)

        case ChangeType.REMOVE =>
          val ids = Seq(token.id.toUUID) ++ token.locations.map(_.id.toUUID)
          graphInfoCollection.removeByIds(ids*)

        case ChangeType.CLOSE => graphInfoCollection.collection.close()
        case ChangeType.DROP  => graphInfoCollection.collection.drop()
    }
  }
}

object TokenChangeListener {
  private[collections] def apply(db: Nitrite): TokenChangeListener = new TokenChangeListener(db)
}
