package com.alphasystem
package arabic
package examples_generator
package ui
package model

import examples_generator.morphologicalchart.Conjugations

import java.nio.file.Path

case class ConjugationsRequest(conjugations: Conjugations, path: Option[Path] = None, newFileSaved: Boolean = false)
