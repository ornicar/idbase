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

  def search = StackAction { implicit req ⇒
    Async {
      for {
        notions ← env.docRepo.notions
        nb ← env.docRepo.count
        res ← env.search.form.bindFromRequest.fold(
          err ⇒ env.docRepo.list map { docs ⇒
            BadRequest(html.search(
              form = err,
              lists = env.lists withNotions notions,
              nb = nb,
              docs = docs))
          },
          setup ⇒ env.search(setup) map { docs ⇒
            Ok(html.search(
              form = env.search.form fill setup,
              lists = env.lists withNotions notions,
              nb = nb,
              docs = docs))
          }
        )
      } yield res
    }
  }

  def show(id: String) = StackAction { implicit req ⇒
    Async {
      env.docRepo byId id map {
        case Some(doc) ⇒ Ok(html.show(doc))
        case None      ⇒ NotFound
      }
    }
  }

  def table = StackAction { implicit req ⇒
    Async {
      for {
        notions ← env.docRepo.notions
        methodes ← env.docRepo.methodePedagogiques
        docs ← env.docRepo.list
      } yield Ok(html.table(notions, methodes, docs))
    }
  }
}
