package com.ice.metadata.controllers

import com.ice.metadata.persistence.entities.TrackDTO
import com.ice.metadata.services.{TrackService, TrackServiceImpl}
import play.api.mvc._

import java.util.UUID
import javax.inject._
import scala.concurrent.ExecutionContext
import play.api.libs.json.Json

@Singleton
class TrackController @Inject()(val controllerComponents: ControllerComponents, val trackService: TrackServiceImpl)(implicit val ec: ExecutionContext) extends BaseController {

  def getTrack(id: UUID) = Action {
    trackService
      .get(id)
      .fold(
        error => BadRequest(error.message),
        result => result.map(track => Ok(Json.toJson(track))).getOrElse(NoContent)
      )
  }

  def createTrack() = Action(parse.json[TrackDTO]) { implicit request =>
    trackService
      .save(request.body)
      .fold(
        error => BadRequest(error.message),
        track => Created(Json.toJson(track))
      )
  }

  def updateTrack(id: UUID) = Action(parse.json[TrackDTO]) { implicit request =>
    trackService
      .update(request.body.copy(id = Some(id)))
      .fold(
        error => BadRequest(error.message),
        _ => NoContent
      )
  }

  def getAllTracks() = Action {
    trackService
      .getAll()
      .fold(
        error => BadRequest(error.message),
        result => Ok(Json.toJson(result))
      )
  }
}
