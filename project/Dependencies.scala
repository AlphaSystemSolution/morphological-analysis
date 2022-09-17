import sbt._

object Dependencies {

  object V {
    val Munit = "0.7.29"
  }

  val TestDependencies: Seq[ModuleID] = Seq(
    "org.scalameta" %% "munit" % V.Munit % Test
  )

  val ModelsDependencies: Seq[ModuleID] = Seq() ++ TestDependencies

  val PersistenceDependencies: Seq[ModuleID] = Seq() ++ TestDependencies
}
