package models

import com.google.inject.Inject
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.{ExecutionContext, Future}

case class Employee(id: Long, firstName: String, lastName:String, email: String, departmentId: Long)

class EmployeeTableDef(tag: Tag) extends Table[Employee](tag, "employee"){

  import DepartmentCompanion.departments

  def id = column[Long]("id", O.PrimaryKey)
  def firstName = column[String]("first_name")
  def lastName = column[String]("last_name")
  def email = column[String]("email")
  def departmentId = column[Long]("department_id")

  def departmentFK =
    foreignKey("DEPT_FK", departmentId, departments)(_.id, onUpdate = ForeignKeyAction.Restrict, onDelete = ForeignKeyAction.Cascade)

  override def * = (id, firstName, lastName, email, departmentId) <> (Employee.tupled, Employee.unapply)
}

class EmployeeRepo @Inject() (protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext)
  extends HasDatabaseConfigProvider[JdbcProfile] {
  import DepartmentCompanion.departments
  val employees = TableQuery[EmployeeTableDef]

  def listAll: Future[Seq[Employee]] = {
    dbConfig.db.run(employees.result)
  }

  def listEmployeeByDepartment(departmentId: Long): Future[Seq[Employee]] = {
    //SELECT * from employees as e, department as d where e.department_id = d.id and d.id=departmentId;

    val joinQuery = for {
      d <- departments if d.id === departmentId
      e <- employees if e.departmentId === d.id
    } yield e

    dbConfig.db.run(joinQuery.result)
  }
  def addEmployee(employee: Employee): Future[Int] = {
    dbConfig.db.run(employees += employee)
  }
}