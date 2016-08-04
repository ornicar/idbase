package idbase

import play.api.cache._
import play.api.libs.ws._
import scala.concurrent.Future
import scala.concurrent.duration._
import play.api.libs.concurrent.Execution.Implicits.defaultContext

final class TextFetch(cache: CacheApi, ws: WSClient) {

  def apply(name: String): Future[String] =
    cache.getOrElse[Future[String]](name, 5 minutes)(fetch(name))

  private def fetch(name: String): Future[String] = {
    val url = s"https://raw.githubusercontent.com/ornicar/idbase/master/conf/$name.md"
    println(s"Fetch $url")
    ws.url(url).get().map(_.body)
  }
}
