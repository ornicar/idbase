import play.Project._
import sbt._, Keys._

object ApplicationBuild extends Build {

  val appName = "idbase"
  val appVersion = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
    "org.reactivemongo" %% "reactivemongo" % "0.10.0",
    "org.reactivemongo" %% "play2-reactivemongo" % "0.10.2",
    "org.pegdown" % "pegdown" % "1.4.2",
    "jp.t2v" %% "play2-auth" % "0.11.0",
    "default" % "ssu_2.10" % "0.1-SNAPSHOT"
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    scalaVersion := "2.10.3",
    resolvers ++= Seq(
      "Sonatype Snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/",
      "iliaz.com" at "http://scala.iliaz.com/"
    ),
    sources in doc in Compile := List(),
    scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature", "-language:_"),
    templatesImport ++= Seq(
      "idbase.models._")
  // Add your own project settings here
  )

}
