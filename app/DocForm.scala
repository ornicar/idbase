import play.api.data._
import play.api.data.Forms._

object DocForm {

  val loginForm = Form(
    tuple(
      "email" -> text,
      "password" -> text
    )
  )
}
