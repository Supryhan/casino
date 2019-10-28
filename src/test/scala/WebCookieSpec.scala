import akka.actor.ActorRef
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.testkit.ScalatestRouteTest
import logic.Boot
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{Matchers, WordSpec}

class WebCookieSpec extends WordSpec with Matchers with ScalaFutures with ScalatestRouteTest {

 /* "The casino" should {
    "returns new user" in {
      Get("/push") ~>  Route.seal(Boot.route) ~> check {
        status should === StatusCodes.OK
        responseAs[String] shouldEqual "user added"
      }
}}*/}
