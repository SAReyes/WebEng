package util.todoTask

import play.api.libs.json.Json
import util.todoTask.TaskPriority._

case class TodoTask(var _id: Long,
                    var name : String,  //user Option[String] to accept null fields
                    var context : String,
                    var project : String,
                    var priority : TaskPriority) {

  def this() = this(0,null,null,null,MEDIUM)

  def hasProject = project != null && project != "-"

  def hasContext = context != null && context != "-"
}

object TodoTask {
  implicit val todoTaskReads = Json.reads[TodoTask]
  implicit def todoTaskWrites = Json.writes[TodoTask]
}