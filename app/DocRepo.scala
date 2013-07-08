package idbase

import models._
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json._
import play.modules.reactivemongo._
import play.modules.reactivemongo.json.collection.JSONCollection
import reactivemongo.api._
import scala.concurrent.Future

private[idbase] final class DocRepo(coll: JSONCollection) {

  def insert(doc: Doc): Future[Doc] =
    Doc.jsonTube.toMongo(doc).fold(
      e ⇒ Future.failed(new Exception(e.toString)),
      coll.insert
    ) map (_ ⇒ doc)

  def byId(id: String): Future[Option[Doc]] =
    coll.find(Json.obj("_id" -> id)).one[JsObject] map {
      _ flatMap { js ⇒
        Doc.jsonTube.fromMongo(js) match {
          case JsSuccess(v, _) ⇒ Some(v)
          case e ⇒ {
            println("[tube] Cannot read %s\n%s".format(js, e))
            None
          }
        }
      }
    }
}
