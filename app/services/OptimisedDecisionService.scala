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

package services

import cats.data.EitherT
import cats.implicits._
import config.featureSwitch.FeatureSwitching
import config.{FrontendAppConfig, SessionKeys}
import connectors.DecisionConnector
import controllers.routes
import forms.DeclarationFormProvider
import handlers.ErrorHandler
import javax.inject.{Inject, Singleton}

import models._
import models.requests.DataRequest
import pages.sections.exit.OfficeHolderPage
import pages.sections.setup.{IsWorkForPrivateSectorPage, WorkerUsingIntermediaryPage}
import play.api.Logger
import play.api.i18n.Messages
import play.api.mvc.{AnyContent, Call, Request}
import play.mvc.Http.Status._
import play.twirl.api.{Html, HtmlFormat}
import uk.gov.hmrc.http.HeaderCarrier
import viewmodels.AnswerSection
import views.html.results.inside._
import views.html.results.inside.officeHolder.{OfficeHolderAgentView, OfficeHolderIR35View, OfficeHolderPAYEView}
import views.html.results.outside._
import views.html.results.undetermined._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class OptimisedDecisionService @Inject()(decisionConnector: DecisionConnector,
                                         errorHandler: ErrorHandler,
                                         formProvider: DeclarationFormProvider,
                                         val officeAgency: OfficeHolderAgentView,
                                         val officeIR35: OfficeHolderIR35View,
                                         val officePAYE: OfficeHolderPAYEView,
                                         val undeterminedAgency: AgentUndeterminedView,
                                         val undeterminedIR35: IR35UndeterminedView,
                                         val undeterminedPAYE: PAYEUndeterminedView,
                                         val insideAgent: AgentInsideView,
                                         val insideIR35: IR35InsideView,
                                         val insidePAYE: PAYEInsideView,
                                         val outsideAgent: AgentOutsideView,
                                         val outsideIR35: IR35OutsideView,
                                         val outsidePAYE: PAYEOutsideView,
                                         implicit val appConf: FrontendAppConfig) extends FeatureSwitching {

  private[services] def collateDecisions(implicit request: DataRequest[_], hc: HeaderCarrier): Future[Either[ErrorResponse, DecisionResponse]] = {
    val interview = Interview(request.userAnswers)
    (for {
      personalService <- EitherT(decisionConnector.decide(interview, Interview.writesPersonalService))
      control <- EitherT(decisionConnector.decide(interview, Interview.writesControl))
      financialRisk <- EitherT(decisionConnector.decide(interview, Interview.writesFinancialRisk))
      wholeInterview <- EitherT(decisionConnector.decide(interview, Interview.writes))
    } yield collatedDecisionResponse(personalService, control, financialRisk, wholeInterview)).value.flatMap {
      case Right(collatedDecision) => logResult(collatedDecision, interview).map(_ => Right(collatedDecision))
      case Left(err) => Future.successful(Left(err))
    }
  }

  private def collatedDecisionResponse(personalService: DecisionResponse,
                                       control: DecisionResponse,
                                       financialRisk: DecisionResponse,
                                       wholeInterview: DecisionResponse): DecisionResponse = {
    DecisionResponse(
      version = wholeInterview.version,
      correlationID = wholeInterview.correlationID,
      score = Score(
        setup = wholeInterview.score.setup,
        exit = wholeInterview.score.exit,
        personalService = personalService.score.personalService,    // Score from isolated Personal Service call 
        control = control.score.control,                            // Score from isolated Control call 
        financialRisk = financialRisk.score.financialRisk,          // Score from isolated Financial Risk call 
        partAndParcel = wholeInterview.score.partAndParcel
      ),
      result = wholeInterview.result
    )
  }

  private def logResult(decision: DecisionResponse, interview: Interview)
                       (implicit hc: HeaderCarrier, ec: ExecutionContext, rh: Request[_]): Future[Either[ErrorResponse, Boolean]] =
    decision match {
      case response if response.result != ResultEnum.NOT_MATCHED => decisionConnector.log(interview, response)
      case _ => Future.successful(Left(ErrorResponse(NO_CONTENT, "No log needed")))
    }

  def determineResultView(postAction: Call,
                          answerSections: Seq[AnswerSection] = Seq(),
                          printMode: Boolean = false,
                          additionalPdfDetails: Option[AdditionalPdfDetails] = None,
                          timestamp: Option[String] = None,
                          decisionVersion: Option[String] = None)
                         (implicit request: DataRequest[_], hc: HeaderCarrier, messages: Messages): Future[Either[Html, Html]] = {
    collateDecisions.map {
      case Right(decision) => {
        val usingIntermediary = request.userAnswers.get(WorkerUsingIntermediaryPage).fold(false)(_.answer)
        val privateSector = request.userAnswers.get(IsWorkForPrivateSectorPage).fold(false)(_.answer)
        val officeHolderAnswer = request.userAnswers.get(OfficeHolderPage).fold(false)(_.answer)

        implicit val resultsDetails: ResultsDetails = ResultsDetails(
          action = postAction,
          officeHolderAnswer = officeHolderAnswer,
          privateSector = privateSector,
          usingIntermediary = usingIntermediary,
          userType = request.userType,
          personalServiceOption = decision.score.personalService,
          controlOption = decision.score.control,
          financialRiskOption = decision.score.financialRisk
        )

        implicit val pdfResultDetails = PDFResultDetails(
          printMode,
          additionalPdfDetails,
          timestamp,
          answerSections
        )

        decision.result match {
          case ResultEnum.INSIDE_IR35 | ResultEnum.EMPLOYED => Right(routeInside)
          case ResultEnum.OUTSIDE_IR35 | ResultEnum.SELF_EMPLOYED => Right(routeOutside)
          case ResultEnum.UNKNOWN => Right(routeUndetermined)
          case ResultEnum.NOT_MATCHED => Logger.error("[OptimisedDecisionService][determineResultView]: NOT MATCHED final decision")
            Left(errorHandler.internalServerErrorTemplate)
        }
      }
      case Left(_) => Left(errorHandler.internalServerErrorTemplate)
    }
  }

  private def routeUndetermined(implicit request: DataRequest[_], messages: Messages, result: ResultsDetails, pdfResultDetails: PDFResultDetails): Html = {
    (result.usingIntermediary, result.isAgent) match {
      case (_, true) => undeterminedAgency(result.action) // AGENT
      case (true, _) => undeterminedIR35(result.action, result.privateSector) // IR35
      case _ => undeterminedPAYE(result.action) // PAYE
    }
  }

  private def routeOutside(implicit request: DataRequest[_], messages: Messages, result: ResultsDetails, pdfResultDetails: PDFResultDetails): Html = {
    val isSubstituteToDoWork: Boolean = result.personalServiceOption.contains(WeightedAnswerEnum.OUTSIDE_IR35)
    val isClientNotControlWork: Boolean = result.controlOption.contains(WeightedAnswerEnum.OUTSIDE_IR35)
    val isIncurCostNoReclaim: Boolean = result.financialRiskOption.contains(WeightedAnswerEnum.OUTSIDE_IR35)

    (result.usingIntermediary, result.isAgent) match {
      case (_, true) =>
        outsideAgent(result.action, isSubstituteToDoWork, isClientNotControlWork, isIncurCostNoReclaim) // AGENT
      case (true, _) =>
        outsideIR35(result.action, result.privateSector, isSubstituteToDoWork, isClientNotControlWork, isIncurCostNoReclaim) // IR35
      case _ =>
        outsidePAYE(result.action, isSubstituteToDoWork, isClientNotControlWork, isIncurCostNoReclaim) // PAYE
    }
  }

  private def routeInside(implicit request: DataRequest[_], messages: Messages, result: ResultsDetails, pdfResultDetails: PDFResultDetails): Html =
    if (result.officeHolderAnswer) routeInsideOfficeHolder else routeInsideIR35

  private def routeInsideIR35(implicit request: DataRequest[_], messages: Messages, result: ResultsDetails, pdfResultDetails: PDFResultDetails): Html =
    (result.usingIntermediary, result.userType) match {
      case (_, Some(UserType.Agency)) => insideAgent(result.action) // AGENT
      case (true, _) => insideIR35(result.action, result.privateSector) // IR35
      case _ => insidePAYE(result.action) // PAYE
    }

  private def routeInsideOfficeHolder(implicit request: DataRequest[_], messages: Messages, result: ResultsDetails, pdfResultDetails: PDFResultDetails): Html =
    (result.usingIntermediary, result.isAgent) match {
      case (_, true) => officeAgency(result.action) // AGENT
      case (true, _) => officeIR35(result.action, result.privateSector) // IR35
      case _ => officePAYE(result.action) // PAYE
    }
}
