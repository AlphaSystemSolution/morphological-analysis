package com.alphasystem
package arabic
package examples_generator
package ui
package control
package examples_view
package skin

import arabic.fx.ui.util.EmDash
import examples_generator.model.ExampleData
import examples_generator.morphologicalchart.Conjugations
import control.conjugations.ConjugationsFileGeneratorView
import control.file.ExampleFileGeneratorView
import ui.model.{ Action, ConjugationsRequest, ExampleRequest, FileType }
import javafx.beans.binding.BooleanBinding
import javafx.collections.ListChangeListener
import javafx.scene.control.SkinBase
import scalafx.Includes.*
import scalafx.application.JFXApp3
import scalafx.beans.binding.Bindings
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control.ButtonBar.ButtonData
import scalafx.scene.control.{ Alert, Tab, TabPane }
import scalafx.scene.input.{ KeyCode, KeyCodeCombination, KeyCombination }
import scalafx.scene.layout.BorderPane

import java.nio.file.{ Path, Paths }
import scala.compiletime.uninitialized

class ExamplesSkin private (control: ExamplesView) extends SkinBase[ExamplesView](control) {

  private var untitledCount = 0
  private var multipleDirtyBinding: BooleanBinding = uninitialized
  private val tabPane = new TabPane()

  tabPane.getTabs.addListener((_: ListChangeListener.Change[? <: javafx.scene.control.Tab]) => rebindMultipleDirty())

  tabPane
    .selectionModel
    .value
    .selectedItemProperty()
    .onChange((_, _, nv) => {
      unbind()
      if Option(nv).isDefined then {
        nv.getContent match {
          case mainContent: ExampleFileGeneratorView      => bind(mainContent)
          case mainContent: ConjugationsFileGeneratorView => bind(mainContent)
          case _                                          => // do nothing
        }
      }
    })

  control
    .actionProperty
    .onChange((_, _, nv) => {
      nv match {
        case Action.NoAction => // do nothing
        case Action.OpenExampleFile(filePath, exampleData) =>
          tabPane.getTabs.find(_.userData == filePath) match {
            case Some(tab) => tabPane.getSelectionModel.select(tab)
            case None      => addExampleTab(Option(filePath), exampleData)
          }

        case Action.NewExampleFile(tableData) => addExampleTab(None, ExampleData(Seq(tableData)))

        case Action.SaveExampleFile(ExampleRequest(Some(path), _, newFileSaved)) =>
          val currentTab = tabPane.selectionModel.value.getSelectedItem
          val content = currentTab.content.value
          content match {
            case mainContent: ExampleFileGeneratorView =>
              mainContent.filePath = path
              if newFileSaved then {
                currentTab.text = normalizeTabTitles(path)
                currentTab.userData = path
              }
              mainContent.markClean()
            case _ => // do nothing
          }

        case Action.AddTable(tableData) =>
          val content = tabPane.selectionModel.value.getSelectedItem.content.value
          content match {
            case mainContent: ExampleFileGeneratorView =>
              mainContent.addTableData(tableData)
              mainContent.selectTable(tableData.tag)
            case _ => // do nothing
          }

        case Action.NewConjugationFile => addConjugationsTab(None, Conjugations())

        case Action.OpenConjugationsFile(filePath, conjugations) =>
          tabPane.getTabs.find(_.userData == filePath) match {
            case Some(tab) => tabPane.getSelectionModel.select(tab)
            case None      => addConjugationsTab(Option(filePath), conjugations)
          }

        case Action.SaveConjugationFile(request) =>
          val currentTab = tabPane.selectionModel.value.getSelectedItem
          val content = currentTab.content.value
          content match {
            case mainContent: ConjugationsFileGeneratorView =>
              request.path.foreach { path =>
                mainContent.filePath = path
                if request.newFileSaved then {
                  currentTab.text = normalizeTabTitles(path)
                  currentTab.userData = path
                }
              }
              mainContent.markClean()
            case _ => // do nothing
          }

        case _ =>
          val content = tabPane.selectionModel.value.getSelectedItem.content.value
          content match {
            case mainContent: ExampleFileGeneratorView =>
              mainContent.action = nv
              control.action = Action.NoAction
            case mainContent: ConjugationsFileGeneratorView =>
              mainContent.action = nv
              control.action = Action.NoAction
            case _ => // do nothing
          }
      }
      control.action = Action.NoAction
    })

  private val mainPane =
    new BorderPane {
      center = tabPane
    }

  getChildren.addAll(mainPane)

  private def addExampleTab(maybePath: Option[Path], exampleData: ExampleData): Unit = {
    val tabTitle =
      maybePath match {
        case Some(path) => normalizeTabTitles(path)
        case None       => createUntitledTitle
      }

    val mainContent = createExamplesFileView(maybePath.orNull, exampleData)
    control.exampleRequestProperty.bind(mainContent.exampleRequestProperty)
    val tab = new Tab {
      text = tabTitle
      userData = maybePath.getOrElse(Paths.get(tabTitle))
      content = mainContent
      onCloseRequest = confirmCloseIfDirty(mainContent.dirty)
    }
    tabPane.getTabs.add(tab)
    tabPane.getSelectionModel.selectLast()
  }

