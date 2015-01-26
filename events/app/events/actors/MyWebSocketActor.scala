package events.actors

import _root_.util.todoTask.TodoTaskList
import akka.actor._
import akka.routing.{Deafen, Listen}
import events.{ClientRequest, ClientResponse}
import play.api.Logger
import play.api.Play.current
import play.api.libs.concurrent.Akka

object MyWebSocketActor {
  def props(client: ActorRef) = Props(new MyWebSocketActor(client))
}
class MyWebSocketActor(client: ActorRef) extends Actor {
  val router = Akka.system.actorSelection("/user/" + MyWSRouter.NAME)

  def log(s: String) = Logger.info(s"[WS $self]: $s")

  /**
   * subscribe to the Listener on start so as to listen for every broadcast
   */
  override def preStart() = {
    log("has joined the channel")
    router ! Listen(self)
  }

  /**
   * Handle the client messages and responds either to the request or
   * broadcasting an update message
   */
  def receive = {
    case ClientRequest(ClientRequest.OP.GET_JSON,_) =>
      log("requests the list")
      client ! ClientResponse(ClientResponse.OP.FULL_LIST,Some(TodoTaskList.singleton))

    case ClientRequest(ClientRequest.OP.ADD_TASK,optionTask) =>
      log(s"add the following task $optionTask")
      optionTask match {
        case Some(task) =>
          TodoTaskList.singleton.addTask(task)
          router ! ListUpdated
          TodoTaskList.singleton.save()
        case None =>
          client ! ClientResponse(ClientResponse.OP.PARSE_ERROR,None)
      }

    case ClientRequest(ClientRequest.OP.DELETE_TASK,optionTask) =>
      log(s"removing the following task $optionTask")
      optionTask match {
        case Some(task) =>
          TodoTaskList.singleton.removeTask(task)
          router ! ListUpdated
          TodoTaskList.singleton.save()
        case None =>
          client ! ClientResponse(ClientResponse.OP.PARSE_ERROR,None)
      }

    case ClientRequest(ClientRequest.OP.DISCONNECT,_) =>
      log("disconnecting")
      self ! PoisonPill

    case ListUpdated =>
      log("updating list")
      client ! ClientResponse(ClientResponse.OP.FULL_LIST,Some(TodoTaskList.singleton))

    case any =>
      Logger.warn(s"[WS $self]: got smth $any")
  }

  /**
   * After dying remove this actor from the listener
   */
  override def postStop() = {
    router ! Deafen(self)
  }
}