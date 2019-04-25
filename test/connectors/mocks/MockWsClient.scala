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

package connectors.mocks

import connectors.utils.WsUtils
import org.mockito.Mockito.when
import org.scalatestplus.mockito.MockitoSugar
import play.api.libs.ws.{BodyWritable, WSClient, WSRequest, WSResponse}

import scala.concurrent.Future

trait MockWsClient extends WsUtils with MockitoSugar {

  val mockWsClient: WSClient = mock[WSClient]
  val mockWsRequest: WSRequest = mock[WSRequest]

  def setupMockHttpPost[I,O](url: String, model: I)(response: Future[WSResponse])(implicit writesI: BodyWritable[I]): Unit = {
    when(mockWsClient.url(url)).thenReturn(mockWsRequest)
    when(mockWsRequest.post(model)).thenReturn(response)
  }
}