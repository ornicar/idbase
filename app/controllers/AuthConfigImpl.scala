package controllers

import idbase._, models._
import jp.t2v.lab.play2.auth._
import jp.t2v.lab.play2.stackc.{ RequestWithAttributes, RequestAttributeKey, StackableController }
import play.api.mvc._
import play.api.mvc.Results._
import play.api.Play._
import play.api.templates._
import reflect.{ classTag, ClassTag }
import views._

trait AuthConfigImpl extends AuthConfig {

  def userRepo: UserRepo

  type Id = String

  type Authority = Permission

  type User = models.User

  val idTag: ClassTag[Id] = classTag[Id]

  val sessionTimeoutInSeconds: Int = 36000000

  def resolveUser(id: Id): Option[User] = userRepo find id

  /**
   * Where to redirect the user after logging out
   */
  def logoutSucceeded(request: RequestHeader): Result = Redirect(routes.Doc.search)

  def authenticationFailed(request: RequestHeader): Result =
    Redirect(routes.Application.login).withSession("access_uri" -> request.uri)

  def loginSucceeded(request: RequestHeader): Result = {
    val uri = request.session.get("access_uri").getOrElse(routes.Doc.search.url.toString)
    Redirect(uri).withSession(request.session - "access_uri").flashing(
      "success" -> "Connexion réussie"
    )
  }

  def authorizationFailed(request: RequestHeader): Result = Forbidden("no permission")

  /**
   * A function that determines what `Authority` a user has.
   * You should alter this procedure to suit your application.
   */
  def authorize(user: User, authority: Authority): Boolean =
    (user.permission, authority) match {
      case (Administrator, _)       ⇒ true
      case (NormalUser, NormalUser) ⇒ true
      case _                        ⇒ false
    }

  override lazy val cookieName: String = "IDBASE"

  override lazy val cookieSecureOption: Boolean = false
}
