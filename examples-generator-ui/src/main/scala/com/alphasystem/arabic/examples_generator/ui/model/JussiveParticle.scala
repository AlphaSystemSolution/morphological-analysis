package com.alphasystem
package arabic
package examples_generator
package ui
package model

import arabic.model.JussiveParticle as ArabicJussiveParticle

enum JussiveParticle(val arabicJussiveParticle: Option[ArabicJussiveParticle]) {

  case NoValue extends JussiveParticle(None)
  case DidNot extends JussiveParticle(Some(ArabicJussiveParticle.DidNot))
  case NotYet extends JussiveParticle(Some(ArabicJussiveParticle.NotYet))
  case LamOfCommand extends JussiveParticle(Some(ArabicJussiveParticle.LamOfCommand))
  case LamOfProhibition extends JussiveParticle(Some(ArabicJussiveParticle.LamOfProhibition))
}

object JussiveParticle {
  def fromArabicValue(m: Option[ArabicJussiveParticle]): JussiveParticle =
    JussiveParticle.values.find(_.arabicJussiveParticle == m).getOrElse(NoValue)
}
