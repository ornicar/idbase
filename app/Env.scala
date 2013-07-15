package idbase

import com.typesafe.config.Config
import play.api.{ Application, Play }
import play.modules.reactivemongo.json.collection.JSONCollection
import play.modules.reactivemongo.ReactiveMongoPlugin
import scala.collection.JavaConversions._

final class Env(config: Config)(implicit app: Application) {

  lazy val lists = new models.Lists(config getConfig "idbase")

  lazy val search = new Search(docRepo)

  lazy val userRepo = new UserRepo(
    idbaseConfig.getStringList("users").toList
  )

  lazy val docRepo = new DocRepo(
    db = ReactiveMongoPlugin.db,
    collName = idbaseConfig getString "doc_collection"
  )

  private lazy val idbaseConfig = config getConfig "idbase"

  lazy val aboutText = {
    val file = play.api.Play.getFile("conf/apropos.md")
    val src = scala.io.Source fromFile file
    src.getLines mkString "\n"
  }
}

object Env {

  private implicit def inApp = withApp(identity)

  lazy val current = new Env(appConfig)

  private def appConfig: Config = withApp(_.configuration.underlying)

  private def withApp[A](op: Application ⇒ A): A =
    Play.maybeApplication.map(op).get
}

