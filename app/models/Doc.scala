package models

import java.util.{Date}

import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._

case class Doc
		( id             : Pk[Long] = NotAssigned
    , titre          : String
    , niveaux        : List[String]
		, notion         : String
		, legalName      : String
		, email          : String
		, password       : String
		, status         : String
		, created        : Date = new Date()
		, firstLogin     : Option[Date]
		, lastLogin      : Option[Date]
		, passwordChanged: Option[Date]
		, failedAttempts : Int = 0
		) {

	def username = email
}
