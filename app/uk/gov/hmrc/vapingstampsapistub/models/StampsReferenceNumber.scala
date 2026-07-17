/*
 * Copyright 2026 HM Revenue & Customs
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

package uk.gov.hmrc.vapingstampsapistub.models

import play.api.libs.json
import play.api.libs.json.*

case class StampsReferenceNumber(srn: String) {
  given prefix: String = srn.take(2)

  val isNorthernIsland: Boolean = prefix startsWith "XI"

  val numberString: String = srn.filter(_.isDigit)
}

object StampsReferenceNumber:
  val regex = "^(GB|XI)V[ACEFMR][0-9]{7}DS$"

  implicit val reads: Reads[StampsReferenceNumber] = __
    .read[String]
    .filter(json.JsonValidationError("Validation failed"))(_.matches(regex))
    .map(StampsReferenceNumber(_))

  given format: Format[StampsReferenceNumber] =
    Format(
      reads,
      Json.writes[StampsReferenceNumber]
    )
