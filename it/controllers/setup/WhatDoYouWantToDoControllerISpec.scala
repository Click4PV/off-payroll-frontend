package controllers.setup

import helpers.IntegrationSpecBase

class WhatDoYouWantToDoControllerISpec extends IntegrationSpecBase {

  s"Post or Get to /what-do-you-want-to-do" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/what-do-you-want-to-do")

      whenReady(res) { result =>
        result.status shouldBe OK
        titleOf(result) should include ("What do you want to do?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/what-do-you-want-to-do")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/what-do-you-want-to-do", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        titleOf(result) should include ("What do you want to do?")

      }
    }

    "Return a 303 on Successful post and redirect to the Worker using intermediary page" in {

      lazy val res = postSessionRequest("/what-do-you-want-to-do",whatDoYouWantToDoValue)

      whenReady(res) { result =>
        result.status shouldBe SEE_OTHER

      }
    }
  }

  s"Post or Get to /what-do-you-want-to-do/change" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/what-do-you-want-to-do/change")

      whenReady(res) { result =>
        result.status shouldBe OK
        titleOf(result) should include ("What do you want to do?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/what-do-you-want-to-do/change")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/what-do-you-want-to-do/change", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        titleOf(result) should include ("What do you want to do?")

      }
    }
  }
}
