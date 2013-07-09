package idbase

import models._
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json._
import play.modules.reactivemongo._
import play.modules.reactivemongo.json.collection.JSONCollection
import reactivemongo.api._
import scala.concurrent.Future

private[idbase] final class DocRepo(coll: JSONCollection) {

  private implicit def format: OFormat[Doc] = OFormat({
    case o: JsObject ⇒ Doc.jsonTube.fromMongo(o)
    case x           ⇒ throw new Exception("Not a JsObject: " + x)
  },
    doc ⇒ Doc.jsonTube.toMongo(doc).fold(
      e ⇒ throw new Exception(e.toString),
      identity)
  )

  def insert(doc: Doc): Future[Doc] =
    coll insert doc map (_ ⇒ doc)

  def update(doc: Doc): Future[Doc] =
    coll.update(Json.obj("_id" -> doc.id), doc) map (_ ⇒ doc)

  def byId(id: String): Future[Option[Doc]] =
    coll.find(Json.obj("_id" -> id)).one[Doc]

  def list: Future[List[Doc]] =
    coll.find(Json.obj()).cursor[Doc].toList
}
