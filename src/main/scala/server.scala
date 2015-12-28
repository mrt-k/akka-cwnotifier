import scala.concurrent._
import ExecutionContext.Implicits.global

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._


object server {
  def main(args: Array[String]) {
    implicit val actorSystem = ActorSystem("hello-system")
    implicit val actorMaterializer = ActorMaterializer()

    // ChatworkのAPIキー $ export API_KEY=YOUR_API_KEY
    val API_KEY = try{ sys.env("API_KEY").toString } catch { case e:Exception =>  println("API_KEY has not been set"); sys.exit(-1)}
    // 送信先のroom id $ export ROOM_ID=YOUR_ROOM_ID
    val ROOM_ID = try{ sys.env("ROOM_ID").toLong } catch { case e:Exception => println("ROOM_ID has not been set"); sys.exit(-1)}
    // ポート番号 $ export PORT=8080
    val port: Int = try{ sys.env("PORT").toInt } catch { case e:Exception => 8080 }


    val route = {
      path("") {
        get {
          complete {
            val notifier = new Notifier
            notifier.notifier(API_KEY, ROOM_ID)
            "Send message!"
          }
        }
      }
    }


    val bindingFuture: Future[Http.ServerBinding] = Http().bindAndHandle(route,"0.0.0.0", port)

    bindingFuture onFailure {
      case e: Exception => println(e.getMessage())
    }
  }
}
