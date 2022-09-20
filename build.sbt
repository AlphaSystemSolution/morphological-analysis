import Dependencies._

def commonSettings(project: Project) = project.settings(
  organization := "com.alphasystem.morphologicalanalysis",
  version := "0.1.0-SNAPSHOT",
  scalaVersion := V.Scala3,
  crossScalaVersions := Seq(V.Scala3, V.Scala2),
  testFrameworks += new TestFramework("munit.Framework"),
  scalacOptions ++= Seq(
    "-deprecation", // emit warning and location for usages of deprecated APIs
    "-explain", // explain errors in more detail
    "-explain-types", // explain type errors in more detail
    "-feature", // emit warning and location for usages of features that should be imported explicitly
    "-indent", // allow significant indentation.
    // "-rewrite",
    // "-source",
    // "3.0-migration",
    "-new-syntax", // require `then` and `do` in control expressions.
    "-print-lines", // show source code line numbers.
    "-unchecked", // enable additional warnings where generated code depends on assumptions
    "-Ykind-projector", // allow `*` as wildcard to be compatible with kind projector
    "-Xfatal-warnings", // fail the compilation if there are any warnings
    "-Xmigration" // warn about constructs whose behavior may have changed since version
  )
)

lazy val commons = project
  .in(file("commons"))
  .configure(commonSettings)
  .settings(
    name := "commons"
  )

lazy val models = project
  .in(file("models"))
  .configure(commonSettings)
  .settings(
    name := "models",
    libraryDependencies ++= ModelsDependencies
  )

lazy val `persistence-model` = project
  .in(file("persistence-model"))
  .configure(commonSettings)
  .settings(
    name := "persistence-model",
    libraryDependencies ++= ModelsDependencies
  )
  .dependsOn(models)

lazy val `persistence-svc` = project
  .in(file("persistence-svc"))
  .configure(commonSettings)
  .settings(
    name := "persistence-svc",
    libraryDependencies ++= PersistenceDependencies
  )
  .dependsOn(`persistence-model`)

lazy val `fx-support` = project
  .in(file("fx-support"))
  .configure(commonSettings)
  .settings(
    name := "fx-support",
    libraryDependencies ++= CommonUiDependencies
  )
  .dependsOn(commons)

lazy val root = project
  .in(file("."))
  .configure(commonSettings)
  .settings(
    name := "morphological-analysis"
  )
  .aggregate(
    commons,
    models,
    `persistence-model`,
    `persistence-svc`,
    `fx-support`
  )
