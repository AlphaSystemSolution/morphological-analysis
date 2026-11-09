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

  test("repeated selector request updates keep propagating to singleConjugation") {
    onFx {
      val view = ui.control.conjugation_selector.ConjugationTermSelectorView()
      view.singleConjugation = SingleConjugation("", Settings(), request())
      val first = input(translation = Some("one"))
      val second = input(translation = Some("two"))
      view.conjugationRequest = request(first)
      assertEquals(view.singleConjugation.request.conjugationInput, first)
      view.conjugationRequest = request(second)
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

  test("tag, selector, root-info and settings input columns align after resizing") {
    onFx {
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
      view.pairedConjugationMode = true
      val scene = new Scene(view, 1000, 1000)
      scene.getRoot.applyCss()
      val alignedGrids = grids(view).filter(_.getColumnConstraints.size() == 2)
      assertEquals(alignedGrids.size, 4)
      Seq(1000.0, 1200.0).foreach { width =>
        view.resize(width, 1000)
        view.requestLayout()
        view.layout()
        val inputPositions = alignedGrids.map { grid =>
          assertEquals(grid.getColumnConstraints.get(0).getPrefWidth, 200.0)
          val input = grid.getChildren.get(1)
          input.localToScene(input.getBoundsInLocal).getMinX
        }
        inputPositions.tail.foreach(position => assertEqualsDouble(position, inputPositions.head, 0.01))
      }
    }
  }

  test("selector label width updates both selector and nested root-info columns") {
    onFx {
      import ui.control.conjugation_selector.ConjugationTermSelectorView
      import ui.control.root_info.RootInfoEditorView
      import javafx.scene.layout.{ BorderPane, GridPane, VBox }

      val view = ConjugationTermSelectorView()
      val panel = view.getChildrenUnmodifiable.get(0).asInstanceOf[BorderPane]
      val sections = panel.getCenter.asInstanceOf[VBox]
      val grid = sections.getChildren.get(0).asInstanceOf[GridPane]
      val rootInfo = sections.getChildren.get(1).asInstanceOf[RootInfoEditorView]
      assertEquals(view.labelColumnWidth, 200.0)
      view.labelColumnWidth = 240.0
      val column = grid.getColumnConstraints.get(0)
      assertEquals(column.getMinWidth, 240.0)
      assertEquals(column.getPrefWidth, 240.0)
      assertEquals(column.getMaxWidth, 240.0)
      assertEquals(rootInfo.labelColumnWidth, 240.0)
    }
  }

  test("settings label width defaults to computed sizing and can be configured") {
    onFx {
      import ui.control.settings.SettingsView
      import javafx.scene.layout.{ BorderPane, GridPane }

      val view = SettingsView()
      val panel = view.getChildrenUnmodifiable.get(0).asInstanceOf[BorderPane]
      val grid = panel.getCenter.asInstanceOf[GridPane]
      val column = grid.getColumnConstraints.get(0)
      assertEquals(view.labelColumnWidth, -1.0)
      assertEquals(column.getPrefWidth, -1.0)
      view.labelColumnWidth = 240.0
      assertEquals(column.getMinWidth, 240.0)
      assertEquals(column.getPrefWidth, 240.0)
      assertEquals(column.getMaxWidth, 240.0)
      view.labelColumnWidth = -1.0
      assertEquals(column.getPrefWidth, -1.0)
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
