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

class FontAwesomeView extends BorderPane {

  import FontAwesomeView.*

  private val gridPane = new GridPane() {
    vgap = VerticalGap
    hgap = HorizontalGap
    alignment = Pos.Center
  }

  left = mainPane()
  center = gridPane

  private def mainPane() = {
    val gridPane = new GridPane() {
      vgap = VerticalGap
      hgap = HorizontalGap
      alignment = Pos.Center
    }

    val names = IconType.values.toSeq
    val comboBox = new ComboBox[IconType](names) {
      value = names.head
    }
    loadIcons(FontAwesomeIconViews)
    comboBox
      .valueProperty()
      .onChange((_, _, nv) => {
        nv match
          case IconType.FontAwesomeIcon    => loadIcons(FontAwesomeIconViews)
          case IconType.MaterialDesignIcon => loadIcons(MaterialDesignIconViews)
          case IconType.MaterialIcon       => loadIcons(MaterialIconViews)
          case IconType.OctIcon            => loadIcons(OctIconViews)
          case IconType.WeatherIcon        => loadIcons(WeatherIconViews)
      })
    gridPane.add(comboBox, 1, 0, 2, 1)
    gridPane
  }

  private def loadIcons[T <: Enum[T] with GlyphIcons, V <: GlyphIcon[T]](views: Array[V]): Unit = {
    gridPane.children.clear()
    val values = views.map { view => view.getGlyphName -> view }.toMap
    val names = values.keys.toSeq.sorted
    val nameComoBox = new ComboBox[String](names) {
      value = names.head
    }
    nameComoBox
      .valueProperty()
      .onChange((_, _, nv) => {
        gridPane.children.remove(1)
        val view = createButton(values(nv))
        gridPane.add(view, 4, 0, 4, 1)
      })
    gridPane.add(nameComoBox, 2, 0, 2, 1)

    val view = createButton(values(names.head))
    gridPane.add(view, 4, 0, 4, 1)
  }

  private def createButton[T <: Enum[T] with GlyphIcons, V <: GlyphIcon[T]](view: V) = {
    val label = new Label() {
      font = ButtonFont
      textAlignment = TextAlignment.Center
      alignment = Pos.Center
      contentDisplay = ContentDisplay.Top
      style = "-fx-background-color: transparent; -fx-border-color: magenta; -fx-border-width: 1px;"
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

  enum IconType {
    case FontAwesomeIcon extends IconType
    case MaterialDesignIcon extends IconType
    case MaterialIcon extends IconType
    case OctIcon extends IconType
    case WeatherIcon extends IconType
  }

  lazy val FontAwesomeIconViews: Array[FontAwesomeIconView] =
    FontAwesomeIcon
      .values()
      .sorted
      .map { icon =>
        val view = FontAwesomeIconView(icon)
        view.setSize(Size)
        view
      }

  lazy val MaterialDesignIconViews: Array[MaterialDesignIconView] =
    MaterialDesignIcon
      .values()
      .sorted
      .map { icon =>
        val view = MaterialDesignIconView(icon)
        view.setSize(Size)
        view
      }

  lazy val MaterialIconViews: Array[MaterialIconView] =
    MaterialIcon
      .values()
      .sorted
      .map { icon =>
        val view = MaterialIconView(icon)
        view.setSize(Size)
        view
      }

  lazy val OctIconViews: Array[OctIconView] =
    OctIcon
      .values()
      .sorted
      .map { icon =>
        val view = OctIconView(icon)
        view.setSize(Size)
        view
      }

  lazy val WeatherIconViews: Array[WeatherIconView] =
    WeatherIcon
      .values()
      .sorted
      .map { icon =>
        val view = WeatherIconView(icon)
        view.setSize(Size)
        view
      }
}
