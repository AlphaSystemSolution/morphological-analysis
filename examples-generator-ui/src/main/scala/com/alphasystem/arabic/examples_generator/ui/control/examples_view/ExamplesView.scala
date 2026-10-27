package com.alphasystem
package arabic
package examples_generator
package ui
package control
package examples_view

import com.alphasystem.arabic.examples_generator.ui.model.ExampleRequest
import javafx.scene.control.{ Control, Skin }
import scalafx.beans.property.ObjectProperty

class ExamplesView extends Control {

  private[examples_view] val exampleRequestProperty = ObjectProperty[Option[ExampleRequest]](this, "exampleRequest")

  setSkin(createDefaultSkin())

  def exampleRequest: Option[ExampleRequest] = exampleRequestProperty.value
  def exampleRequest_=(value: ExampleRequest): Unit = exampleRequestProperty.value = Option(value)

  override def createDefaultSkin(): Skin[?] = skin.ExamplesSkin(this)
}

object ExamplesView {
  def apply(): ExamplesView = new ExamplesView()
}
