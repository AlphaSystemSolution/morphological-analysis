package com.alphasystem.fx.ui

import de.jensd.fx.glyphs.fontawesome.{ FontAwesomeIcon, FontAwesomeIconView }
import de.jensd.fx.glyphs.materialdesignicons.{ MaterialDesignIcon, MaterialDesignIconView }
import de.jensd.fx.glyphs.materialicons.{ MaterialIcon, MaterialIconView }
import de.jensd.fx.glyphs.octicons.{ OctIcon, OctIconView }
import de.jensd.fx.glyphs.weathericons.{ WeatherIcon, WeatherIconView }
import de.jensd.fx.glyphs.{ GlyphIcon, GlyphIcons }
import scalafx.Includes.*
import scalafx.geometry.Pos
import scalafx.scene.Node
import scalafx.scene.control.*
import scalafx.scene.layout.{ BorderPane, GridPane, Region }
import scalafx.scene.text.{ Font, FontPosture, FontWeight, TextAlignment }

class FontAwesomeView(maxContentHeight: Double) extends BorderPane {

  import FontAwesomeView.*

  private val tabPane = new TabPane() {
    tabClosingPolicy = TabPane.TabClosingPolicy.Unavailable
    // Without an explicit cap, the TabPane keeps growing to the selected tab's full (unclipped)
    // content height instead of staying within the window and letting the ScrollPane handle the
    // overflow internally.
    maxHeight = maxContentHeight
    tabs = Seq(
      createTab("FontAwesomeIcon", FontAwesomeIconViews),
      createTab("MaterialDesignIcon", MaterialDesignIconViews),
      createTab("MaterialIcon", MaterialIconViews),
      createTab("OctIcon", OctIconViews),
      createTab("WeatherIcon", WeatherIconViews)
    )
  }

  // TabPaneSkin keeps every tab's content node attached to a shared container and only toggles its
  // visibility (rather than detaching it) when switching tabs. A managed-but-invisible node still
  // contributes to that container's computed preferred size, so if every tab's content stayed built,
  // the tab with the most icons would force all the other (much smaller) tabs' ScrollPanes to be
  // allocated that same oversized viewport, making their content appear to not need scrolling.
  // Building content lazily and discarding it again on deselection keeps at most one tab's content
  // attached at a time, so the TabPane is only ever sized for the currently selected tab.
  loadTabContent(tabPane.tabs.head)
  tabPane.selectionModel().selectedItemProperty().onChange { (_, oldTab, newTab) =>
    if oldTab != null then oldTab.content = null
    if newTab != null then loadTabContent(newTab)
  }

  center = tabPane

  private def loadTabContent(tab: Tab): Unit =
    if tab.content.value == null then tab.content = tab.userData.asInstanceOf[() => Node]()

  private def createTab[T <: Enum[T] & GlyphIcons, V <: GlyphIcon[T]](title: String, views: Iterator[Array[V]]) = {
    // A lazy val, so the (expensive) content is only ever built once, on first selection, and then
    // cached for reuse on subsequent selections rather than rebuilt from scratch every time.
    lazy val tabContent: Node = initializeIcons(views)
    new Tab() {
      text = title
      userData = () => tabContent
    }
  }

  private def initializeIcons[T <: Enum[T] & GlyphIcons, V <: GlyphIcon[T]](
    views: Iterator[Array[V]]
  ) = {
    val gridPane = new GridPane() {
      vgap = VerticalGap
      hgap = HorizontalGap
      alignment = Pos.Center
    }

    views.zipWithIndex.foreach { case (views, rowIndex) =>
      views.zipWithIndex.foreach { case (view, columnIndex) =>
        gridPane.add(createButton(view), columnIndex, rowIndex)
      }
    }

    new BorderPane() {
      center = new ScrollPane() {
        content = gridPane
        vbarPolicy = ScrollPane.ScrollBarPolicy.Always
        hbarPolicy = ScrollPane.ScrollBarPolicy.AsNeeded
        fitToWidth = true
      }
    }
  }

  private def createButton[T <: Enum[T] & GlyphIcons, V <: GlyphIcon[T]](view: V) = {
    val label = new Label() {
      font = ButtonFont
      textAlignment = TextAlignment.Center
      alignment = Pos.Center
      contentDisplay = ContentDisplay.Top
      style = "-fx-background-color: transparent; -fx-border-color: lightgray; -fx-border-width: 1px;"
      minWidth = Region.USE_PREF_SIZE
      maxWidth = Region.USE_PREF_SIZE
      minHeight = Region.USE_PREF_SIZE
      maxHeight = Region.USE_PREF_SIZE
      graphic = view
      text = view.getGlyphName
    }
    label.setPrefSize(250, 80)
    label
  }

}

object FontAwesomeView {
  val NumOfColumns = 5
  val VerticalGap = 5
  val HorizontalGap = 10
  val Size = "2em"
  val ButtonFont: Font = Font("Candara", FontWeight.Normal, FontPosture.Regular, 12.0)

  lazy val FontAwesomeIconViews: Iterator[Array[FontAwesomeIconView]] =
    FontAwesomeIcon
      .values()
      .sorted
      .map { icon =>
        val view = FontAwesomeIconView(icon)
        view.setSize(Size)
        view
      }
      .grouped(NumOfColumns)

  lazy val MaterialDesignIconViews: Iterator[Array[MaterialDesignIconView]] =
    MaterialDesignIcon
      .values()
      .sorted
      .map { icon =>
        val view = MaterialDesignIconView(icon)
        view.setSize(Size)
        view
      }
      .grouped(NumOfColumns)

  lazy val MaterialIconViews: Iterator[Array[MaterialIconView]] =
    MaterialIcon
      .values()
      .sorted
      .map { icon =>
        val view = MaterialIconView(icon)
        view.setSize(Size)
        view
      }
      .grouped(NumOfColumns)

  lazy val OctIconViews: Iterator[Array[OctIconView]] =
    OctIcon
      .values()
      .sorted
      .map { icon =>
        val view = OctIconView(icon)
        view.setSize(Size)
        view
      }
      .grouped(NumOfColumns)

  lazy val WeatherIconViews: Iterator[Array[WeatherIconView]] =
    WeatherIcon
      .values()
      .sorted
      .map { icon =>
        val view = WeatherIconView(icon)
        view.setSize(Size)
        view
      }
      .grouped(NumOfColumns)
}
