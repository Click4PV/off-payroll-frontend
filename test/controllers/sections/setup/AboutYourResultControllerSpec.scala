/*
 * Copyright 2019 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package controllers.sections.setup

import controllers.ControllerSpecBase
import controllers.actions._
import navigation.FakeNavigator
import play.api.mvc.Call
import play.api.test.Helpers._
import views.html.sections.setup.AboutYourResultView

class AboutYourResultControllerSpec extends ControllerSpecBase {

  def onwardRoute = Call("POST", "/foo")

  val view = injector.instanceOf[AboutYourResultView]

  def controller(dataRetrievalAction: DataRetrievalAction = getEmptyCacheMap) = new AboutYourResultController(
    navigator = new FakeNavigator(onwardRoute),
    identify = FakeIdentifierAction,
    getData = dataRetrievalAction,
    requireData = new DataRequiredActionImpl(messagesControllerComponents),
    controllerComponents = messagesControllerComponents,
    view = view,
    appConfig = frontendAppConfig
  )

  def viewAsString = view(routes.AboutYourResultController.onSubmit())(fakeRequest, messages, frontendAppConfig).toString

  "AboutYou Controller" must {

    "return OK and the correct view for a GET" in {
      val result = controller().onPageLoad(fakeRequest)
      status(result) mustBe OK
      contentAsString(result) mustBe viewAsString
    }

    "redirect to the next page when valid data is submitted" in {
      val result = controller().onSubmit(fakeRequest)
      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(onwardRoute.url)
    }

    "redirect to Index Controller for a GET if no existing data is found" in {
      val result = controller(dontGetAnyData).onPageLoad(fakeRequest)
      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(controllers.routes.IndexController.onPageLoad().url)
    }

    "redirect to Index Controller for a POST if no existing data is found" in {
      val result = controller(dontGetAnyData).onSubmit(fakeRequest)
      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(controllers.routes.IndexController.onPageLoad().url)
    }
  }
}