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

package controllers.sections.personalService

import akka.util.ByteString
import connectors.FakeDataCacheConnector
import connectors.mocks.MockDataCacheConnector
import controllers.{ControllerHelper, ControllerSpecBase}
import controllers.actions._
import forms.NeededToPayHelperFormProvider
import models.{Answers, NormalMode, UserAnswers}
import navigation.FakeNavigator
import org.mockito.Matchers
import org.mockito.Matchers.any
import org.mockito.Mockito.when
import pages.sections.personalService.{DidPaySubstitutePage, NeededToPayHelperPage}
import pages.sections.personalService.NeededToPayHelperPage
import play.api.data.Form
import play.api.http.HttpEntity
import play.api.libs.json.Json
import play.api.mvc.{Call, ResponseHeader, Result}
import play.api.mvc.Results.Redirect
import play.api.mvc.Call
import play.api.test.Helpers._
import services.mocks.MockCompareAnswerService
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.http.cache.client.CacheMap
import views.html.subOptimised.sections.personalService.NeededToPayHelperView

class NeededToPayHelperControllerSpec extends ControllerSpecBase {

  val formProvider = new NeededToPayHelperFormProvider()
  val form = formProvider()

  val view = injector.instanceOf[NeededToPayHelperView]

  def controller(dataRetrievalAction: DataRetrievalAction = FakeEmptyCacheMapDataRetrievalAction) = new NeededToPayHelperController(
    FakeIdentifierAction,
    dataRetrievalAction,
    new DataRequiredActionImpl(messagesControllerComponents),
    formProvider,
    controllerComponents = messagesControllerComponents,
    view = view,
    mockControllerHelper,
    frontendAppConfig
  )

  def viewAsString(form: Form[_] = form) = view(form, NormalMode)(fakeRequest, messages, frontendAppConfig).toString

  val validData = Map(NeededToPayHelperPage.toString -> Json.toJson(Answers(true,0)))

  "NeededToPayHelper Controller" must {

    "return OK and the correct view for a GET" in {

      val result = controller().onPageLoad(NormalMode)(fakeRequest)
      status(result) mustBe OK
      contentAsString(result) mustBe viewAsString()
    }

    "populate the view correctly on a GET when the question has previously been answered" in {
      val getRelevantData = new FakeGeneralDataRetrievalAction(Some(CacheMap(cacheMapId, validData)))

      val result = controller(getRelevantData).onPageLoad(NormalMode)(fakeRequest)

      contentAsString(result) mustBe viewAsString(form.fill(true))
    }

    "redirect to the next page when valid data is submitted" in {

      implicit val hc = new HeaderCarrier()

      val userAnswers = UserAnswers("id")
      mockConstructAnswers(userAnswers)(userAnswers)
      mockSave(CacheMap(cacheMapId, validData))(CacheMap(cacheMapId, validData))
      mockDecide(userAnswers)(onwardRoute)

      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", "true"))

      val result = controller().onSubmit(NormalMode)(postRequest)

      status(result) mustBe SEE_OTHER
    }

    "return a Bad Request and errors when invalid data is submitted" in {
      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", "invalid value"))
      val boundForm = form.bind(Map("value" -> "invalid value"))

      val result = controller().onSubmit(NormalMode)(postRequest)

      status(result) mustBe BAD_REQUEST
      contentAsString(result) mustBe viewAsString(boundForm)
    }

    "redirect to Index Controller for a GET if no existing data is found" in {
      val result = controller(FakeDontGetDataDataRetrievalAction).onPageLoad(NormalMode)(fakeRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(controllers.routes.IndexController.onPageLoad().url)
    }

    "redirect to Index Controller for a POST if no existing data is found" in {
      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", "true"))
      val result = controller(FakeDontGetDataDataRetrievalAction).onSubmit(NormalMode)(postRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(controllers.routes.IndexController.onPageLoad().url)
    }
  }
}
