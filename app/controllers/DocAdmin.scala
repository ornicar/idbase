package controllers

import jp.t2v.lab.play2.auth._
import jp.t2v.lab.play2.stackc.{ RequestWithAttributes, RequestAttributeKey, StackableController }
import play.api._, mvc._
import play.api.libs.concurrent.Execution.Implicits._
import scala.concurrent.Future
import play.api.Play.current
import play.api.i18n.Messages.Implicits._

import idbase._, models._
import views._

class DocAdmin extends Controller with AuthElement with AuthConfigImpl {

  private lazy val env = Env.current
  def userRepo = env.userRepo

  def newForm = AsyncStack(AuthorityKey -> NormalUser) { implicit req ⇒
    env.docRepo.notions(true) map { notions ⇒
      Ok(html.newForm(Forms.docForm, env.lists withNotions notions))
    }
  }

  def create = AsyncStack(AuthorityKey -> NormalUser) { implicit req ⇒
    Forms.docForm.bindFromRequest.fold(
      err ⇒ env.docRepo.notions(true) map { notions ⇒
        BadRequest(html.newForm(err, env.lists withNotions notions))
      },
      setup ⇒ env.docRepo insert setup.toDoc(None) map { doc ⇒
        Redirect(routes.Doc.showSlug(doc.id, doc.slug)).flashing(
          "success" -> "La notice a été ajoutée"
        )
      }
    )
  }

  def editForm(id: String) = AsyncStack(AuthorityKey -> NormalUser) { implicit req ⇒
    env.docRepo.notions(true) flatMap { notions ⇒
      env.docRepo.byId(id, true) map {
        case Some(doc) ⇒ Ok(html.editForm(
          doc,
          Forms.docForm fill Forms.DocToSetup(doc),
          env.lists withNotions notions))
        case None ⇒ NotFound
      }
    }
  }

  def update(id: String) = AsyncStack(AuthorityKey -> NormalUser) { implicit req ⇒
    env.docRepo.byId(id, true) flatMap {
      case Some(doc) ⇒
        Forms.docForm.bindFromRequest.fold(
          err ⇒ env.docRepo.notions(true) map { notions ⇒
            BadRequest(html.editForm(doc, err, env.lists withNotions notions))
          },
          setup ⇒ env.docRepo update setup.toDoc(Some(doc)) map { doc2 ⇒
            Redirect(routes.Doc.showSlug(doc2.id, doc2.slug)).flashing(
              "success" -> "La notice a été modifiée"
            )
          }
        )
      case None ⇒ Future successful NotFound
    }
  }

  def delete(id: String) = AsyncStack(AuthorityKey -> NormalUser) { implicit req ⇒
    env.docRepo.byId(id, true) flatMap {
      case Some(doc) ⇒ env.docRepo delete doc map { _ ⇒
        Redirect(routes.Doc.search).flashing(
          "success" -> "La notice a été supprimée"
        )
      }
      case None ⇒ Future successful NotFound
    }
  }
}
