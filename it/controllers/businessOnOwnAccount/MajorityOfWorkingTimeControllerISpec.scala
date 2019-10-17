package controllers.businessOnOwnAccount

import helpers.{CreateRequestHelper, IntegrationSpecBase, TestData}
import play.api.http.Status

class MajorityOfWorkingTimeControllerISpec extends IntegrationSpecBase with CreateRequestHelper with Status with TestData{

  s"Post or Get to /majority-of-working-time" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/majority-of-working-time")

      whenReady(res) { result =>
         result.status shouldBe OK
        result.body should include ("Will this work take up the majority of your available working time?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/majority-of-working-time")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/majority-of-working-time", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        result.body should include ("Will this work take up the majority of your available working time?")

      }
    }

    "Return a 200 on Successful post and move onto next page" in {

      lazy val res = postSessionRequest("/majority-of-working-time", selectedNo)

      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("Have you done any work for other clients in the last 12 months?")
      }
    }
  }

  s"Post or Get to /majority-of-working-time/change" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/majority-of-working-time/change")

      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("Will this work take up the majority of your available working time?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/majority-of-working-time/change")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/majority-of-working-time/change", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        result.body should include ("Will this work take up the majority of your available working time?")

      }
    }

    "Return a 409 on Successful post as answers not complete" in {

      lazy val res = postSessionRequest("/majority-of-working-time/change", selectedNo, followRedirect = false)

      whenReady(res) { result =>
        redirectLocation(result) shouldBe Some("/check-employment-status-for-tax/similar-work/change")
      }
    }
  }
}
