package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package tokeneditor
package control
package skin

import morphology.model.{
  AbstractNounProperties,
  AbstractProperties,
  BaseProperties,
  NounProperties,
  ParticleProperties,
  ProNounProperties,
  VerbProperties,
  WordType
}
import tokeneditor.*
import ui.{ ArabicSupportEnumComboBox, ListType }
import javafx.scene.control.SkinBase
import scalafx.Includes.*
import scalafx.collections.ObservableBuffer
import scalafx.event.subscriptions.Subscription
import scalafx.geometry.{ Insets, Pos }
import scalafx.scene.Node
import scalafx.scene.control.Label
import scalafx.scene.layout.{ BorderPane, GridPane, HBox }

import scala.collection.mutable.ListBuffer

class LocationSkin(control: LocationView) extends SkinBase[LocationView](control) {

  import LocationSkin.*

  private val subscriptions = ListBuffer.empty[Subscription]

  private val hBox = new HBox() {
    spacing = Gap
    alignment = Pos.Center
    padding = Insets(Gap, Gap, Gap, Gap)
  }

  private val nounPropertiesView = NounPropertiesView()
  private val proNounPropertiesView = ProNounPropertiesView()
  private val verbPropertiesView = VerbPropertiesView()
  private val particlePropertiesView = ParticlePropertiesView()
  private val commonPropertiesView = commonPropertiesPane

  getChildren.add(initializeSkin)

  private def initializeSkin = {
    val pane = new BorderPane()

    hBox.getChildren.addAll(commonPropertiesView, nounPropertiesView)
    updateView()

    pane.center = hBox
    pane
  }

  private def commonPropertiesPane = {
    val pane = new BorderPane() {
      styleClass = ObservableBuffer[String]("border")
    }

    val gridPane = new GridPane() {
      hgap = Gap
      vgap = Gap
      padding = Insets(Gap, Gap, Gap, Gap)
      alignment = Pos.Center
    }

    val wordTypeLabel = Label("Word Type:")
    gridPane.add(wordTypeLabel, 0, 0)

    val wordTypeComboBox = createWordTypeComboBox
    wordTypeLabel.labelFor = wordTypeComboBox
    gridPane.add(wordTypeComboBox, 1, 0)

    pane.center = gridPane
    pane
  }

  private def updateView(): Unit = {
    subscriptions.foreach(_.cancel())
    subscriptions.clear()

    control
      .wordTypeProperty
      .onChange((_, _, nv) => control.properties = Option(nv).map(_.properties).getOrElse(WordType.NOUN.properties))

    control
      .locationPropertiesProperty
      .onChange((_, _, nv) => {
        val node =
          if Option(nv).isDefined then {
            nv match
              case p: NounProperties =>
                val subscription =
                  nounPropertiesView
                    .propertiesProperty
                    .onChange((_, _, nv) => {
                      if Option(nv).isDefined && nv != control.properties then control.properties = nv
                    })

                subscriptions.addOne(subscription)
                nounPropertiesView.properties = p
                nounPropertiesView

              case p: ProNounProperties =>
                val subscription =
                  proNounPropertiesView
                    .propertiesProperty
                    .onChange((_, _, nv) => {
                      if Option(nv).isDefined && nv != control.properties then control.properties = nv
                    })

                subscriptions.addOne(subscription)
                proNounPropertiesView.properties = p
                proNounPropertiesView

              case p: VerbProperties =>
                val subscription =
                  verbPropertiesView
                    .propertiesProperty
                    .onChange((_, _, nv) => {
                      if Option(nv).isDefined && nv != control.properties then control.properties = nv
                    })

                subscriptions.addOne(subscription)
                verbPropertiesView.properties = p
                verbPropertiesView

              case p: ParticleProperties =>
                val subscription =
                  particlePropertiesView
                    .propertiesProperty
                    .onChange((_, _, nv) => {
                      if Option(nv).isDefined && nv != control.properties then control.properties = nv
                    })

                subscriptions.addOne(subscription)
                particlePropertiesView.properties = p
                particlePropertiesView
          } else {
            nounPropertiesView.properties = WordType.NOUN.properties.asInstanceOf[NounProperties]
            nounPropertiesView
          }
        hBox.getChildren.clear()
        hBox.getChildren.addAll(commonPropertiesView, node)
      })
  }

  private def createWordTypeComboBox = {
    val comboBox = ArabicSupportEnumComboBox(
      WordType.values,
      ListType.LABEL_AND_CODE
    )
    control.wordTypeProperty.bindBidirectional(comboBox.valueProperty())

    comboBox
  }
}

object LocationSkin {

  private val Gap = 10.0

  def apply(control: LocationView): LocationSkin = new LocationSkin(control)
}
