package controllers

import events.actors.MyWebSocketActor
import events.{ClientRequest, ClientResponse}
import play.Logger
import play.api.Play.current
import play.api.libs.json.Json
import play.api.mvc.WebSocket.FrameFormatter
import play.api.mvc._

object Application extends Controller {

  /**
   * Handle ClientRequest as input format and
   * ClientResponse as output
   */
  implicit val requestFormat = Json.format[ClientRequest]
  implicit val responseFormat = Json.format[ClientResponse]
//
  implicit val requestFrameFormatter = FrameFormatter.jsonFrame[ClientRequest]
  implicit val responseFrameFormatter = FrameFormatter.jsonFrame[ClientResponse]

  //@GET /
  def index = Action {
    Ok(views.html.index("WE-Events"))
  }

  //@GET /ws/tasks
  def tasks = WebSocket.acceptWithActor[ClientRequest, ClientResponse] {
    request =>
      client =>
        Logger.info("[WS endpoint]: client connection received")
        MyWebSocketActor.props(client)
  }
}