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

package navigation

import javax.inject.{Inject, Singleton}
import play.api.mvc.Call
import controllers.routes
import controllers.sections.setup.{routes => setupRoutes}
import controllers.sections.exit.{routes => exitRoutes}
import controllers.sections.personalService.{routes => personalServiceRoutes}
import controllers.sections.control.{routes => controlRoutes}
import controllers.sections.financialRisk.{routes => financialRiskRoutes}
import controllers.sections.partParcel.{routes => partParcelRoutes}
import models.ArrangedSubstitute.{No, YesClientAgreed, YesClientNotAgreed}
import pages._
import models._

@Singleton
class Navigator @Inject()() {

  private val routeMap: Map[Page, UserAnswers => Call] = Map(

    //Initialisation Section
    IndexPage -> (_ => setupRoutes.AboutYouController.onPageLoad(NormalMode)),

    //Setup Section
    AboutYouPage -> (_ => setupRoutes.ContractStartedController.onPageLoad(NormalMode)),
    ContractStartedPage -> (_ => setupRoutes.WorkerTypeController.onPageLoad(NormalMode)),
    WorkerTypePage -> (_ => exitRoutes.OfficeHolderController.onPageLoad(NormalMode)),

    //Early Exit Section
    OfficeHolderPage -> (answers => answers.get(ContractStartedPage) match {
      case Some(Answers(true,_)) => personalServiceRoutes.ArrangedSubstituteController.onPageLoad(NormalMode)
      case Some(_) => personalServiceRoutes.RejectSubstituteController.onPageLoad(NormalMode)
      case _ => setupRoutes.ContractStartedController.onPageLoad(NormalMode)
    }),

    //Personal Service Section
    ArrangedSubstitutePage -> (answers =>
      answers.get(ArrangedSubstitutePage) match {
        case Some(Answers(YesClientAgreed,_)) => personalServiceRoutes.DidPaySubstituteController.onPageLoad(NormalMode)
        case Some(Answers(YesClientNotAgreed,_)) => personalServiceRoutes.NeededToPayHelperController.onPageLoad(NormalMode)
        case Some(Answers(No,_)) => personalServiceRoutes.RejectSubstituteController.onPageLoad(NormalMode)
        case _ => personalServiceRoutes.ArrangedSubstituteController.onPageLoad(NormalMode)
      }),
    DidPaySubstitutePage -> (_ => personalServiceRoutes.NeededToPayHelperController.onPageLoad(NormalMode)),
    RejectSubstitutePage -> (answers =>
      (answers.get(ContractStartedPage), answers.get(RejectSubstitutePage)) match {
        case (Some(Answers(true,_)), Some(Answers(true,_))) => personalServiceRoutes.NeededToPayHelperController.onPageLoad(NormalMode)
        case (_, Some(Answers(false,_))) => personalServiceRoutes.WouldWorkerPaySubstituteController.onPageLoad(NormalMode)
        case (_, Some(Answers(true,_))) => controlRoutes.MoveWorkerController.onPageLoad(NormalMode)
        case (None, _) => setupRoutes.ContractStartedController.onPageLoad(NormalMode)
        case (_, None) => personalServiceRoutes.RejectSubstituteController.onPageLoad(NormalMode)
      }),
    WouldWorkerPaySubstitutePage -> (answers =>
      answers.get(ContractStartedPage) match {
        case Some(Answers(true,_)) => personalServiceRoutes.NeededToPayHelperController.onPageLoad(NormalMode)
        case Some(_) => controlRoutes.MoveWorkerController.onPageLoad(NormalMode)
        case _ => setupRoutes.ContractStartedController.onPageLoad(NormalMode)
    }),
    NeededToPayHelperPage -> (_ => controlRoutes.MoveWorkerController.onPageLoad(NormalMode)),

    //Control Section
    MoveWorkerPage -> (_ => controlRoutes.HowWorkIsDoneController.onPageLoad(NormalMode)),
    HowWorkIsDonePage -> (_ => controlRoutes.ScheduleOfWorkingHoursController.onPageLoad(NormalMode)),
    ScheduleOfWorkingHoursPage -> (_ => controlRoutes.ChooseWhereWorkController.onPageLoad(NormalMode)),
    ChooseWhereWorkPage -> (_ => financialRiskRoutes.CannotClaimAsExpenseController.onPageLoad(NormalMode)),

    //Financial Risk Section
    CannotClaimAsExpensePage -> (_ => financialRiskRoutes.HowWorkerIsPaidController.onPageLoad(NormalMode)),
    HowWorkerIsPaidPage -> (_ => financialRiskRoutes.PutRightAtOwnCostController.onPageLoad(NormalMode)),
    PutRightAtOwnCostPage -> (_ => partParcelRoutes.BenefitsController.onPageLoad(NormalMode)),

    //Part and Parcel Section
    BenefitsPage -> (_ => partParcelRoutes.LineManagerDutiesController.onPageLoad(NormalMode)),
    LineManagerDutiesPage -> (_ => partParcelRoutes.InteractWithStakeholdersController.onPageLoad(NormalMode)),
    InteractWithStakeholdersPage -> (_ => partParcelRoutes.IdentifyToStakeholdersController.onPageLoad(NormalMode)),
    IdentifyToStakeholdersPage -> (_ => routes.ResultController.onPageLoad()),

    //Results Page
    ResultPage -> (_ => routes.PDFController.onPageLoad(NormalMode))
  )

  private val checkRouteMap: Map[Page, UserAnswers => Call] = Map()

  def nextPage(page: Page, mode: Mode): UserAnswers => Call = mode match {
    case NormalMode =>
      routeMap.getOrElse(page, _ => routes.IndexController.onPageLoad())
    case CheckMode =>
      checkRouteMap.getOrElse(page, _ => routes.ResultController.onPageLoad())
  }
}
