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

object FinancialRiskACluster extends Cluster {

  /**
    * Use this value to informatively name the cluster and use as a key to tags
    */
  override def name: String = "financialRiskA"

  override def clusterID: Int = 2

  val clusterElements: List[Element] = List(
    Element("workerPaidInclusive", RADIO, 0, this),
    Element("workerNeedConsumablesMaterials", RADIO, 1, this),
    Element("workerProvideConsumablesMaterials", RADIO, 2, this),
    Element("engagerPayConsumables", RADIO, 3, this),
    Element("workerNeedEquipment", RADIO, 4, this),
    Element("workerProvideEquipment", RADIO, 5, this),
    Element("engagerPayEquipment", RADIO, 6, this)
  )

  private val flows = List(
    FlowElement("financialRiskA.workerPaidInclusive",
      Map("financialRiskA.workerPaidInclusive" -> "Yes"),
      Option.empty),
    FlowElement("financialRiskA.workerNeedConsumablesMaterials",
      Map("financialRiskA.workerNeedConsumablesMaterials" -> "No"),
      Option("financialRiskA.workerNeedEquipment")),
    FlowElement("financialRiskA.workerProvideConsumablesMaterials",
      Map("financialRiskA.workerProvideConsumablesMaterials" -> "No"),
      Option("financialRiskA.workerNeedEquipment")),
    FlowElement("financialRiskA.engagerPayConsumables",
      Map("financialRiskA.engagerPayConsumables" -> "No"),
      Option.empty),
    FlowElement("financialRiskA.workerNeedEquipment",
      Map("financialRiskA.workerNeedEquipment" -> "No"),
      Option.empty),
    FlowElement("financialRiskA.workerProvideEquipment",
      Map("financialRiskA.workerProvideEquipment" -> "No"),
      Option.empty)
  )

  /**
    * Returns the next element in the cluster or empty if we should ask for a decision
    *
    * @param clusterAnswers
    * @return
    */
  override def shouldAskForDecision(clusterAnswers: List[(String, String)], currentQnA: (String, String)): Option[Element] = {
    if (allQuestionsAnswered(clusterAnswers))Option.empty
    else
      getNextQuestionElement(clusterAnswers, currentQnA)

  }

  def getNextQuestionElement(clusterAnswers: List[(String, String)], currentQnA: (String, String)): Option[Element] = {
    val currentQuestionFlowElements = flows.filter(_.currentQuestion.equalsIgnoreCase(currentQnA._1))
    val relevantFlowElement = currentQuestionFlowElements.filter{
      element => element.answers.forall(clusterAnswers.contains(_))
    }
    if(relevantFlowElement.isEmpty) findNextQuestion(currentQnA)
    else
      getElementForQuestionTag(relevantFlowElement.head.nextQuestion.getOrElse(""))
  }

}