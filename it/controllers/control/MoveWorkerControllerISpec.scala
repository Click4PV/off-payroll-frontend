package controllers.control

import helpers.IntegrationSpecBase

class MoveWorkerControllerISpec extends IntegrationSpecBase {

  s"Post or Get to /worker-move-task" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/worker-move-task")

      whenReady(res) { result =>
         result.status shouldBe OK
        result.body should include ("Does your client have the right to move you from the task you originally agreed to do?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/worker-move-task")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/worker-move-task", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        result.body should include ("Does your client have the right to move you from the task you originally agreed to do?")

      }
    }

    "Return a 200 on Successful post and move onto next page" in {

      lazy val res = postSessionRequest("/worker-move-task",taskChangeValue)

      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("Does your client have the right to decide how the work is done? ")
      }
    }
  }

  s"Post or Get to /worker-move-task/change" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/worker-move-task/change")

      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("Does your client have the right to move you from the task you originally agreed to do?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/worker-move-task/change")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/worker-move-task/change", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        result.body should include ("Does your client have the right to move you from the task you originally agreed to do?")

      }
    }

    "Return a 409 on Successful post and move onto something went wrong" in {

      lazy val res = postSessionRequest("/worker-move-task/change",taskChangeValue)

      whenReady(res) { result =>
        result.status shouldBe CONFLICT
        result.body should include ("Something went wrong")
      }
    }
  }
}
