import Dependencies._

val scala2Version = "2.13.8"
val scala3Version = "3.2.0"

def commonSettings(project: Project) = project.settings(
  organization := "com.alphasystem",
  version := "0.1.0-SNAPSHOT",
  scalaVersion := scala3Version,
  crossScalaVersions := Seq(scala3Version, scala2Version),
  testFrameworks += new TestFramework("munit.Framework"),
  scalacOptions ++= Seq(
    "-deprecation", // emit warning and location for usages of deprecated APIs
    "-explain", // explain errors in more detail
    "-explain-types", // explain type errors in more detail
    "-feature", // emit warning and location for usages of features that should be imported explicitly
    "-indent", // allow significant indentation.
    "-new-syntax", // require `then` and `do` in control expressions.
    "-print-lines", // show source code line numbers.
    "-unchecked", // enable additional warnings where generated code depends on assumptions
    "-Ykind-projector", // allow `*` as wildcard to be compatible with kind projector
    "-Xfatal-warnings", // fail the compilation if there are any warnings
    "-Xmigration" // warn about constructs whose behavior may have changed since version
  )
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

lazy val root = project
  .in(file("."))
  .configure(commonSettings)
  .settings(
    name := "morphological-analysis"
  )
  .aggregate(models, `persistence-model`)
