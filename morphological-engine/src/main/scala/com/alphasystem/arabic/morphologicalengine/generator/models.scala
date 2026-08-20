package com.alphasystem
package arabic
package morphologicalengine
package generator

import com.alphasystem.arabic.morphologicalengine.conjugation.model.RootLetters
import morphologicalengine.conjugation.model.NamedTemplate

case class Word(
  rootLetters: RootLetters,
  family: NamedTemplate,
  baseTranslation: String,
  translations: Set[String] = Set.empty)

object Word {
  given ordering: Ordering[Word] = Ordering.by(w => (w.rootLetters, w.family))
}

case class WordGroup(root: String, words: Seq[Word])

object WordGroup {
  given ordering: Ordering[WordGroup] = (x: WordGroup, y: WordGroup) => {
    val iteratorX = x.words.iterator
    val iteratorY = y.words.iterator
    var result = 0
    while iteratorX.hasNext && iteratorY.hasNext && result == 0 do {
      result = summon[Ordering[Word]].compare(iteratorX.next(), iteratorY.next())
    }
    if result != 0 then result else x.words.length.compareTo(y.words.length)
  }
}
