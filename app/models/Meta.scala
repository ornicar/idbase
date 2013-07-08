package idbase
package models

case class Meta(
    titre: String, 
    reference: Markdown, 
    url: String, 
    scenario: Markdown, 
    objectifT1: Option[Markdown], 
    objectifT2: Option[Markdown], 
    objectifT3: Option[Markdown], 
    demarches: List[String],
    deroulement: Markdown, 
    tache: String, 
    materiaux: Markdown, 
    evaluations: List[String],
    outilDidactiques: List[String],
    duree: String,
    commentaire: Markdown 
  ) {
}

object Meta {

  import play.api.libs.json.Json
  import play.api.data._
  import play.api.data.Forms._
  import play.api.data.validation._

  implicit val jsonFormat = Json.format[Meta]

  val formMapping = mapping(
    "titre" -> nonEmptyText,
    "reference" -> nonEmptyText,
    "url" -> nonEmptyText,
    "scenario" -> nonEmptyText,
    "objectifT1" -> optional(nonEmptyText),
    "objectifT2" -> optional(nonEmptyText),
    "objectifT3" -> optional(nonEmptyText),
    "demarches" -> nonEmptyList(text),
    "deroulement" -> nonEmptyText,
    "tache" -> nonEmptyText,
    "materiaux" -> nonEmptyText,
    "evaluations" -> nonEmptyList(text),
    "outilDidactiques" -> nonEmptyList(text),
    "duree" -> nonEmptyText,
    "commentaire" -> nonEmptyText
  )(Meta.apply _)(Meta.unapply _)
}
