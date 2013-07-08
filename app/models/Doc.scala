package idbase
package models

case class Doc(
    id: String,
    notion: List[String],
    niveau: List[String],
    methodePedagogique: List[String],
    annee: Int,
    disciplineInfodoc: Boolean,
    interdisciplinarite: List[String],
    dispositifPedagogique: String,
    dispositifEducatif: String,
    auteur: String,
    source: String,
    production: Markdown, 
    meta: Meta) {
}

object Doc {

  val idSize = 8

  def identify(doc: Doc) = doc.copy(id = Helper.Random nextString idSize)

  import play.api.libs.json.Json
  import play.api.data._
  import play.api.data.Forms._
  import play.api.data.validation._

  private implicit def metaFormat = Meta.jsonFormat

  implicit val jsonFormat = Json.format[Doc]

  private def nonEmptyList[A](mapping: Mapping[A]): Mapping[List[A]] =
    RepeatedMapping(mapping) verifying Constraint[List[_]]("constraint.required") { o ⇒
      if (o.isEmpty) Invalid(ValidationError("error.required")) else Valid
    }

  val form = Form(
    mapping(
      "id" -> nonEmptyText,
      "notion" -> nonEmptyList(text),
      "niveau" -> nonEmptyList(text),
      "methodePedagogique" -> nonEmptyList(text),
      "annee" -> number.verifying("Année requise", Helper.yearsToNow contains _),
      "disciplineInfodoc" -> boolean,
      "interdisciplinarite" -> nonEmptyList(text),
      "dispositifPedagogique" -> nonEmptyText,
      "dispositifEducatif" -> nonEmptyText,
      "auteur" -> text,
      "source" -> nonEmptyText,
      "production" -> nonEmptyText,
      "meta" -> Meta.formMapping
    )(Doc.apply _)(Doc.unapply _))
}
