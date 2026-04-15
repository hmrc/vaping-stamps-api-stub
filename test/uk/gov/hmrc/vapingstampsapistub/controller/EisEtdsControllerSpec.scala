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

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import play.api.http.Status.*
import play.api.libs.json.{JsBoolean, JsObject, Json}
import play.api.mvc.ControllerComponents
import play.api.test.Helpers.{POST, contentAsJson, defaultAwaitTimeout, status}
import play.api.test.{FakeRequest, Helpers}
import uk.gov.hmrc.vapingstampsapistub.utils.ExpectedResponses.*

class EisEtdsControllerSpec extends AnyWordSpec with Matchers {

  private val stubControllerComponents: ControllerComponents = Helpers.stubControllerComponents()
  val controller: EisEtdsController = new EisEtdsController(stubControllerComponents)

  "checkApprovalStatus" should {
    "return 400 status code" when {
      "Invalid Request is supplied" in {
        val invalidBody: JsObject = Json.obj("invalidField" -> JsBoolean(true))
        val request = FakeRequest(POST, "/etds/vaping/stamps/status").withBody(invalidBody)
        val response = controller.checkApprovalStatus().apply(request)

        status(response) shouldBe BAD_REQUEST

        contentAsJson(response) shouldBe badRequestJson
      }

      "Invalid vdsApprovalId is supplied" in {
        val invalidBody = Json.obj(
          "contactEmail"  -> "email@example.com",
          "vdsApprovalId" -> "approvalId"
        )
        val request = FakeRequest(POST, "/etds/vaping/stamps/status").withBody(invalidBody)
        val response = controller.checkApprovalStatus().apply(request)

        status(response) shouldBe BAD_REQUEST

        contentAsJson(response) shouldBe badRequestJson
      }
    }

    Seq(
      (OK, "GBVA0000200DS", successJson),
      (UNAUTHORIZED, "GBVA0000401DS", unauthorizedJson),
      (FORBIDDEN, "GBVA0000403DS", forbiddenJson),
      (NOT_FOUND, "GBVA0000404DS", notFoundJson),
      (CONFLICT, "GBVA0000409DS", conflictJson),
      (INTERNAL_SERVER_ERROR, "GBVA0000500DS", internalServerErrorJson),
      (SERVICE_UNAVAILABLE, "GBVA0000503DS", serviceUnavailableJson)
    ) foreach { case (statusCode, approvalId, json) =>
      s"return $statusCode when request made for id $approvalId" in {
        val requestBody = Json.obj(
          "contactEmail"  -> "email@example.com",
          "vdsApprovalId" -> approvalId
        )
        val request = FakeRequest(POST, "/etds/vaping/stamps/status").withBody(requestBody)
        val response = controller.checkApprovalStatus().apply(request)

        status(response) shouldBe statusCode
        contentAsJson(response) shouldBe json
      }
    }

  }
}
