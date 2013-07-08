package idbase

import play.api.libs.functional.syntax._
import play.api.libs.json._
import play.modules.reactivemongo.json.ImplicitBSONHandlers._
import reactivemongo.bson._
import Reads.constraints._

case class Tube[Doc](
  reader: Reads[Doc],
  writer: Writes[Doc])
    extends Reads[Doc]
    with Writes[Doc]
    with BSONDocumentReader[Option[Doc]] {

  implicit def reads(js: JsValue): JsResult[Doc] = reader reads js
  implicit def writes(doc: Doc): JsValue = writer writes doc

  def read(bson: BSONDocument): Option[Doc] = {
    val js = JsObjectReader read bson
    fromMongo(js) match {
      case JsSuccess(v, _) ⇒ Some(v)
      case e ⇒ {
        println("[tube] Cannot read %s\n%s".format(js, e))
        None
      }
    }
  }

  def read(js: JsObject): JsResult[Doc] = reads(js)

  def write(doc: Doc): JsResult[JsObject] = writes(doc) match {
    case obj: JsObject ⇒ JsSuccess(obj)
    case something ⇒ {
      println("[tube] Cannot write %s\ngot %s".format(doc, something))
      JsError()
    }
  }

  def toMongo(doc: Doc): JsResult[JsObject] = 
    write(doc) flatMap Tube.toMongoId

  def fromMongo(js: JsObject): JsResult[Doc] = 
    Tube.depath(Tube fromMongoId js) flatMap read
}

object Tube {

  val json = Tube[JsObject](
    __.read[JsObject],
    __.write[JsObject])

  def toMongoId(js: JsValue): JsResult[JsObject] =
    js transform Helpers.rename('id, '_id)

  def fromMongoId(js: JsValue): JsResult[JsObject] =
    js transform Helpers.rename('_id, 'id)

  object Helpers {

    // Adds Writes[A].andThen combinator, symmetric to Reads[A].andThen
    // Explodes on failure
    implicit final class LilaTubePimpedWrites[A](writes: Writes[A]) {
      def andThen(transformer: Reads[JsObject]): Writes[A] =
        writes.transform(Writes[JsValue] { origin ⇒
          origin transform transformer match {
            case err: JsError     ⇒ throw new Exception("[tube] Cannot transform %s\n%s".format(origin, err))
            case JsSuccess(js, _) ⇒ js
          }
        })
    }

    def rename(from: Symbol, to: Symbol) = __.json update (
      (__ \ to).json copyFrom (__ \ from).json.pick
    ) andThen (__ \ from).json.prune

    def readDate(field: Symbol) =
      (__ \ field).json.update(of[JsObject] map (_ \ "$date"))

    def readDateOpt(field: Symbol) = readDate(field) orElse json.reader

    def writeDate(field: Symbol) = (__ \ field).json.update(of[JsNumber] map {
      millis ⇒ Json.obj("$date" -> millis)
    })

    def writeDateOpt(field: Symbol) = (__ \ field).json.update(of[JsNumber] map {
      millis ⇒ Json.obj("$date" -> millis)
    }) orElse json.reader

    def merge(obj: JsObject) = __.read[JsObject] map (obj ++)
  }

  private def depath[A](r: JsResult[A]): JsResult[A] = r.flatMap(JsSuccess(_))
}

