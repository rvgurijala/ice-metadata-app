package com.ice.metadata.controllers

import com.ice.metadata.persistence.entities.ArtistDTO
import com.ice.metadata.services.{ArtistOfTheDayService, ArtistServiceImpl}
import play.api.libs.json.Json
import play.api.mvc._

import java.util.UUID
import javax.inject._
import scala.concurrent.ExecutionContext

class ArtistController @Inject()(val controllerComponents: ControllerComponents, val artistService: ArtistServiceImpl, val artistOfTheDayService: ArtistOfTheDayService)(implicit val ec: ExecutionContext) extends BaseController {

  def getArtist(id: UUID) = Action {
    artistService
      .get(id)
      .fold(
        error => BadRequest(error.message),
        result => result.map(artist => Ok(Json.toJson(artist))).getOrElse(NoContent)
      )
  }

  def createArtist() = Action(parse.json[ArtistDTO]) { implicit request =>
    artistService
      .save(request.body)
      .fold(
        error => BadRequest(error.message),
        artist => Created(Json.toJson(artist))
      )
  }

  def updateArtist(id: UUID) = Action(parse.json[ArtistDTO]) { implicit request =>
    artistService
      .update(request.body.copy(id = Some(id)))
      .fold(
        error => BadRequest(error.message),
        _ => NoContent
      )
  }

  def getArtistTracks(artistId: UUID) = Action {
    artistService
      .getArtistTracks(artistId)
      .fold(
        error => BadRequest(error.message),
        result => Ok(Json.toJson(result))
      )
  }

  def getAllArtists() = Action {
    artistService
      .getAll()
      .fold(
        error => BadRequest(error.message),
        result => Ok(Json.toJson(result))
      )
  }

  def getArtistOfTheDay() = Action {
    artistOfTheDayService
      .getArtistOfTheDay()
      .fold(
        error => BadRequest(error.message),
        result => Ok(Json.toJson(result))
      )
  }
}
