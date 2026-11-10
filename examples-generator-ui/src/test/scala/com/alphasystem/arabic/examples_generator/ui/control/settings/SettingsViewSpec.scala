package com.alphasystem
package arabic
package examples_generator
package ui
package control
package settings

import arabic.morphologicalengine.conjugation.model.MorphologicalTermType
import examples_generator.morphologicalchart.{ Conjugations, Settings }
import ui.control.conjugations.ConjugationsFileGeneratorView
import ui.control.conjugations.single.{ SingleConjugationEditorDialog, SingleConjugationEditorView }
import ui.model.{ Action, ConjugationsRequest }
import ui.utils.ConjugationsSpec
import javafx.application.Platform
import javafx.scene.control.{ Accordion, ScrollPane, TextField }
import javafx.scene.layout.{ BorderPane, GridPane }
import munit.FunSuite
import org.controlsfx.control.ToggleSwitch

import java.util.concurrent.{ FutureTask, TimeUnit }
import scala.jdk.CollectionConverters.*

class SettingsViewSpec extends FunSuite {

  private def onFx[A](body: => A): A = {
    ConjugationsSpec.initialize()
    val task = new FutureTask[A](() => body)
    Platform.runLater(task)
    task.get(20, TimeUnit.SECONDS)
  }

  private val enabledSettings = Settings(
    showPronouns = Some(true),
    showNumbers = Some(true),
    showGenders = Some(true),
    showConversationTypes = Some(true),
    showNounStatus = Some(true),
    showTermTypeCaption = Some(true),
    tableWidth = Some(80)
  )

  private def grid(view: SettingsView): GridPane =
    view.getChildrenUnmodifiable.get(0).asInstanceOf[BorderPane].getCenter.asInstanceOf[GridPane]

  private def toggles(view: SettingsView): Seq[ToggleSwitch] =
    grid(view).getChildren.asScala.collect { case toggle: ToggleSwitch => toggle }.toSeq

  private def widthField(view: SettingsView): TextField =
    grid(view).getChildren.asScala.collectFirst { case field: TextField => field }.get

  test("turning settings switches off clears their optional values and turning them on restores them") {
    onFx {
      val view = SettingsView()
      view.settings = enabledSettings
      val switches = toggles(view)
      assertEquals(switches.size, 6)
      switches.foreach(_.setSelected(false))
      assertEquals(view.settings, Settings(tableWidth = Some(80)))
      switches.foreach(_.setSelected(true))
      assertEquals(view.settings, enabledSettings)
    }
  }

  test("clearing table width removes the saved width and subsequent edits still propagate") {
    onFx {
      val view = SettingsView()
      view.settings = enabledSettings
      val field = widthField(view)
      assertEquals(field.getText, "80")
      field.setText("")
      assertEquals(view.settings, enabledSettings.copy(tableWidth = None))
      field.setText("60")
      assertEquals(view.settings.tableWidth, Some(60))
      field.setText("999999999999999999999")
      assertEquals(view.settings.tableWidth, None)
    }
  }

  test("loading settings preserves explicit false and None values without publishing intermediate models") {
    onFx {
      val view = SettingsView()
      var changes = Vector.empty[Settings]
      view.settingsProperty.onChange((_, _, nv) => changes :+= nv)
      val values = Seq(
        enabledSettings,
        Settings(showPronouns = Some(false), showNumbers = Some(true), tableWidth = Some(60)),
        Settings(),
        enabledSettings
      )
      values.foreach { value =>
        changes = Vector.empty
        view.settings = value
        assertEquals(view.settings, value)
        assertEquals(changes, Vector(value))
      }
    }
  }

  test("changing term type changes enabled state without changing selected settings") {
    onFx {
      val view = SettingsView()
      view.settings = enabledSettings
      view.morphologicalTermType = MorphologicalTermType.VerbalNoun
      assertEquals(view.settings, enabledSettings)
      val switches = toggles(view)
      assert(switches.head.isDisabled)
      assert(!switches(4).isDisabled)
      assert(switches.forall(_.isSelected))
      view.morphologicalTermType = MorphologicalTermType.PastTense
      assertEquals(view.settings, enabledSettings)
      assert(!switches.head.isDisabled)
      assert(switches(4).isDisabled)
      assert(switches.forall(_.isSelected))
    }
  }

  test("cleared settings reach the dialog result and update the existing stored conjugation") {
    onFx {
      val original = DefaultSingleConjugation.copy(tag = "existing", settings = enabledSettings)
      val dialog = SingleConjugationEditorDialog()
      dialog.reset(maybeSingleConjugation = Some(original))
      val editor = dialog.getDialogPane.getContent.asInstanceOf[SingleConjugationEditorView]
      val scrollPane = editor
        .getChildrenUnmodifiable
        .get(0)
        .asInstanceOf[BorderPane]
        .getCenter
        .asInstanceOf[ScrollPane]
      val accordion = scrollPane.getContent.asInstanceOf[Accordion]
      val view = accordion.getPanes.get(1).getContent.asInstanceOf[SettingsView]
      toggles(view).foreach(_.setSelected(false))
      widthField(view).setText("")
      assertEquals(editor.singleConjugation.settings, Settings())
      val ok = dialog.getDialogPane.getButtonTypes.asScala.find(_.getText == "OK").get
      val updated = dialog.getResultConverter.call(ok).get
      assertEquals(updated.id, original.id)
      assertEquals(updated.settings, Settings())
      val generator = ConjugationsFileGeneratorView()
      generator.conjugationsRequest = ConjugationsRequest(Conjugations(single = Some(Seq(original))))
      generator.action = Action.UpdateSingleConjugation(updated)
      assertEquals(generator.conjugationsRequest.conjugations.single.get.head, updated)
      dialog.reset(maybeSingleConjugation = Some(updated))
      assert(toggles(view).forall(toggle => !toggle.isSelected))
      assertEquals(widthField(view).getText, "")
      dialog.reset(maybeSingleConjugation = Some(original))
      assertEquals(view.settings, enabledSettings)
      assertEquals(original.settings, enabledSettings)
    }
  }
}
