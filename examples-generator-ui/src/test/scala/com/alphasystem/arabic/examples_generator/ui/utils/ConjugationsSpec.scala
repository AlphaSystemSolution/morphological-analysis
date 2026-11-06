package com.alphasystem
package arabic
package examples_generator
package ui
package utils

import arabic.morphologicalanalysis.ui.RootLettersKeyBoardView.DefaultRootLetters
import arabic.morphologicalengine.conjugation.model.{ ConjugationConfiguration, ConjugationInput, MorphologicalTermType, NamedTemplate }
import scalafx.Includes.*
import examples_generator.morphologicalchart.{ ConjugationRequest, Conjugations, Settings, SingleConjugation }
import ui.control.conjugations.ConjugationsFileGeneratorView
import ui.model.{ Action, ConjugationsRequest, ConjugationsTreeNode, ConjugationType }
import javafx.application.Platform
import munit.FunSuite

import java.nio.file.Paths
import java.util.concurrent.{ Callable, CountDownLatch, FutureTask, TimeUnit }

class ConjugationsSpec extends FunSuite {

  private def single(tag: String): SingleConjugation = SingleConjugation(
    tag,
    Settings(showPronouns = Some(true)),
    ConjugationRequest(
      MorphologicalTermType.PastTense,
      ConjugationInput(namedTemplate = NamedTemplate.FormICategoryAGroupATemplate, conjugationConfiguration = ConjugationConfiguration(), rootLetters = DefaultRootLetters)
    )
  )

  private def onFx[A](body: => A): A = {
    ConjugationsSpec.initialize()
    val task = new FutureTask[A](new Callable[A] {
      override def call(): A = body
    })
    Platform.runLater(task)
    task.get(20, TimeUnit.SECONDS)
  }

  test("empty tree retains all three categories") {
    val root = ConjugationsTreeNode.buildTree(Conjugations())
    assertEquals(root.children.map(_.value.value).toSeq, ConjugationType.values.toSeq.map(ConjugationsTreeNode.ConjugationTypeNode.apply))
    assert(root.children.forall(_.children.isEmpty))
  }

  test("single entries retain complete wrappers and ordered request children") {
    val entries = Seq(single("first"), single("second"))
    val root = ConjugationsTreeNode.buildTree(Conjugations(single = Some(entries)))
    val category = root.children.find(_.value.value == ConjugationsTreeNode.ConjugationTypeNode(ConjugationType.Single)).get
    assertEquals(category.children.map(_.value.value).toSeq, entries.map(ConjugationsTreeNode.SingleConjugationNode.apply))
    category.children.zip(entries).foreach { (node, entry) =>
      assertEquals(node.children.map(_.value.value).toSeq, Seq(ConjugationsTreeNode.ConjugationRequestNode(entry.request)))
    }
    assert(root.children.filterNot(_ == category).forall(_.children.isEmpty))
  }

  test("action appends from None, resets, and accepts identical consecutive additions") {
    onFx {
      val view = ConjugationsFileGeneratorView()
      val entry = single("same")
      view.action = Action.AddSingleConjugation(entry)
      assertEquals(view.conjugationsRequest.conjugations.single, Some(Seq(entry)))
      assertEquals(view.action, Action.NoAction)
      view.action = Action.AddSingleConjugation(entry)
      assertEquals(view.conjugationsRequest.conjugations.single, Some(Seq(entry, entry)))
      assertEquals(view.action, Action.NoAction)
    }
  }

  test("appended entry is expanded and selected without changing another view") {
    onFx {
      val view = ConjugationsFileGeneratorView()
      val other = ConjugationsFileGeneratorView()
      val entry = single("visible")
      view.action = Action.AddSingleConjugation(entry)
      val tree = view.lookup(".tree-view").asInstanceOf[javafx.scene.control.TreeView[ConjugationsTreeNode]]
      val selected = tree.getSelectionModel.getSelectedItem
      assertEquals(selected.getValue, ConjugationsTreeNode.SingleConjugationNode(entry))
      assert(selected.isExpanded)
      assert(selected.getParent.isExpanded)
      assertEquals(other.conjugationsRequest.conjugations.single, None)
      view.conjugationsRequest = view.conjugationsRequest.copy(path = Some(Paths.get("saved.yml")))
      assertEquals(tree.getSelectionModel.getSelectedItem, null)
    }
  }

  test("append preserves existing data and file metadata; unrelated actions do nothing") {
    onFx {
      val view = ConjugationsFileGeneratorView()
      val first = single("first")
      val second = single("second")
      val original = ConjugationsRequest(
        Conjugations(single = Some(Seq(first)), paired = Some(Seq.empty), full = Some(Seq.empty)),
        Some(Paths.get("conjugations.yml")),
        newFileSaved = true
      )
      view.conjugationsRequest = original
      view.action = Action.AddSingleConjugation(second)
      val expected = original.copy(conjugations = original.conjugations.copy(single = Some(Seq(first, second))))
      assertEquals(view.conjugationsRequest, expected)
      view.action = Action.NewConjugationFile
      assertEquals(view.conjugationsRequest, expected)
    }
  }
}

object ConjugationsSpec {
  private lazy val initialized: Unit = {
    val ready = new CountDownLatch(1)
    try Platform.startup(() => ready.countDown())
    catch {
      case _: IllegalStateException => Platform.runLater(() => ready.countDown())
    }
    require(ready.await(20, TimeUnit.SECONDS), "JavaFX toolkit did not initialize")
    Platform.setImplicitExit(false)
  }

  def initialize(): Unit = initialized
}
