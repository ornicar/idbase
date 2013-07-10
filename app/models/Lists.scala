package idbase
package models

import com.typesafe.config.Config
import scala.collection.JavaConversions._

final class Lists(config: Config, val notions: List[String] = Nil) {

  val niveau = getList("niveau")
  val methodePedagogique = getList("methodePedagogique")
  val interdisciplinarite = getList("interdisciplinarite")
  val dispositifPedagogique = getList("dispositifPedagogique")
  val dispositifEducatif = getList("dispositifEducatif")
  val source = getList("source")
  val demarche = getList("demarche")
  val evaluation = getList("evaluation")

  def withNotions(ns: List[String]) = new Lists(config, notions = ns)

  private def getList(key: String) = config.getStringList(key).toList
}
