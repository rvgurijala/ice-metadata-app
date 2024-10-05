package com.ice.metadata.controllers

import com.ice.metadata.persistence.entities.AlbumDTO
import com.ice.metadata.services.AlbumServiceImpl
import play.api.libs.json.Json
import play.api.mvc._

import java.util.UUID
import javax.inject._
import scala.concurrent.ExecutionContext

@Singleton
class AlbumController @Inject()(val controllerComponents: ControllerComponents, val albumService: AlbumServiceImpl)(implicit val ec: ExecutionContext) extends BaseController {

  def getAlbum(id: UUID) = Action {
    albumService
      .get(id)
      .fold(
        error => BadRequest(error.message),
        result => result.map(track => Ok(Json.toJson(track))).getOrElse(NoContent)
      )
  }

  def createAlbum() = Action(parse.json[AlbumDTO]) { implicit request =>
    albumService
      .save(request.body)
      .fold(
        error => BadRequest(error.message),
        track => Created(Json.toJson(track))
      )
  }

  def updateAlbum(id: UUID) = Action(parse.json[AlbumDTO]) { implicit request =>
    albumService
      .update(request.body.copy(id = Some(id)))
      .fold(
        error => BadRequest(error.message),
        _ => NoContent
      )
  }

  def getAllAlbums() = Action {
    albumService
      .getAll()
      .fold(
        error => BadRequest(error.message),
        result => Ok(Json.toJson(result))
      )
  }
}
