package com.alphasystem
package arabic
package morphologicalengine
package ui
package control

import morphologicalengine.generator.model.ChartConfiguration
import scalafx.Includes.*
import scalafx.application.JFXApp3
import scalafx.beans.property.ObjectProperty
import scalafx.scene.control.ButtonBar.ButtonData
import scalafx.scene.control.{ ButtonType, Dialog }
import scalafx.stage.Modality

class ChartConfigurationDialog extends Dialog[Option[ChartConfiguration]] {

  private val okButtonType = new ButtonType("OK", ButtonData.OKDone)
  private val view = ChartConfigurationView()
  private val chartConfigurationProperty =
    new ObjectProperty[ChartConfiguration](this, "chartConfiguration", view.chartConfiguration)
  view.chartConfigurationProperty.bindBidirectional(chartConfigurationProperty)

  initOwner(JFXApp3.Stage)
  initModality(Modality.WindowModal)
  title = "Export to chart to Word document"
  headerText = "Select chart configuration"
  dialogPane().buttonTypes = Seq(okButtonType, ButtonType.Cancel)
  dialogPane().content = view
  resultConverter = dialogButtonType => if dialogButtonType == okButtonType then Some(chartConfiguration) else None

  def chartConfiguration: ChartConfiguration = chartConfigurationProperty.value
  def chartConfiguration_=(value: ChartConfiguration): Unit = chartConfigurationProperty.value = value
}

object ChartConfigurationDialog {

  def apply(): ChartConfigurationDialog = new ChartConfigurationDialog()
}
