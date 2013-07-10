package idbase

import models._

final class UserRepo(strings: List[String]) {

  private val regex = """^([^\s]+)\s([^\s]+)$""".r

  val all: List[User] = strings collect {
    case regex(name, password) ⇒ User(name, password, NormalUser)
  }

  def find(name: String): Option[User] = all find (_.name == name)

  def authenticate(name: String, password: String): Option[User] = 
    find(name) filter (_.password == password)
}
