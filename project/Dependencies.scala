import sbt.*

object Dependencies {

  private lazy val osName = System.getProperty("os.name") match {
    case n if n.startsWith("Linux")   => "linux"
    case n if n.startsWith("Mac")     => "mac"
    case n if n.startsWith("Windows") => "win"
    case _                            => throw new Exception("Unknown platform!")
  }

  object Versions {
    val circe = "0.14.6"
    val controlsFx = "11.1.2"
    val emojione = "2.2.7-2"
    val flyway = "9.3.0"
    val fontAwesome = "4.7.0-5"
    val http4s = "0.23.23"
    val icons525 = "3.0.0-4"
    val jansi = "1.18"
    val jdom = "2.0.6.1"
    val logback = "1.4.5"
    val materialIcons = "2.2.0-5"
    val materialDesignFont = "1.7.22-4"
    val materialStackIcons = "2.1-5"
    val munit = "1.0.0-M10"
    val munitCatsEffect = "1.0.7"
    val nitrite = "3.4.4"
    val octIcons = "4.3.0-5"
    val openFx = "18.0.2"
    val openXmlBuilder = "0.5.5"
    val postgres = "42.7.1"
    val postgresTestContainer = "1.19.3"
    val scaffeine = "5.2.1"
    val scala2 = "2.13.10"
    val scala3 = "3.3.1"
    val scalaFx = "19.0.0-R30"
    val scallop = "5.0.1"
    val slf4j = "2.1.0-alpha1"
    val slick = "3.5.0-M5"
    val typesafeConfig = "1.4.3"
    val weatherIcons = "2.0.10-5"
  }

  val TestDependencies: Seq[ModuleID] = Seq(
    "org.scalameta" %% "munit" % Versions.munit % Test
  )

  val CommonDependencies: Seq[ModuleID] = Seq(
    "io.circe" %% "circe-core" % Versions.circe,
    "io.circe" %% "circe-parser" % Versions.circe,
    "io.circe" %% "circe-generic" % Versions.circe,
    "com.typesafe" % "config" % Versions.typesafeConfig,
    "ch.qos.logback" % "logback-classic" % Versions.logback
  ) ++ TestDependencies

  val ModelsDependencies: Seq[ModuleID] = Seq() ++ CommonDependencies

  val PersistenceDependencies: Seq[ModuleID] =
    Seq(
      "com.github.blemale" %% "scaffeine" % Versions.scaffeine
    )

  val PersistencePostgresDependencies: Seq[ModuleID] =
    Seq(
      "org.postgresql" % "postgresql" % Versions.postgres,
      "com.typesafe.slick" %% "slick" % Versions.slick,
      "com.typesafe.slick" %% "slick-hikaricp" % Versions.slick,
      "org.testcontainers" % "postgresql" % Versions.postgresTestContainer % Test,
      "org.fusesource.jansi" % "jansi" % Versions.jansi % Test,
      "org.flywaydb" % "flyway-core" % Versions.flyway % Test
    ) ++ TestDependencies

  val PersistenceNitriteDependencies: Seq[ModuleID] =
    Seq(
      "org.dizitart" % "nitrite" % Versions.nitrite
    ) ++ TestDependencies

  val CommonUiDependencies: Seq[ModuleID] =
    Seq("base", "controls", "fxml", "graphics", "media", "swing", "web")
      .map(m => "org.openjfx" % s"javafx-$m" % Versions.openFx classifier osName) ++
      Seq(
        "org.scalafx" % "scalafx_3" % Versions.scalaFx,
        "de.jensd" % "fontawesomefx-fontawesome" % Versions.fontAwesome % "provided",
        "de.jensd" % "fontawesomefx-materialdesignfont" % Versions.materialDesignFont % "provided",
        "de.jensd" % "fontawesomefx-materialicons" % Versions.materialIcons % "provided",
        "de.jensd" % "fontawesomefx-octicons" % Versions.octIcons % "provided",
        "de.jensd" % "fontawesomefx-weathericons" % Versions.weatherIcons % "provided",
        "de.jensd" % "fontawesomefx-emojione" % Versions.emojione % "provided",
        "de.jensd" % "fontawesomefx-icons525" % Versions.icons525 % "provided",
        "de.jensd" % "fontawesomefx-materialstackicons" % Versions.materialStackIcons % "provided"
      )

  val DataParserDependencies: Seq[ModuleID] =
    Seq(
      "org.jdom" % "jdom2" % Versions.jdom,
      "org.rogach" %% "scallop" % Versions.scallop
    )

  val MorphologicalAnalysisCommonsUi: Seq[ModuleID] =
    Seq(
      "de.jensd" % "fontawesomefx-fontawesome" % Versions.fontAwesome,
      "de.jensd" % "fontawesomefx-materialicons" % Versions.materialIcons,
      "org.controlsfx" % "controlsfx" % Versions.controlsFx
    )

  val MorphologicalEngineGenerator: Seq[ModuleID] =
    Seq("io.github.sfali23" % "open-xml-builder" % Versions.openXmlBuilder) ++ TestDependencies

  val CliCommons: Seq[ModuleID] =
    Seq(
      "org.rogach" %% "scallop" % Versions.scallop,
      "org.slf4j" % "jul-to-slf4j" % Versions.slf4j
    ) ++ CommonDependencies ++ TestDependencies

  val MorphologicalEngineCli: Seq[ModuleID] = CliCommons

  val TokenEditorDependencies: Seq[ModuleID] =
    Seq()

  val DependencyGraphDependencies: Seq[ModuleID] =
    Seq()

  val MorphologicalEngineUi: Seq[ModuleID] = Seq()

  val MorphologicalEngineServer: Seq[ModuleID] = Seq(
    "org.http4s" %% "http4s-ember-server" % Versions.http4s,
    "org.http4s" %% "http4s-ember-client" % Versions.http4s,
    "org.http4s" %% "http4s-circe" % Versions.http4s,
    "org.http4s" %% "http4s-dsl" % Versions.http4s,
    "org.typelevel" %% "munit-cats-effect-3" % Versions.munitCatsEffect % Test
  ) ++ CommonDependencies
}
