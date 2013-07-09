package controllers

import play.api._
import play.api.libs.concurrent.Execution.Implicits._
import play.api.mvc._
import scala.concurrent.Future

import idbase._
import views._

object Doc extends Controller {

  private lazy val env = Env.current

  def newForm = Action {
    Ok(html.newForm(Forms.docForm, env.lists))
  }

  def create = Action { implicit req ⇒
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

  def show(id: String) = Action { implicit req ⇒
    Async {
      env.docRepo byId id map {
        case Some(doc) ⇒ Ok(html.show(doc))
        case None      ⇒ NotFound
      }
    }
  }

  def editForm(id: String) = Action { implicit req ⇒
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

  def update(id: String) = Action { implicit req ⇒
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

  def delete(id: String) = Action { implicit req ⇒
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

  def list = Action { implicit req ⇒
    Async {
      env.docRepo.list map { l ⇒
        Ok(html.list(l))
      }
    }
  }
}
