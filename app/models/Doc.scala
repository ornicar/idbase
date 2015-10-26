package idbase
package models

case class Doc(
    id: String,
    notion: List[String],
    niveau: List[String],
    methodePedagogique: List[String],
    annee: Int,
    interdisciplinarite: List[String],
    dispositifPedagogique: Option[Markdown],
    dispositifEducatif: Option[Markdown],
    source: String,
    production: Option[Markdown],
    meta: Meta) {

  def hasNotionAndDiscipline(n: String) =
    interdisciplinarite.nonEmpty && notion.contains(n)

  def hasNotionAndNiveau(n: String) =
    niveau.nonEmpty && notion.contains(n)

  def slug = Helper slugify meta.titre
}

object Doc {

  val idSize = 8

  import play.api.libs.json._

  private implicit def metaFormat = Meta.jsonFormat

  implicit val jsonTube = Tube(Json.reads[Doc], Json.writes[Doc])

  def rename(from: Symbol, to: Symbol) = __.json update (
    (__ \ to).json copyFrom (__ \ from).json.pick
  ) andThen (__ \ from).json.prune
}
