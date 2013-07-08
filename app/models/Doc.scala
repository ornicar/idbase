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

  import play.api.libs.json.Json

  private implicit def metaFormat = Meta.jsonFormat

  implicit val jsonFormat = Json.format[Doc]
}
