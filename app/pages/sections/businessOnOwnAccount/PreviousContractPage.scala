/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package pages.sections.businessOnOwnAccount

import pages.QuestionPage

case object PreviousContractPage extends QuestionPage[Boolean] {

  override def toString: String = "previousContract"
}
