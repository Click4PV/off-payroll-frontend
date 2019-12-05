package controllers.financialRisk

import helpers.IntegrationSpecBase

class VehicleControllerISpec extends IntegrationSpecBase {

  s"Post or Get to /vehicle-costs" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/vehicle-costs")

      whenReady(res) { result =>
         result.status shouldBe OK
        titleOf(result) should include ("Will you have to fund any vehicle costs before your client pays you?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/vehicle-costs")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/vehicle-costs", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        titleOf(result) should include ("Will you have to fund any vehicle costs before your client pays you?")

      }
    }

    "Return a 200 on Successful post and move onto next page" in {

      lazy val res = postSessionRequest("/vehicle-costs", selectedNo)

      whenReady(res) { result =>
        result.status shouldBe OK
        titleOf(result) should include ("Will you have to buy materials before your client pays you?")
      }
    }
  }

  s"Post or Get to /vehicle-costs/change" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/vehicle-costs/change")

      whenReady(res) { result =>
        result.status shouldBe OK
        titleOf(result) should include ("Will you have to fund any vehicle costs before your client pays you?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/vehicle-costs/change")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/vehicle-costs/change", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        titleOf(result) should include ("Will you have to fund any vehicle costs before your client pays you?")

      }
    }

    "Return a 409 on Successful post as answers not complete" in {

      lazy val res = postSessionRequest("/vehicle-costs/change", selectedNo)

      whenReady(res) { result =>
        result.status shouldBe CONFLICT
      }
    }
  }
}
