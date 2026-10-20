package com.alphasystem
package arabic
package examples_generator
package ui
package utils

import com.alphasystem.arabic.fx.ui.util.UIUserPreferences

class ExampleGeneratorPreferences extends UIUserPreferences(classOf[ExampleGeneratorPreferences]) {

  override protected val nodePrefix: String = "example-generator"

}

object ExampleGeneratorPreferences {
  def apply(): ExampleGeneratorPreferences = new ExampleGeneratorPreferences()
}
