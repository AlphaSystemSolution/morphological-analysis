import Dependencies._

def configureBuildInfo(project: Project) = project
  .enablePlugins(BuildInfoPlugin)
  .settings(
    buildInfoPackage := s"${organization.value}.${normalizedName.value.replace('-', '_')}",
    buildInfoKeys := Seq[BuildInfoKey](
      name,
      normalizedName,
      description,
      homepage,
      startYear,
      organization,
      organizationName,
      version,
      scalaVersion,
      sbtVersion,
      Compile / allDependencies
    )
  )

def commonSettings(project: Project): Project = project
  .settings(
    organization := "com.alphasystem.arabic",
    version := "0.1.0-SNAPSHOT",
    scalaVersion := V.Scala3,
    // crossScalaVersions := Seq(V.Scala3, V.Scala2),
    testFrameworks += new TestFramework("munit.Framework"),
    resolvers += Resolver.mavenLocal,
    onChangedBuildSource in Global := ReloadOnSourceChanges,
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
      "-Xmigration", // warn about constructs whose behavior may have changed since version
      "-Xmax-inlines",
      "512"
    )
  )
  .configure(configureBuildInfo)
  .enablePlugins(ScalafmtPlugin)

def postgresFlywayMigrations(
  schemaName: String,
  migrationLocations: Seq[String] = Seq("filesystem:./flyway"),
  placeholders: Map[String, String] = Map.empty
)(project: Project
): Project = {
  val deployment_environment =
    sys.props.getOrElse("env", sys.env.getOrElse("K8S_ENV", "docker"))
  val password =
    sys.props.getOrElse("password", sys.env.getOrElse("POSTGRES_PASSWORD", ""))
  val username = sys
    .props
    .getOrElse("postgres_user", sys.env.getOrElse("POSTGRES_USER", "postgres"))
  val database = sys
    .props
    .getOrElse(
      "postgres_database",
      sys.env.getOrElse("POSTGRES_DATABASE", "postgres")
    )
  val port = sys
    .props
    .getOrElse("postgres_port", sys.env.getOrElse("POSTGRES_PORT", "5432"))
    .toInt
  val server = sys
    .props
    .getOrElse(
      "postgres_host",
      sys.env.getOrElse("POSTGRES_SERVER_HOST", "localhost")
    )

  val schema = schemaName

  println(
    s"Running migration from $migrationLocations against $schema in $server:$port/$database as role $username"
  )

  project
    .enablePlugins(FlywayPlugin)
    .settings(
      flywayUrl := s"jdbc:postgresql://$server:$port/$database",
      flywayUser := username,
      flywayLocations := migrationLocations,
      flywaySchemas := Seq(schema),
      flywayPassword := password,
      flywayPlaceholders := Map("schema" -> schema) ++ placeholders
    )
}

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
    name := "persistence-model"
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

/*lazy val `persistence-svc-postgres` = project
  .in(file("persistence-svc-postgres"))
  .configure(commonSettings)
  .enablePlugins(FlywayPlugin)
  .configure(
    postgresFlywayMigrations(
      schemaName = "morphological_analysis"
    )
  )
  .settings(
    name := "persistence-svc-postgres",
    libraryDependencies ++= PersistencePostgresDependencies
  )
  .dependsOn(`persistence-model`)*/

lazy val `persistence-svc-nitrite` = project
  .in(file("persistence-svc-nitrite"))
  .configure(commonSettings)
  .settings(
    name := "persistence-svc-nitrite",
    libraryDependencies ++= PersistenceNitriteDependencies
  )
  .dependsOn(`persistence-svc`)

lazy val `fx-support` = project
  .in(file("fx-support"))
  .configure(commonSettings)
  .settings(
    name := "fx-support",
    libraryDependencies ++= CommonUiDependencies
  )
  .dependsOn(commons, models)

lazy val `morphological-analysis-commons-ui` = project
  .in(file("morphological-analysis-commons-ui"))
  .configure(commonSettings)
  .settings(
    name := "morphological-analysis-commons-ui",
    libraryDependencies ++= MorphologicalAnalysisCommonsUi
  )
  .dependsOn(`fx-support`, `persistence-svc-nitrite`)

