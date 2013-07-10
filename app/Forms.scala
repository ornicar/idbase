package idbase

import models._
import play.api.data._
import play.api.data.Forms._
import play.api.data.validation._

object Forms {

  private def nonEmptyList[A](mapping: Mapping[A]): Mapping[List[A]] =
    RepeatedMapping(mapping) verifying Constraint[List[_]]("constraint.required") { o ⇒
      if (o.isEmpty) Invalid(ValidationError("error.required")) else Valid
    }

  lazy val docForm = Form(
    mapping(
      "notion" -> text,
      "hidden-notion" -> text,
      "niveau" -> nonEmptyList(text),
      "methodePedagogique" -> nonEmptyList(text),
      "annee" -> number.verifying("Année requise", Helper.yearsToNow contains _),
      "interdisciplinarite" -> list(text),
      "dispositifPedagogique" -> optional(nonEmptyText),
      "dispositifEducatif" -> optional(nonEmptyText),
      "source" -> nonEmptyText,
      "production" -> nonEmptyText,
      "meta" -> metaMapping
    )(DocSetup.apply _)(DocSetup.unapply _))

  lazy val metaMapping = mapping(
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
    "outilEvaluation" -> optional(nonEmptyText),
    "outilDidactique" -> optional(nonEmptyText),
    "ficheEleve" -> optional(nonEmptyText),
    "duree" -> nonEmptyText,
    "commentaire" -> nonEmptyText
  )(MetaSetup.apply _)(MetaSetup.unapply _)

  case class DocSetup(
      notion: String,
      `hidden-notion`: String,
      niveau: List[String],
      methodePedagogique: List[String],
      annee: Int,
      interdisciplinarite: List[String],
      dispositifPedagogique: Option[Markdown],
      dispositifEducatif: Option[Markdown],
      source: String,
      production: Markdown,
      meta: MetaSetup) {

    def toDoc(doc: Option[Doc]) = Doc(
      id = doc.fold(Helper.Random nextString Doc.idSize)(_.id),
      notion = `hidden-notion`.split(',').toList.map(_.trim).filter(_.nonEmpty).distinct,
      niveau = niveau,
      methodePedagogique = methodePedagogique,
      annee = annee,
      interdisciplinarite = interdisciplinarite,
      dispositifPedagogique = dispositifPedagogique,
      dispositifEducatif = dispositifEducatif,
      source = source,
      production = production,
      meta = meta.toMeta)
  }

  def DocToSetup(doc: Doc) = DocSetup(
      notion = doc.notion mkString ",",
      `hidden-notion` = "",
      niveau = doc.niveau,
      methodePedagogique = doc.methodePedagogique,
      annee = doc.annee,
      interdisciplinarite = doc.interdisciplinarite,
      dispositifPedagogique = doc.dispositifPedagogique,
      dispositifEducatif = doc.dispositifEducatif,
      source = doc.source,
      production = doc.production,
      meta = MetaToSetup(doc.meta))

  case class MetaSetup(
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
      outilEvaluation: Option[Markdown],
      outilDidactique: Option[Markdown],
      ficheEleve: Option[Markdown],
      duree: String,
      commentaire: Markdown) {

    def toMeta = Meta(
      titre = titre,
      reference = reference,
      url = url,
      scenario = scenario,
      objectifT1 = objectifT1,
      objectifT2 = objectifT2,
      objectifT3 = objectifT3,
      demarche = demarche,
      deroulement = deroulement,
      tache = tache,
      materiau = materiau,
      evaluation = evaluation,
      outilEvaluation = outilEvaluation,
      outilDidactique = outilDidactique,
      ficheEleve = ficheEleve,
      duree = duree,
      commentaire = commentaire)
  }

  def MetaToSetup(meta: Meta) = MetaSetup(
      titre = meta.titre,
      reference = meta.reference,
      url = meta.url,
      scenario = meta.scenario,
      objectifT1 = meta.objectifT1,
      objectifT2 = meta.objectifT2,
      objectifT3 = meta.objectifT3,
      demarche = meta.demarche,
      deroulement = meta.deroulement,
      tache = meta.tache,
      materiau = meta.materiau,
      evaluation = meta.evaluation,
      outilEvaluation = meta.outilEvaluation,
      outilDidactique = meta.outilDidactique,
      ficheEleve = meta.ficheEleve,
      duree = meta.duree,
      commentaire = meta.commentaire)
}
