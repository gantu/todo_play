package controllers

import com.google.inject.Inject
import com.rallyhealth.weejson.v1.jackson.{FromJson, ToJson}
import com.rallyhealth.weepickle.v1.WeePickle.{FromScala, FromTo, ToScala, macroFromTo}
import com.rallyhealth.weepickle.v1.core.TransformException
import models.{Department, Employee}
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents, Request}
import services.{DepartmentService, EmployeeService}

import scala.concurrent.ExecutionContext.Implicits.global

class DepartmentController @Inject() (cc: ControllerComponents, departmentService: DepartmentService)
  extends AbstractController(cc){

  implicit val rw: FromTo[Department] = macroFromTo
  implicit val rwStatus: FromTo[MyStatus] = macroFromTo

  def listOfDepartments = Action.async { request =>
    departmentService.listAllDepartments.map(departments => Ok(FromScala(departments).transform(ToJson.string)))
  }

  def addDepartment = Action(parse.anyContent) { request =>
    val jsonEmployee = request.body.asJson
    jsonEmployee match {
      case Some(value) => try {
        val department = FromJson(value.toString).transform(ToScala[Department])
        departmentService.addDepartment(department)
        Ok(FromScala(MyStatus(200, "Inserted Successfully")).transform(ToJson.string))
      } catch {
        case e: TransformException => BadRequest(FromScala(MyStatus(400, "Problem in parsing in JSON!")).transform(ToJson.string))
      }
      case None => BadRequest(FromScala(MyStatus(400, "No data as Json!")).transform(ToJson.string))
    }
  }
}


