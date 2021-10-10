package services

import com.google.inject.Inject
import models.{Employee, EmployeeRepo}

import javax.inject.Singleton
import scala.concurrent.Future


class EmployeeService @Inject() (employeeRepo: EmployeeRepo){

  def listAllEmployees: Future[Seq[Employee]] = {
    employeeRepo.listAll
  }

  def addEmployee(employee: Employee): Future[String] ={
    employeeRepo.addEmployee(employee)
  }

}
