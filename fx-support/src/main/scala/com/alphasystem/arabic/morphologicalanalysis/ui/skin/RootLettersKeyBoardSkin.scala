package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package skin

import arabic.fx.ui.util.UIUserPreferences
import arabic.model.ArabicLetterType
import javafx.scene.control.SkinBase
import scalafx.Includes.*
import scalafx.geometry.{ NodeOrientation, Pos }
import scalafx.scene.control.Button
import scalafx.scene.layout.{ BorderPane, FlowPane, Region, VBox }
import scalafx.scene.text.Text

class RootLettersKeyBoardSkin(control: RootLettersKeyBoardView)(using preferences: UIUserPreferences)
    extends SkinBase[RootLettersKeyBoardView](control) {

  private val row1 = Seq(
    ArabicLetterType.Ddad,
    ArabicLetterType.Sad,
    ArabicLetterType.Tha,
    ArabicLetterType.Qaf,
    ArabicLetterType.Fa,
    ArabicLetterType.Ghain,
    ArabicLetterType.Ain,
    ArabicLetterType.Hha,
    ArabicLetterType.Kha,
    ArabicLetterType.Ha,
    ArabicLetterType.Jeem,
    ArabicLetterType.Dal,
    ArabicLetterType.Thal
  )

  private val row2 = Seq(
    ArabicLetterType.Sheen,
    ArabicLetterType.Seen,
    ArabicLetterType.Ya,
    ArabicLetterType.Ba,
    ArabicLetterType.Lam,
    ArabicLetterType.Alif,
    ArabicLetterType.Ta,
    ArabicLetterType.Noon,
    ArabicLetterType.Meem,
    ArabicLetterType.Kaf,
    ArabicLetterType.Tta,
    ArabicLetterType.Tatweel,
    ArabicLetterType.Tatweel
  )

  private val row3 = Seq(
    ArabicLetterType.YaHamzaAbove,
    ArabicLetterType.Hamza,
    ArabicLetterType.WawHamzaAbove,
    ArabicLetterType.Ra,
    ArabicLetterType.AlifMaksura,
    ArabicLetterType.TaMarbuta,
    ArabicLetterType.Waw,
    ArabicLetterType.Zain,
    ArabicLetterType.Dtha,
    ArabicLetterType.Tatweel,
    ArabicLetterType.Tatweel
  )

  private var currentIndex = 0
  private val firstRadicalLabel = ArabicLabelView(control.firstRadical)
  private val secondRadicalLabel = ArabicLabelView(control.secondRadical)
  private val thirdRadicalLabel = ArabicLabelView(control.thirdRadical)
  private val fourthRadicalLabel = ArabicLabelView(fourthRadicalValue(control.fourthRadical))
  private var currentView: ArabicLabelView = firstRadicalLabel
  private val group = ArabicLabelToggleGroup("root-letters-keyboard")
  private val labels = Array.ofDim[ArabicLabelView](4)

  getChildren.addAll(initializeSkin)

  private def initializeSkin = {
    val vbox = new VBox() {
      padding = control.getPadding
      spacing = control.spacing
      focusTraversable = true
      styleClass = Seq("popup")
    }

    vbox.paddingProperty().bind(control.paddingProperty())
    vbox.spacingProperty().bind(control.spacingProperty)

    labels(0) = firstRadicalLabel
    labels(1) = secondRadicalLabel
    labels(2) = thirdRadicalLabel
    labels(3) = fourthRadicalLabel

    // initialize label group
    initializeLabelGroup()

    // initialize labels
    initializeFirstRadical()
    initializeSecondRadical()
    initializeThirdRadical()
    initializeFourthRadical()

    selectFirst()

    val rootLettersPane = new FlowPane() {
      hgap = control.spacing
      nodeOrientation = NodeOrientation.RightToLeft
      alignment = Pos.TopCenter
    }
    rootLettersPane.getChildren.addAll(firstRadicalLabel, secondRadicalLabel, thirdRadicalLabel, fourthRadicalLabel)
    rootLettersPane.hgapProperty().bind(control.spacingProperty)

    val thirdRow = {
      val pane = createRow(row3)
      pane.children.addOne(createClearButton())
      pane
    }

    vbox.children = Seq(rootLettersPane, createRow(row1), createRow(row2), thirdRow)
    new BorderPane() {
      center = vbox
    }
  }

  private def initializeLabelGroup(): Unit = {
    group.multipleSelect = false
    group.width = control.selectedLabelWidth
    group.height = control.selectedLabelHeight
    group.font = control.font
    group.widthProperty.bind(control.selectedLabelWidthProperty)
    group.heightProperty.bind(control.selectedLabelHeightProperty)
    group.fontProperty.bind(control.fontProperty)
  }

  private def initializeFirstRadical(): Unit = {
    firstRadicalLabel.label = control.firstRadical
    firstRadicalLabel.group = group
    firstRadicalLabel
      .selectedProperty
      .onChange((_, _, _) => {
        currentIndex = 0
        currentView = firstRadicalLabel
      })
    firstRadicalLabel.labelProperty.onChange((_, _, nv) => control.firstRadical = nv.asInstanceOf[ArabicLetterType])
    control.firstRadicalProperty.onChange((_, _, nv) => firstRadicalLabel.label = nv)
  }

  private def initializeSecondRadical(): Unit = {
    secondRadicalLabel.label = control.secondRadical
    secondRadicalLabel.group = group
    secondRadicalLabel
      .selectedProperty
      .onChange((_, _, _) => {
        currentIndex = 1
        currentView = secondRadicalLabel
      })
    secondRadicalLabel.labelProperty.onChange((_, _, nv) => control.secondRadical = nv.asInstanceOf[ArabicLetterType])
    control.secondRadicalProperty.onChange((_, _, nv) => secondRadicalLabel.label = nv)
  }

  private def initializeThirdRadical(): Unit = {
    thirdRadicalLabel.label = control.thirdRadical
    thirdRadicalLabel.group = group
    thirdRadicalLabel
      .selectedProperty
      .onChange((_, _, _) => {
        currentIndex = 2
        currentView = thirdRadicalLabel
      })
    thirdRadicalLabel.labelProperty.onChange((_, _, nv) => control.thirdRadical = nv.asInstanceOf[ArabicLetterType])
    control.thirdRadicalProperty.onChange((_, _, nv) => thirdRadicalLabel.label = nv)
  }

  private def initializeFourthRadical(): Unit = {
    fourthRadicalLabel.label = fourthRadicalValue(control.fourthRadical)
    fourthRadicalLabel.group = group
    thirdRadicalLabel
      .selectedProperty
      .onChange((_, _, _) => {
        currentIndex = 3
        currentView = fourthRadicalLabel
      })
    fourthRadicalLabel
      .labelProperty
      .onChange((_, _, nv) => {
        val label = nv.label
        control.fourthRadical = if label.isBlank then None else Some(ArabicLetterType.UnicodesMap(label.charAt(0)))
      })
    control.fourthRadicalProperty.onChange((_, _, nv) => fourthRadicalLabel.label = fourthRadicalValue(nv))
  }

  private def selectFirst(): Unit = {
    currentIndex = 0
    currentView = firstRadicalLabel
    currentView.select = true
  }

  private def fourthRadicalValue(value: Option[ArabicLetterType]) = value.getOrElse(ArabicLetterType.Tatweel)

  private def createRow(values: Seq[ArabicLetterType]) = {
    val pane = new FlowPane() {
      hgap = control.spacing
      minWidth = 700
    }
    pane.hgapProperty().bind(control.spacingProperty)
    values.foreach { letter =>
      pane.getChildren.addOne(createKeyBoardButton(letter))
    }
    pane
  }

  private def createKeyBoardButton(letter: ArabicLetterType) = {
    val button = new Button() {
      style = control.getStyle

      private val d = letter == ArabicLetterType.Tatweel
      disable = d

      prefWidth = control.keyboardButtonWidth
      prefHeight = control.keyboardButtonHeight
      minWidth = Region.USE_PREF_SIZE
      minHeight = Region.USE_PREF_SIZE
      maxWidth = Region.USE_PREF_SIZE
      maxHeight = Region.USE_PREF_SIZE

      if !d then {
        val text = new Text(letter.label) {
          font = control.font
        }
        text.fontProperty().bind(control.fontProperty)
        graphic = text
      }
      onAction = event => {
        clickKeyboardButtonAction(letter)
        event.consume()
      }
    }
    button.prefWidthProperty().bind(control.keyboardButtonWidthProperty)
    button.prefHeightProperty().bind(control.keyboardButtonHeightProperty)

    button
  }

  private def clickKeyboardButtonAction(letter: ArabicLetterType): Unit = {
    val selectedLabel = group.selectedLabel
    selectedLabel.label = letter
    selectedLabel.select = false
    currentIndex = (currentIndex + 1) % 4
    currentView = labels(currentIndex)
    currentView.select = true
  }

  private def createClearButton() = {
    new Button() {
      text = "    Clear   "
      style = control.getStyle
      prefWidth = control.keyboardButtonWidth * 2 + control.spacing
      prefHeight = control.keyboardButtonHeight
      minWidth = Region.USE_PREF_SIZE
      minHeight = Region.USE_PREF_SIZE
      maxWidth = Region.USE_PREF_SIZE
      maxHeight = Region.USE_PREF_SIZE

      control.keyboardButtonWidthProperty.onChange((_, _, nv) => minWidth = nv.doubleValue() * 2 + control.spacing)
      control.spacingProperty.onChange((_, _, nv) => minWidth = control.keyboardButtonWidth * 2 + nv.doubleValue())
      control.keyboardButtonHeightProperty.onChange((_, _, nv) => minHeight = nv.doubleValue())

      onAction = event => {
        firstRadicalLabel.label = RootLettersKeyBoardView.DefaultRootLetters.firstRadical
        secondRadicalLabel.label = RootLettersKeyBoardView.DefaultRootLetters.secondRadical
        thirdRadicalLabel.label = RootLettersKeyBoardView.DefaultRootLetters.thirdRadical
        fourthRadicalLabel.label = ArabicLetterType.Tatweel
        selectFirst()
        event.consume()
      }
    }
  }
}

object RootLettersKeyBoardSkin {
  def apply(control: RootLettersKeyBoardView)(using pref: UIUserPreferences): RootLettersKeyBoardSkin =
    new RootLettersKeyBoardSkin(control)
}
