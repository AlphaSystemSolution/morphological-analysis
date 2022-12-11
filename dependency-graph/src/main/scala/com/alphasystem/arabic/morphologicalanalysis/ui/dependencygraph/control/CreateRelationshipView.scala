package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package dependencygraph
package control

import morphologicalanalysis.morphology.model.{ Linkable, RelationshipType }
import control.skin.CreateRelationshipSkin
import javafx.scene.control.{ Control, Skin }
import scalafx.beans.property.{ BooleanProperty, ObjectProperty }

class CreateRelationshipView extends Control {

  private[control] val ownerProperty = ObjectProperty[Linkable](this, "owner")

  private[control] val dependentProperty = ObjectProperty[Linkable](this, "dependent")

  private[control] val invalidSelectionProperty = BooleanProperty(true)

  private[control] val relationshipTypeProperty =
    ObjectProperty[RelationshipType](this, "relationshipType", RelationshipType.None)

  setSkin(createDefaultSkin())

  def owner: Linkable = ownerProperty.value
  def owner_=(value: Linkable): Unit = ownerProperty.value = value

  def dependent: Linkable = dependentProperty.value
  def dependent_=(value: Linkable): Unit = dependentProperty.value = value

  def relationshipType: RelationshipType = relationshipTypeProperty.value
  def relationshipType_=(value: RelationshipType): Unit = relationshipTypeProperty.value = value

  def invalidSelection: Boolean = invalidSelectionProperty.value
  def invalidSelection_=(value: Boolean): Unit = invalidSelectionProperty.value = value

  override def createDefaultSkin(): Skin[_] = new CreateRelationshipSkin(this)
}

object CreateRelationshipView {
  def apply() = new CreateRelationshipView()
}
