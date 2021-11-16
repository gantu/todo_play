package services

import com.google.inject.Inject
import models.{Employee, EmployeeRepo}

import scala.concurrent.Future

class EmployeeService @Inject() (employeeRepo: EmployeeRepo){

  def listAll : Future[Seq[Employee]] = employeeRepo.listAll

  def addEmployee(employee: Employee): Future[Int] = employeeRepo.addEmployee(employee)

  def addMultipleEmployees(employee: Seq[Employee]): Future[Option[Int]] = employeeRepo.addMultipleEmployees(employee)

  def listEmployeesOfDepartment(departmentId: Long): Future[Seq[Employee]] = employeeRepo.listEmployeeByDepartment(departmentId)
}
