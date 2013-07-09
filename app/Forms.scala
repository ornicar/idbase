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
      "notion" -> nonEmptyList(text),
      "niveau" -> nonEmptyList(text),
      "methodePedagogique" -> nonEmptyList(text),
      "annee" -> number.verifying("Année requise", Helper.yearsToNow contains _),
      "interdisciplinarite" -> nonEmptyList(text),
      "dispositifPedagogique" -> text,
      "dispositifEducatif" -> text,
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
    "outilDidactique" -> nonEmptyText,
    "duree" -> nonEmptyText,
    "commentaire" -> nonEmptyText
  )(MetaSetup.apply _)(MetaSetup.unapply _)

  case class DocSetup(
      notion: List[String],
      niveau: List[String],
      methodePedagogique: List[String],
      annee: Int,
      interdisciplinarite: List[String],
      dispositifPedagogique: String,
      dispositifEducatif: String,
      source: String,
      production: Markdown,
      meta: MetaSetup) {

    def toDoc = Doc(
      id = Helper.Random nextString Doc.idSize,
      notion = notion,
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
      outilDidactique: String,
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
      outilDidactique = outilDidactique,
      duree = duree,
      commentaire = commentaire)
  }
}
