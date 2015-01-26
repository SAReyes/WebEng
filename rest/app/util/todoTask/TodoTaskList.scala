package util.todoTask

import java.io.{FileNotFoundException, FileWriter}

import play.api.libs.json.Json

import scala.io.Source

case class TodoTaskList (var _id: Long,
                         var taskList : List[TodoTask]) {
  implicit val format = Json.format[TodoTaskList]

//  def this() = this(new mutable.MutableList[TodoTask])
  def this() = this(0,List[TodoTask]())

  /**
   * Prepends a new Task to the list
   * cost: O(1)
   */
  def addTask (task: TodoTask) = this.synchronized {
    _id += 1
    task._id = _id
    taskList = taskList.+:(task)
  }

  /**
   * Appends a new Task to the list
   * cost: O(n)
   */
  def addTaskAppend (task: TodoTask) = this.synchronized {
    _id += 1
    task._id = _id
    taskList = taskList.:+(task)
  }

  def removeTask (task: TodoTask) = this.synchronized{
    taskList = taskList filterNot { _._id == task._id }
  }

  def removeTaskById (id: Long) = this.synchronized{
    taskList = taskList filterNot { _._id == id }
  }

  def getById(id: Long) = {
    taskList find { _._id == id }
  }

  def toJson = Json.toJson(TodoTaskList.this)

  def load() = {
    try {
      taskList = Json.parse(Source.fromFile(TodoTaskList.FILENAME).map(_.toByte).toArray).as[TodoTaskList].taskList
    }
    catch{
      case fnfe: FileNotFoundException =>
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

  val singleton = new TodoTaskList

  val FILENAME = "todo-json-list.json"

  def load: TodoTaskList = {
    var ttl = new TodoTaskList
    try {
      ttl = Json.parse(Source.fromFile(TodoTaskList.FILENAME).map(_.toByte).toArray).as[TodoTaskList]
    }
    catch{
      case fnfe: FileNotFoundException =>
    }
    ttl
  }
}