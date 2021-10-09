package models

case class Employee(id: Long, firstName: String, lastName:String, email: String)

import com.google.inject.Inject
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.{ExecutionContext, Future}

class EmployeeTableDef(tag: Tag) extends Table[Employee](tag, "employee"){

  def id = column[Long]("id", O.PrimaryKey)
  def firstName = column[String]("first_name")
  def lastName = column[String]("last_name")
  def email = column[String]("email")

  override def * = (id, firstName, lastName, email) <> (Employee.tupled, Employee.unapply)
}

class EmployeeRepo @Inject() (protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext)
  extends HasDatabaseConfigProvider[JdbcProfile] {

  val employees = TableQuery[EmployeeTableDef]

  def listAll: Future[Seq[Employee]] = {
    dbConfig.db.run(employees.result)
  }
}