import scala.concurrent._
import ExecutionContext.Implicits.global

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._


object Server {
  def main(args: Array[String]) {
    implicit val actorSystem = ActorSystem("hello-system")
    implicit val actorMaterializer = ActorMaterializer()

    // ChatworkのAPIキー $ export API_KEY=YOUR_API_KEY
    val API_KEY = try{ sys.env("API_KEY").toString } catch { case e:Exception =>  println("API_KEY has not been set"); sys.exit(-1)}
    // 自身のroom id $ export MY_ROOM_ID=YOUR_ROOM_ID
    val MY_ROOM_ID = try{ sys.env("MY_ROOM_ID").toString } catch { case e:Exception => println("MY_ROOM_ID has not been set"); sys.exit(-1)}
    // 送信先のroom_id $ export ROOM_ID=DST_ROOM_ID1:DST_ROOM_ID2:...
    val ROOM_IDS: Array[String] = try{ sys.env("ROOM_ID") split ":" } catch { case e: Exception => println("ROOM_ID has not been set"); sys.exit(-1)}
    // ポート番号 $ export PORT=8080
    val port: Int = sys.env.getOrElse("PORT", "8080").toInt
    println(s"run Application: http://localhost:$port/")

    val route = {
      path("") {
        get {
          complete {
            val notifier = new Notifier
            notifier.myRoomNotifier(API_KEY, MY_ROOM_ID)
            notifier.someRoomNotifier(API_KEY, ROOM_IDS)
            "OK"
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
