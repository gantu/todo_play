package models

import slick.lifted.Tag
import slick.jdbc.PostgresProfile.api._
import slick.jdbc.JdbcProfile
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}

case class Department(id: Long, name: String)

object DepartmentCompanion {
  val departments = TableQuery[DepartmentTableDef]
}

class DepartmentTableDef(tag: Tag) extends Table[Department](tag, "department"){

  def id = column[Long]("id", O.PrimaryKey)
  def name = column[String]("name")

  override def * = (id, name) <> (Department.tupled, Department.unapply)
}
