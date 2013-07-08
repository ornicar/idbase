import play.api.data.Forms._
import play.api.data.validation._
import play.api.data._

package object models {

  def nonEmptyList[A](mapping: Mapping[A]): Mapping[List[A]] =
    RepeatedMapping(mapping) verifying Constraint[List[_]]("constraint.required") { o ⇒
      if (o.isEmpty) Invalid(ValidationError("error.required")) else Valid
    }
}
