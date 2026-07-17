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
import play.api.libs.json.{JsObject, JsValue, Json}
import play.api.mvc.{Action, ControllerComponents}
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController
import uk.gov.hmrc.vapingstampsapistub.models.{BusinessApproval, BusinessNotApproved, StampsReferenceNumber, VDSDetails}

import javax.inject.{Inject, Singleton}

@Singleton
class EisEtdsController @Inject() (
  cc: ControllerComponents
) extends BackendController(cc) with Logging:

  def checkApprovalStatus(): Action[JsValue] =
    Action(parse.json) { implicit request =>
      request.body
        .validate[VDSDetails]
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

  private def processRequest(stampsReferenceNumber: StampsReferenceNumber) =
    logger.info(s"Checking approval status for stampsReferenceNumber=$stampsReferenceNumber")
    (stampsReferenceNumber.isNorthernIsland, stampsReferenceNumber.numberString) match

      case (false, "0000200") =>
        Ok(
          Json.toJson(
            BusinessApproval(
              approvalStatus = "approved",
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
      case (true, "0000200") =>
        Ok(
          Json.toJson(
            BusinessApproval(
              approvalStatus = "approved",
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
      case (_, "0000266") =>
        Ok(
          Json.toJson(
            BusinessNotApproved(
              approvalStatus = "not approved"
            )
          )
        )
      case (_, "0000401") =>
        Unauthorized
      case (_, "0000403") =>
        Forbidden
      case (_, "0000422") =>
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
      case (_, "1000422") =>
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
      case (_, "2000422") =>
        UnprocessableEntity(
          Json.obj(
            "errorDetail" -> Json.obj(
              "errorCode"         -> "422",
              "errorMessage"      -> "Unprocessable Entity",
              "source"            -> "backend",
              "sourceFaultDetail" -> Json.obj(
                "detail" -> Seq("003")
              ),
              "timestamp"     -> "2026-06-23T15:05:07.236916",
              "correlationId" -> "2f6bb2ff-4279-4d84-931c-60da02f5026d"
            )
          )
        )
      case (_, "0000500") =>
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
      case (_, "1000503") =>
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
      case (_, "0000503") => BadGateway
      case (_, "2000503") =>
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
