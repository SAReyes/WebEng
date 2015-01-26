import org.junit.runner._
import org.specs2.mutable._
import org.specs2.runner._
import play.api.libs.json.{JsSuccess, Json}
import play.api.test.Helpers._
import play.api.test._
import util.todoTask.{TodoTask, TodoTaskList}

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
@RunWith(classOf[JUnitRunner])
class ApplicationSpec extends Specification {

  "Application" should {
    "send 404 on a bad request" in new WithApplication {
      route(FakeRequest(GET, "/btooom")) must beNone
    }

    "render the index page" in new WithApplication {
      val home = route(FakeRequest(GET, "/")).get

      status(home) must_== OK
      contentType(home) must beSome.which(_ == "text/html")
      contentAsString(home) must contain("To-do Tasks")
    }

    "[GET:/todoList/get] respond TodoTaskList as json" in new WithApplication {
      val list = route(FakeRequest(GET, "/todoList/get")).get

      status(list) must_== OK
      contentAsJson(list).validate[TodoTaskList] must beAnInstanceOf[JsSuccess[TodoTaskList]]
    }

    "[GET:/todoList/get/:id] respond TodoTask as json " in new WithApplication {
      /* Adds a new task to be compared */
      val fakeTask = new TodoTask
      fakeTask.name = ""
      fakeTask.context = ""
      fakeTask.project = ""

      var fakeRequestPost = FakeRequest(POST, "/todoList/post")
        .withHeaders((CONTENT_TYPE, "application/json"))
        .withJsonBody(Json.toJson(fakeTask))

      val responsePost = route(fakeRequestPost).get

      val task = contentAsJson(responsePost).as[TodoTask]

      var fakeRequestGet = FakeRequest(GET, "/todoList/get/" + task._id)

      val responseGet = route(fakeRequestGet).get

      status(responseGet) must_==  OK

      val callbackTask = contentAsJson(responseGet).as[TodoTask]

      task must_== callbackTask
    }

    "[GET:/todoList/get/:id] send 404 with a feedback message when asking for nonexistent task " in new WithApplication {
      val list = route(FakeRequest(GET, "/todoList/get/-1")).get

      status(list) must_== NOT_FOUND
      contentAsString(list) must contain("No task found")
    }

    "[POST:/todoList/post] post a new task with json" in new WithApplication {
      val fakeTask = new TodoTask
      fakeTask.name = ""
      fakeTask.context = ""
      fakeTask.project = ""

      var fakeRequest = FakeRequest(POST, "/todoList/post")
                        .withHeaders((CONTENT_TYPE, "application/json"))
                        .withJsonBody(Json.toJson(fakeTask))

      val response = route(fakeRequest).get

      status(response) must_== OK
      contentAsJson(response).validate[TodoTask] must beAnInstanceOf[JsSuccess[TodoTask]]
    }

    "[POST:/todoList/post] send 400 when the body is invalid" in new WithApplication {
      val fakeTask = """ {"not":"a todo task"} """

      var fakeRequest = FakeRequest(POST, "/todoList/post")
        .withHeaders((CONTENT_TYPE, "application/json"))
        .withJsonBody(Json.toJson(fakeTask))

      val response = route(fakeRequest).get

      status(response) must_== BAD_REQUEST
    }

    "[DELETE:/todoList/delete/:id] send 200 when deleted" in new WithApplication {
      /* Adds a new task to be removed */
      val fakeTask = new TodoTask
      fakeTask.name = ""
      fakeTask.context = ""
      fakeTask.project = ""

      var fakeRequestPost = FakeRequest(POST, "/todoList/post")
        .withHeaders((CONTENT_TYPE, "application/json"))
        .withJsonBody(Json.toJson(fakeTask))

      val responsePost = route(fakeRequestPost).get

      val task = contentAsJson(responsePost).as[TodoTask]

      var fakeRequestDelete = FakeRequest(DELETE, "/todoList/delete/" + task._id)

      val responseDelete = route(fakeRequestDelete).get

      status(responseDelete) must_==  OK
    }
  }
}
