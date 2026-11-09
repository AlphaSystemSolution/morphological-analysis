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

  test("root-info label width defaults to computed sizing and updates its column constraints") {
    onFx {
      import ui.control.root_info.RootInfoEditorView
      import javafx.scene.layout.{ BorderPane, GridPane }

      val view = RootInfoEditorView()
      val pane = view.getSkin.getNode.asInstanceOf[javafx.scene.Parent]
      val mainPanel = pane.getChildrenUnmodifiable.get(0).asInstanceOf[BorderPane]
      val grid = mainPanel.getCenter.asInstanceOf[GridPane]
      val column = grid.getColumnConstraints.get(0)
      assertEquals(view.labelColumnWidth, -1.0)
      assertEquals(column.getPrefWidth, -1.0)
      view.labelColumnWidth = 240.0
      assertEquals(column.getMinWidth, 240.0)
      assertEquals(column.getPrefWidth, 240.0)
      assertEquals(column.getMaxWidth, 240.0)
      view.labelColumnWidth = -1.0
      assertEquals(column.getMinWidth, -1.0)
      assertEquals(column.getPrefWidth, -1.0)
      assertEquals(column.getMaxWidth, -1.0)
    }
  }

  /*test("embedded root-info fields align with the morphological-term field") {
    onFx {
      import skin.SingleConjugationEditorSkin2
      import javafx.scene.{ Parent, Scene }
      import javafx.scene.layout.GridPane
      import scala.jdk.CollectionConverters.*

      def grids(parent: Parent): Seq[GridPane] =
        parent.getChildrenUnmodifiable.asScala.toSeq.flatMap {
          case grid: GridPane => Seq(grid) ++ grids(grid)
          case child: Parent  => grids(child)
          case _              => Seq.empty
        }

      val view = SingleConjugationEditorView()
      view.setSkin(SingleConjugationEditorSkin2(view))
      val scene = new Scene(view, 1000, 800)
      scene.getRoot.applyCss()
      scene.getRoot.layout()
      val alignedGrids = grids(view).filter(_.getColumnConstraints.size() == 2)
      assertEquals(alignedGrids.size, 2)
      alignedGrids.foreach(grid => assertEquals(grid.getColumnConstraints.get(0).getPrefWidth, 200.0))
      val inputPositions = alignedGrids.map { grid =>
        val input = grid.getChildren.get(1)
        input.localToScene(input.getBoundsInLocal).getMinX
      }
      assertEqualsDouble(inputPositions.head, inputPositions.last, 0.01)
    }
  }*/

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
