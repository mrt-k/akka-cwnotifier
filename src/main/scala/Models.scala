/*
[
    {
        "task_id": 3,
        "room": {
            "room_id": 5,
            "name": "Group Chat Name",
            "icon_path": "https://example.com/ico_group.png"
        },
        "assigned_by_account": {
            "account_id": 78,
            "name": "Anna",
            "avatar_image_url": "https://example.com/def.png"
        },
        "message_id": 13,
        "body": "buy milk",
        "limit_time": 1384354799,
        "status": "open"
    }
]
 */

// 自身のタスクが1つのみのケース
case class MyTask(task_id: Long,
                  room: Room,
                  assigned_by_account: AssignedByAccount,
                  message_id: Long,
                  body: String,
                  limit_time: Long,
                  status: String
                 )

// 自身のタスクが複数のケース
case class MyTasks(task_id: List[Long],
                room: List[Room],
                assigned_by_account: List[AssignedByAccount],
                message_id: List[Long],
                body: List[String],
                limit_time: List[Long],
                status: List[String]
               )

// 部屋のタスクが1つのみのケース
case class RoomTask(task_id: Long,
                    account: Account,
                    assigned_by_account: AssignedByAccount,
                    message_id: Long,
                    body: String,
                    limit_time: Long,
                    status: String
                   )

// 部屋のタスクが複数のケース
case class RoomTasks(task_id: List[Long],
                    account: List[Account],
                    assigned_by_account: List[AssignedByAccount],
                    message_id: List[Long],
                    body: List[String],
                    limit_time: List[Long],
                    status: List[String]
                   )

case class MsgMyRoom(message_id: List[Long],
                     body: List[String],
                     limit_time: List[Long]
                    )

case class MsgRoom(message_id: List[Long],
                   body: List[String],
                   limit_time: List[Long],
                   account: List[Account],
                   assigned_by_account: List[AssignedByAccount]
                  )

case class Room(room_id: Long,
                name: String,
                icon_path: String
               )

case class AssignedByAccount(account_id: Long,
                             name: String,
                             avatar_image_url: String
                            )

case class Account(account_id: Long,
                   name: String,
                   avatar_image_url: String
                  )

