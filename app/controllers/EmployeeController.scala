package controllers

import com.google.inject.Inject
import models.Employee
import play.api.libs.functional.syntax.toFunctionalBuilderOps
import play.api.libs.json.JsError.toJson
import play.api.libs.json.{JsError, JsPath, JsSuccess, JsValue, Json, Reads, Writes}
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents, Request}
import services.EmployeeService

import scala.concurrent.ExecutionContext.Implicits.global

class EmployeeController @Inject() (cc: ControllerComponents, employeeService: EmployeeService)
  extends AbstractController(cc){

  implicit val employeeWrites = new Writes[Employee] {
    override def writes(employee: Employee): JsValue = Json.obj(
      "id" -> employee.id,
      "firstName" -> employee.firstName,
      "lastName" -> employee.lastName,
      "email" -> employee.email
    )
  }

  implicit val employeeReads: Reads[Employee] = (
    (JsPath \ "id").read[Long] and
      (JsPath \ "firstName").read[String] and
      (JsPath \ "lastName").read[String] and
      (JsPath \ "email").read[String]
    )(Employee.apply _)

  def list = Action.async { implicit request: Request[AnyContent] =>
    employeeService.listAll.map(employees => Ok(Json.toJson(employees)))
  }

  def add = Action(parse.json) { request =>
    val maybeEmployee = request.body.validate
    maybeEmployee match {
      case JsSuccess(employee, _) => {
        employeeService.addEmployee(employee)
        Ok(Json.obj("status" -> "OK", "message" -> "Successfully inserted!"))
      }
      case error: JsError => BadRequest(Json.obj("status" -> "Error", "message" -> toJson(error)))
    }
  }
}
