package com.ice.metadata.controllers

import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.libs.json.Json
import play.api.mvc.{AnyContentAsJson, Headers}
import play.api.test.Helpers._
import play.api.test._

class TrackControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {

  val fakeArtist = """{
                     |    "title": "hello123",
                     |    "album": "cf343381-58c3-4619-a543-b09f300c1ddf",
                     |    "artists": [
                     |        "bd1bb407-9d44-4f02-8c28-cf0a6b6c0f5f"
                     |    ],
                     |    "isPlayable": true,
                     |    "previewUrl": "/hi",
                     |    "uri": "/uri",
                     |    "genres": [
                     |        "jazz"
                     |    ],
                     |    "lengthInSeconds": 200
                     |}""".stripMargin

  "TrackController" should {

    "get all tracks" in {
      val request = FakeRequest(GET, "/api/v1/tracks")
      val res = route(app, request).get

      status(res) mustBe OK
    }

    "get tracks by id" in {
      val request = FakeRequest(GET, "/api/v1/tracks/ef0f0818-cb2d-4c87-926e-a57ef302c023")
      val res = route(app, request).get

      status(res) mustBe NO_CONTENT
    }

    "update tracks by id but not found with bad request" in {
      print(AnyContentAsJson(Json.parse(fakeArtist)))
      val request = FakeRequest(PUT, "/api/v1/tracks/ef0f0818-cb2d-4c87-926e-a57ef302c023").withBody(Json.parse(fakeArtist))
      val res = route(app, request).get

      status(res) mustBe BAD_REQUEST
    }

    "create tracks with bad request" in {
      val request = FakeRequest(POST, "/api/v1/tracks").withBody(Json.parse(fakeArtist))
      val res = route(app, request).get

      status(res) mustBe BAD_REQUEST
    }
  }
}
