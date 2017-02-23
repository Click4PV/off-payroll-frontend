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

package uk.gov.hmrc.offpayroll.util

import uk.gov.hmrc.offpayroll.models.Element
import uk.gov.hmrc.offpayroll.util.ElementBitAssemblerImplicits._
import uk.gov.hmrc.offpayroll.util.ElementBitSplitterImplicits._

object InterviewStack {
  def push(previous: CompressedInterview, values: String, element: Element): CompressedInterview = {
    val pairs = previous.asValueWidthPairs
    elementIndex(element) match {
      case None => previous
      case Some(index) => CompressedInterview(insertAtIndexZeroRight(pairs, index, element.toBitPair(values)))
    }
  }

  def pop(previous: CompressedInterview, element: Element): (CompressedInterview, String) = {
    val (interview, bitValue) = doPop(previous, element)
    (interview, element.fromBitValue(bitValue))
  }

  def doPop(previous: CompressedInterview, element: Element): (CompressedInterview, Int) = {
    val pairs = previous.asValueWidthPairs
    elementIndex(element) match {
      case None => (previous, 0)
      case Some(index) => deleteAndReadIndexZeroRight(pairs, index)
    }
  }

  def peek(previous: CompressedInterview, element: Element): (CompressedInterview, String) = {
    val (interview, bitValue) = doPeek(previous, element)
    (interview, element.fromBitValue(bitValue))
  }

  def doPeek(previous: CompressedInterview, element: Element): (CompressedInterview, Int) = {
    val pairs = previous.asValueWidthPairs
    elementIndex(element) match {
      case None => (previous, 0)
      case Some(index) =>
        val (v,_) = pairs(index)
        (previous, v)
    }
  }

  private def insertAtIndexZeroRight(pairs: List[(Int,Int)], index: Int, pair: (Int,Int)): List[(Int,Int)] = {
    val (a,b) = pairs.splitAt(index)
    a ::: List(pair) ::: b.drop(1).map { case (_,w) => (0,w) }
  }

  private def deleteAndReadIndexZeroRight(pairs: List[(Int,Int)], index: Int): (CompressedInterview, Int) = {
    val (a,b) = pairs.splitAt(index)
    val (v,_) = b.head
    val compressedInterview = CompressedInterview(a ::: b.map { case (_,w) => (0,w) })
    (compressedInterview, v)
  }

  def elementIndex(element: Element): Option[Int] = {
    val found = ElementProvider.toElements.zipWithIndex.collect{ case (e, i) if (e == element) => i }
    found.headOption
  }

}
