package events.actors

import akka.actor.Actor
import akka.routing.Listeners

object MyWSRouter{
  val NAME = "WSRouter"
}

/**
 * A simple listener that will broadcast ListUpdated to
 * every subscribed actor
 */
class MyWSRouter extends Actor with Listeners{

  def receive = listenerManagement orElse {
    case ListUpdated => gossip(ListUpdated)
  }

}
