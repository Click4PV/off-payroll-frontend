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

object DidPaySubstituteMessages extends BaseMessages {

  val subheading = "About substitutes and helpers"

  object Worker {
    val heading = "Did you pay the person who did the work instead of you?"
    val title = heading
    val exclamation = "If the substitute was paid by an agency, it does not count as substitution."
  }

  object Hirer {
    val heading = "Did the worker’s business pay the person who did the work instead of them?"
    val title = heading
    val exclamation = "If the substitute was paid by an agency, it does not count as substitution."
  }

  object NonTailored {
    val heading = "Did the worker’s business pay the person who did the work instead of them?"
    val title = heading
    val exclamation = "If the substitute was paid by an agency, it does not count as substitution."
  }

}