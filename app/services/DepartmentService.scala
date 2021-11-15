package services

import com.google.inject.Inject
import models.{Department, DepartmentRepo, Employee, EmployeeRepo}

import scala.concurrent.Future

class DepartmentService @Inject()(departmentRepo: DepartmentRepo){

  def listAllDepartments : Future[Seq[Department]] = departmentRepo.listAllDepartments

  def addDepartment(department: Department): Future[Int] = departmentRepo.addDepartment(department)


}
