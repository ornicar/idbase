package idbase 

import com.typesafe.config.Config
import play.api.{ Application, Play }

final class Env(config: Config) {

  lazy val lists = new models.Lists(config getConfig "idbase")
}

object Env {

  lazy val current = new Env(config = appConfig)

  private def appConfig: Config = withApp(_.configuration.underlying)

  private def withApp[A](op: Application ⇒ A): A =
    Play.maybeApplication.map(op).get
}

