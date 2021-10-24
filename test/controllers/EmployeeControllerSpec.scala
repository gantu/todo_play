package controllers


import models.Employee
import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play._
import org.mockito.Mockito._
import play.api.Play.materializer
import play.api.libs.json.{JsArray, JsNumber, JsObject, JsString, JsValue, Json}
import play.api.mvc.Result
import play.api.test.{FakeHeaders, FakeRequest}
import play.api.test.Helpers.{GET, POST, contentAsJson, defaultAwaitTimeout, status, stubControllerComponents}
import services.EmployeeService

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global


class EmployeeControllerSpec extends PlaySpec with MockitoSugar {

  "EmployeeController GET" should {
    val employeeService = mock[EmployeeService]
    val controller = new EmployeeController(stubControllerComponents(), employeeService)

    "render employees page should show all employees" in {


      when(employeeService.listAll).thenReturn(Future(List(
        Employee(1, "james", "bond", "james@bond.com"),
        Employee(2, "sherlock", "holmes", "sherlock@holmes.com")
      )))


      val result = controller.list().apply(FakeRequest(GET, "/employees"))
      status(result) must be(200)
      contentAsJson(result) must be(
        Json.parse(
          """
            [{
              "id":1,
              "firstName": "james",
              "lastName": "bond",
              "email":"james@bond.com"
            },
            {
              "id":2,
              "firstName": "sherlock",
              "lastName": "holmes",
              "email":"sherlock@holmes.com"
            }
            ]
            """)
      )


    }

    "POST call to employees with valid JSON object should add employee to DB" in {

      val employee = Employee(1, "james", "bond", "james@bond.com")
      when(employeeService.addEmployee(employee)).thenReturn(Future(1))

      val result: Future[Result] = controller.add().apply(
        FakeRequest(
          POST,
          "/employees"
        ).withJsonBody(
          Json.parse("""{"id":2, "firstName": "sherlock", "lastName": "holmes", "email":"sherlock@holmes.com"}""")
        )
     )

      status(result) must be(200)
      contentAsJson(result) must be(Json.parse(
        """
          {
            "code":200,
            "message": "Inserted Successfully"
          }
          """
      )
      )
    }

    "POST call to employees without JSON boyd should return appropriate bad request message " in {
      val result: Future[Result] = controller.add().apply(
        FakeRequest(
          POST,
          "/employees"
        )
      )

      status(result) must be(400)
      contentAsJson(result) must be(Json.parse(
        """
          {
            "code":400,
            "message": "No data as Json!"
          }
          """
      )
      )
    }

    "POST call to employees with invalid JSON body should return appropriate bad request message " in {
      val result: Future[Result] = controller.add().apply(
        FakeRequest(
          POST,
          "/employees"
        ).withJsonBody(
          Json.parse("""{"id":2, "stName": "sherlock", "lastName": "holmes", "email":"sherlock@holmes.com"}""")
        )
      )

      status(result) must be(400)
      contentAsJson(result) must be(Json.parse(
        """
          {
            "code":400,
            "message": "Problem in parsing in JSON!"
          }
          """
      )
      )
    }
  }
}