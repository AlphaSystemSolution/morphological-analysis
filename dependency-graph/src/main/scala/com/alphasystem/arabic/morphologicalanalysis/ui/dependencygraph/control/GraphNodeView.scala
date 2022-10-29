package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package dependencygraph
package control

import scalafx.Includes.*
import morphology.graph.model.{ FontMetaInfo, GraphNode }
import scalafx.beans.property.{ DoubleProperty, ObjectProperty, StringProperty }
import scalafx.event.subscriptions.Subscription

abstract class GraphNodeView[N <: GraphNode] {

  lazy val sourceProperty: ObjectProperty[N] = ObjectProperty[N](this, "source")
  lazy val textProperty: StringProperty = StringProperty("")
  lazy val xProperty: DoubleProperty = DoubleProperty(0.0)
  lazy val yProperty: DoubleProperty = DoubleProperty(0.0)
  lazy val translateXProperty: DoubleProperty = DoubleProperty(0.0)
  lazy val translateYProperty: DoubleProperty = DoubleProperty(0.0)
  lazy val fontProperty: ObjectProperty[FontMetaInfo] = ObjectProperty[FontMetaInfo](this, "font")
  private var subscription: Subscription = _

  // initializations & bindings
  createSubscription()
  xProperty.onChange((_, _, nv) => update(nv.doubleValue(), updateX))
  yProperty.onChange((_, _, nv) => update(nv.doubleValue(), updateY))
  translateXProperty.onChange((_, _, nv) => update(nv.doubleValue(), updateTranslateX))
  translateYProperty.onChange((_, _, nv) => update(nv.doubleValue(), updateTranslateY))
  fontProperty.onChange((_, _, nv) => update(nv, updateFont))

  // getters & setters
  def source: N = sourceProperty.value
  def source_=(value: N): Unit = sourceProperty.value = value

  def x: Double = xProperty.value
  def x_=(value: Double): Unit = xProperty.value = value

  def y: Double = yProperty.value
  def y_=(value: Double): Unit = yProperty.value = value

  def translateX: Double = translateXProperty.value
  def translateX_=(value: Double): Unit = translateXProperty.value = value

  def translateY: Double = translateYProperty.value
  def translateY_=(value: Double): Unit = translateYProperty.value = value

  def font: FontMetaInfo = fontProperty.value
  def font_=(value: FontMetaInfo): Unit = fontProperty.value = value

  protected def createSubscription(): Unit =
    subscription = sourceProperty.onChange((_, _, nv) => if Option(nv).isDefined && nv != source then initValues(nv))

  protected def cancelSubscription(): Unit = subscription.cancel()

  protected def initValues(src: N): Unit = {
    x = src.x
    y = src.y
    translateX = src.translateX
    translateY = src.translateY
    font = src.font
  }

  protected def updateX(value: Double, src: N): N
  protected def updateY(value: Double, src: N): N
  protected def updateTranslateX(value: Double, src: N): N
  protected def updateTranslateY(value: Double, src: N): N
  protected def updateFont(value: FontMetaInfo, src: N): N

  protected def update[V](value: V, f: (V, N) => N): Unit = {
    if Option(source).isDefined then {
      cancelSubscription()
      source = f(value, source)
      createSubscription()
    }
  }

}
