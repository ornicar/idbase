package controllers

import jp.t2v.lab.play2.auth._
import jp.t2v.lab.play2.stackc.{ RequestWithAttributes, RequestAttributeKey, StackableController }
import play.api._, mvc._
import scala.concurrent.Future

import idbase._, models._
import views._

object DocAdmin extends Controller with AuthElement with AuthConfigImpl {

  private lazy val env = Env.current
  def userRepo = env.userRepo

  def newForm = StackAction(AuthorityKey -> NormalUser) { implicit req ⇒
    Ok(html.newForm(Forms.docForm, env.lists))
  }

  def create = StackAction(AuthorityKey -> NormalUser) { implicit req ⇒
    Async {
      Forms.docForm.bindFromRequest.fold(
        err ⇒ Future successful {
          BadRequest(html.newForm(err, env.lists))
        },
        setup ⇒ env.docRepo insert setup.toDoc(None) map { doc ⇒
          Redirect(routes.Doc.show(doc.id)).flashing(
            "success" -> "La notice a été ajoutée"
          )
        }
      )
    }
  }

  def editForm(id: String) = StackAction(AuthorityKey -> NormalUser) { implicit req ⇒
    Async {
      env.docRepo byId id map {
        case Some(doc) ⇒ Ok(html.editForm(
          doc,
          Forms.docForm fill Forms.DocToSetup(doc),
          env.lists))
        case None ⇒ NotFound
      }
    }
  }

  def update(id: String) = StackAction(AuthorityKey -> NormalUser) { implicit req ⇒
    Async {
      env.docRepo byId id flatMap {
        case Some(doc) ⇒
          Forms.docForm.bindFromRequest.fold(
            err ⇒ Future successful {
              BadRequest(html.editForm(doc, err, env.lists))
            },
            setup ⇒ env.docRepo update setup.toDoc(Some(doc)) map { doc2 ⇒
              Redirect(routes.Doc.show(doc2.id)).flashing(
                "success" -> "La notice a été modifiée"
              )
            }
          )
        case None ⇒ Future successful NotFound
      }
    }
  }

  def delete(id: String) = StackAction(AuthorityKey -> NormalUser) { implicit req ⇒
    Async {
      env.docRepo byId id flatMap {
        case Some(doc) ⇒ env.docRepo delete doc map { _ ⇒
          Redirect(routes.Doc.list).flashing(
            "success" -> "La notice a été supprimée"
          )
        }
        case None ⇒ Future successful NotFound
      }
    }
  }
}
