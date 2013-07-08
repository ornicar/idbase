package controllers

import play.api._
import play.api.mvc._

import idbase._

object Doc extends Controller {

  private lazy val env = Env.current
  
  def newForm = Action {
    Ok(views.html.newForm(models.Doc.form, env.lists))
  }
  
}
