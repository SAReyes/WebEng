import akka.actor.{Props, ActorRef, ActorSystem}
import events.actors.MyWSRouter
import play.api.libs.concurrent.Akka
import play.api.{Logger, Application, GlobalSettings}
import play.api.Play.current
import util.todoTask.TodoTaskList

import scala.collection.immutable.HashMap

object Global extends GlobalSettings{

  /**
   * Global settings, on app's starts this will load the tasks list and the router
   * @param app
   */
  override def onStart(app: Application) = {
    Logger.info("[onStart] Loading tasks")
    TodoTaskList.singleton.load
    val router = Akka.system.actorOf(Props[MyWSRouter], name = MyWSRouter.NAME)
    Logger.info(s"[onStart] tasks ${TodoTaskList.singleton}")
    Logger.info(s"[onStart] router = ${router.path}")
  }
}
