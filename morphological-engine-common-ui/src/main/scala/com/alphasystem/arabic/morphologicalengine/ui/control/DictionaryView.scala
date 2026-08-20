package com.alphasystem
package arabic
package morphologicalengine
package ui
package control

import com.alphasystem.arabic.model.ArabicLetterType
import com.alphasystem.arabic.morphologicalengine.conjugation.model.RootLetters
import javafx.scene.control.{ Control, Skin }
import scalafx.beans.property.ObjectProperty

class DictionaryView extends Control {

  import DictionaryView.*

  private[control] val rootLettersProperty = ObjectProperty[RootLetters](this, "rootLetters", DefaultRootLetters)

  setSkin(createDefaultSkin())

  def rootLetters: RootLetters = rootLettersProperty.value
  def rootLetters_=(value: RootLetters): Unit =
    rootLettersProperty.value = if Option(value).isEmpty then DefaultRootLetters else value

  override def createDefaultSkin(): Skin[?] = skin.DictionarySkin(this)
}

object DictionaryView {
  private val DictionaryUrl = "https://ejtaal.net/aa/index.html#bwq="
  private val DefaultRootLetters = RootLetters(ArabicLetterType.Fa, ArabicLetterType.Ain, ArabicLetterType.Lam)

  def apply(): DictionaryView = new DictionaryView()

  private def normalizeDictionaryQuery(query: String) = if query.startsWith("'") then s"a${query.drop(1)}" else query

  private[control] def getMawridReaderUrl(rootLetters: RootLetters): String =
    s"$DictionaryUrl${normalizeDictionaryQuery(rootLetters.buckWalterString)}"
}
