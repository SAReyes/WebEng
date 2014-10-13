package tad.todoTask

import TaskPriority._

case class TodoTask(var name : String
            ,var context : String
            ,var project : String
            ,var priority : TaskPriority) {

  def this() = this(null,null,null,MEDIUM)

  def hasProject = project != null || project != "-"

  def hasContext = context != null || context != "-"
}