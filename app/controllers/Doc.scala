package controllers

import jp.t2v.lab.play2.auth._
import jp.t2v.lab.play2.stackc.{ RequestWithAttributes, RequestAttributeKey, StackableController }
import play.api._, mvc._
import scala.concurrent.Future

import idbase._
import views._

object Doc extends Controller with OptionalAuthElement with AuthConfigImpl {

  private lazy val env = Env.current
  def userRepo = env.userRepo

  def show(id: String) = StackAction { implicit req ⇒
    Async {
      env.docRepo byId id map {
        case Some(doc) ⇒ Ok(html.show(doc))
        case None      ⇒ NotFound
      }
    }
  }

  def list = StackAction { implicit req ⇒
    Async {
      env.docRepo.list map { l ⇒
        Ok(html.list(l))
      }
    }
  }
}
