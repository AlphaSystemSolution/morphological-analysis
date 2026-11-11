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

  test("paired sections share a scrollable compact area with only one expanded at a time") {
    onFx {
      import javafx.scene.Scene
      import javafx.scene.control.{ Accordion, ScrollPane }
      import javafx.scene.layout.BorderPane
      import scala.jdk.CollectionConverters.*

      val view = PairedConjugationEditorView()
      val scene = new Scene(view, 640, 360)
      view.applyCss()
      view.layout()
      val panel = view.getChildrenUnmodifiable.get(0).asInstanceOf[BorderPane]
      assert(panel.getTop != null)
      val scroll = panel.getCenter.asInstanceOf[ScrollPane]
      assert(scroll.isFitToWidth)
      val sections = scroll.getContent.asInstanceOf[Accordion]
      assertEquals(sections.getPanes.size(), 3)
      assertEquals(sections.getExpandedPane, sections.getPanes.get(0))
      sections.getPanes.asScala.foreach(_.setAnimated(false))
      sections.setExpandedPane(sections.getPanes.get(1))
      view.layout()
      assert(!sections.getPanes.get(0).isExpanded)
      assert(sections.getPanes.get(1).isExpanded)
      assert(scroll.getViewportBounds.getHeight > 0)
      assert(scroll.getViewportBounds.getHeight < scene.getHeight)
      assert(scroll.getContent.getLayoutBounds.getHeight > scroll.getViewportBounds.getHeight)
      sections.setExpandedPane(sections.getPanes.get(2))
      assert(sections.getPanes.get(2).isExpanded)
      assert(!sections.getPanes.get(1).isExpanded)
    }
  }

  test("paired dialog opens within the usable screen and remains resizable") {
    onFx {
      import javafx.stage.Screen

      val dialog = PairedConjugationEditorDialog()
      try {
        dialog.show()
        val screens = Screen.getScreensForRectangle(dialog.getX, dialog.getY, dialog.getWidth, dialog.getHeight)
        val bounds = (if screens.isEmpty then Screen.getPrimary else screens.get(0)).getVisualBounds
        assert(dialog.isResizable)
        assert(dialog.getWidth <= math.min(900.0, bounds.getWidth * 0.9) + 2)
        assert(dialog.getHeight <= math.min(700.0, bounds.getHeight * 0.85) + 2)
        assert(dialog.getX >= bounds.getMinX)
        assert(dialog.getY >= bounds.getMinY)
        assert(dialog.getX + dialog.getWidth <= bounds.getMaxX + 2)
        assert(dialog.getY + dialog.getHeight <= bounds.getMaxY + 2)
        val pane = dialog.getDialogPane
        pane.applyCss()
        pane.resize(pane.getScene.getWidth, pane.getScene.getHeight)
        pane.layout()
        val cancel = pane.getButtonTypes.get(1)
        val button = pane.lookupButton(cancel)
        val buttonBounds = button.localToScene(button.getBoundsInLocal)
        assert(buttonBounds.getMaxY <= pane.getScene.getHeight + 1)
      } finally dialog.close()
    }
  }

  private def selectors(view: PairedConjugationEditorView) = {
    import ui.control.conjugation_selector.ConjugationTermSelectorView
    import javafx.scene.control.{ Accordion, ScrollPane }
    import javafx.scene.layout.BorderPane

    val panel = view.getChildrenUnmodifiable.get(0).asInstanceOf[BorderPane]
    val accordion = panel.getCenter.asInstanceOf[ScrollPane].getContent.asInstanceOf[Accordion]
    (0 to 1).map(index => accordion.getPanes.get(index).getContent.asInstanceOf[ConjugationTermSelectorView])
  }

  private def termCombo(selector: ui.control.conjugation_selector.ConjugationTermSelectorView) = {
    import javafx.scene.control.ComboBox
    import javafx.scene.layout.{ BorderPane, GridPane, VBox }

    val panel = selector.getChildrenUnmodifiable.get(0).asInstanceOf[BorderPane].getCenter.asInstanceOf[VBox]
    panel.getChildren.get(0).asInstanceOf[GridPane].getChildren.get(1).asInstanceOf[ComboBox[MorphologicalTermType]]
  }

  test("editing the first term filters the second combo and keeps both requests synchronized") {
    onFx {
      import examples_generator.morphologicalchart.PairedConjugation
      import scala.jdk.CollectionConverters.*

      val view = PairedConjugationEditorView()
      val Seq(first, second) = selectors(view): @unchecked
      val firstCombo = termCombo(first)
      val secondCombo = termCombo(second)
      val secondInput = view.secondConjugationRequest.conjugationInput
      val id = view.pairedConjugation.id
      Seq(MorphologicalTermType.VerbalNoun, MorphologicalTermType.Imperative, MorphologicalTermType.PastTense)
        .foreach { term =>
          firstCombo.setValue(term)
          assertEquals(view.firstConjugationRequest.morphologicalTermType, term)
          assertEquals(second.otherMorphologicalTermType, Some(term))
          assertEquals(secondCombo.getItems.asScala.toSeq, PairedConjugation.getSimilarTypes(term))
          assert(secondCombo.getItems.contains(secondCombo.getValue))
          assertEquals(view.secondConjugationRequest.morphologicalTermType, secondCombo.getValue)
          assertEquals(view.secondConjugationRequest.conjugationInput, secondInput)
          assertEquals(view.pairedConjugation.id, id)
          assert(view.hasSimilarTypes)
        }
      secondCombo.setValue(MorphologicalTermType.PresentPassiveTense)
      assertEquals(view.secondConjugationRequest.morphologicalTermType, MorphologicalTermType.PresentPassiveTense)
      firstCombo.setValue(MorphologicalTermType.PastPassiveTense)
      assertEquals(secondCombo.getValue, MorphologicalTermType.PresentPassiveTense)
      assertEquals(view.pairedConjugation.left, Some(second.singleConjugation.request))
      assertEquals(view.pairedConjugation.right, Some(first.singleConjugation.request))
    }
  }

  test("reset loads both selectors across term groups without losing the saved second request") {
    onFx {
      import examples_generator.morphologicalchart.{ PairedConjugation, Settings }

      val view = PairedConjugationEditorView()
      val Seq(first, second) = selectors(view): @unchecked
      Seq(MorphologicalTermType.VerbalNoun, MorphologicalTermType.Imperative, MorphologicalTermType.PastTense)
        .foreach { term =>
          val right = request().copy(morphologicalTermType = term)
          val left = request().copy(
            morphologicalTermType = term,
            conjugationInput = request().conjugationInput.copy(translation = Some("saved second"))
          )
          val saved = PairedConjugation("saved", Settings(), Some(right), Some(left))
          view.reset(Some(saved))
          assertEquals(view.pairedConjugation, saved)
          assertEquals(first.singleConjugation.request, right)
          assertEquals(second.singleConjugation.request, left)
          assertEquals(second.otherMorphologicalTermType, Some(term))
          assertEquals(termCombo(second).getValue, term)
        }
    }
  }

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
