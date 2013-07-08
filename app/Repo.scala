package idbase

import models._
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json._
import play.modules.reactivemongo._
import play.modules.reactivemongo.json.collection.JSONCollection
import reactivemongo.api._
import scala.concurrent.Future

private[idbase] final class Repo(coll: JSONCollection) {

  import Doc.jsonFormat

  def insert(doc: Doc): Future[Unit] = {
    val identified = Doc identify doc
    coll insert identified map (_ ⇒ ())
  }

  def byId(id: String): Future[Option[Doc]] = 
    coll.find(Json.obj("_id" -> id)).one
}
