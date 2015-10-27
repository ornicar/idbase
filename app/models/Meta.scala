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
    tache: Markdown, 
    materiau: Option[Markdown], 
    evaluation: List[String],
    outilEvaluation: Option[Markdown],
    outilDidactique: Option[Markdown],
    ficheEleve: Option[Markdown],
    duree: String,
    commentaire: Markdown,
    retour: Option[Markdown],
    ressource: Option[Markdown]) {
}

object Meta {

  import play.api.libs.json.Json

  implicit val jsonFormat = Json.format[Meta]
}
