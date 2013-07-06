package controllers

import play.api._
import play.api.mvc._

object Doc extends Controller {
  
  def newForm = Action {
    Ok(views.html.newForm("Your new application is ready."))
  }
  
}
