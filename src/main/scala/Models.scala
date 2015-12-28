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
case class Task(task_id: List[Long],
                room: List[Room],
                assigned_by_account: List[AssignedByAccount],
                message_id: List[Long],
                body: List[String],
                limit_time: List[Long],
                status: List[String]
               )

case class Room(room_id: Long,
                name: String,
                icon_path: String
               )

case class AssignedByAccount(account_id: Long,
                             name: String,
                             avatar_image_url: String
                            )

