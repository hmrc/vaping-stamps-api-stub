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
import play.api.mvc.{Action, AnyContent, ControllerComponents}
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController
import uk.gov.hmrc.vapingstampsapistub.models.BusinessApproval
import play.api.libs.json.Json

import javax.inject.{Inject, Singleton}

@Singleton
class EisEtdsController @Inject() (
                                    cc: ControllerComponents
                                  ) extends BackendController(cc)
  with Logging {

  private val approvalIdRegex = "^[A-Z]{4}[0-9]{7}[A-Z]{2}$".r

  def checkApprovalStatus(vdsApprovalId: String): Action[AnyContent] = Action { implicit request =>
    logger.info(s"Checking approval status for vdsApprovalId=$vdsApprovalId")

    // ---- 400: invalid approval id format
    if (!approvalIdRegex.matches(vdsApprovalId)) {
      BadRequest(Json.obj(
        "code" -> "INVALID_REQUEST",
        "message" -> "The request payload is invalid or malformed."
      ))
    } else {
      vdsApprovalId match {

        // ---- 200: approved status
        case "ABCD1234567EF" =>
          Ok(Json.toJson(
            BusinessApproval(
              approvalStatus = "APPROVED",
              businessName = "Example Trading Ltd",
              registeredBusinessAddress = "10 Example Street, London, SW1A 1AA",
              correspondenceAddress = "PO Box 123, London, SW1A 2AB",
              contactName = "Jane Smith",
              contactTelephone = "+44 20 7946 0123",
              contactEmail = "jane.smith@example.com",
              approvalNumber = vdsApprovalId,
              stampThreshold = 500000
            )
          ))

        // ---- 204: processed successfully, no content
        case "AAAA0000000BB" =>
          NoContent

        // ---- 401: unauthorized
        case "AAAA0000401BB" =>
          Unauthorized(Json.obj(
            "code" -> "UNAUTHORISED",
            "message" -> "Authentication credentials are missing or invalid."
          ))
        // ---- 403: forbidden
        case "AAAA0000403BB" =>
          Forbidden(Json.obj(
            "code" -> "FORBIDDEN",
            "message" -> "You are not authorised to access this resource."
          ))
        // ---- 409: conflict
        case "AAAA0000409BB" =>
          Conflict(Json.obj(
            "code" -> "CONFLICT",
            "message" -> "The request conflicts with the current state of the resource."
          ))
        // ---- 500: internal server error
        case "AAAA0000500BB" =>
          InternalServerError(Json.obj(
            "code" -> "INTERNAL_SERVER_ERROR",
            "message" -> "An unexpected error occurred while processing the request."
          ))
        // ---- 503: service unavailable
        case "AAAA0000503BB" =>
          ServiceUnavailable(Json.obj(
            "code" -> "SERVICE_UNAVAILABLE",
            "message" -> "The service is temporarily unavailable. Please try again later."
          ))
        // ---- 404: not found
        case _ =>
          NotFound(Json.obj(
            "code" -> "NOT_FOUND",
            "message" -> "The requested approval could not be found."
          ))
      }
    }
  }
}
