package idbase

import models._
import play.api.data._
import play.api.data.Forms._
import play.api.libs.json._

import scala.concurrent.Future

final class Search(repo: DocRepo) {

  case class Field(
    key: String,
    values: List[String],
    and: Boolean,
    multi: Boolean)

  def apply(setup: Setup, mod: Boolean): Future[List[Doc]] = {
    val parts = List(
      Field("notion", setup.notion, false, true),
      Field("niveau", setup.niveau, false, true),
      Field("methodePedagogique", setup.methodePedagogique, false, true),
      Field("interdisciplinarite", setup.interdisciplinarite filter ("Information-documentation"!=), false, true)
    ) map {
        case Field(_, Nil, _, _) ⇒ Json.obj()
        case Field(key, values, true, _) ⇒
          Json.obj(key -> Json.obj("$all" -> values))
        case Field(key, values, false, _) ⇒
          Json.obj(key -> Json.obj("$in" -> values))
      }
    val req = parts.foldLeft(Json.obj())(_ ++ _)
    setup.texte.fold(repo.find(req, mod)) { texte ⇒
      repo.search(texte, req, mod)
    }
  }

  val form = Form(
    mapping(
      "notion" -> list(text),
      "niveau" -> list(text),
      "methodePedagogique" -> list(text),
      "interdisciplinarite" -> list(text),
      "texte" -> optional(nonEmptyText)
    )(Setup.apply _)(Setup.unapply _))

  case class Setup(
    notion: List[String],
    niveau: List[String],
    methodePedagogique: List[String],
    interdisciplinarite: List[String],
    texte: Option[String])
}
