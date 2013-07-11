package idbase

import models._
import play.api.data._
import play.api.data.Forms._

object Search {

  val form = Form(
    mapping(
      "methodePedagogique" -> list(text)
    )(Setup.apply _)(Setup.unapply _))

  case class Setup(
    methodePedagogique: List[String])
}
