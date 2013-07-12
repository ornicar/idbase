package idbase

import play.api.libs.json._
import play.modules.reactivemongo._
import play.modules.reactivemongo.json.collection.JSONCollection
import play.modules.reactivemongo.json.ImplicitBSONHandlers._
import reactivemongo.api._
import reactivemongo.bson._
import reactivemongo.core.commands._

import models.Doc

object Mongodb {

  import DocRepo.format

  case class Text(
      collectionName: String,
      terms: String,
      filter: JsObject = Json.obj()) extends Command[List[Doc]] {

    override def makeDocuments = JsObjectWriter write {
      Json.obj(
        "text" -> collectionName,
        "search" -> terms,
        "filter" -> filter)
    }

    val ResultMaker = Text
  }

  object Text extends BSONCommandResultMaker[List[Doc]] {
    def apply(document: BSONDocument) =
      CommandError.checkOk(document, Some("text")) toLeft {
        val results = ((JsObjectReader read document) \ "results")
        val objs = results.asOpt[List[JsObject]] getOrElse Nil
        (objs map { o ⇒ (o \ "obj").asOpt[Doc] }).flatten
      }
  }
}
