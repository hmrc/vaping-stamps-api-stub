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

package uk.gov.hmrc.vapingstampsapistub.utils

import play.api.libs.json.{JsObject, JsValue, Json}
import uk.gov.hmrc.vapingstampsapistub.models.BusinessApproval

object ExpectedResponses {

  val successJson: JsValue = Json.toJson(
    BusinessApproval(
      approvalStatus = "APPROVED",
      businessName = "Example Trading Ltd",
      addressLine1 = "10 Example Street",
      addressLine2 = Some("London"),
      postCode = "SW1A 1AA",
      contactName = Some("Jane Smith"),
      telephoneNumber = Some("+44 20 7946 0123"),
      stampsThreshold = 500000
    )
  )

  val badRequestJson: JsObject = errorJsonBuilder("INVALID_REQUEST", "The request payload is invalid or malformed.")
  val unauthorizedJson: JsObject =
    errorJsonBuilder("UNAUTHORISED", "Authentication credentials are missing or invalid.")
  val forbiddenJson: JsObject = errorJsonBuilder("FORBIDDEN", "You are not authorised to access this resource.")
  val notFoundJson: JsObject = errorJsonBuilder("NOT_FOUND", "The requested approval could not be found.")
  val conflictJson: JsObject =
    errorJsonBuilder("CONFLICT", "The request conflicts with the current state of the resource.")
  val internalServerErrorJson: JsObject =
    errorJsonBuilder("INTERNAL_SERVER_ERROR", "An unexpected error occurred while processing the request.")
  val serviceUnavailableJson: JsObject =
    errorJsonBuilder("SERVICE_UNAVAILABLE", "The service is temporarily unavailable. Please try again later.")

  private def errorJsonBuilder(code: String, message: String): JsObject =
    Json.obj(
      "code"    -> code,
      "message" -> message
    )
}
