import scalaj.http._
import org.json4s._
import org.json4s.jackson.JsonMethods._
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

class Notifier {
  def notifier(API_KEY:String, MY_ROOM_ID:String) {
    implicit val formats = DefaultFormats
    // $ export API_KEY=YOUR_API_KEY
    //val API_KEY = try{ sys.env("API_KEY").toString } catch { case e:Exception =>  println("API_KEY has not been set"); sys.exit(1)}
    // $ export ROOM_ID=YOUR_ROOM_ID
    //val ROOM_ID = try{ sys.env("ROOM_ID").toLong } catch { case e:Exception => println("ROOM_ID has not been set"); sys.exit(1)}

    // 未完了のタスクを取得
    val my_task_response: HttpResponse[String] = Http("https://api.chatwork.com/v1/my/tasks?status=open").header("X-ChatWorkToken", API_KEY).asString

    // jsonのパース
    val my_task_json = parse(my_task_response.body).extract[MyTask]

    // 現在取得をエポックタイムで取得
    val current_time: Long = System.currentTimeMillis / 1000

    var sendmsg = "[info][title]My 期限切れタスク[/title]"

    (my_task_json.body, my_task_json.limit_time, my_task_json.message_id).zipped foreach { (body, limit_time, message_id) =>
      if(current_time > limit_time && limit_time != 0) {
        val dt = new DateTime(limit_time.longValue*1000)
        val limit = dt.toString(DateTimeFormat.fullDate())
        val msg_link = s"https://www.chatwork.com/#!rid$MY_ROOM_ID-$message_id"
        sendmsg += s"Due: $limit\n$body\n$msg_link\n\n"
        }
    }
    val res = Http("https://api.chatwork.com/v1/rooms/" + MY_ROOM_ID + "/messages").postForm(Seq("body" -> sendmsg)).header("X-ChatWorkToken", API_KEY).asString

  }

  def room_notifier(API_KEY: String, ROOM_IDS: Array[String]){
    implicit val formats = DefaultFormats

    for(roomid <- ROOM_IDS) {
      val room_task_response: HttpResponse[String] = Http("https://api.chatwork.com/v1/rooms/" + roomid + "/tasks?status=open").header("X-ChatWorkToken", API_KEY).asString
      val room_task_json = parse(room_task_response.body).extract[RoomTask]
      val current_time: Long = System.currentTimeMillis / 1000

      var sendmsg = "[info][title]Room 期限切れタスク[/title]"

      ((room_task_json.body, room_task_json.limit_time, room_task_json.message_id).zipped,
        room_task_json.assigned_by_account, room_task_json.account).zipped foreach {
        case ((body, limit_time, message_id), create_account_info, have_account_info) =>

          if (current_time > limit_time && limit_time != 0) {
            val dt = new DateTime(limit_time.longValue * 1000)
            val limit = dt.toString(DateTimeFormat.fullDate())
            val msg_link = s"https://www.chatwork.com/#!rid$roomid-$message_id"

            val create_account_id = create_account_info.account_id
            val create_account_name = create_account_info.name

            val have_account_id = have_account_info.account_id
            val have_account_name = have_account_info.name

            sendmsg += s"Due: $limit\n$body\n$msg_link\n\n[To:$create_account_id]$create_account_name\n[To:$have_account_id]$have_account_name\n"
          }
          val res = Http("https://api.chatwork.com/v1/rooms/" + roomid + "/messages").postForm(Seq("body" -> sendmsg)).header("X-ChatWorkToken", API_KEY).asString

      }
    }
  }
}
