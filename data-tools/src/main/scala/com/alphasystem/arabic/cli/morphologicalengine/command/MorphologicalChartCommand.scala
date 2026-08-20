package com.alphasystem
package arabic
package cli
package morphologicalengine
package command

import arabic.morphologicalengine.asciidoc_generator.MorphologicalChartGenerator

class MorphologicalChartCommand extends BaseCommand("full") {

  banner("Generate full morphological chart")

  override def buildDocument(): Unit =
    MorphologicalChartGenerator.buildDocument(srcPath(), destPath(), readAsciidocAttributes(attributesPath.toOption))
}

object MorphologicalChartCommand {
  def apply(): MorphologicalChartCommand = new MorphologicalChartCommand()
}
