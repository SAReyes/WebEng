package events

import play.api.libs.json.Json
import util.todoTask.TodoTaskList

/**
 * A respond to the client
 * @param op an operation code, see ClientResponse.OP
 * @param todoTaskList send the task list when sending full_list, this can be
 *                     ignored when answering back an error code
 */
case class ClientResponse (var op: String, var todoTaskList: Option[TodoTaskList])

object ClientResponse {

  implicit val responseReads = Json.reads[ClientResponse]
  implicit def responseWrites = Json.writes[ClientResponse]

  object OP {
    val PARSE_ERROR = "parse_error"
    val FULL_LIST = "full_list"
  }
}
