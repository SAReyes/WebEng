package todo.console

import java.io.BufferedReader
import java.io.PrintStream
import java.io.FileReader
import java.io.FileNotFoundException
import java.io.InputStreamReader
import java.io.FileWriter
import TaskPriority._
import org.json4s.DefaultFormats
import org.json4s.ext.EnumNameSerializer
import org.json4s.jackson.Serialization.{read => readJson, write => writeJson}

object AddTodoTask{
  
  val DEFAULT_FILE_NAME = "task_list.json"
  
  /**
   * This function fills in a task based on user input.
   */
  def PromptForTask(stdin : BufferedReader, stdout : PrintStream) = {
    val task = new TodoTask

    stdout.print("Enter task name: ")
    task.name = stdin.readLine()

    stdout.print("Enter context (blank for none): ")
    val context = stdin.readLine()
    if (context.length() > 0) task.context = context

    stdout.print("Enter project (blank for none): ")
    val project = stdin.readLine()
    if (project.length() > 0) task.project = project

    stdout.print("Enter priority (low, medium or high): ")
    val priority = stdin.readLine()

    priority match {
      case "low" =>
        task.priority = LOW
      case "medium" =>
        /* default priority */
      case "high" =>
        task.priority = HIGH
      case _ => /* Default case */
        stdout.println("Unknown type. Using default (MEDIUM).")
    }

    task
  }
  
  /**
   *  Main function: Reads the entire tasks list from a file,
   *  adds one task based on user input, then writes it back out to the same
   *  file.
   */
  def main(args : Array[String]) = {
    
    // json formats + enum support
    implicit val formats = DefaultFormats + new EnumNameSerializer(TaskPriority)
    
    var filename = DEFAULT_FILE_NAME

    if(args.length > 0){
      filename = args(0)
    }
    
    var taskList = new TodoTaskList
    
    try{
      taskList = readJson[TodoTaskList](new FileReader(filename))
    }
    catch{
      case fnfe: FileNotFoundException =>
        println(filename + ": File not found.  Creating a new file.")
    }
    
    taskList.addTask(PromptForTask(new BufferedReader(
        new InputStreamReader(System.in)), System.out))
        
    val output = new FileWriter(filename)
    output.write(writeJson(taskList))
    output.close()
  }

}