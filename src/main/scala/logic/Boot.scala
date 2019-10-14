package logic

import java.util.UUID

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.headers.HttpCookie
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.io.StdIn
import scala.util.Try

object Boot extends App {

  implicit val system: ActorSystem = ActorSystem("my-casino-system")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher
  type UserId = UUID
  val r = new scala.util.Random

  var users: Map[UserId, (Int, Int, Double)] = Map.empty

  def processBet(in: (Int, Int, Double)): (Int, Int, Double) = {
    val (balance, bet, threshold) = in
    (balance - (if (r.nextFloat * 100 < threshold) -1 else 1) * bet, bet + 5, threshold - threshold / 100)
  }

  def getUuid(value: String): Try[Boolean] = Try(UUID.fromString(value)).flatMap(uuid => Try(users.contains(uuid)))

  def validateBalance(value: String): Boolean = if (users(UUID.fromString(value))._1 >= 0) true else false

  val route: Route =
    path("push") {
      optionalCookie("userName") {
        case Some(v) if getUuid(v.value).get && validateBalance(v.value) =>
          get { ctx =>
            val userId = UUID.fromString(v.value)
            users += (userId -> processBet(users(userId)))
            ctx.complete(Future(s"Your balance is: ${users(userId)._1}, your bet is: ${users(userId)._2}"))
          }
        case _ =>
          val uuid = UUID.randomUUID()
          setCookie(HttpCookie("userName", value = uuid.toString)) {
            get { ctx =>
              users += (uuid -> processBet((100, 10, 50.0)))
              ctx.complete(Future(s"You are nsew uer. Your balance is: ${users(uuid)._1}, youer bt is: ${users(uuid)._2}"))
            }
          }
      }
    } ~ path("delete") {
      deleteCookie("userName") {
        complete("The user was deleted")
      }
    }

  val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)

  println(s"Server online at http://localhost:8080/\nuse /push to play, /delete to remove user and press RETURN to stop...")
  StdIn.readLine()
  bindingFuture
    .flatMap(_.unbind())
    .onComplete(_ => system.terminate())
}