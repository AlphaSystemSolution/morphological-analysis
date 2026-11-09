package com.alphasystem
package arabic
package examples_generator
package ui
package control
package conjugations
package paired

import examples_generator.morphologicalchart.ConjugationRequest
import com.alphasystem.arabic.model.ArabicLetterType
import arabic.morphologicalengine.conjugation.model.{
  ConjugationInput,
  MorphologicalTermType,
  NamedTemplate,
  RootLetters
}
import ui.control.conjugation_selector.ConjugationTermSelectorView
import ui.control.root_info.RootInfoEditorView
import ui.utils.ConjugationsSpec
import javafx.application.Platform
import javafx.scene.{ Node, Parent }
import javafx.scene.control.{ Accordion, ScrollPane, TitledPane }
import munit.FunSuite

import java.util.concurrent.{ Callable, FutureTask, TimeUnit }
import scala.jdk.CollectionConverters.*

class PairedConjugationEditorDialogSpec extends FunSuite {

  private def onFx[A](body: => A): A = {
    ConjugationsSpec.initialize()
    val task = new FutureTask[A](new Callable[A] {
      override def call(): A = body
    })
    Platform.runLater(task)
    task.get(20, TimeUnit.SECONDS)
  }

  private def request(letter: ArabicLetterType = ArabicLetterType.Fa) =
    ConjugationRequest(
      MorphologicalTermType.PastTense,
      ConjugationInput(
        namedTemplate = NamedTemplate.FormICategoryAGroupATemplate,
        rootLetters = RootLetters(letter, ArabicLetterType.Ain, ArabicLetterType.Lam)
      )
    )

  private def descendants(node: Node): Seq[Node] =
    node +: (node match {
      case pane: TitledPane     => descendants(pane.getContent)
      case pane: ScrollPane     => descendants(pane.getContent)
      case accordion: Accordion => accordion.getPanes.asScala.toSeq.flatMap(descendants)
      case parent: Parent       => parent.getChildrenUnmodifiable.asScala.toSeq.flatMap(descendants)
      case _                    => Seq.empty
    })

  private def firstSelector(view: PairedConjugationEditorView): ConjugationTermSelectorView =
    descendants(view).collectFirst {
      case pane: TitledPane if pane.getText == "Select First Conjugation" =>
        pane.getContent.asInstanceOf[ConjugationTermSelectorView]
    }.get

  test("first conjugation selector refreshes first root section") {
    onFx {
      val dialog = PairedConjugationEditorDialog()
      val pairedView = dialog.getDialogPane.getContent.asInstanceOf[PairedConjugationEditorView]
      val selector = firstSelector(pairedView)
      val newRequest = request(ArabicLetterType.Ba)

      selector.conjugationRequest_=(newRequest)

      assertEquals(pairedView.firstConjugationRequest, newRequest)
      assertEquals(pairedView.pairedConjugation.right, Some(newRequest))
      assertEquals(selector.singleConjugation.request, newRequest)
    }
  }

  test("embedded root edit refreshes first root section") {
    onFx {
      val dialog = PairedConjugationEditorDialog()
      val pairedView = dialog.getDialogPane.getContent.asInstanceOf[PairedConjugationEditorView]
      val selector = firstSelector(pairedView)
      val rootView = descendants(selector).collectFirst { case view: RootInfoEditorView => view }.get
      val newRootLetters = RootLetters(ArabicLetterType.Ba, ArabicLetterType.Ain, ArabicLetterType.Lam)

      rootView.rootLetters = newRootLetters

      assertEquals(rootView.conjugationInput.rootLetters, newRootLetters)
      assertEquals(selector.singleConjugation.request.conjugationInput.rootLetters, newRootLetters)
      assertEquals(pairedView.firstConjugationRequest.conjugationInput.rootLetters, newRootLetters)
      assertEquals(pairedView.pairedConjugation.right, Some(pairedView.firstConjugationRequest))
    }
  }
}
