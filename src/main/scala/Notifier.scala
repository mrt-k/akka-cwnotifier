import scala.util.Try
import scalaj.http._
import org.json4s._
import org.json4s.jackson.JsonMethods._
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat


class Notifier {
  def myRoomNotifier(API_KEY:String, MY_ROOM_ID:String) {
    implicit val formats = DefaultFormats
    // 未完了のタスクを取得
    val my_task_response: HttpResponse[String] = Http("https://api.chatwork.com/v1/my/tasks?status=open").header("X-ChatWorkToken", API_KEY).asString
    // jsonのパース
    val my_task_json_exception = Try {
      val my_task_json = parse(my_task_response.body).extract[MyTasks]
      val sendmsg = sendMyRoom(my_task_json, MY_ROOM_ID, API_KEY)
      } recover {
      case e: org.json4s.package$MappingException => {
        parse(my_task_response.body).extract[MyTask]
      }
      case e => throw e
    }
  }

  def sendMyRoom(json: Any, MY_ROOM_ID: String, API_KEY: String): Unit = {
    val msg = json match {
      case json: MyTask =>
        val body_list = List(json.body)
        val limit_time_list = List(json.limit_time)
        val message_id_list = List(json.message_id)
        createMyRoomMsg(MsgMyRoom(message_id_list, body_list, limit_time_list), MY_ROOM_ID)
      case json: MyTasks =>
        createMyRoomMsg(MsgMyRoom(json.message_id, json.body, json.limit_time), MY_ROOM_ID)
        }
    val res = Http("https://api.chatwork.com/v1/rooms/" + MY_ROOM_ID + "/messages").postForm(Seq("body" -> msg.toString)).header("X-ChatWorkToken", API_KEY).asString
  }

  def sendSomeRoom(json: Any, roomid: String, API_KEY: String): Unit = {
    val msg = json match {
      case json: RoomTask =>
        val body_list = List(json.body)
        val limit_time_list = List(json.limit_time)
        val message_id_list = List(json.message_id)
        val assined_by_account = List(json.assigned_by_account)
        val account = List(json.account)
        createSomeMsg(MsgRoom(message_id_list, body_list, limit_time_list, account, assined_by_account), roomid)
      case json: RoomTasks =>
        createSomeMsg(MsgRoom(json.message_id, json.body, json.limit_time, json.account, json.assigned_by_account), roomid)
    }
    val res = Http("https://api.chatwork.com/v1/rooms/" + roomid + "/messages").postForm(Seq("body" -> msg.toString)).header("X-ChatWorkToken", API_KEY).asString
  }

  def createMyRoomMsg(json: MsgMyRoom, MY_ROOM_ID: String): String = {
    // 現在取得をエポックタイムで取得
    val current_time: Long = System.currentTimeMillis / 1000
    // 送信するメッセージ
    var msg = "[info][title]自分の期限切れタスク[/title]"
    (json.body, json.limit_time, json.message_id).zipped foreach { (body, limit_time, message_id) =>
      if (current_time > limit_time && limit_time != 0) {
        val dt = new DateTime(limit_time.longValue * 1000)
        val limit = dt.toString(DateTimeFormat.fullDate())
        val msg_link = s"https://www.chatwork.com/#!rid$MY_ROOM_ID-$message_id"
        msg += s"Due: $limit\n$body\n$msg_link\n[hr]\n\n"
      }
    }
    msg
  }

  def createSomeMsg(json: MsgRoom, roomid: String): String ={
    // 現在取得をエポックタイムで取得
    val current_time: Long = System.currentTimeMillis / 1000
    // 送信するメッセージ
    var msg = "[info][title]この部屋の期限切れタスク[/title]"
    ((json.body, json.limit_time, json.message_id).zipped,
      json.assigned_by_account, json.account).zipped foreach {
      case ((body, limit_time, message_id), create_account_info, have_account_info) =>

        if (current_time > limit_time && limit_time != 0) {
          val dt = new DateTime(limit_time.longValue * 1000)
          val limit = dt.toString(DateTimeFormat.fullDate())
          val msg_link = s"https://www.chatwork.com/#!rid$roomid-$message_id"

          val create_account_id = create_account_info.account_id
          val create_account_name = create_account_info.name

          val have_account_id = have_account_info.account_id
          val have_account_name = have_account_info.name

          msg += s"Due: $limit\n[To:$create_account_id]$create_account_name\n[To:$have_account_id]$have_account_name\n\n$body\n$msg_link\n[hr]\n\n"
        }
      }
    msg
  }

  def someRoomNotifier(API_KEY: String, ROOM_IDS: Array[String]){
    implicit val formats = DefaultFormats
    for(roomid <- ROOM_IDS) {
      val room_task_response: HttpResponse[String] = Http("https://api.chatwork.com/v1/rooms/" + roomid + "/tasks?status=open").header("X-ChatWorkToken", API_KEY).asString
      val room_task_json = Try {
        val room_task_json = parse(room_task_response.body).extract[RoomTasks]
        val sendmsg = sendSomeRoom(room_task_json, roomid, API_KEY)
      } recover {
        case e: org.json4s.package$MappingException => {
          val room_task_json = parse(room_task_response.body).extract[RoomTask]
          val sendmsg = sendSomeRoom(room_task_json, roomid, API_KEY)
        }
        case e => throw e
      }
    }
  }
}
