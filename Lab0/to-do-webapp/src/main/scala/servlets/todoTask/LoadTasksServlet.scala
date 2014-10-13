package servlets.todoTask

import java.io.{FileNotFoundException, FileReader}
import javax.servlet.http.{HttpServletRequest => HSReq, HttpServletResponse => HSResp, HttpServlet}
import javax.servlet.annotation.WebServlet
import org.json4s.DefaultFormats
import org.json4s.ext.EnumNameSerializer
import org.json4s.jackson.Serialization.{read => readJson, write => writeJson}
import tad.todoTask.{TodoTaskList, TaskPriority}

@WebServlet(urlPatterns = Array("/loadTodoTasks"))
class LoadTasksServlet extends HttpServlet{

  override def doGet(req: HSReq, resp: HSResp) = {
    val filename =  StaticData.JSONFILE

    // json formats + enum support
    implicit val formats = DefaultFormats + new EnumNameSerializer(TaskPriority)

    var taskList = new TodoTaskList

    try{
      taskList = readJson[TodoTaskList](new FileReader(filename))
    }
    catch{
      case fnfe: FileNotFoundException =>
    }

    resp.setContentType("application/json")
    resp.getWriter.print(writeJson(taskList))
  }

}
