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

package views.results

import akka.http.scaladsl.model.HttpMethods
import assets.messages.results.InDecisionMessages
import config.SessionKeys
import config.featureSwitch.OptimisedFlow
import forms.DeclarationFormProvider
import models.UserType.Hirer
import play.api.libs.json.Json
import play.api.mvc.{Call, Request}
import views.ViewSpecBase
import views.html.results.InsidePAYEView

class InsidePAYEViewSpec extends ViewSpecBase {

  override def beforeEach(): Unit = {
    super.beforeEach()
    enable(OptimisedFlow)
  }

  object Selectors extends BaseCSSSelectors {
    override val subheading = "p.font-large"
  }

  val form = new DeclarationFormProvider()()

  val view = injector.instanceOf[InsidePAYEView]

  val postAction = Call(HttpMethods.POST.value, "/")

  def createView(req: Request[_]) = view(form, postAction)(req, messages, frontendAppConfig)

  "The OfficeHolderPAYEView page" should {

    lazy val request = fakeRequest.withSession(SessionKeys.userType -> Json.toJson(Hirer).toString)
    lazy val document = asDocument(createView(request))

    "Have the correct title" in {
      document.title mustBe title(InDecisionMessages.HirerPAYE.title)
    }

    "Have the correct heading" in {
      document.select(Selectors.heading).text mustBe InDecisionMessages.HirerPAYE.heading
    }

    "Have the correct subheading" in {
      document.select(Selectors.subheading).text mustBe InDecisionMessages.HirerPAYE.subHeading
    }

    "Have the correct Why Result section" in {
      document.select(Selectors.h2(1)).text mustBe InDecisionMessages.whyResultHeading
      document.select(Selectors.p(1)).text mustBe InDecisionMessages.HirerPAYE.whyResult
    }

    "Have the correct Do Next section" in {
      document.select(Selectors.h2(2)).text mustBe InDecisionMessages.doNextHeading
      document.select(Selectors.p(2)).text mustBe InDecisionMessages.HirerPAYE.doNext
    }

    "Have the correct Download section" in {
      document.select(Selectors.h2(3)).text mustBe InDecisionMessages.downloadHeading
      document.select(Selectors.p(3)).text mustBe InDecisionMessages.download_p1
    }
  }
}
