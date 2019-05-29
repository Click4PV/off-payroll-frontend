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

package services.mocks

import models.requests.DataRequest
import models.{ErrorTemplate, UserAnswers}
import org.scalamock.scalatest.MockFactory
import play.api.mvc.Call
import play.api.mvc.Results.Redirect
import services.DecisionService
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.{ExecutionContext, Future}

trait MockDecisionService extends MockFactory {

  val mockDecisionService = mock[DecisionService]

  def mockDecide(userAnswers: UserAnswers)(call: Call): Unit = {
    (mockDecisionService.decide(_: UserAnswers, _: Call, _: ErrorTemplate)(_: HeaderCarrier, _: ExecutionContext, _: DataRequest[_]))
      .expects(userAnswers, *, *, *, *, *)
      .returns(Future.successful(Redirect(call)))

  }
}