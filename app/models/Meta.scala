package models

case class Meta(
    titre: String,
    reference: String,
    url: String,
    scenario: String,
    objectifT1: String,
    objectifT2: Option[String],
    objectifT3: Option[String],
    demarches: List[String],
    deroulement: String,
    tache: String,
    materiaux: String,
    evaluations: List[String],
    outilDidactiques: List[String],
    duree: String,
    commentaire: String) {
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
    "objectifT1" -> nonEmptyText,
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
