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

import uk.gov.hmrc.offpayroll.util.Base62EncoderDecoder.{decode, encode}

import scala.annotation.tailrec

import uk.gov.hmrc.offpayroll.util.ElementBitAssemblerImplicits._

case class CompressedInterview(str: String) {
  def asLong: Long = {
    decode(str)
  }

  def asValueWidthPairs: List[(Int, Int)] = {
    def longAndWidthsToValueWidthPairs(l: Long, widths: List[Int]): List[(Int, Int)] = {
      def go(l: Long, widths: List[Int], acc: List[(Int, Int)]): List[(Int, Int)] = widths match {
        case Nil => List()
        case w :: Nil => (l.toInt, w) :: acc
        case w :: xs => go(l >> w, xs, ((l & ((1 << w) - 1)).toInt, w) :: acc)
      }

      go(l, widths.reverse, List())
    }

    val l = decode(str)
    longAndWidthsToValueWidthPairs(l, ElementProvider.toWidths)
  }

  def asValues: List[Int] = asValueWidthPairs.map { case (v, _) => v }

  def asMap: Map[String, List[String]] = {
    val elementIntAnswers = ElementProvider.toElements.zip(asValues)
    elementIntAnswers.map { case (e, a) => (e.questionTag, e.fromBitValue(a)) }.toMap
  }
}

object CompressedInterview {
  def apply(l: Long): CompressedInterview = new CompressedInterview(encode(l))

  def apply(pairs: List[(Int, Int)]) = {
    def valuesWidthPairsToLong(p: List[(Int, Int)]): Long = {
      @tailrec
      def go(p: List[(Int, Int)], acc: Long): Long = p match {
        case Nil => acc
        case (v, w) :: xs => go(xs, (acc << w) | v)
      }

      go(p, 0L)
    }

    val l = valuesWidthPairsToLong(pairs)
    new CompressedInterview(encode(l))
  }
}
