package com.alphasystem
package arabic
package morphologicalengine
package generator
package docx

import generator.model.ChartConfiguration
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart
import org.docx4j.wml.Tbl

abstract class DocumentGenerator(val chartConfiguration: ChartConfiguration) {

  private[docx] def buildDocument(mdp: MainDocumentPart): Unit
}

abstract class ChartGenerator(chartConfiguration: ChartConfiguration) extends DocumentGenerator(chartConfiguration) {

  override private[docx] def buildDocument(mdp: MainDocumentPart): Unit = mdp.getContent.add(getChart)

  protected def getChart: Tbl
}
