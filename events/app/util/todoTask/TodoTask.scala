package util.todoTask

import TaskPriority._
import play.api.libs.json.Json

case class TodoTask(var _id: Option[Long],
                    var name : String,
                    var context : String,
                    var project : String,
                    var priority : TaskPriority) {

  def this() = this(Some(0),null,null,null,MEDIUM)

  def hasProject = project != null && project != "-"

  def hasContext = context != null && context != "-"
}

object TodoTask {
  implicit val todoTaskReads = Json.reads[TodoTask]
  implicit def todoTaskWrites = Json.writes[TodoTask]
}