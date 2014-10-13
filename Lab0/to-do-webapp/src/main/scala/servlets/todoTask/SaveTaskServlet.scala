package servlets.todoTask

import java.io.{FileWriter, FileNotFoundException, FileReader}
import javax.servlet.http.{HttpServletRequest => HSReq, HttpServletResponse => HSResp, HttpServlet}
import javax.servlet.annotation.WebServlet
import org.json4s.DefaultFormats
import org.json4s.ext.EnumNameSerializer
import org.json4s.jackson.Serialization.{read => readJson, write => writeJson}

import tad.todoTask.{TodoTaskList, TaskPriority, TodoTask}

@WebServlet(urlPatterns = Array("/saveTodoTask"))
class SaveTaskServlet extends HttpServlet {

  override def doGet(req: HSReq, resp: HSResp) = {
    val filename =  StaticData.JSONFILE

    // json formats + enum support
    implicit val formats = DefaultFormats + new EnumNameSerializer(TaskPriority)

    val todoTask = new TodoTask

    todoTask.name = req.getParameter("taskName")
    todoTask.context = req.getParameter("taskContext")
    todoTask.project = req.getParameter("taskProject")
    todoTask.priority = TaskPriority.getValueFromName(req.getParameter("taskPriority"))

    if(todoTask.name == null || todoTask.context == null || todoTask.priority == null
        || todoTask.priority == null) {
      resp.sendError(HSResp.SC_FORBIDDEN)
    }

    var taskList = new TodoTaskList

    try{
      taskList = readJson[TodoTaskList](new FileReader(filename))
    }
    catch{
      case fnfe: FileNotFoundException =>
    }

    taskList.addTask(todoTask)

    val output = new FileWriter(filename)
    output.write(writeJson(taskList))
    output.close()
  }
}