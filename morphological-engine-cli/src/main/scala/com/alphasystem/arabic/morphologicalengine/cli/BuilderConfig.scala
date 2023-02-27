package com.alphasystem
package arabic
package morphologicalengine
package cli

import morphologicalengine.conjugation.model.ConjugationConfiguration
import morphologicalengine.generator.model.{ ChartConfiguration, ConjugationInput }

import java.nio.file.Path

case class BuilderConfig(
  chartConfiguration: ChartConfiguration,
  conjugationConfiguration: ConjugationConfiguration,
  inputs: Seq[ConjugationInput])
