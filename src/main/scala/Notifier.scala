import scalaj.http._
import org.json4s._
import org.json4s.jackson.JsonMethods._
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

class Notifier {
  def notifier(API_KEY:String, ROOM_ID:Long) {
    implicit val formats = DefaultFormats
    // $ export API_KEY=YOUR_API_KEY
    //val API_KEY = try{ sys.env("API_KEY").toString } catch { case e:Exception =>  println("API_KEY has not been set"); sys.exit(1)}
    // $ export ROOM_ID=YOUR_ROOM_ID
    //val ROOM_ID = try{ sys.env("ROOM_ID").toLong } catch { case e:Exception => println("ROOM_ID has not been set"); sys.exit(1)}

    // 未完了のタスクを取得
    val response: HttpResponse[String] = Http("https://api.chatwork.com/v1/my/tasks?status=open").header("X-ChatWorkToken", API_KEY).asString
    // jsonのパース
    val pjson = parse(response.body).extract[Task]
    // 現在取得をエポックタイムで取得
    val current_time: Long = System.currentTimeMillis / 1000

    var sendmsg = "[info][title]期限切れタスク[/title]"
    (pjson.body, pjson.limit_time).zipped.foreach { (body, limit_time) =>
      if(current_time > limit_time && limit_time != 0) {
        val dt = new DateTime(limit_time.longValue*1000)
        val limit = dt.toString(DateTimeFormat.fullDate())
        sendmsg += s"Due: $limit\n$body\n\n"
      }
    }
    val res = Http("https://api.chatwork.com/v1/rooms/" + ROOM_ID + "/messages").postForm(Seq("body" -> sendmsg)).header("X-ChatWorkToken", API_KEY).asString
  }
}