  private def addConjugationsTab(maybePath: Option[Path], conjugations: Conjugations): Unit = {
    val tabTitle =
      maybePath match {
        case Some(path) => normalizeTabTitles(path)
        case None       => createUntitledTitle
      }

    val mainContent = ConjugationsFileGeneratorView()
    mainContent.conjugationsRequest = ConjugationsRequest(conjugations = conjugations, path = maybePath)
    val tab = new Tab {
      text = tabTitle
      userData = maybePath.getOrElse(Paths.get(tabTitle))
      content = mainContent
      onCloseRequest = confirmCloseIfDirty(mainContent.dirty)
    }
    tabPane.getTabs.add(tab)
    tabPane.getSelectionModel.selectLast()
  }

  private def confirmCloseIfDirty(dirty: => Boolean): javafx.event.EventHandler[javafx.event.Event] = { event =>
    if dirty then {
      new Alert(AlertType.Confirmation) {
        initOwner(JFXApp3.Stage)
        title = "Are you sure?"
        contentText = "There are unsaved changes. Do you want to save them?"
      }.showAndWait() match {
        case Some(buttonType) if buttonType.buttonData == ButtonData.OKDone =>
          val key = new KeyCodeCombination(KeyCode.S, KeyCombination.MetaDown)
          Option(JFXApp3.Stage.getScene.getAccelerators.get(key)).foreach(_.run())
          event.consume()
        case _ => event.consume()
      }
    }
  }

  private def createUntitledTitle = {
    untitledCount += 1
    s"Untitled$untitledCount.yml"
  }

  private def rebindMultipleDirty(): Unit = {
    Option(multipleDirtyBinding).foreach(_ => control.multipleDirtyPropertyWrapper.unbind())
    val dirtyProperties = tabPane
      .tabs
      .toArray
      .toSeq
      .collect { case tab => tab.getContent }
      .collect {
        case view: ExampleFileGeneratorView      => view.dirtyProperty
        case view: ConjugationsFileGeneratorView => view.dirtyProperty
      }
    if dirtyProperties.isEmpty then {
      multipleDirtyBinding = null
      control.multipleDirty = false
    } else {
      multipleDirtyBinding = Bindings.createBooleanBinding(() => dirtyProperties.exists(_.value), dirtyProperties*)
      control.multipleDirtyPropertyWrapper.bind(multipleDirtyBinding)
    }
  }

  private def createExamplesFileView(path: Path, exampleData: ExampleData) = {
    val fileView = ExampleFileGeneratorView()
    fileView.addExampleData(exampleData)
    if Option(path).isDefined then {
      fileView.filePath = path
      fileView.markClean()
    }
    fileView
  }

  private def bind(mainContent: ExampleFileGeneratorView): Unit = {
    control.fileType = FileType.Example
    control.dirtyPropertyWrapper.bind(mainContent.dirtyProperty)
    control.hasSelectedTablePropertyWrapper.bind(mainContent.selectedTableProperty.isNotNull)
    control.hasSelectedColumnPropertyWrapper.bind(mainContent.selectedColumnProperty.isNotNull)
    control.exampleRequestProperty.bind(mainContent.exampleRequestProperty)
  }

  private def bind(mainContent: ConjugationsFileGeneratorView): Unit = {
    control.fileType = FileType.Conjugation
    control.dirtyPropertyWrapper.bind(mainContent.dirtyProperty)
    control.conjugationsRequestPropertyWrapper.bind(mainContent.conjugationsRequestProperty)
  }

  private def unbind(): Unit = {
    control.exampleRequestProperty.unbind()
    control.dirtyPropertyWrapper.unbind()
    control.multipleDirtyPropertyWrapper.unbind()
    control.hasSelectedTablePropertyWrapper.unbind()
    control.hasSelectedColumnPropertyWrapper.unbind()
    control.dirty = false
    control.multipleDirty = false
    control.tableIsSelected = false
    control.columnIsSelected = false
    control.exampleRequest = null
    control.fileType = FileType.Missing
    control.conjugationsRequestPropertyWrapper.unbind()
    control.conjugationsRequest = null
  }

  private def normalizeTabTitles(path: Path) = {
    val fileName = path.getFileName.toString
    val tabs = tabPane.getTabs.filter(_.text.value == fileName)
    tabs.foreach { tab =>
      val currentPath = tab.userData.asInstanceOf[Path]
      tab.text = createTabTitle(currentPath)
    }
    if tabs.nonEmpty then createTabTitle(path) else fileName
  }

  private def createTabTitle(path: Path) = {
    val fileName = path.getFileName.toString
    val parentName = Option(path.getParent).map(_.getFileName.toString).map(s" $EmDash " + _).getOrElse("")
    fileName + parentName
  }
}

object ExamplesSkin {

  private[examples_view] def apply(control: ExamplesView): ExamplesSkin = new ExamplesSkin(control)
}
