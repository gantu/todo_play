package controllers


import akka.http.scaladsl.model.HttpHeader.ParsingResult.Ok
import org.scalatestplus.mockito.MockitoSugar

import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.test.Helpers._
import play.api.test.{FakeRequest, Injecting}
import services.EmployeeService



class EmployeeControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting with MockitoSugar {

  "EmployeeController GET" should {
      "render emplyees page should show all employees" in {

        val employeeService = mock[EmployeeService]
        val controller = new EmployeeController(stubControllerComponents(), employeeService)

        val result = controller.list().apply(FakeRequest(GET, "/employees"))

        status(result) mustBe(Ok)
      }
  }

}
