package com.alphasystem
package arabic
package cli
package morphologicalengine
package command

class MorphologicalChartCommand extends BaseCommand("full") {

  banner("Generate full morphological chart")

  override def buildDocument(): Unit = () // TO BE REMOVED
}

object MorphologicalChartCommand {
  def apply(): MorphologicalChartCommand = new MorphologicalChartCommand()
}
