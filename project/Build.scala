import play.Project._
import sbt._, Keys._

object ApplicationBuild extends Build {

  val appName = "idbase"
  val appVersion = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
    "org.reactivemongo" %% "reactivemongo" % "0.10.1-THIB",
    "org.reactivemongo" %% "play2-reactivemongo" % "0.10-THIB",
    "org.pegdown" % "pegdown" % "1.4.0"
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    scalaVersion := "2.10.2",
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
