package controllers

import com.google.inject.Inject
import models.Employee
import play.api.Logging
import play.api.libs.functional.syntax.toFunctionalBuilderOps
import play.api.libs.json.OFormat.oFormatFromReadsAndOWrites
import play.api.libs.json.{JsError, JsPath, Json, Reads, Writes}
import play.api.mvc.{AbstractController, AnyContent, BaseController, ControllerComponents, Request}
import services.EmployeeService

import javax.inject.Singleton
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class EmployeeController @Inject() (cc: ControllerComponents, employeeService: EmployeeService)
  extends AbstractController(cc) with Logging{

  implicit val employeeWrites: Writes[Employee] = (employee: Employee) => Json.obj(
    "id" -> employee.id,
    "firstName" -> employee.firstName,
    "lastName" -> employee.lastName,
    "email" -> employee.email
  )

  implicit val employeeReads : Reads[Employee] = (
    (JsPath \ "id").read[Long] and
      (JsPath \ "firstName").read[String] and
      (JsPath \ "lastName").read[String] and
      (JsPath \ "email").read[String]
  )(Employee.apply _)

  def list() = Action.async { implicit request: Request[AnyContent] =>
    employeeService.listAllEmployees map { employees =>
      Ok(Json.toJson(employees))
    }
  }

  def add = Action(parse.json){ request =>
    val maybeEmployee = request.body.validate

    maybeEmployee.fold (
      errors => {
        BadRequest(Json.obj("status" -> "Error", "message" -> JsError.toJson(errors)))
      },
        employee => {
          employeeService.addEmployee(employee)
          Ok(Json.obj("status" -> "OK", "message" -> "Successfully added employee!"))
        }
    )
  }

}
