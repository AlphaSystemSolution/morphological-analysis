package com.alphasystem
package arabic
package examples_generator
package ui
package control
package conjugation_selector

import arabic.model.ProNoun
import arabic.morphologicalengine.conjugation.model.MorphologicalTermType
import ui.model.VerbTranslationResult
import ui.utils.ConjugationsSpec
import javafx.application.Platform
import javafx.scene.control.TextField
import javafx.scene.layout.{ BorderPane, GridPane, VBox }
import munit.FunSuite

import java.util.concurrent.{ FutureTask, TimeUnit }

class ConjugationTermSelectorViewSpec extends FunSuite {

  private def onFx[A](body: => A): A = {
    ConjugationsSpec.initialize()
    val task = new FutureTask[A](() => body)
    Platform.runLater(task)
    task.get(20, TimeUnit.SECONDS)
  }

  private def selectorView(
    editor: ui.control.conjugations.single.SingleConjugationEditorView
  ): ConjugationTermSelectorView = {
    val scrollPane = editor
      .getChildrenUnmodifiable
      .get(0)
      .asInstanceOf[BorderPane]
      .getCenter
      .asInstanceOf[javafx.scene.control.ScrollPane]
    val accordion = scrollPane.getContent.asInstanceOf[javafx.scene.control.Accordion]
    accordion.getPanes.get(0).getContent.asInstanceOf[ConjugationTermSelectorView]
  }

  test("text conversion removes blank alternatives and empty pronoun entries") {
    Seq("", " ", "\t", "/", " / / ").foreach { text =>
      val result = VerbTranslationResult.fromStringValues(Map(ProNoun.values.head -> text))
      assertEquals(result.translations, Map.empty)
    }
    val result = VerbTranslationResult.fromStringValues(
      Map(ProNoun.values.head -> " one / / two / ", ProNoun.values.last -> " ")
    )
    assertEquals(result.translations, Map(ProNoun.values.head -> Seq("one", "two")))
  }

  test("blank strings in direct translation results are removed before publishing the request") {
    onFx {
      val view = ConjugationTermSelectorView()
      view.translationResult = VerbTranslationResult(
        Map(ProNoun.values.head -> Seq(" one ", "", "\t", " two "), ProNoun.values.last -> Seq(" "))
      )
      assertEquals(view.singleConjugation.request.translations, Some(Map(ProNoun.values.head -> Seq("one", "two"))))
      view.translationResult = VerbTranslationResult(Map(ProNoun.values.head -> Seq("", " ", "\t")))
      assertEquals(view.singleConjugation.request.translations, None)
      view.morphologicalTermTypeProperty.value = MorphologicalTermType.VerbalNoun
      view.morphologicalTermTypeProperty.value = MorphologicalTermType.PastTense
      assertEquals(view.singleConjugation.request.translations, None)
    }
  }

  test("clearing dialog fields updates the stored conjugation translations to None") {
    onFx {
      import ui.control.conjugations.ConjugationsFileGeneratorView
      import ui.control.conjugations.single.{ SingleConjugationEditorDialog, SingleConjugationEditorView }
      import ui.model.{ Action, ConjugationsRequest }
      import examples_generator.morphologicalchart.Conjugations
      import scala.jdk.CollectionConverters.*

      val initial = DefaultSingleConjugation
      val original = initial.copy(request =
        initial
          .request
          .copy(
            translations = Some(Map(ProNoun.values.head -> Seq("original")))
          )
      )
      val editorDialog = SingleConjugationEditorDialog()
      editorDialog.reset(maybeSingleConjugation = Some(original))
      val editor = editorDialog.getDialogPane.getContent.asInstanceOf[SingleConjugationEditorView]
      val selector = selectorView(editor)
      val translationsDialog = VerbalTranslationsDialog()
      translationsDialog.update(original.request.translations.get)
      val translationsView = translationsDialog.getDialogPane.getContent.asInstanceOf[VerbTranslationsView]
      val grid = translationsView
        .getChildrenUnmodifiable
        .get(0)
        .asInstanceOf[BorderPane]
        .getCenter
        .asInstanceOf[GridPane]
      grid.getChildren.asScala.collect { case field: TextField => field }.foreach(_.setText(" "))
      val translationsOk = translationsDialog.getDialogPane.getButtonTypes.asScala.find(_.getText == "OK").get
      selector.translationResult = translationsDialog.getResultConverter.call(translationsOk).get
      val editorOk = editorDialog.getDialogPane.getButtonTypes.asScala.find(_.getText == "OK").get
      val updated = editorDialog.getResultConverter.call(editorOk).get
      assertEquals(updated.id, original.id)
      assertEquals(updated.request.translations, None)

      val generator = ConjugationsFileGeneratorView()
      generator.conjugationsRequest = ConjugationsRequest(Conjugations(single = Some(Seq(original))))
      generator.action = Action.UpdateSingleConjugation(updated)
      assertEquals(generator.conjugationsRequest.conjugations.single.get.head.request.translations, None)
      assertEquals(generator.conjugationsRequest.conjugations.single.get.head.id, original.id)
    }
  }

