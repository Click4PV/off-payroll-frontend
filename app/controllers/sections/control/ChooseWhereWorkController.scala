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

package controllers.sections.control

import config.FrontendAppConfig
import config.featureSwitch.{FeatureSwitching, OptimisedFlow}
import connectors.DataCacheConnector
import controllers.BaseNavigationController
import controllers.actions._
import forms.sections.control.ChooseWhereWorkFormProvider
import javax.inject.Inject
import models.Mode
import models.sections.control.ChooseWhereWork
import navigation.ControlNavigator
import pages.sections.control.ChooseWhereWorkPage
import play.api.data.Form
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents, _}
import play.twirl.api.HtmlFormat
import services.{CheckYourAnswersService, CompareAnswerService, DecisionService}
import views.html.sections.control.ChooseWhereWorkView
import views.html.subOptimised.sections.control.{ChooseWhereWorkView => SubOptimisedChooseWhereWorkView}

import scala.concurrent.Future

class ChooseWhereWorkController @Inject()(identify: IdentifierAction,
                                          getData: DataRetrievalAction,
                                          requireData: DataRequiredAction,
                                          formProvider: ChooseWhereWorkFormProvider,
                                          controllerComponents: MessagesControllerComponents,
                                          optimisedView: ChooseWhereWorkView,
                                          subOptimisedView: SubOptimisedChooseWhereWorkView,
                                          checkYourAnswersService: CheckYourAnswersService,
                                          compareAnswerService: CompareAnswerService,
                                          dataCacheConnector: DataCacheConnector,
                                          decisionService: DecisionService,
                                          navigator: ControlNavigator,
                                          implicit val appConfig: FrontendAppConfig) extends BaseNavigationController(
  controllerComponents,compareAnswerService,dataCacheConnector,navigator,decisionService) with FeatureSwitching {

  private def view(form: Form[ChooseWhereWork], mode: Mode)(implicit request: Request[_]): HtmlFormat.Appendable =
    if(isEnabled(OptimisedFlow)) optimisedView(form, mode) else subOptimisedView(form, mode)

  def onPageLoad(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    Ok(view(fillForm(ChooseWhereWorkPage, formProvider()), mode))
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    formProvider().bindFromRequest().fold(
      formWithErrors =>
        Future.successful(BadRequest(view(formWithErrors, mode))),
      value => {
        redirect(mode,value,ChooseWhereWorkPage,callDecisionService = true)
      }
    )
  }
}
