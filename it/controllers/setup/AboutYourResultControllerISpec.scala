package controllers.setup

import helpers.IntegrationSpecBase

class AboutYourResultControllerISpec extends IntegrationSpecBase {


  s"Get /disclaimer" should {

    "Return a 200 on successful get" in {

      lazy val res = getRequest("/disclaimer")

      whenReady(res) { result =>
        result.status shouldBe OK
      }
    }

    "Return a 404 on unsuccessful post" in {

      lazy val res = optionsRequest("/disclaimer")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 303 on unsuccessful post" in {

      lazy val res = postRequest("/disclaimer", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe SEE_OTHER

      }
    }
  }
}
