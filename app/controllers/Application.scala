package controllers

import idbase._, models._
import jp.t2v.lab.play2.auth._
import jp.t2v.lab.play2.stackc.{ RequestWithAttributes, RequestAttributeKey, StackableController }
import play.api.data._
import play.api.data.Forms._
import play.api.i18n.Messages.Implicits._
import play.api.libs.concurrent.Execution.Implicits._
import play.api.mvc._
import play.api.mvc.Results._
import play.api.Play._
import play.api.templates._
import scala.concurrent.Future
import views._

object Application extends Controller with LoginLogout with OptionalAuthElement with AuthConfigImpl {

  private lazy val env = Env.current

  def userRepo = env.userRepo

  def about = StackAction { implicit req ⇒
    Ok(html.about(env.aboutText))
  }

  def tools = StackAction { implicit req ⇒
    Ok(html.tools(env.toolsText))
  }

  /** Your application's login form.  Alter it to fit your application */
  val loginForm = Form {
    mapping("name" -> nonEmptyText, "password" -> nonEmptyText)(userRepo.authenticate)(_.map(u ⇒ (u.name, "")))
      .verifying("Identifiants invalides", result ⇒ result.isDefined)
  }

  /** Alter the login page action to suit your application. */
  def login = StackAction { implicit req ⇒
    Ok(html.login(loginForm))
  }

  /**
   * Return the `gotoLogoutSucceeded` method's result in the logout action.
   *
   * Since the `gotoLogoutSucceeded` returns `Result`,
   * If you import `jp.t2v.lab.play2.auth._`, you can add a procedure like the following.
   *
   *   gotoLogoutSucceeded.flashing(
   *     "success" -> "You've been logged out"
   *   )
   */
  def logout = Action.async { implicit request ⇒
    gotoLogoutSucceeded
  }

  def authenticate = Action.async { implicit request ⇒
    loginForm.bindFromRequest.fold(
      formWithErrors ⇒ Future successful BadRequest(html.login(formWithErrors)),
      user ⇒ gotoLoginSucceeded(user.get.name)
    )
  }

}
