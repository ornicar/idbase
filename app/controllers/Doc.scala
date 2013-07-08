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
        setup ⇒ env.docRepo insert setup.toDoc map { doc ⇒
          Redirect(routes.Doc.show(doc.id))
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
}
