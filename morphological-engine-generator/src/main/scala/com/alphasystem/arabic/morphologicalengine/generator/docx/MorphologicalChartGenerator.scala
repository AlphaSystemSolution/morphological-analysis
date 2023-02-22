package com.alphasystem
package arabic
package morphologicalengine
package generator
package docx

import morphologicalengine.conjugation.model.MorphologicalChart
import generator.model.ChartConfiguration
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart

class MorphologicalChartGenerator(
  override val chartConfiguration: ChartConfiguration,
  morphologicalChart: MorphologicalChart)
    extends DocumentGenerator(chartConfiguration) {

  override private[docx] def buildDocument(mdp: MainDocumentPart): Unit = {
    val abbreviatedConjugation = morphologicalChart.abbreviatedConjugation
    if abbreviatedConjugation.isDefined then {
      val generator = AbbreviatedConjugationGenerator(
        chartConfiguration,
        morphologicalChart.conjugationHeader,
        abbreviatedConjugation.get,
        Some("Translation") // TODO:
      )
      generator.buildDocument(mdp)
    }

  }
}

object MorphologicalChartGenerator {
  def apply(
    chartConfiguration: ChartConfiguration,
    morphologicalChart: MorphologicalChart
  ): MorphologicalChartGenerator = new MorphologicalChartGenerator(chartConfiguration, morphologicalChart)
}