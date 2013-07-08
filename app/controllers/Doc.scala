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
    Ok(html.newForm(models.Doc.form, env.lists))
  }

  def create = Action.async { implicit req ⇒
    models.Doc.form.bindFromRequest.fold(
      err ⇒ Future successful {
        BadRequest(html.newForm(err, env.lists))
      },
      doc ⇒ env.repo insert doc map { _ ⇒ 
        Redirect(routes.Doc.show(doc.id))
      }
    )
  }

  def show(id: String) = Action.async { implicit req ⇒
    env.repo.byId(id) map {
      case Some(doc) ⇒ Ok(html.show(doc))
      case None      ⇒ NotFound
    }
  }
}
