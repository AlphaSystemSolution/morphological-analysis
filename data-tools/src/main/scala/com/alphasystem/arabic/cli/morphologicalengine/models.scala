package com.alphasystem
package arabic
package cli
package morphologicalengine

import arabic.model.ProNoun
import arabic.morphologicalengine.conjugation.model.{MorphologicalTermType, NamedTemplate, RootLetters}
import com.alphasystem.arabic.morphologicalengine.conjugation.forms.noun.NounSupportBase

class models {}

case class SingleConjugation(
  tag: String,
  namedTemplate: NamedTemplate,
  rootLetters: RootLetters,
  morphologicalTermType: MorphologicalTermType,
  translations: Map[ProNoun, String],
  verbalNoun: Option[NounSupportBase] = None,
  showPronouns: Option[Boolean] = None,
  showNumbers: Option[Boolean] = None,
  showGenders: Option[Boolean] = None,
  showConversationTypes: Option[Boolean] = None)
