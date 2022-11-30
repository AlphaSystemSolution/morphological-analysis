import sbt._

object Dependencies {

  private lazy val osName = System.getProperty("os.name") match {
    case n if n.startsWith("Linux")   => "linux"
    case n if n.startsWith("Mac")     => "mac"
    case n if n.startsWith("Windows") => "win"
    case _                            => throw new Exception("Unknown platform!")
  }

  object V {
    val Circe = "0.14.3"
    val Controlsfx = "11.1.2"
    val Emojione = "2.2.7-2"
    val Flyway = "9.3.0"
    val FontAwsome = "4.7.0-5"
    val Icons525 = "3.0.0-4"
    val Jansi = "1.18"
    val Jdom = "2.0.6.1"
    val Logback = "1.4.1"
    val Materialicons = "2.2.0-5"
    val Materialdesignfont = "1.7.22-4"
    val Materialstackicons = "2.1-5"
    val Munit = "0.7.29"
    val Nitrite = "3.4.4"
    val Octicons = "4.3.0-5"
    val OpenFx = "18.0.2"
    val Postgres = "42.5.0"
    val PostgresTestContainer = "1.17.3"
    val Quill = "4.4.1"
    val Scaffeine = "5.2.1"
    val Scala2 = "2.13.8"
    val Scala3 = "3.2.0"
    val ScalaFx = "18.0.2-R29"
    val TypesafeConfig = "1.4.2"
    val Weathericons = "2.0.10-5"
  }

  val TestDependencies: Seq[ModuleID] = Seq(
    "org.scalameta" %% "munit" % V.Munit % Test
  )

  val ModelsDependencies: Seq[ModuleID] = Seq() ++ TestDependencies

  val CommonDependencies: Seq[ModuleID] = Seq(
    "io.circe" %% "circe-core" % V.Circe,
    "io.circe" %% "circe-parser" % V.Circe,
    "io.circe" %% "circe-generic" % V.Circe,
    "io.getquill" %% "quill-jdbc" % V.Quill,
    "com.typesafe" % "config" % V.TypesafeConfig,
    "ch.qos.logback" % "logback-classic" % V.Logback,
    "com.github.blemale" %% "scaffeine" % V.Scaffeine,
    "org.testcontainers" % "postgresql" % V.PostgresTestContainer % Test,
    "org.fusesource.jansi" % "jansi" % V.Jansi % Test,
    "org.flywaydb" % "flyway-core" % V.Flyway % Test
  ) ++ TestDependencies

  val PersistenceDependencies: Seq[ModuleID] = CommonDependencies ++
    Seq(
      "org.postgresql" % "postgresql" % V.Postgres
    )

  val PersistenceNitriteDependencies: Seq[ModuleID] =
    Seq(
      "org.dizitart" % "nitrite" % V.Nitrite,
      "io.circe" %% "circe-core" % V.Circe,
      "io.circe" %% "circe-parser" % V.Circe,
      "io.circe" %% "circe-generic" % V.Circe,
      "com.typesafe" % "config" % V.TypesafeConfig
    ) ++ TestDependencies

  val CommonUiDependencies: Seq[ModuleID] =
    Seq("base", "controls", "fxml", "graphics", "media", "swing", "web")
      .map(m => "org.openjfx" % s"javafx-$m" % V.OpenFx classifier osName) ++
      Seq(
        "org.scalafx" % "scalafx_3" % V.ScalaFx,
        "de.jensd" % "fontawesomefx-fontawesome" % V.FontAwsome % "provided",
        "de.jensd" % "fontawesomefx-materialdesignfont" % V.Materialdesignfont % "provided",
        "de.jensd" % "fontawesomefx-materialicons" % V.Materialicons % "provided",
        "de.jensd" % "fontawesomefx-octicons" % V.Octicons % "provided",
        "de.jensd" % "fontawesomefx-weathericons" % V.Weathericons % "provided",
        "de.jensd" % "fontawesomefx-emojione" % V.Emojione % "provided",
        "de.jensd" % "fontawesomefx-icons525" % V.Icons525 % "provided",
        "de.jensd" % "fontawesomefx-materialstackicons" % V.Materialstackicons % "provided"
      )

  val DataParserDependencies: Seq[ModuleID] =
    Seq(
      "org.jdom" % "jdom2" % V.Jdom
    )

  val MorphologicalAnalysisCommonsUi: Seq[ModuleID] =
    Seq(
      "de.jensd" % "fontawesomefx-fontawesome" % V.FontAwsome,
      "org.controlsfx" % "controlsfx" % V.Controlsfx
    )

  val TokenEditorDependencies: Seq[ModuleID] =
    Seq()

  val DependencyGraphDependencies: Seq[ModuleID] =
    Seq()
}
