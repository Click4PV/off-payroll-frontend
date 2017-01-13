/*
 * Copyright 2017 HM Revenue & Customs
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

package uk.gov.hmrc.offpayroll.models


/**
  * Created by peter on 15/12/2016.
  */
object PersonalServiceCluster extends Cluster {

  /**
    * Use this value to informatively name the cluster and use as a key to tags
    */
  override def name: String = "personalService"

  override def clusterID: Int = 0

  val clusterElements: List[Element] = List(
    Element("contractualObligationForSubstitute", RADIO, 0, this),
    Element("contractualObligationInPractise", RADIO, 1, this),
    Element("contractualRightForSubstitute", RADIO, 2, this),
    Element("actualRightToSendSubstitute", RADIO, 3, this),
    Element("contractualRightReflectInPractise", RADIO, 4, this),
    Element("engagerArrangeIfWorkerIsUnwillingOrUnable", RADIO, 5, this),
    Element("possibleSubstituteRejection", RADIO, 6, this),
    Element("contractTermsWorkerPaysSubstitute", RADIO, 7, this),
    Element("workerSentActualSubstitute", RADIO, 8, this),
    Element("actualSubstituteRejection", RADIO, 9, this),
    Element("possibleHelper", RADIO, 10, this),
    Element("wouldWorkerPayHelper", RADIO, 11, this),
    Element("workerSentActualHelper", RADIO, 12, this),
    Element("workerPayActualHelper", RADIO, 13, this)
  )

}
