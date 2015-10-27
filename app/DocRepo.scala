package idbase

import models._
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json._
import play.modules.reactivemongo._
import play.modules.reactivemongo.json._
import play.modules.reactivemongo.json.collection.JSONCollection
import reactivemongo.api._
import reactivemongo.core.commands.Count
import scala.concurrent.Future

private[idbase] final class DocRepo(db: DB, val collName: String) {

  import DocRepo.format

  private val coll: JSONCollection = db.collection[JSONCollection](collName)

  private def selectMod(mod: Boolean) =
    if (mod) Json.obj() else Json.obj("public" -> true)

  def search(terms: String, filter: JsObject, mod: Boolean): Future[List[Doc]] = {
    coll.find(filter ++ selectMod(mod) ++ Json.obj(
      "$text" -> Json.obj("$search" -> terms)
    )).cursor[Doc]().collect[List]()
  }

  def notions(mod: Boolean): Future[List[String]] =
    distinctProjection("notion", mod)

  def methodePedagogiques(mod: Boolean): Future[List[String]] =
    distinctProjection("methodePedagogique", mod)

  def disciplines(mod: Boolean): Future[List[String]] =
    distinctProjection("interdisciplinarite", mod)

  private def distinctProjection(field: String, mod: Boolean): Future[List[String]] =
    coll.find(selectMod(mod), Json.obj("_id" -> false, field -> true)).cursor[JsValue]().collect[List]() map { res ⇒
      (res collect {
        case JsObject(fields) ⇒ (fields collect {
          case (field, v) ⇒ v.asOpt[List[String]]
        }).headOption.flatten
      }).flatten.flatten.distinct.sorted
    }

  def count(mod: Boolean): Future[Int] = coll count Some(selectMod(mod))

  def insert(doc: Doc): Future[Doc] =
    coll insert doc map (_ ⇒ doc)

  def update(doc: Doc): Future[Doc] =
    coll.update(Json.obj("_id" -> doc.id), doc) map (_ ⇒ doc)

  def delete(doc: Doc): Future[Unit] =
    coll.remove(Json.obj("_id" -> doc.id)) map (_ ⇒ ())

  def byId(id: String, mod: Boolean): Future[Option[Doc]] =
    coll.find(Json.obj("_id" -> id) ++ selectMod(mod)).one[Doc]

  def list(mod: Boolean): Future[List[Doc]] = find(Json.obj(), mod)

  def find(query: JsObject, mod: Boolean): Future[List[Doc]] =
    coll.find(query ++ selectMod(mod)).sort(Json.obj("$natural" -> -1)).cursor[Doc]().collect[List]()
}

object DocRepo {

  implicit def format: OFormat[Doc] = OFormat({
    case o: JsObject ⇒ Doc.jsonTube.fromMongo(o)
    case x           ⇒ throw new Exception("Not a JsObject: " + x)
  },
    doc ⇒ Doc.jsonTube.toMongo(doc).fold(
      e ⇒ throw new Exception(e.toString),
      identity)
  )
}
