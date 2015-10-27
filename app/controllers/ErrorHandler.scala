package idbase

import javax.inject._

import play.api._
import play.api.http.DefaultHttpErrorHandler
import play.api.mvc._
import play.api.mvc.Results._
import play.api.routing.Router
import scala.concurrent._

class ErrorHandler @Inject() (
    env: Environment,
    config: Configuration,
    sourceMapper: OptionalSourceMapper,
    router: Provider[Router]) extends DefaultHttpErrorHandler(env, config, sourceMapper, router) {

  override def onClientError(request: RequestHeader, statusCode: Int, message: String): Future[Result] =
    Future.successful {
      NotFound(views.html.notFound())
    }
}