  test("reusing the translations dialog clears pronouns missing from the next map") {
    onFx {
      val dialog = VerbalTranslationsDialog()
      dialog.update(Map(ProNoun.values.head -> Seq("first"), ProNoun.values.last -> Seq("old")))
      dialog.update(Map(ProNoun.values.head -> Seq("next")))
      val view = dialog.getDialogPane.getContent.asInstanceOf[VerbTranslationsView]
      assertEquals(view.translations(ProNoun.values.head), "next")
      assertEquals(view.translations(ProNoun.values.last), "")
    }
  }

  test("translation edits preserve identity, normalize empty maps and refresh the summary") {
    onFx {
      val view = ConjugationTermSelectorView()
      val original = DefaultSingleConjugation
      view.singleConjugation = original
      val translations = Map(ProNoun.values.head -> Seq("one", "two"), ProNoun.values.last -> Seq.empty[String])
      view.translationResult = VerbTranslationResult(translations)
      assertEquals(view.singleConjugation.id, original.id)
      assertEquals(view.singleConjugation.request.translations, Some(translations.filter(_._2.nonEmpty)))
      val panel = view.getChildrenUnmodifiable.get(0).asInstanceOf[BorderPane].getCenter.asInstanceOf[VBox]
      val summary = panel.getChildren.get(0).asInstanceOf[GridPane].getChildren.get(3).asInstanceOf[TextField]
      assertEquals(summary.getText, s"1 of ${ProNoun.values.length} values are set")
      view.translationResult = VerbTranslationResult()
      assertEquals(view.singleConjugation.request.translations, None)
      assertEquals(summary.getText, s"0 of ${ProNoun.values.length} values are set")
    }
  }

  test("translation result edits reach the dialog result with the original conjugation ID") {
    onFx {
      import ui.control.conjugations.single.{ SingleConjugationEditorDialog, SingleConjugationEditorView }
      import scala.jdk.CollectionConverters.*

      val translations = Map(ProNoun.values.head -> Seq("updated"))
      val original = DefaultSingleConjugation.copy(tag = "existing")
      val dialog = SingleConjugationEditorDialog()
      dialog.reset(maybeSingleConjugation = Some(original))
      val editor = dialog.getDialogPane.getContent.asInstanceOf[SingleConjugationEditorView]
      val selector = selectorView(editor)
      selector.translationResult = VerbTranslationResult(translations)
      val ok = dialog.getDialogPane.getButtonTypes.asScala.find(_.getText == "OK").get
      val result = dialog.getResultConverter.call(ok).get
      assertEquals(result.id, original.id)
      assertEquals(result.tag, original.tag)
      assertEquals(result.request.translations, Some(translations))
    }
  }

  test("loaded verb translations survive term switches without leaking into the next model") {
    onFx {
      val view = ConjugationTermSelectorView()
      val translations = Map(ProNoun.values.head -> Seq("loaded"))
      val original = DefaultSingleConjugation
      view.singleConjugation = original.copy(request = original.request.copy(translations = Some(translations)))
      view.morphologicalTermTypeProperty.value = MorphologicalTermType.VerbalNoun
      assertEquals(view.singleConjugation.request.translations, None)
      view.conjugationRequest = view
        .conjugationRequest
        .copy(
          conjugationInput = view.conjugationRequest.conjugationInput.copy(translation = Some("edited base"))
        )
      view.morphologicalTermTypeProperty.value = MorphologicalTermType.PresentTense
      assertEquals(view.singleConjugation.request.translations, Some(translations))
      val next = DefaultSingleConjugation
      view.singleConjugation = next.copy(
        request = next.request.copy(morphologicalTermType = MorphologicalTermType.VerbalNoun)
      )
      view.morphologicalTermTypeProperty.value = MorphologicalTermType.PastTense
      assertEquals(view.singleConjugation.id, next.id)
      assertEquals(view.singleConjugation.request.translations, None)
    }
  }
}
