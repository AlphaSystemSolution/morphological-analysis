package com.alphasystem
package arabic
package examples_generator
package ui
package control
package conjugations
package single
package skin

import arabic.fx.ui.util.*
import arabic.model.ProNoun
import arabic.model.ProNoun.*
import javafx.scene.control.SkinBase
import scalafx.scene.control.TextField
import scalafx.scene.layout.{ BorderPane, GridPane, Pane }
import scalafx.Includes.*
import scalafx.collections.ObservableMap
import scalafx.scene.text.{ Text, TextFlow }

private class VerbTranslationsSkin(control: VerbTranslationsView) extends SkinBase[VerbTranslationsView](control) {

  private val gridPane = {
    val gridPane = new GridPane {
      hgap = DefaultGap
      vgap = DefaultGap
      padding = DefaultInsets
    }

    val thirdPersonMasculineSingular = createTextField(ThirdPersonMasculineSingular)
    val thirdPersonMasculineDual = createTextField(ThirdPersonMasculineDual)
    val thirdPersonMasculinePlural = createTextField(ThirdPersonMasculinePlural)
    val thirdPersonFeminineSingular = createTextField(ThirdPersonFeminineSingular)
    val thirdPersonFeminineDual = createTextField(ThirdPersonFeminineDual)
    val thirdPersonFemininePlural = createTextField(ThirdPersonFemininePlural)
    val secondPersonMasculineSingular = createTextField(SecondPersonMasculineSingular)
    val secondPersonMasculineDual = createTextField(SecondPersonMasculineDual)
    val secondPersonMasculinePlural = createTextField(SecondPersonMasculinePlural)
    val secondPersonFeminineSingular = createTextField(SecondPersonFeminineSingular)
    val secondPersonFeminineDual = createTextField(SecondPersonFeminineDual)
    val secondPersonFemininePlural = createTextField(SecondPersonFemininePlural)
    val firstPersonSingular = createTextField(FirstPersonSingular)
    val firstPersonPlural = createTextField(FirstPersonPlural)

    def updateTextField(proNoun: ProNoun, newValue: String): Unit = {
      proNoun match {
        case ProNoun.ThirdPersonMasculineSingular  => thirdPersonMasculineSingular.text = newValue
        case ProNoun.ThirdPersonMasculineDual      => thirdPersonMasculineDual.text = newValue
        case ProNoun.ThirdPersonMasculinePlural    => thirdPersonMasculinePlural.text = newValue
        case ProNoun.ThirdPersonFeminineSingular   => thirdPersonFeminineSingular.text = newValue
        case ProNoun.ThirdPersonFeminineDual       => thirdPersonFeminineDual.text = newValue
        case ProNoun.ThirdPersonFemininePlural     => thirdPersonFemininePlural.text = newValue
        case ProNoun.SecondPersonMasculineSingular => secondPersonMasculineSingular.text = newValue
        case ProNoun.SecondPersonMasculineDual     => secondPersonMasculineDual.text = newValue
        case ProNoun.SecondPersonMasculinePlural   => secondPersonMasculinePlural.text = newValue
        case ProNoun.SecondPersonFeminineSingular  => secondPersonFeminineSingular.text = newValue
        case ProNoun.SecondPersonFeminineDual      => secondPersonFeminineDual.text = newValue
        case ProNoun.SecondPersonFemininePlural    => secondPersonFemininePlural.text = newValue
        case ProNoun.FirstPersonSingular           => firstPersonSingular.text = newValue
        case ProNoun.FirstPersonPlural             => firstPersonPlural.text = newValue
      }
    }

    control
      .translationsProperties
      .onChange((_, change) => {
        change match {
          case ObservableMap.Add(key, added)        => updateTextField(key, added)
          case ObservableMap.Remove(key, removed)   => updateTextField(key, "")
          case ObservableMap.Replace(key, added, _) => updateTextField(key, added)
          case _                                    => // do nothing
        }
      })

    var row = 0
    gridPane.add(createPronounLabel(ThirdPersonMasculineSingular), 0, row)
    gridPane.add(createPronounLabel(ThirdPersonMasculineDual), 1, row)
    gridPane.add(createPronounLabel(ThirdPersonMasculinePlural), 2, row)

    row += 1
    gridPane.add(thirdPersonMasculineSingular, 0, row)
    gridPane.add(thirdPersonMasculineDual, 1, row)
    gridPane.add(thirdPersonMasculinePlural, 2, row)

    row += 1
    gridPane.add(createPronounLabel(ThirdPersonFeminineSingular), 0, row)
    gridPane.add(createPronounLabel(ThirdPersonFeminineDual), 1, row)
    gridPane.add(createPronounLabel(ThirdPersonFemininePlural), 2, row)

    row += 1
    gridPane.add(thirdPersonFeminineSingular, 0, row)
    gridPane.add(thirdPersonFeminineDual, 1, row)
    gridPane.add(thirdPersonFemininePlural, 2, row)

    row += 1
    gridPane.add(createPronounLabel(SecondPersonMasculineSingular), 0, row)
    gridPane.add(createPronounLabel(SecondPersonMasculineDual), 1, row)
    gridPane.add(createPronounLabel(SecondPersonMasculinePlural), 2, row)

    row += 1
    gridPane.add(secondPersonMasculineSingular, 0, row)
    gridPane.add(secondPersonMasculineDual, 1, row)
    gridPane.add(secondPersonMasculinePlural, 2, row)

    row += 1
    gridPane.add(createPronounLabel(SecondPersonFeminineSingular), 0, row)
    gridPane.add(createPronounLabel(SecondPersonFeminineDual), 1, row)
    gridPane.add(createPronounLabel(SecondPersonFemininePlural), 2, row)

    row += 1
    gridPane.add(secondPersonFeminineSingular, 0, row)
    gridPane.add(secondPersonFeminineDual, 1, row)
    gridPane.add(secondPersonFemininePlural, 2, row)

    row += 1
    gridPane.add(createPronounLabel(FirstPersonSingular), 0, row)
    gridPane.add(createPronounLabel(FirstPersonPlural), 1, row)
    gridPane.add(new Pane(), 2, row)

    row += 1
    gridPane.add(firstPersonSingular, 0, row)
    gridPane.add(firstPersonPlural, 1, row)
    gridPane.add(new Pane(), 2, row)

    gridPane
  }

  private val mainPanel = new BorderPane() {
    center = gridPane
  }

  getChildren.add(mainPanel)

  private def createPronounLabel(proNoun: ProNoun) = {
    new TextFlow() {
      style = "-fx-font-weight: bold;"
      children = Seq(
        new Text(s"${proNoun.conversation.code} ${proNoun.gender} ${proNoun.number} ("),
        new Text() {
          font = preferences.arabicFont(20)
          text = proNoun.word.unicode
          style = "-fx-font-weight: bold;"
        },
        new Text("):")
      )
    }
  }

  private def createTextField(proNoun: ProNoun) = {
    val textField = new TextField() {
      prefColumnCount = 20
      promptText = proNoun.name()
    }
    textField.textProperty().onChange { (_, _, nv) => control.translationsProperties.put(proNoun, nv) }
    textField
  }
}

object VerbTranslationsSkin {
  private[single] def apply(control: VerbTranslationsView): VerbTranslationsSkin =
    new VerbTranslationsSkin(control)
}
