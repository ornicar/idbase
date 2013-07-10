package idbase
package models

sealed trait Permission
case object Administrator extends Permission
case object NormalUser extends Permission

case class User(name: String, password: String, permission: Permission) 
