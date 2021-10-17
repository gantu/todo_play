package controllers


import models.Employee
import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play._
import org.mockito.Mockito._
import play.api.libs.json.JsObject
import play.api.test.FakeRequest
import play.api.test.Helpers.{GET, contentAsJson, defaultAwaitTimeout, status, stubControllerComponents}
import services.EmployeeService

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global


class EmployeeControllerSpec extends PlaySpec with MockitoSugar {

  "EmployeeController GET" should {
      "render emplyees page should show all employees" in {

        val employeeService = mock[EmployeeService]
        when(employeeService.listAll).thenReturn(Future(List(Employee(1,"james","bond", "james@bond.com"))))
        val controller = new EmployeeController(stubControllerComponents(), employeeService)

        val result = controller.list().apply(FakeRequest(GET, "/employees"))
        status(result) must be (200)
        contentAsJson(result) must be (JsObject{
          Seq(

          )
        })

      }
  }

}
