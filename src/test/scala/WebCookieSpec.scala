import akka.http.scaladsl.model.headers._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.model.StatusCodes._
import org.specs2.Specification
import org.specs2.specification.core.SpecStructure
import akka.http.scaladsl.testkit.{ScalatestRouteTest, Specs2RouteTest}
import logic.Boot
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{Matchers, WordSpec, _}

class WebCookieSpec extends Specification with Specs2RouteTest with WordSpec with FlatSpec with Matchers with ScalaFutures with ScalatestRouteTest {

  "The casino" should {
    "returns new user" in {
      Get("/push") ~> Route.seal(Boot.route) ~> check {
        status should === StatusCodes.OK
        header[`Set-Cookie`].responseAs[String] shouldEqual "user added"
      }
    }
  }
}
