package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package dependencygraph
package control

import javafx.scene.control.Control
import scalafx.Includes.*
import morphology.graph.model.{ FontMetaInfo, GraphNode }
import scalafx.beans.property.{ DoubleProperty, ObjectProperty, StringProperty }
import scalafx.event.subscriptions.Subscription

abstract class GraphNodeView[N <: GraphNode] extends Control {

  protected val initial: N

  lazy val sourceProperty: ObjectProperty[N] = ObjectProperty[N](this, "source", initial)
  lazy val textProperty: StringProperty = StringProperty("")
  lazy val xProperty: DoubleProperty = DoubleProperty(0.0)
  lazy val yProperty: DoubleProperty = DoubleProperty(0.0)
  lazy val txProperty: DoubleProperty = DoubleProperty(0.0)
  lazy val tyProperty: DoubleProperty = DoubleProperty(0.0)
  lazy val fontProperty: ObjectProperty[FontMetaInfo] = ObjectProperty[FontMetaInfo](this, "font", defaultArabicFont)
  private var subscription: Subscription = _

  // initializations & bindings
  createSubscription()
  textProperty.onChange((_, _, nv) => update(nv, updateText))
  xProperty.onChange((_, _, nv) => update(nv.doubleValue(), updateX))
  yProperty.onChange((_, _, nv) => update(nv.doubleValue(), updateY))
  txProperty.onChange((_, _, nv) => update(nv.doubleValue(), updateTranslateX))
  tyProperty.onChange((_, _, nv) => update(nv.doubleValue(), updateTranslateY))
  fontProperty.onChange((_, _, nv) => update(nv, updateFont))

  // getters & setters
  def source: N = sourceProperty.value
  def source_=(value: N): Unit = sourceProperty.value = value

  def text: String = textProperty.value
  def text_=(value: String): Unit = textProperty.value = value

  def x: Double = xProperty.value
  def x_=(value: Double): Unit = xProperty.value = value

  def y: Double = yProperty.value
  def y_=(value: Double): Unit = yProperty.value = value

  def translateX: Double = txProperty.value
  def translateX_=(value: Double): Unit = txProperty.value = value

  def translateY: Double = tyProperty.value
  def translateY_=(value: Double): Unit = tyProperty.value = value

  def font: FontMetaInfo = fontProperty.value
  def font_=(value: FontMetaInfo): Unit = fontProperty.value = value

  protected def createSubscription(): Unit =
    subscription = sourceProperty.onChange((_, _, nv) => if Option(nv).isDefined then initValues(nv))

  protected def cancelSubscription(): Unit = subscription.cancel()

  protected def initValues(src: N): Unit = {
    setId(src.id)
    text = src.text
    x = src.x
    y = src.y
    translateX = src.translateX
    translateY = src.translateY
    font = src.font
  }

  protected def updateText(value: String, src: N): N
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
