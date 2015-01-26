package controllers

import play.api.libs.json.{JsResult, Json}
import play.api.mvc._
import util.todoTask.{TodoTask, TodoTaskList}
import scala.Some

object Application extends Controller {

  val tasks = TodoTaskList.load

  def index = Action {
    Ok(views.html.index("WE - Rest"))
  }

  //GET /todoList/get
  def getTasks = Action {
    implicit request =>
      render {
        case Accepts.Json() =>
          Ok(Json.toJson(tasks))
      }
  }

  //GET     /todoList/get/:id
  def getTask(id: Long) = Action {
    implicit request =>
      render {
        case Accepts.Json() =>
          tasks.getById(id) match {
            case Some(task) => Ok(Json.toJson(task))
            case None => NotFound("No task found.")
          }
      }
  }

  def myMap[A](result: JsResult[A])(route: A => Result) = {
    result.map{
      route
    } recoverTotal {
      e => BadRequest
    }
  }

  //POST    /todoList/post
  def addTask() = Action(BodyParsers.parse.json) {
    request =>
      myMap(request.body.validate[TodoTask]){
        case task =>
          tasks.addTask(task)
          tasks.save()
          Ok(Json.toJson(task))
      }
  }

  //DELETE  /todoList/delete
  def removeTask() = Action(BodyParsers.parse.json) {
    request =>
      myMap(request.body.validate[TodoTask]){
        case task =>
          tasks.removeTask(task)
          tasks.save()
          Ok
      }
  }

  //DELETE  /todoList/delete/:id
  def removeTaskById(id: Long) = Action {
    request =>
      tasks.removeTaskById(id)
      tasks.save()
      Ok
  }
}