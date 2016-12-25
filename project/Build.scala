import com.typesafe.sbt.web.SbtWeb.autoImport._
import play.Play.autoImport._
import play.sbt.PlayImport._
import play.sbt.routes.RoutesKeys._
import play.twirl.sbt.Import._
import PlayKeys._
import sbt._, Keys._

object ApplicationBuild extends Build {

  val appName = "idbase"
  val appVersion = "1.1"

  val appDependencies = Seq(
    cache,
    ws,
    "org.reactivemongo" %% "reactivemongo" % "0.11.14",
    "org.reactivemongo" %% "play2-reactivemongo" % s"0.11.14-play24",
    "org.pegdown" % "pegdown" % "1.6.0",
    "jp.t2v" %% "play2-auth" % "0.14.1",
    "default" % "ssu_2.10" % "0.1-SNAPSHOT"
  )

  val main = Project(appName, file(".")) enablePlugins _root_.play.sbt.PlayScala settings (
    scalaVersion := "2.11.8",
    resolvers ++= Seq(
      "Sonatype Snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/",
      "iliaz.com" at "http://scala.iliaz.com/"
    ),
      libraryDependencies ++= appDependencies,
      sources in doc in Compile := List(),
      scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature", "-language:_"),
      TwirlKeys.templateImports ++= Seq("idbase.models.{Doc => D,_}"),
      routesGenerator := InjectedRoutesGenerator
  )

}
