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

package controllers.sections.financialRisk

import akka.util.ByteString
import connectors.FakeDataCacheConnector
import controllers.ControllerSpecBase
import controllers.actions._
import forms.PutRightAtOwnCostFormProvider
import models.Answers._
import models.PutRightAtOwnCost.OutsideOfHoursNoCharge
import models._
import navigation.FakeNavigator
import org.mockito.Matchers
import org.mockito.Matchers._
import org.mockito.Mockito.when
import pages.sections.financialRisk.{HowWorkerIsPaidPage, PutRightAtOwnCostPage}
import play.api.data.Form
import play.api.http.HttpEntity
import play.api.libs.json.Json
import play.api.mvc.{Call, ResponseHeader, Result}
import play.api.mvc.Results.Redirect
import play.api.test.Helpers._
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.http.cache.client.CacheMap
import views.html.subOptimised.sections.financialRisk.PutRightAtOwnCostView

import scala.concurrent.Future

class PutRightAtOwnCostControllerSpec extends ControllerSpecBase {

  val formProvider = new PutRightAtOwnCostFormProvider()
  val form = formProvider()

  val view = injector.instanceOf[PutRightAtOwnCostView]

  def controller(dataRetrievalAction: DataRetrievalAction = getEmptyCacheMap) = new PutRightAtOwnCostController(
    FakeIdentifierAction,
    dataRetrievalAction,
    new DataRequiredActionImpl(messagesControllerComponents),
    formProvider,
    controllerComponents = messagesControllerComponents,
    view = view,
    controllerHelper,
    frontendAppConfig
  )

  def viewAsString(form: Form[_] = form) = view(form, NormalMode)(fakeRequest, messages, frontendAppConfig).toString

  "PutRightAtOwnCost Controller" must {

    "return OK and the correct view for a GET" in {
      val result = controller().onPageLoad(NormalMode)(fakeRequest)

      status(result) mustBe OK
      contentAsString(result) mustBe viewAsString()
    }

    "populate the view correctly on a GET when the question has previously been answered" in {
      val validData = Map(PutRightAtOwnCostPage.toString -> Json.toJson(Answers(PutRightAtOwnCost.values.head,0)))
      val getRelevantData = new FakeDataRetrievalAction(Some(CacheMap(cacheMapId, validData)))

      val result = controller(getRelevantData).onPageLoad(NormalMode)(fakeRequest)

      contentAsString(result) mustBe viewAsString(form.fill(PutRightAtOwnCost.values.head))
    }

    "redirect to the next page when valid data is submitted" in {
      val validCacheMap = CacheMap(cacheMapId, Map(PutRightAtOwnCostPage.toString -> Json.toJson(Answers("",0))))
      when(mockDataCacheConnector.save(Matchers.any())).thenReturn(Future.successful(validCacheMap))
      val userAnswers: UserAnswers => Call = UserAnswers => Call("/POST","/foo")
      when(mockNavigator.nextPage(Matchers.any(),Matchers.any())).thenReturn(userAnswers)
      when(mockDecisionService.decide(Matchers.any(),Matchers.any(),Matchers.any())(Matchers.any(),Matchers.any(),Matchers.any()))
        .thenReturn(Future.successful(Result(ResponseHeader(SEE_OTHER),HttpEntity.Strict(ByteString(""),None))))
      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", PutRightAtOwnCost.options.head.value))

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
      val result = controller(dontGetAnyData).onPageLoad(NormalMode)(fakeRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(controllers.routes.IndexController.onPageLoad().url)
    }

    "redirect to Index Controller for a POST if no existing data is found" in {
      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", PutRightAtOwnCost.options.head.value))
      val result = controller(dontGetAnyData).onSubmit(NormalMode)(postRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(controllers.routes.IndexController.onPageLoad().url)
    }
  }
}
