package com.alphasystem.arabic.morphologicalanalysis.ui.tokeneditor.control.skin

import com.alphasystem.arabic.morphologicalanalysis.morphology.model.{
  AbstractNounProperties,
  AbstractProperties,
  BaseProperties,
  NounProperties,
  ParticleProperties,
  ProNounProperties,
  VerbProperties,
  WordType
}
import com.alphasystem.arabic.morphologicalanalysis.ui.tokeneditor.control.*
import javafx.scene.control.SkinBase
import scalafx.Includes.*
import scalafx.event.subscriptions.Subscription
import scalafx.geometry.{ Insets, Pos }
import scalafx.scene.Node
import scalafx.scene.layout.{ BorderPane, HBox }

import scala.collection.mutable.ListBuffer

class LocationSkin(control: LocationView)
    extends SkinBase[LocationView](control) {

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

  getChildren.add(initializeSkin)

  private def initializeSkin = {
    val pane = new BorderPane()

    hBox.getChildren.add(nounPropertiesView)
    updateView()

    pane.center = hBox
    pane
  }

  private def updateView(): Unit = {
    subscriptions.foreach(_.cancel())
    subscriptions.clear()

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
                      if Option(nv).isDefined && nv != control.properties then
                        control.properties = nv
                    })

                subscriptions.addOne(subscription)
                nounPropertiesView.properties = p
                nounPropertiesView

              case p: ProNounProperties =>
                val subscription =
                  proNounPropertiesView
                    .propertiesProperty
                    .onChange((_, _, nv) => {
                      if Option(nv).isDefined && nv != control.properties then
                        control.properties = nv
                    })

                subscriptions.addOne(subscription)
                proNounPropertiesView.properties = p
                proNounPropertiesView

              case p: VerbProperties =>
                val subscription =
                  verbPropertiesView
                    .propertiesProperty
                    .onChange((_, _, nv) => {
                      if Option(nv).isDefined && nv != control.properties then
                        control.properties = nv
                    })

                subscriptions.addOne(subscription)
                verbPropertiesView.properties = p
                verbPropertiesView

              case p: ParticleProperties =>
                val subscription =
                  particlePropertiesView
                    .propertiesProperty
                    .onChange((_, _, nv) => {
                      if Option(nv).isDefined && nv != control.properties then
                        control.properties = nv
                    })

                subscriptions.addOne(subscription)
                particlePropertiesView.properties = p
                particlePropertiesView
          } else {
            nounPropertiesView.properties =
              WordType.NOUN.properties.asInstanceOf[NounProperties]
            nounPropertiesView
          }
        hBox.getChildren.clear()
        hBox.getChildren.add(node)
      })
  }
}

object LocationSkin {

  private val Gap = 10.0

  def apply(control: LocationView): LocationSkin = new LocationSkin(control)
}
