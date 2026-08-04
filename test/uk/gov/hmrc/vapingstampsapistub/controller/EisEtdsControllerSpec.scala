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

      Seq(
        (OK, "GBVC0000200DS", successJson),
        (OK, "GBVC0000266DS", partialSuccessJson),
        (OK, "XIVC0000200DS", successNIJson),
        (OK, "XIVC0000266DS", partialSuccessJson),
        (UNPROCESSABLE_ENTITY, "GBVC0000422DS", unprocessableEntityJson("001")),
        (UNPROCESSABLE_ENTITY, "XIVC0000422DS", unprocessableEntityJson("001")),
        (UNPROCESSABLE_ENTITY, "GBVC1000422DS", unprocessableEntityJson("002")),
        (UNPROCESSABLE_ENTITY, "XIVC1000422DS", unprocessableEntityJson("002")),
        (UNPROCESSABLE_ENTITY, "GBVC2000422DS", unprocessableEntityJson("003")),
        (UNPROCESSABLE_ENTITY, "XIVC2000422DS", unprocessableEntityJson("003")),
        (UNPROCESSABLE_ENTITY, "GBVC1111111DS", unprocessableEntityJson("001")),
        (UNPROCESSABLE_ENTITY, "XIVC1111111DS", unprocessableEntityJson("001")),
        (OK, "GBVC0000500DS", businessApprovalFormatErrorJson),
        (OK, "XIVC0000500DS", businessApprovalFormatErrorJson),
        (
          INTERNAL_SERVER_ERROR,
          "GBVC1000503DS",
          errorJson(
            code = Seq("Error occurred in request to ETDS"),
            message = "Internal Server Error",
            statusCode = "500"
          )
        ),
        (
          INTERNAL_SERVER_ERROR,
          "XIVC1000503DS",
          errorJson(
            code = Seq("Error occurred in request to ETDS"),
            message = "Internal Server Error",
            statusCode = "500"
          )
        ),
        (SERVICE_UNAVAILABLE, "GBVC2000503DS", errorJson(message = "503 error", statusCode = "503")),
        (SERVICE_UNAVAILABLE, "XIVC2000503DS", errorJson(message = "503 error", statusCode = "503"))
      ) foreach { case (statusCode, stampsReferenceNumber, json) =>
        s"return $statusCode when request made for id $stampsReferenceNumber" in {
          val requestBody = Json.obj(
            "vdsdetails" -> Json.obj(
              "vdsEmail"              -> "email@example.com",
              "stampsReferenceNumber" -> stampsReferenceNumber
            )
          )
          val request = FakeRequest(POST, "/etds/vaping/stamps/status").withBody(requestBody)
          val response = controller.checkApprovalStatus().apply(request)

          status(response) shouldBe statusCode
          contentAsJson(response) shouldBe json
        }
      }
    }

    Seq(
      "GBVC0000401DS",
      "XIVC0000401DS"
    ) foreach { stampsReferenceNumber =>
      s"return 401 when request for stampsReferenceNumber: $stampsReferenceNumber " in {
        val requestBody = Json.obj(
          "vdsdetails" -> Json.obj(
            "vdsEmail"              -> "email@example.com",
            "stampsReferenceNumber" -> stampsReferenceNumber
          )
        )
        val request = FakeRequest(POST, "/etds/vaping/stamps/status").withBody(requestBody)
        val response = controller.checkApprovalStatus().apply(request)

        status(response) shouldBe 401
      }
    }

    Seq(
      "GBVC0000403DS",
      "XIVC0000403DS"
    ) foreach { stampsReferenceNumber =>
      s"return 403 when request for stampsReferenceNumber: $stampsReferenceNumber " in {
        val requestBody = Json.obj(
          "vdsdetails" -> Json.obj(
            "vdsEmail"              -> "email@example.com",
            "stampsReferenceNumber" -> stampsReferenceNumber
          )
        )
        val request = FakeRequest(POST, "/etds/vaping/stamps/status").withBody(requestBody)
        val response = controller.checkApprovalStatus().apply(request)

        status(response) shouldBe 403
      }
    }

    Seq(
      "GBVC0000503DS",
      "XIVC0000503DS"
    ) foreach { stampsReferenceNumber =>
      s"return 502 when request for stampsReferenceNumber: $stampsReferenceNumber " in {
        val requestBody = Json.obj(
          "vdsdetails" -> Json.obj(
            "vdsEmail"              -> "email@example.com",
            "stampsReferenceNumber" -> stampsReferenceNumber
          )
        )
        val request = FakeRequest(POST, "/etds/vaping/stamps/status").withBody(requestBody)
        val response = controller.checkApprovalStatus().apply(request)

        status(response) shouldBe 502
      }
    }
  }
}
