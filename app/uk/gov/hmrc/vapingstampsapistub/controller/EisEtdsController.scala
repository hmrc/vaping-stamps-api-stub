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

package uk.gov.hmrc.vapingstampsapistub.controller

import play.api.Logging
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.{Action, ControllerComponents}
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController
import uk.gov.hmrc.vapingstampsapistub.models.{ApprovalRequest, BusinessApproval, BusinessNotApproved}

import javax.inject.{Inject, Singleton}

@Singleton
class EisEtdsController @Inject() (
  cc: ControllerComponents
) extends BackendController(cc) with Logging:

  def checkApprovalStatus(): Action[JsValue] =
    Action(parse.json) { implicit request =>
      request.body
        .validate[ApprovalRequest]
        .fold(
          _ =>
            logger.error(s"The request payload is invalid or malformed.")
            BadRequest(
              Json.obj(
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
            )
          ,
          req => processRequest(req.stampsReferenceNumber)
        )
    }

  private def processRequest(stampsReferenceNumber: String) =
    logger.info(s"Checking approval status for stampsReferenceNumber=$stampsReferenceNumber")
    stampsReferenceNumber match

      case "GBVA0000200DS" =>
        Ok(
          Json.toJson(
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
        )
      case "XIVA0000200DS" =>
        Ok(
          Json.toJson(
            BusinessApproval(
              approvalStatus = "APPROVED",
              businessName = "Example Trading Ltd",
              addressLine1 = "10 Example Street",
              addressLine2 = Some("Belfast"),
              postCode = "BT1 1AA",
              contactName = Some("Jane Smith"),
              telephoneNumber = Some("+44 20 7946 0123"),
              stampsThreshold = 500000
            )
          )
        )
      case "GBVA0000266DS" | "XIVA0000266DS" =>
        Ok(
          Json.toJson(
            BusinessNotApproved(
              approvalStatus = "NOT_APPROVED"
            )
          )
        )
      case "GBVA0000401DS" | "XIVA0000401DS" =>
        Unauthorized
      case "GBVA0000403DS" | "XIVA0000403DS" =>
        Forbidden
      case "GBVA0000422DS" | "XIVA0000422DS" =>
        UnprocessableEntity(
          Json.obj(
            "errorDetail" -> Json.obj(
              "errorCode"         -> "422",
              "errorMessage"      -> "Unprocessable Entity",
              "source"            -> "backend",
              "sourceFaultDetail" -> Json.obj(
                "detail" -> Seq("001")
              ),
              "timestamp"     -> "2026-06-23T15:05:07.236916",
              "correlationId" -> "2f6bb2ff-4279-4d84-931c-60da02f5026d"
            )
          )
        )
      case "GBVA1000422DS" | "XIVA1000422DS" =>
        UnprocessableEntity(
          Json.obj(
            "errorDetail" -> Json.obj(
              "errorCode"         -> "422",
              "errorMessage"      -> "Unprocessable Entity",
              "source"            -> "backend",
              "sourceFaultDetail" -> Json.obj(
                "detail" -> Seq("002")
              ),
              "timestamp"     -> "2026-06-23T15:05:07.236916",
              "correlationId" -> "2f6bb2ff-4279-4d84-931c-60da02f5026d"
            )
          )
        )
      case "GBVA0000500DS" | "XIVA0000500DS" =>
        Ok(
          Json.toJson(
            BusinessApproval(
              approvalStatus = "REVOKED",
              businessName = "Example Trading Ltd",
              addressLine1 = "10 Example Street",
              addressLine2 = Some("London"),
              postCode = "SW1A 1AA",
              contactName = Some("Jane Smith"),
              telephoneNumber = Some("+44 20 7946 0123"),
              stampsThreshold = 500000
            )
          )
        )
      case "GBVA1000502DS" | "XIVA1000502DS" =>
        InternalServerError(
          Json.obj(
            "errorDetail" -> Json.obj(
              "correlationId"     -> "2f6bb2ff-4279-4d84-931c-60da02f5026d",
              "errorCode"         -> "500",
              "errorMessage"      -> "Internal Server Error",
              "source"            -> "backend",
              "sourceFaultDetail" -> Json.obj(
                "detail" -> Seq("Error occurred in request to ETDS")
              ),
              "timestamp" -> "2026-06-23T15:05:07.236916"
            )
          )
        )
      case "GBVA0000502DS" | "XIVA0000502DS" => BadGateway
      case "GBVA2000502DS" | "XIVA2000502DS" =>
        ServiceUnavailable(
          Json.obj(
            "errorDetail" -> Json.obj(
              "correlationId"     -> "2f6bb2ff-4279-4d84-931c-60da02f5026d",
              "errorCode"         -> "503",
              "errorMessage"      -> "503 error",
              "source"            -> "backend",
              "sourceFaultDetail" -> Json.obj(
                "detail" -> Seq("")
              ),
              "timestamp" -> "2026-06-23T15:05:07.236916"
            )
          )
        )
      case _ =>
        UnprocessableEntity(
          Json.obj(
            "errorDetail" -> Json.obj(
              "errorCode"         -> "422",
              "errorMessage"      -> "Unprocessable Entity",
              "source"            -> "backend",
              "sourceFaultDetail" -> Json.obj(
                "detail" -> Seq("001")
              ),
              "timestamp"     -> "2026-06-23T15:05:07.236916",
              "correlationId" -> "2f6bb2ff-4279-4d84-931c-60da02f5026d"
            )
          )
        )
