package models

import com.google.inject.Inject
import models.DepartmentCompanion.departments
import slick.lifted.Tag
import slick.jdbc.PostgresProfile.api._
import slick.jdbc.JdbcProfile
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}

import scala.concurrent.{ExecutionContext, Future}

case class Department(id: Long, name: String)

object DepartmentCompanion {
  val departments = TableQuery[DepartmentTableDef]
}

class DepartmentTableDef(tag: Tag) extends Table[Department](tag, "department"){

  def id = column[Long]("id", O.PrimaryKey)
  def name = column[String]("name")

  override def * = (id, name) <> (Department.tupled, Department.unapply)
}

class DepartmentRepo @Inject() (protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext)
  extends HasDatabaseConfigProvider[JdbcProfile] {
  def listAllDepartments: Future[Seq[Department]] = {
    dbConfig.db.run(departments.result)
  }

  def addDepartment(department: Department): Future[Int] = {
    dbConfig.db.run(departments += department)
  }

}

