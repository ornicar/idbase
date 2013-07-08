package idbase
package models

import com.typesafe.config.Config
import scala.collection.JavaConversions._

final class Lists(config: Config) {

  val notion = getList("notion")
  val methodePedagogique = getList("methodePedagogique")
  val dispositifPedagogique = getList("dispositifPedagogique")
  val dispositifEducatif = getList("dispositifEducatif")
  val source = getList("source")
  val demarche = getList("demarche")
  val evaluation = getList("evaluation")

  private def getList(key: String) = config.getStringList(key).toList
}
