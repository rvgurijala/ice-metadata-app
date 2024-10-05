package com.ice.metadata.controllers

import com.ice.metadata.persistence.entities.{ArtistDTO, Image}
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.libs.json.{JsValue, Json}
import play.api.test.Helpers.status
import play.api.test.Injecting
import play.api.mvc.{AnyContentAsJson, Headers}
import play.api.test.Helpers._
import play.api.test._

class ArtistControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {

  val fakeArtist = """{
                     |    "name": "hello123",
                     |    "images": [
                     |        {
                     |            "url": "/heloo",
                     |            "height": 300,
                     |            "width": 300
                     |        }
                     |    ]
                     |}""".stripMargin

  "ArtistController" should {

    "get all atrists" in {
      val request = FakeRequest(GET, "/api/v1/artists")
      val res = route(app, request).get

      status(res) mustBe OK
    }

    "get atrist by id" in {
      val request = FakeRequest(GET, "/api/v1/artists/ef0f0818-cb2d-4c87-926e-a57ef302c023")
      val res = route(app, request).get

      status(res) mustBe NO_CONTENT
    }

    "update atrist by id but not found" in {
      val headers = Headers(("Content-Type", "application/json"))
      print(AnyContentAsJson(Json.parse(fakeArtist)))
      val request = FakeRequest(PUT, "/api/v1/artists/ef0f0818-cb2d-4c87-926e-a57ef302c023").withBody(Json.parse(fakeArtist))
      val res = route(app, request).get

      status(res) mustBe BAD_REQUEST
    }

    "create atrist" in {
      val headers = Headers(("Content-Type", "application/json"))
      val request = FakeRequest(POST, "/api/v1/artists").withBody(Json.parse(fakeArtist))
      val res = route(app, request).get

      status(res) mustBe CREATED
    }

    "get artist-of-the-day" in {
      val request = FakeRequest(GET, "/api/v1/artists/artist-of-the-day")
      val res = route(app, request).get

      status(res) mustBe OK
    }

    "get artists tracks" in {
      val request = FakeRequest(GET, "/api/v1/artists/ef0f0818-cb2d-4c87-926e-a57ef302c023/tracks")
      val res = route(app, request).get

      status(res) mustBe OK
    }
  }
}
