package com.alphasystem
package arabic
package examples_generator
package ui
package control
package conjugations
package single

import examples_generator.morphologicalchart.{ ConjugationRequest, Settings, SingleConjugation }
import arabic.morphologicalanalysis.ui.RootLettersKeyBoardView.DefaultRootLetters
import arabic.morphologicalengine.conjugation.model.{ ConjugationInput, MorphologicalTermType, NamedTemplate }
import ui.utils.ConjugationsSpec
import javafx.application.Platform
import munit.FunSuite

import java.util.concurrent.{ Callable, FutureTask, TimeUnit }

class SingleConjugationEditorViewSpec extends FunSuite {

  private def onFx[A](body: => A): A = {
    ConjugationsSpec.initialize()
    val task = new FutureTask[A](() => body)
    Platform.runLater(task)
    task.get(20, TimeUnit.SECONDS)
  }

  private def input(translation: Option[String] = None) = ConjugationInput(
    namedTemplate = NamedTemplate.FormICategoryAGroupATemplate,
    rootLetters = DefaultRootLetters,
    translation = translation
  )

  private def request(conjugationInput: ConjugationInput = input()) =
    ConjugationRequest(MorphologicalTermType.PastTense, conjugationInput)

  test("repeated conjugationInput updates keep propagating to singleConjugation") {
    onFx {
      val view = SingleConjugationEditorView()
      view.singleConjugation = SingleConjugation("", Settings(), request())
      val first = input(translation = Some("one"))
      val second = input(translation = Some("two"))
      view.conjugationInput = first
      assertEquals(view.singleConjugation.request.conjugationInput, first)
      view.conjugationInput = second
      assertEquals(view.singleConjugation.request.conjugationInput, second)
    }
  }

  test("repeated settings updates keep propagating to singleConjugation") {
    onFx {
      val view = SingleConjugationEditorView()
      view.singleConjugation = SingleConjugation("", Settings(), request())
      val first = Settings(showPronouns = Some(true))
      val second = Settings(showPronouns = Some(false))
      view.settings = first
      assertEquals(view.singleConjugation.settings, first)
      view.settings = second
      assertEquals(view.singleConjugation.settings, second)
    }
  }
}
