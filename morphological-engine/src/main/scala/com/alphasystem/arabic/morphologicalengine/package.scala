package com.alphasystem
package arabic

import com.alphasystem.arabic.model.ArabicWord
import com.alphasystem.arabic.model.ArabicLetterType.*

package object morphologicalengine {

  val ParticiplePrefix: ArabicWord = ArabicWord(Fa, Ha, Waw)

  val ImperativePrefix: ArabicWord = ArabicWord(Alif, Lam, AlifHamzaAbove, Meem, Ra, Space, Meem, Noon, Ha)

  val ForbiddenPrefix: ArabicWord = ArabicWord(Waw, Noon, Ha, Ya, Space, Ain, Noon, Ha)

  val AdverbPrefix: ArabicWord = ArabicWord(Waw, Alif, Lam, Dtha, Ra, Fa, Space, Meem, Noon, Ha)

}
