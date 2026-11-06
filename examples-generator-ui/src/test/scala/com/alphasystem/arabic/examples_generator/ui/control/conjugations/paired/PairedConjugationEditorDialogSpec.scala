package com.alphasystem
package arabic
package examples_generator
package ui
package control
package conjugations
package paired

import examples_generator.morphologicalchart.{ ConjugationRequest, Settings }
import arabic.morphologicalanalysis.ui.RootLettersKeyBoardView.DefaultRootLetters
import com.alphasystem.arabic.model.ArabicLetterType
import arabic.morphologicalengine.conjugation.model.{
  ConjugationConfiguration,
  ConjugationInput,
  MorphologicalTermType,
  NamedTemplate,
  RootLetters
}
import ui.control.conjugations.single.{ SingleConjugationEditorDialog, SingleConjugationEditorView }
import ui.control.root_info.RootInfoEditorView
import ui.utils.ConjugationsSpec
import javafx.application.Platform
import javafx.scene.control.{ Button, ButtonBar }
import scalafx.scene.control.Dialog
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
        conjugationConfiguration = ConjugationConfiguration(),
        rootLetters = RootLetters(letter, ArabicLetterType.Ain, ArabicLetterType.Lam)
      )
    )

  private def buttonWithText(pane: javafx.scene.control.DialogPane, text: String): Button =
    pane.getButtonTypes.asScala
      .find(_.getText == text)
      .map(bt => pane.lookupButton(bt).asInstanceOf[Button])
      .get

  private def okButton(pane: javafx.scene.control.DialogPane): Button =
    pane.getButtonTypes.asScala
      .find(_.getButtonData == ButtonBar.ButtonData.OK_DONE)
      .map(bt => pane.lookupButton(bt).asInstanceOf[Button])
      .get

  test("select first root OK refreshes first root section") {
    val dialog = onFx(PairedConjugationEditorDialog())
    val pairedView = onFx(dialog.getDialogPane.getContent.asInstanceOf[PairedConjugationEditorView])
    val newRequest = request(ArabicLetterType.Ba)

    // fire "Select First Root ..." — the filter calls selectRootDialog.showAndWait()
    // which blocks the FX task inside a nested event loop until the inner dialog closes
    val showAndSelect = new FutureTask[Unit](new Callable[Unit] {
      override def call(): Unit = {
        dialog.show()
        buttonWithText(dialog.getDialogPane, "Select First Root ...").fire()
      }
    })
    Platform.runLater(showAndSelect)
    Thread.sleep(1000)

    // runs inside the nested event loop: simulate user producing a new request, then OK
    onFx {
      val field = classOf[PairedConjugationEditorDialog].getDeclaredField("selectRootDialog")
      field.setAccessible(true)
      val selectDialog = field.get(dialog).asInstanceOf[Dialog[?]]
      val singleView = selectDialog.dialogPane().getContent.asInstanceOf[SingleConjugationEditorView]
      singleView.conjugationRequest = newRequest
      okButton(selectDialog.dialogPane()).fire()
    }

    showAndSelect.get(20, TimeUnit.SECONDS)
    val updated = onFx(pairedView.firstConjugationRequest)
    assertEquals(updated.conjugationInput, newRequest.conjugationInput)
    assertEquals(updated.morphologicalTermType, newRequest.morphologicalTermType)
  }

  test("inner Select Root edit refreshes first root section") {
    val dialog = onFx(PairedConjugationEditorDialog())
    val pairedView = onFx(dialog.getDialogPane.getContent.asInstanceOf[PairedConjugationEditorView])
    val newRootLetters = RootLetters(ArabicLetterType.Ba, ArabicLetterType.Ain, ArabicLetterType.Lam)

    // open selectRootDialog, then fire its inner "Select Root ..." which opens
    // RootInfoEditorDialog inside a second nested event loop
    val showAndSelect = new FutureTask[Unit](new Callable[Unit] {
      override def call(): Unit = {
        dialog.show()
        buttonWithText(dialog.getDialogPane, "Select First Root ...").fire()
      }
    })
    Platform.runLater(showAndSelect)
    Thread.sleep(1000)

    val innerOpen = new FutureTask[Unit](new Callable[Unit] {
      override def call(): Unit = {
        val field = classOf[PairedConjugationEditorDialog].getDeclaredField("selectRootDialog")
        field.setAccessible(true)
        val selectDialog = field.get(dialog).asInstanceOf[Dialog[?]]
        buttonWithText(selectDialog.dialogPane(), "Select Root ...").fire()
      }
    })
    Platform.runLater(innerOpen)
    Thread.sleep(1000)

    // inside the deepest loop: edit root letters in RootInfoEditorView, then OK
    onFx {
      val sdField = classOf[PairedConjugationEditorDialog].getDeclaredField("selectRootDialog")
      sdField.setAccessible(true)
      val selectDialog = sdField.get(dialog).asInstanceOf[Dialog[?]]
      val riField = classOf[SingleConjugationEditorDialog].getDeclaredField("rootInfoEditorDialog")
      riField.setAccessible(true)
      val rootDialog = riField.get(selectDialog).asInstanceOf[Dialog[?]]
      val rootView = rootDialog.dialogPane().getContent.asInstanceOf[RootInfoEditorView]
      rootView.rootLetters = newRootLetters
      okButton(rootDialog.dialogPane()).fire()
    }

    // separate FX turn (like a real second click): the inner-OK continuation has
    // now assigned conjugationInput -> conjugationRequest -> singleConjugation
    onFx {
      val sdField = classOf[PairedConjugationEditorDialog].getDeclaredField("selectRootDialog")
      sdField.setAccessible(true)
      val selectDialog = sdField.get(dialog).asInstanceOf[Dialog[?]]
      val singleView = selectDialog.dialogPane().getContent.asInstanceOf[SingleConjugationEditorView]
      assertEquals(singleView.conjugationRequest.conjugationInput.rootLetters, newRootLetters)
      assertEquals(singleView.singleConjugation.request.conjugationInput.rootLetters, newRootLetters)
      okButton(selectDialog.dialogPane()).fire()
    }

    innerOpen.get(20, TimeUnit.SECONDS)
    showAndSelect.get(20, TimeUnit.SECONDS)
    val updated = onFx(pairedView.firstConjugationRequest)
    assertEquals(updated.conjugationInput.rootLetters, newRootLetters)
  }
}
