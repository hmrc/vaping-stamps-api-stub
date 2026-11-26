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
import uk.gov.hmrc.vapingstampsapistub.models.{BusinessApproval, BusinessNotApproved}

object ExpectedResponses {

  val successJson: JsValue = Json.toJson(
    BusinessApproval(
      approvalStatus = "approved",
      businessName = "Test Company",
      addressLine1 = "ABC Street",
      addressLine2 = Some("AB Town"),
      postCode = "ZZ1 1ZZ",
      contactName = Some("Firstname Surname"),
      telephoneNumber = Some("+44 20 7123 4567"),
      stampsThreshold = 500000
    )
  )

  val businessApprovalFormatErrorJson: JsValue = Json.toJson(
    BusinessApproval(
      approvalStatus = "REVOKED",
      businessName = "Test Company",
      addressLine1 = "ABC Street",
      addressLine2 = Some("AB Town"),
      postCode = "ZZ1 1ZZ",
      contactName = Some("Firstname Surname"),
      telephoneNumber = Some("+44 20 7123 4567"),
      stampsThreshold = 500000
    )
  )

  val successNIJson: JsValue = Json.toJson(
    BusinessApproval(
      approvalStatus = "approved",
      businessName = "Test Company",
      addressLine1 = "ABC Street",
      addressLine2 = Some("NI Town"),
      postCode = "NI1 1ZZ",
      contactName = Some("Firstname Surname"),
      telephoneNumber = Some("+44 20 7123 4567"),
      stampsThreshold = 500000
    )
  )

  val partialSuccessJson: JsValue = Json.toJson(
    BusinessNotApproved(
      approvalStatus = "not approved"
    )
  )

  val badRequestJson: JsObject = Json.obj(
    "errorDetail" -> Json.obj(
      "correlationId"     -> "2f6bb2ff-4279-4d84-931c-60da02f5026d",
      "errorCode"         -> "400",
      "errorMessage"      -> "Invalid JSON document",
      "source"            -> "journey-vds03-service-camel",
      "sourceFaultDetail" -> Json.obj(
        "detail" -> Seq("Invalid JSON payload")
      ),
      "timestamp" -> "2026-06-23T15:05:07.236916"
    )
  )

  def unprocessableEntityJson(errorCode: String): JsObject =
    Json.obj(
      "errorDetail" -> Json.obj(
        "errorCode"         -> "422",
        "errorMessage"      -> "Unprocessable Entity",
        "source"            -> "backend",
        "sourceFaultDetail" -> Json.obj(
          "detail" -> Seq(errorCode)
        ),
        "timestamp"     -> "2026-06-23T15:05:07.236916",
        "correlationId" -> "2f6bb2ff-4279-4d84-931c-60da02f5026d"
      )
    )
  def errorJson(code: Seq[String] = Seq(""), message: String, statusCode: String): JsObject =
    errorJsonBuilder(code, message, statusCode)

  private def errorJsonBuilder(code: Seq[String], message: String, statusCode: String): JsObject =
    Json.obj(
      "errorDetail" -> Json.obj(
        "correlationId"     -> "2f6bb2ff-4279-4d84-931c-60da02f5026d",
        "errorCode"         -> statusCode,
        "errorMessage"      -> message,
        "source"            -> "backend",
        "sourceFaultDetail" -> Json.obj(
          "detail" -> code
        ),
        "timestamp" -> "2026-06-23T15:05:07.236916"
      )
    )
}
