package todo.console

import org.json4s.DefaultFormats
import org.json4s.ext.EnumNameSerializer
import org.json4s.jackson.Serialization.{read => readJson}
import java.io.FileReader

object ListTodoTasks {
  val DEFAULT_FILE_NAME = AddTodoTask.DEFAULT_FILE_NAME
  
  /**
   * Iterates though all people in the AddressBook and prints info about them.
   */
  def Print(taskList: TodoTaskList) = {
    /* Nt: Reversed iteration  */
    for(task <- taskList.taskList.reverse){
      println("Task: " + task.name)

      if(task.hasContext)println("  Context: " + task.context)

      if(task.hasProject) println("  Project: " + task.project)

      println("  Priority: " + task.priority)
    }
  }
  
  /** 
   * Main function: Reads the entire tasks list from a file and prints all
   * the information inside.
   */
  def main(args: Array[String]) = {
    
    // json formats + enum support
    implicit val formats = DefaultFormats + new EnumNameSerializer(TaskPriority)
    
    var filename = DEFAULT_FILE_NAME
    
    if(args.length > 0){
      filename = args(0)
    }
    
    // Read the existing to-do list
    val addressBook = readJson[TodoTaskList](new FileReader(filename))
    
    Print(addressBook)
    
  }
}