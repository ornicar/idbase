package idbase

import com.typesafe.config.Config
import play.api.{ Application, Play }
import play.modules.reactivemongo.json.collection.JSONCollection
import play.modules.reactivemongo.ReactiveMongoPlugin
import reactivemongo.api._
import scala.collection.JavaConversions._
import scala.concurrent.ExecutionContext.Implicits.global

final class Env(config: Config)(implicit app: Application) {

  lazy val lists = new models.Lists(config getConfig "idbase")

  lazy val search = new Search(docRepo)

  lazy val userRepo = new UserRepo(
    idbaseConfig.getStringList("users").toList
  )

  lazy val db = {
    import scala.util.{ Success, Failure }
    val parsedUri: MongoConnection.ParsedURI =
      MongoConnection.parseURI(config.getString("mongodb.uri")) match {
        case Success(parsedURI) => parsedURI
        case Failure(e)         => sys error s"Invalid mongodb.uri"
      }
    val driver = new MongoDriver(Some(config))
    val connection = driver.connection(parsedUri)

    parsedUri.db.fold[DefaultDB](sys error s"cannot resolve database from URI: $parsedUri") { dbUri =>
      val db = DB(dbUri, connection)
      registerDriverShutdownHook(driver)
      println(s"""ReactiveMongoApi successfully started with DB '$dbUri'! Servers:\n\t\t${parsedUri.hosts.map { s => s"[${s._1}:${s._2}]" }.mkString("\n\t\t")}""")
      db
    }
  }

  private def lifecycle = 
    Play.maybeApplication map { 
      _.injector.instanceOf[play.api.inject.ApplicationLifecycle]
    } getOrElse sys.error("Play application is not started!")

  private def registerDriverShutdownHook(mongoDriver: MongoDriver): Unit =
    lifecycle.addStopHook { () => scala.concurrent.Future(mongoDriver.close()) }

  lazy val docRepo = new DocRepo(
    db = db,
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

