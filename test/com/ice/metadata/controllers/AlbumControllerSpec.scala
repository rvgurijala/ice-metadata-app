package com.ice.metadata.controllers

import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.libs.json.Json
import play.api.mvc.{AnyContentAsJson, Headers}
import play.api.test.Helpers._
import play.api.test._

class AlbumControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {

  val fakeArtist = """{
                     |    "name": "hello",
                     |    "release_date": "2003-05-01",
                     |    "artists": [
                     |        "bd1bb407-9d44-4f02-8c28-cf0a6b6c0f5f"
                     |    ],
                     |    "image": {
                     |        "url": "/heloo",
                     |        "height": 300,
                     |        "width": 300
                     |    },
                     |    "uri": "/uri"
                     |}""".stripMargin

  "AlbumController" should {

    "get all Albums" in {
      val request = FakeRequest(GET, "/api/v1/albums")
      val res = route(app, request).get

      status(res) mustBe OK
    }

    "get Album by id" in {
      val request = FakeRequest(GET, "/api/v1/albums/ef0f0818-cb2d-4c87-926e-a57ef302c023")
      val res = route(app, request).get

      status(res) mustBe NO_CONTENT
    }

    "update Album by id but not found" in {
      val headers = Headers(("Content-Type", "application/json"))
      print(AnyContentAsJson(Json.parse(fakeArtist)))
      val request = FakeRequest(PUT, "/api/v1/albums/ef0f0818-cb2d-4c87-926e-a57ef302c023").withBody(Json.parse(fakeArtist))
      val res = route(app, request).get

      status(res) mustBe BAD_REQUEST
    }

    "create Album" in {
      val headers = Headers(("Content-Type", "application/json"))
      val request = FakeRequest(POST, "/api/v1/albums").withBody(Json.parse(fakeArtist))
      val res = route(app, request).get

      status(res) mustBe BAD_REQUEST
    }
  }
}
