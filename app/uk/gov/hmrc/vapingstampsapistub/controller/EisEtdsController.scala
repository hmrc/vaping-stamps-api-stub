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
import play.api.mvc.{Action, AnyContent, ControllerComponents}
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController
import uk.gov.hmrc.vapingstampsapistub.models.{ApprovalRequest, BusinessApproval}

import javax.inject.{Inject, Singleton}

@Singleton
class EisEtdsController @Inject() (
  cc: ControllerComponents
) extends BackendController(cc) with Logging:

  private val approvalIdRegex = "^GBVA[0-9]{7}DS$".r

  def checkApprovalStatus(): Action[JsValue] =
    Action(parse.json) { implicit request =>
      request.body
        .validate[ApprovalRequest]
        .fold(
          _ =>
            logger.error(s"The request payload is invalid or malformed.")
            BadRequest(
              Json.obj(
                "datetime"     -> "2021-12-17T09:30:47Z",
                "errorCode"    -> Seq("001", "002", "010"),
                "errorMessage" -> "The request payload is invalid or malformed."
              )
            )
          ,
          req => processRequest(req.vdsApprovalId)
        )
    }

  def getApprovalStatus(vdsApprovalId: String): Action[AnyContent] =
    Action { implicit request =>
      processRequest(vdsApprovalId)
    }

  private def processRequest(vdsApprovalId: String) =
    logger.info(s"Checking approval status for vdsApprovalId=$vdsApprovalId")
    if !approvalIdRegex.matches(vdsApprovalId) then
      logger.error(s"The request payload is invalid or malformed: $vdsApprovalId.")
      BadRequest(
        Json.obj(
          "datetime"     -> "2021-12-17T09:30:47Z",
          "errorCode"    -> Seq("001", "002", "010"),
          "errorMessage" -> "The request payload is invalid or malformed."
        )
      )
    else
      vdsApprovalId match

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
        case "GBVA0000401DS" =>
          Unauthorized(
            Json.obj(
              "datetime"     -> "2021-12-17T09:30:47Z",
              "errorCode"    -> Seq("001"),
              "errorMessage" -> "Authentication credentials are missing or invalid."
            )
          )
        case "GBVA0000403DS" =>
          Forbidden(
            Json.obj(
              "datetime"     -> "2021-12-17T09:30:47Z",
              "errorCode"    -> Seq("001"),
              "errorMessage" -> "You are not authorised to access this resource."
            )
          )
        case "GBVA0000422DS" =>
          UnprocessableEntity(
            Json.obj(
              "datetime"     -> "2021-12-17T09:30:47Z",
              "errorCode"    -> Seq("001"),
              "errorMessage" -> "Business validation failure"
            )
          )
        case "GBVA0000500DS" =>
          InternalServerError(
            Json.obj(
              "datetime" -> "2021-12-17T09:30:47Z",
              "message"  -> "An unexpected error occurred while processing the request."
            )
          )
        case _ =>
          NotFound(
            Json.obj(
              "datetime"     -> "2021-12-17T09:30:47Z",
              "errorCode"    -> Seq("001"),
              "errorMessage" -> "The requested approval could not be found."
            )
          )
