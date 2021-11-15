package controllers

import com.google.inject.Inject
import com.rallyhealth.weejson.v1.jackson.{FromJson, ToJson}
import com.rallyhealth.weepickle.v1.WeePickle.{FromScala, FromTo, ToScala, macroFromTo}
import com.rallyhealth.weepickle.v1.core.TransformException
import models.Employee
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents, Request}
import services.EmployeeService

import scala.concurrent.ExecutionContext.Implicits.global

case class MyStatus(code: Int, message: String)

class EmployeeController @Inject() (cc: ControllerComponents, employeeService: EmployeeService)
  extends AbstractController(cc){

  implicit val rw: FromTo[Employee] = macroFromTo
  implicit val rwStatus: FromTo[MyStatus] = macroFromTo
//  implicit val rwMultiple: FromTo[Seq[Employee]] = macroFromTo

  def list = Action.async { request =>
    employeeService.listAll.map(employees => Ok(FromScala(employees).transform(ToJson.string)))
  }

  def add = Action(parse.anyContent) { request =>
    val jsonEmployee = request.body.asJson
    jsonEmployee match {
      case Some(value) => try {
        val employee = FromJson(value.toString).transform(ToScala[Employee])
        employeeService.addEmployee(employee)
        Ok(FromScala(MyStatus(200, "Inserted Successfully")).transform(ToJson.string))
      } catch {
        case e: TransformException => BadRequest(FromScala(MyStatus(400, "Problem in parsing in JSON!")).transform(ToJson.string))
      }
      case None => BadRequest(FromScala(MyStatus(400, "No data as Json!")).transform(ToJson.string))
    }
  }

  def addMultipleEmployees = Action(parse.anyContent) { request =>
    val jsonEmployee = request.body.asJson
    jsonEmployee match {
      case Some(value) => try {
        val multipleEmployee = FromJson(value.toString).transform(ToScala[Seq[Employee]])
        employeeService.addMultipleEmployees(multipleEmployee)
        Ok(FromScala(MyStatus(200, "Inserted Successfully")).transform(ToJson.string))
      } catch {
        case e: TransformException => BadRequest(FromScala(MyStatus(400, "Problem in parsing in JSON!")).transform(ToJson.string))
      }
      case None => BadRequest(FromScala(MyStatus(400, "No data as Json!")).transform(ToJson.string))
    }
  }

  def listEmployeesOfDepartment(departmentId: Long) = Action.async{request =>
    employeeService.listEmployeesOfDepartment(departmentId).map(employees => Ok(FromScala(employees).transform(ToJson.string)))
  }
}


