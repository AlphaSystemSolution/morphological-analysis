package com.alphasystem
package arabic
package morphologicalengine

import ui.utils.MorphologicalEnginePreferences

package object ui {

  given preferences: MorphologicalEnginePreferences = MorphologicalEnginePreferences()

  def roundTo100(srcValue: Double): Double =
    ((srcValue.toInt + 99) / 100).toDouble * 100
}
