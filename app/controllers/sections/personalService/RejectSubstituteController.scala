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

import config.FrontendAppConfig
import config.featureSwitch.{FeatureSwitching, OptimisedFlow}
import connectors.DataCacheConnector
import controllers.BaseController
import controllers.actions._
import forms.RejectSubstituteFormProvider
import javax.inject.Inject
import models.Mode
import navigation.OldNavigator
import pages.sections.personalService.RejectSubstitutePage
import play.api.data.Form
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents, Request}
import play.twirl.api.HtmlFormat
import services.{CheckYourAnswersService, CompareAnswerService, DecisionService}
import views.html.sections.personalService.RejectSubstituteView
import views.html.subOptimised.sections.personalService.{RejectSubstituteView => SubOptimisedRejectSubstituteView}

import scala.concurrent.Future

class RejectSubstituteController @Inject()(identify: IdentifierAction,
                                           getData: DataRetrievalAction,
                                           requireData: DataRequiredAction,
                                           formProvider: RejectSubstituteFormProvider,
                                           controllerComponents: MessagesControllerComponents,
                                           optimisedView: RejectSubstituteView,
                                           subOptimisedView: SubOptimisedRejectSubstituteView,
                                           checkYourAnswersService: CheckYourAnswersService,
                                           compareAnswerService: CompareAnswerService,
                                           dataCacheConnector: DataCacheConnector,
                                           decisionService: DecisionService,
                                           navigator: OldNavigator,
                                           implicit val appConfig: FrontendAppConfig
                                          ) extends BaseController(
  controllerComponents,compareAnswerService,dataCacheConnector,navigator,decisionService) with FeatureSwitching {

  val form: Form[Boolean] = formProvider()

  private def view(form: Form[Boolean], mode: Mode)(implicit request: Request[_]): HtmlFormat.Appendable =
    if(isEnabled(OptimisedFlow)) optimisedView(form, mode) else subOptimisedView(form, mode)

  def onPageLoad(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    Ok(view(fillForm(RejectSubstitutePage, form), mode))
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    form.bindFromRequest().fold(
      formWithErrors =>
        Future.successful(BadRequest(view(formWithErrors, mode))),
      value => redirect(mode,value,RejectSubstitutePage)
    )
  }
}
