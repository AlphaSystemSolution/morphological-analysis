package com.alphasystem.morphologicalanalysis.morphology.model

import com.alphasystem.arabic.model.ArabicLetterType.*
import com.alphasystem.arabic.model.ArabicWord

enum VerbPartOfSpeechType(
  override val code: String,
  override val label: ArabicWord)
    extends PartOfSpeechType {

  case VERB extends VerbPartOfSpeechType("Verb", ArabicWord(FA, AIN, LAM))
}
