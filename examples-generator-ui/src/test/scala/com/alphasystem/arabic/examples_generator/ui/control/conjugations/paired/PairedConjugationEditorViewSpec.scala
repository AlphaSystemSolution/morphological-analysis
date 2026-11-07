package com.alphasystem
package arabic
package examples_generator
package ui
package control
package conjugations
package paired

import examples_generator.morphologicalchart.ConjugationRequest
import arabic.morphologicalanalysis.ui.RootLettersKeyBoardView.DefaultRootLetters
import arabic.morphologicalengine.conjugation.model.{ ConjugationInput, MorphologicalTermType, NamedTemplate }
import ui.utils.ConjugationsSpec
import javafx.application.Platform
import munit.FunSuite

import java.util.concurrent.{ Callable, FutureTask, TimeUnit }

class PairedConjugationEditorViewSpec extends FunSuite {

  private def onFx[A](body: => A): A = {
    ConjugationsSpec.initialize()
    val task = new FutureTask[A](() => body)
    Platform.runLater(task)
    task.get(20, TimeUnit.SECONDS)
  }

  private def request() =
    ConjugationRequest(
      MorphologicalTermType.PastTense,
      ConjugationInput(
        namedTemplate = NamedTemplate.FormICategoryAGroupATemplate,
        rootLetters = DefaultRootLetters
      )
    )

  test("firstConjugationRequest updates refresh listeners and pairedConjugation") {
    onFx {
      val view = PairedConjugationEditorView()
      var fired = 0
      view.firstConjugationRequestPropertyWrapper.onChange((_, _, _) => fired += 1)
      val first = request()
      view.firstConjugationRequest = first
      assertEquals(view.pairedConjugation.right, Some(first))
      assertEquals(fired, 1)
      val second = request()
      view.firstConjugationRequest = second
      assertEquals(view.pairedConjugation.right, Some(second))
      assertEquals(fired, 2)
    }
  }
}
