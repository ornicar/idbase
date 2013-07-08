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
    demarche: List[String],
    deroulement: Markdown, 
    tache: String, 
    materiau: Markdown, 
    evaluation: List[String],
    outilDidactique: String,
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
    "demarche" -> nonEmptyList(text),
    "deroulement" -> nonEmptyText,
    "tache" -> nonEmptyText,
    "materiau" -> nonEmptyText,
    "evaluation" -> nonEmptyList(text),
    "outilDidactique" -> nonEmptyText,
    "duree" -> nonEmptyText,
    "commentaire" -> nonEmptyText
  )(Meta.apply _)(Meta.unapply _)
}
