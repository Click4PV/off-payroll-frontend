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

import config.FrontendAppConfig
import config.featureSwitch.OptimisedFlow
import connectors.DataCacheConnector
import javax.inject.Inject
import models.{Section, _}
import models.requests.DataRequest
import navigation.Navigator
import pages.sections.exit.OfficeHolderPage
import pages.{BusinessOnOwnAccountSectionChangeWarningPage, PersonalServiceSectionChangeWarningPage, QuestionPage}
import play.api.libs.json.{Reads, Writes}
import play.api.mvc.{AnyContent, MessagesControllerComponents, Result}
import services.{CompareAnswerService, DecisionService}

import scala.concurrent.Future

abstract class BaseNavigationController @Inject()(mcc: MessagesControllerComponents, compareAnswerService: CompareAnswerService,
                                                  dataCacheConnector: DataCacheConnector, navigator: Navigator, decisionService: DecisionService)
                                                 (implicit frontendAppConfig: FrontendAppConfig) extends BaseController(mcc) {

  def redirect[T](mode: Mode,
                  value: T,
                  page: QuestionPage[T],
                  callDecisionService: Boolean = false)(implicit request: DataRequest[AnyContent],
                                                        reads: Reads[T],
                                                        writes: Writes[T],
                                                        aWrites: Writes[Answers[T]],
                                                        aReads: Reads[Answers[T]]): Future[Result] = {

    val currentAnswer = request.userAnswers.get(page).map(_.answer)

    // If this is the first redirect since the Personal Service warning page was displayed
    // And, it is in CheckMode. And, the Answer has not changed.
    // Then redirect back to CYA
    val answerUnchanged = mode == CheckMode && currentAnswer.contains(value)

    val personalWarning = request.userAnswers.get(PersonalServiceSectionChangeWarningPage).isDefined
    val boOAWarning = request.userAnswers.get(BusinessOnOwnAccountSectionChangeWarningPage).isDefined

    //Remove the Personal Service warning page viewed flag from the request
    val req = DataRequest(request.request, request.internalId ,request.userAnswers.remove(PersonalServiceSectionChangeWarningPage).remove(BusinessOnOwnAccountSectionChangeWarningPage))

    val answers =
      if (isEnabled(OptimisedFlow)) {
        compareAnswerService.optimisedConstructAnswers(req, value, page)
      } else {
        compareAnswerService.constructAnswers(req, value, page)
      }

    dataCacheConnector.save(answers.cacheMap).flatMap { _ =>
      (answerUnchanged, personalWarning, boOAWarning) match {
        case (true, true, _) => Future.successful(Redirect(routes.CheckYourAnswersController.onPageLoad(Some(Section.personalService))))
        case (true, _, true) => Future.successful(Redirect(routes.CheckYourAnswersController.onPageLoad(Some(Section.businessOnOwnAccount))))
        case _ => {

          val call = navigator.nextPage(page, mode)(answers)
          (callDecisionService, isEnabled(OptimisedFlow), page) match {
            //early exit office holder
            case (_, _, OfficeHolderPage) => decisionService.decide(answers, call)(hc, ec, req)
            //don't call decision every time, only once at the end (opt flow)
            case (true, false, _) => decisionService.decide(answers, call)(hc, ec, req)
            case _ => Future.successful(Redirect(call))
          }
        }
      }
    }
  }
}