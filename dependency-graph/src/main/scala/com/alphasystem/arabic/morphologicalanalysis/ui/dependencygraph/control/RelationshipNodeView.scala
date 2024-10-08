package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package dependencygraph
package control

import skin.RelationshipNodeSkin
import javafx.scene.control.Skin
import morphology.graph.model.{ FontMetaInfo, RelationshipNode }
import scalafx.beans.property.DoubleProperty

class RelationshipNodeView extends GraphNodeView[RelationshipNode] {

  override protected val initial: RelationshipNode = defaultRelationshipNode
  private[control] lazy val controlX1Property = DoubleProperty(0d)
  private[control] lazy val controlY1Property = DoubleProperty(0d)
  private[control] lazy val controlX2Property = DoubleProperty(0d)
  private[control] lazy val controlY2Property = DoubleProperty(0d)
  private[control] lazy val arrowXProperty = DoubleProperty(0d)
  private[control] lazy val arrowYProperty = DoubleProperty(0d)

  // initializations & bindings
  controlX1Property.onChange((_, _, nv) => update(nv.doubleValue(), updateControlX1))
  controlY1Property.onChange((_, _, nv) => update(nv.doubleValue(), updateControlY1))
  controlX2Property.onChange((_, _, nv) => update(nv.doubleValue(), updateControlX2))
  controlY2Property.onChange((_, _, nv) => update(nv.doubleValue(), updateControlY2))
  arrowXProperty.onChange((_, _, nv) => update(nv.doubleValue(), updateT1))
  arrowYProperty.onChange((_, _, nv) => update(nv.doubleValue(), updateT2))

  setSkin(createDefaultSkin())

  // getters & setters
  def controlX1: Double = controlX1Property.value
  def controlX1_=(value: Double): Unit = controlX1Property.value = value

  def controlY1: Double = controlY1Property.value
  def controlY1_=(value: Double): Unit = controlY1Property.value = value

  def controlX2: Double = controlX2Property.value
  def controlX2_=(value: Double): Unit = controlX2Property.value = value

  def controlY2: Double = controlY2Property.value
  def controlY2_=(value: Double): Unit = controlY2Property.value = value

  def arrowX: Double = arrowXProperty.value
  def arrowX_=(value: Double): Unit = arrowXProperty.value = value

  def arrowY: Double = arrowYProperty.value
  def arrowY_=(value: Double): Unit = arrowYProperty.value = value

  override protected def updateX(value: Double, src: RelationshipNode): RelationshipNode =
    src.copy(textPoint = src.textPoint.copy(x = value))

  override protected def updateY(value: Double, src: RelationshipNode): RelationshipNode =
    src.copy(textPoint = src.textPoint.copy(y = value))

  override protected def updateTranslateX(value: Double, src: RelationshipNode): RelationshipNode =
    src.copy(textPoint = src.translate.copy(x = value))

  override protected def updateTranslateY(value: Double, src: RelationshipNode): RelationshipNode =
    src.copy(textPoint = src.translate.copy(y = value))

  override protected def updateFont(value: FontMetaInfo, src: RelationshipNode): RelationshipNode =
    src.copy(font = value)

  private def updateControlX1(value: Double, src: RelationshipNode): RelationshipNode =
    src.copy(control1 = src.control1.copy(x = value))

  private def updateControlY1(value: Double, src: RelationshipNode): RelationshipNode =
    src.copy(control1 = src.control1.copy(y = value))

  private def updateControlX2(value: Double, src: RelationshipNode): RelationshipNode =
    src.copy(control2 = src.control2.copy(x = value))

  private def updateControlY2(value: Double, src: RelationshipNode): RelationshipNode =
    src.copy(control2 = src.control2.copy(y = value))

  private def updateT1(value: Double, src: RelationshipNode): RelationshipNode =
    src.copy(arrow = src.arrow.copy(x = value))

  private def updateT2(value: Double, src: RelationshipNode): RelationshipNode =
    src.copy(arrow = src.arrow.copy(y = value))

  override protected def initValues(src: RelationshipNode): Unit = {
    super.initValues(src)
    controlX1 = src.control1.x
    controlY1 = src.control1.y
    controlX2 = src.control2.x
    controlY2 = src.control2.y
    arrowX = src.arrow.x
    arrowY = src.arrow.y
  }

  override def createDefaultSkin(): Skin[?] = RelationshipNodeSkin(this)
}

object RelationshipNodeView {
  def apply(): RelationshipNodeView = new RelationshipNodeView()
}
