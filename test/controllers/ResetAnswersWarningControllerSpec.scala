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

package controllers

import assets.messages.CheckYourAnswersMessages
import controllers.actions._
import forms.ResetAnswersWarningFormProvider
import navigation.FakeNavigator
import play.api.test.Helpers._
import views.html.ResetAnswersWarningView

class ResetAnswersWarningControllerSpec extends ControllerSpecBase {

  val view = injector.instanceOf[ResetAnswersWarningView]
  val formProvider = new ResetAnswersWarningFormProvider()
  val form = formProvider()

  def controller(dataRetrievalAction: DataRetrievalAction = FakeEmptyCacheMapDataRetrievalAction) = new ResetAnswersWarningController(
    new FakeNavigator(onwardRoute),
    FakeIdentifierAction,
    dataRetrievalAction,
    new DataRequiredActionImpl(messagesControllerComponents),
    controllerComponents = messagesControllerComponents,
    view = view,
    appConfig = frontendAppConfig,
    compareAnswerService = mockCompareAnswerService,
    dataCacheConnector = mockDataCacheConnector,
    decisionService = mockDecisionService,
    formProvider = formProvider
  )

  "ResetAnswersWarning Controller" must {

    "return OK and the correct view for a GET" in {

      mockCheckYourAnswers(Seq.empty)

      val result = controller().onPageLoad(fakeRequest)
      status(result) mustBe OK
      titleOf(result) mustBe title(ResetAnswersWarningMessages.title)
    }

    "redirect to the result page" in {
      val result = controller().onSubmit(fakeRequest)
      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some("/foo")
    }
  }
}




