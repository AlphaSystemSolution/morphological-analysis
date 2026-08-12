package com.alphasystem
package arabic
package cli
package morphologicalengine

import arabic.morphologicalengine.generator.{ Conjugations, PairedConjugationRequest, SingleConjugationRequest, given }
import arabic.morphologicalengine.generator.model.ConjugationTemplate

import java.nio.file.Path

private[cli] def toSingleConjugationRequest(path: Path): SingleConjugationRequest =
  fromFile(path, fromString[SingleConjugationRequest])

private[cli] def toPairedConjugationRequest(path: Path): PairedConjugationRequest =
  fromFile(path, fromString[PairedConjugationRequest])

private[cli] def toConjugationTemplate(path: Path): ConjugationTemplate =
  fromFile(path, fromString[ConjugationTemplate])

private[cli] def toConjugations(path: Path): Conjugations =
  fromFile(path, fromString[Conjugations])
