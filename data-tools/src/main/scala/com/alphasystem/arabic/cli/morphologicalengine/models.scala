package com.alphasystem
package arabic
package cli
package morphologicalengine

import arabic.model.ProNoun
import arabic.morphologicalengine.conjugation.model.{ MorphologicalTermType, NamedTemplate, RootLetters }
import com.alphasystem.arabic.morphologicalengine.conjugation.forms.noun.NounSupportBase

case class SingleConjugationRequest(conjugations: Seq[SingleConjugation])

case class SingleConjugation(
  tag: String,
  namedTemplate: NamedTemplate,
  rootLetters: RootLetters,
  morphologicalTermType: MorphologicalTermType,
  translations: Map[ProNoun, String], // so far only valid for verbs
  verbalNoun: Option[NounSupportBase] = None,
  showPronouns: Option[Boolean] = None, // valid for verbs only
  showNumbers: Option[Boolean] = None, // valid for both verbs and nouns, numbers header
  showGenders: Option[Boolean] = None, // valid for verbs only
  showConversationTypes: Option[Boolean] = None, // valid for verbs only
  showNounStatus: Option[Boolean] = None // valid for nouns only, nominative, accusative, and genitive
)