lazy val `token-editor` = project
  .in(file("token-editor"))
  .configure(commonSettings)
  .settings(
    name := "token-editor",
    libraryDependencies ++= TokenEditorDependencies
  )
  .dependsOn(`morphological-analysis-commons-ui`)

lazy val `dependency-graph` = project
  .in(file("dependency-graph"))
  .configure(commonSettings)
  .settings(
    name := "dependency-graph",
    libraryDependencies ++= DependencyGraphDependencies
  )
  .dependsOn(`morphological-analysis-commons-ui`)

lazy val `data-parser` = project
  .in(file("data-parser"))
  .configure(commonSettings)
  .settings(
    name := "data-parser",
    libraryDependencies ++= DataParserDependencies
  )
  .dependsOn(`persistence-svc-nitrite`)

lazy val `morphological-engine` = project
  .in(file("morphological-engine"))
  .configure(commonSettings)
  .settings(
    name := "morphological-engine",
    libraryDependencies ++= TestDependencies
  )
  .dependsOn(commons, models)

lazy val `morphological-engine-generator` = project
  .in(file("morphological-engine-generator"))
  .configure(commonSettings)
  .settings(
    name := "morphological-engine-generator",
    libraryDependencies ++= MorphologicalEngineGenerator
  )
  .dependsOn(`morphological-engine`)

lazy val `morphological-engine-cli` = project
  .in(file("morphological-engine-cli"))
  .configure(commonSettings)
  .settings(
    name := "morphological-engine-cli",
    libraryDependencies ++= MorphologicalEngineCli,
    buildInfoPackage := organization.value + ".morphologicalengine.cli",
    assembly / assemblyJarName := "morphological-engine-cli.jar",
    ThisBuild / assemblyMergeStrategy := {
      case PathList("META-INF", "versions", xs @ _*)       => MergeStrategy.discard
      case PathList("reference.conf")                      => MergeStrategy.concat
      case "META-INF/io.netty.versions.properties"         => MergeStrategy.first
      case PathList("logback.xml")                         => MergeStrategy.last
      case "org/docx4j/fonts/microsoft/MicrosoftFonts.xml" => MergeStrategy.last
      case "version.conf"                                  => MergeStrategy.concat
      case "module-info.class"                             => MergeStrategy.discard
      case "validate/ScalapbOptions.class"                 => MergeStrategy.first
      // case x                                              => MergeStrategy.first
      case x =>
        val oldStrategy = (ThisBuild / assemblyMergeStrategy).value
        oldStrategy(x)
    },
    assembly / artifact := {
      val art = (assembly / artifact).value
      art.withClassifier(Some("assembly"))
    }, // add assembly-jar an artifact
    addArtifact(
      assembly / artifact,
      assembly
    ) // include assembly-jar in list of artifacts, to publish it automatically
  )
  .dependsOn(`morphological-engine-generator`)

lazy val `morphological-engine-common-ui` = project
  .in(file("morphological-engine-common-ui"))
  .configure(commonSettings)
  .settings(
    name := "morphological-engine-ui",
    buildInfoPackage := organization.value + ".morphologicalengine.common.ui"
  )
  .dependsOn(`fx-support`, `morphological-engine`)

lazy val `morphological-engine-ui` = project
  .in(file("morphological-engine-ui"))
  .configure(commonSettings)
  .settings(
    name := "morphological-engine-ui",
    buildInfoPackage := organization.value + ".morphologicalengine.ui",
    libraryDependencies ++= MorphologicalEngineUi
  )
  .dependsOn(`morphological-engine-common-ui`, `morphological-engine-generator`)

lazy val root = project
  .in(file("."))
  .configure(commonSettings)
  .settings(
    name := "morphological-analysis"
  )
  .aggregate(
    `data-parser`,
    commons,
    models,
    `persistence-model`,
    `persistence-svc`,
    `persistence-svc-nitrite`,
    `fx-support`,
    `morphological-analysis-commons-ui`,
    `token-editor`,
    `dependency-graph`,
    `morphological-engine`,
    `morphological-engine-generator`,
    `morphological-engine-cli`,
    `morphological-engine-common-ui`,
    `morphological-engine-ui`
  )

addCommandAlias("mec-assembly", "morphological-engine-cli / clean; morphological-engine-cli / assembly")
