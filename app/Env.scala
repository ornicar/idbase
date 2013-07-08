package idbase

import com.typesafe.config.Config
import play.api.{ Application, Play }
import play.modules.reactivemongo.ReactiveMongoPlugin
import play.modules.reactivemongo.json.collection.JSONCollection

final class Env(config: Config)(implicit app: Application) {

  lazy val lists = new models.Lists(config getConfig "idbase")

  lazy val docRepo = new DocRepo(
    coll = ReactiveMongoPlugin.db.collection[JSONCollection](
      config getConfig "idbase" getString "doc_collection"
    )
  )
}

object Env {

  private implicit def inApp = withApp(identity)

  lazy val current = new Env(appConfig)

  private def appConfig: Config = withApp(_.configuration.underlying)

  private def withApp[A](op: Application ⇒ A): A =
    Play.maybeApplication.map(op).get
}

