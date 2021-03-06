package controllers

import jp.t2v.lab.play2.auth._
import jp.t2v.lab.play2.stackc.{ RequestWithAttributes, RequestAttributeKey, StackableController }
import play.api._, mvc._
import play.api.libs.concurrent.Execution.Implicits._
import scala.concurrent.Future
import play.api.Play.current
import play.api.i18n.Messages.Implicits._

import idbase._
import views._

class Doc extends Controller with OptionalAuthElement with AuthConfigImpl {

  private lazy val env = Env.current
  def userRepo = env.userRepo
  def lists = env.lists
  def mod(implicit req: RequestWithAttributes[_]) = loggedIn.isDefined

  def search = AsyncStack { implicit req ⇒
    for {
      notions ← env.docRepo.notions(mod)
      nb ← env.docRepo.count(mod)
      res ← env.search.form.bindFromRequest.fold(
        err ⇒ env.docRepo.list(mod) map { docs ⇒
          BadRequest(html.search(
            form = err,
            lists = env.lists withNotions notions,
            nb = nb,
            docs = docs))
        },
        setup ⇒ env.search(setup, mod) map { docs ⇒
          Ok(html.search(
            form = env.search.form fill setup,
            lists = env.lists withNotions notions,
            nb = nb,
            docs = docs))
        }
      )
    } yield res
  }

  def show(id: String) = AsyncStack { implicit req ⇒
    env.docRepo.byId(id, mod) map {
      case Some(doc) ⇒ Redirect(routes.Doc.showSlug(doc.id, doc.slug))
      case None      ⇒ NotFound
    }
  }

  def showSlug(id: String, slug: String) = AsyncStack { implicit req ⇒
    env.docRepo.byId(id, mod) map {
      case Some(doc) if doc.slug == slug ⇒ Ok(html.show(doc))
      case Some(doc)                     => Redirect(routes.Doc.showSlug(doc.id, doc.slug))
      case None                          ⇒ NotFound(html.notFound())
    }
  }

  def tableMethode = AsyncStack { implicit req ⇒
    for {
      notions ← env.docRepo.notions(mod)
      methodes ← env.docRepo.methodePedagogiques(mod)
      docs ← env.docRepo.list(mod)
    } yield Ok(html.tableMethode(notions, methodes, docs))
  }

  def tableDiscipline = AsyncStack { implicit req ⇒
    for {
      notions ← env.docRepo.notions(mod)
      disciplines ← env.docRepo.disciplines(mod)
      docs ← env.docRepo.list(mod)
    } yield Ok(html.tableDiscipline(notions, disciplines, docs))
  }

  def tableProgressions = AsyncStack { implicit req ⇒
    for {
      notions ← env.docRepo.notions(mod)
      niveaux = env.lists.progressionNiveaux
      docs ← env.docRepo.list(mod)
    } yield Ok(html.tableProgressions(notions, niveaux, docs))
  }

  def rss = Action.async { implicit req ⇒
    import java.net.URL
    import com.tlorrain.ssu.rss._
    val baseUrl = "http://idbase.esmeree.fr/"
    val authorEmail = "pascalDuplessis@aol.com"
    env.docRepo.list(false) map { docs ⇒
      Ok {
        val items = docs map { doc ⇒
          (doc.meta.titre,
            """<p>Notion(s) : %s</p><p>Méthode(s) pédagogique(s) : %s</p>%s""".format(
              doc.notion mkString ", ",
              doc.methodePedagogique mkString ", ",
              Helper.Markdown(doc.meta.scenario).body),
              new URL(baseUrl + routes.Doc.showSlug(doc.id, doc.slug)), authorEmail)
        }
        (RssFeed("ID Base", new URL(baseUrl), "Fiches pédagogiques à l'usage des professeurs documentalistes")
          withWebMaster authorEmail
          withItems (items map (item ⇒ RssItem
            withTitle item._1
            withDescription item._2
            withLink item._3
            withAuthor item._4))).toXml
      } as XML
    }
  }
}
