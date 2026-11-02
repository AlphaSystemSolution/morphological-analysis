package com.alphasystem
package arabic
package examples_generator
package ui
package model

enum ConjugationType(val value: String) {

  case Single extends ConjugationType("Single Morphological Chart")

  case Paired extends ConjugationType("Paired Morphological Chart")

  case Full extends ConjugationType("Full Morphological Chart")
}
