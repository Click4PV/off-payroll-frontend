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

package controllers.sections.setup

import config.FrontendAppConfig
import controllers.BaseController
import controllers.actions._
import javax.inject.Inject

import config.featureSwitch.FeatureSwitching
import connectors.DataCacheConnector
import models.NormalMode
import navigation.Navigator
import pages.sections.setup.WorkerAdvisoryPage
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import services.{CheckYourAnswersService, CompareAnswerService, DecisionService}
import views.html.sections.setup.WorkerAdvisoryView

class WorkerAdvisoryController @Inject()(navigator: Navigator,
                                         identify: IdentifierAction,
                                         getData: DataRetrievalAction,
                                         requireData: DataRequiredAction,
                                         controllerComponents: MessagesControllerComponents,
                                         view: WorkerAdvisoryView,
                                         checkYourAnswersService: CheckYourAnswersService,
                                         compareAnswerService: CompareAnswerService,
                                         dataCacheConnector: DataCacheConnector,
                                         decisionService: DecisionService,
                                         implicit val appConfig: FrontendAppConfig) extends BaseController(
  controllerComponents,compareAnswerService,dataCacheConnector,navigator,decisionService) with FeatureSwitching {

  def onPageLoad: Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    Ok(view(
      postAction = routes.WorkerAdvisoryController.onSubmit(),
      finishAction = controllers.routes.ExitSurveyController.redirectToExitSurvey()
    ))
  }

  def onSubmit: Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    Redirect(navigator.nextPage(WorkerAdvisoryPage, NormalMode)(request.userAnswers))
  }
}
