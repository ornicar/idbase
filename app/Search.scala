package idbase

import models._
import play.api.data._
import play.api.data.Forms._

final class Search(lists: Lists) {

  val form = Form(
    mapping(
      "methodePedagogique" -> list(text)
    )(Setup.apply _)(Setup.unapply _))

  case class Setup(
    methodePedagogique: List[String])
}
