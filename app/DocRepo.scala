package idbase

import models._
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json._
import play.modules.reactivemongo._
import play.modules.reactivemongo.json.collection.JSONCollection
import reactivemongo.api._
import reactivemongo.core.commands.Count
import scala.concurrent.Future

private[idbase] final class DocRepo(db: DB, collName: String) {

  private val coll: JSONCollection = db.collection[JSONCollection](collName)

  private implicit def format: OFormat[Doc] = OFormat({
    case o: JsObject ⇒ Doc.jsonTube.fromMongo(o)
    case x           ⇒ throw new Exception("Not a JsObject: " + x)
  },
    doc ⇒ Doc.jsonTube.toMongo(doc).fold(
      e ⇒ throw new Exception(e.toString),
      identity)
  )

  def notions: Future[List[String]] =
    coll.find(Json.obj(), Json.obj("notion" -> true)).cursor[JsValue].toList map { res ⇒
      (res collect {
        case JsObject(fields) ⇒ (fields collect {
          case ("notion", v) ⇒ v.asOpt[List[String]]
        }).headOption.flatten
      }).flatten.flatten.distinct
    }

  def count: Future[Int] = db.command(Count(collName))

  def insert(doc: Doc): Future[Doc] =
    coll insert doc map (_ ⇒ doc)

  def update(doc: Doc): Future[Doc] =
    coll.update(Json.obj("_id" -> doc.id), doc) map (_ ⇒ doc)

  def delete(doc: Doc): Future[Unit] =
    coll.remove(Json.obj("_id" -> doc.id)) map (_ ⇒ ())

  def byId(id: String): Future[Option[Doc]] =
    coll.find(Json.obj("_id" -> id)).one[Doc]

  def list: Future[List[Doc]] = find(Json.obj())

  def find(query: JsObject): Future[List[Doc]] =
    coll.find(query).cursor[Doc].toList
}
