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

package assets.messages

object HowWorkIsDoneMessages extends BaseMessages {

  object Worker {
    val heading = "Once you start the engagement, does the end client have the right to decide how the work is done?"
    val title = heading
    val hint = "This does not include general induction, or the need to follow statutory requirements like health and safety."
    val yesClientDecides = "Yes - the end client decides how the work needs to be done without my input"
    val noWorkerDecides = "No - I decide how the work needs to be done without input from the end client"
    val noSkilledRole = "No - the end client cannot decide how the work needs to be done because it is a highly skilled role"
    val partly = "Partly - I and other people employed or engaged by the end client agree how the work needs to be done"
  }

  object Hirer {
    val heading = "Once the worker starts the engagement, do you have the right to decide how the work is done?"
    val title = heading
    val hint = "This does not include general induction, or the need to follow statutory requirements like health and safety."
    val yesClientDecides = "Yes - we decide how the work needs to be done without input from the worker"
    val noWorkerDecides = "No - the worker decides how the work needs to be done without your input"
    val noSkilledRole = "No - we cannot decide how the work needs to be done because it is a highly skilled role"
    val partly = "Partly - the worker and other people employed or engaged by your organisation agree how the work needs to be done"
  }

  object OptimisedWorker {
    val error = "Select if your client will decide how the work is done"
    val heading = "Will your client decide how the work is done?"
    val title = heading
    val p1 = "This can include your client instructing, guiding or advising the way the task should be completed."
    val yesClientDecides = "Yes"
    val noWorkerDecides = "No, you solely decide"
    val partly = "No, you and your client agree together"
    val noSkilledRole = "No, because it is highly skilled work"
  }

  object OptimisedHirer {
    val error = "Select yes if your organisation will decide how the work is done"
    val heading = "Will your organisation decide how the work is done?"
    val title = heading
    val p1 = "This can include your organisation instructing, guiding or advising the way the task should be completed."
    val yesClientDecides = "Yes"
    val noWorkerDecides = "No, the worker solely decides"
    val partly = "No, your organisation and the worker agree together"
    val noSkilledRole = "No, because it is highly skilled work"
  }

  object NonTailored {
    val heading = "Once the worker starts the engagement, does the end client have the right to decide how the work is done?"
    val title = heading
    val hint = "This does not include general induction, or the need to follow statutory requirements like health and safety."
    val yesClientDecides = "Yes - the end client decides how the work needs to be done without input from the worker"
    val noWorkerDecides = "No - the worker decides how the work needs to be done without input from the end client"
    val noSkilledRole = "No - the end client cannot decide how the work needs to be done because it is a highly skilled role"
    val partly = "Partly - the worker and other people employed or engaged by the end client agree how the work needs to be done"
  }
}
