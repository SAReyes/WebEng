package events

import play.api.libs.json.Json
import util.todoTask.TodoTask

/**
 * A request coming from the client
 * @param op an operation code, see ClientRequest.OP
 * @param task a task that comes along with the operation, if needed
 */
case class ClientRequest (op: String, task: Option[TodoTask])

object ClientRequest {

  implicit val clientReads = Json.reads[ClientRequest]
  implicit def clientWrites = Json.writes[ClientRequest]

  object OP {
    val GET_JSON = "get_json"
    val ADD_TASK = "add_task"
    val DELETE_TASK = "delete_task"
    val DISCONNECT = "disconnect"
  }
}
