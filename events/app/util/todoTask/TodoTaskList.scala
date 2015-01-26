package util.todoTask

import java.io.{FileNotFoundException, FileWriter}

import play.Logger
import play.api.libs.json.Json

import scala.io.Source

case class TodoTaskList (var _id: Long,
                         var taskList : List[TodoTask]) {

  implicit val format = Json.format[TodoTaskList]
  def this() = this(0,List[TodoTask]())

  /**
   * Prepends a new Task to the list
   * cost: O(1)
   */
  def addTask (task: TodoTask) = this.synchronized {
    _id += 1
    task._id = Some(_id)
    taskList = taskList.+:(task)
  }

  /**
   * Appends a new Task to the list
   * cost: O(n)
   */
  def addTaskAppend (task: TodoTask) = this.synchronized {
    _id += 1
    task._id = Some(_id)
    taskList = taskList.:+(task)
  }
  
  def removeTask (task: TodoTask) = this.synchronized{
    taskList = taskList.filterNot{ _ == task }
  }

  def toJson = Json.toJson(TodoTaskList.this)

  def load() = this.synchronized {
    try {
      val ttl = Json.parse(Source.fromFile(TodoTaskList.FILENAME).map(_.toByte).toArray).as[TodoTaskList]
      taskList = ttl.taskList
      _id = ttl._id
    }
    catch{
      case fnfe: FileNotFoundException =>
        Logger.info("no tasks file found")
    }
  }

  def save() = this.synchronized {
    val output = new FileWriter(TodoTaskList.FILENAME)
    output.write(Json.toJson(TodoTaskList.this).toString())
    output.close()
  }
}

object TodoTaskList {

  implicit val todoTaskListReads = Json.reads[TodoTaskList]
  implicit def todoTaskListWrites = Json.writes[TodoTaskList]

  // Singleton instance of TodoTaskList
  val singleton = new TodoTaskList

  val FILENAME = "todo-json-list.json"
}