package com.ice.metadata.persistence.entities

import ObjectType.ObjectType
import play.api.libs.json.{Format, Json}

import java.util.UUID

case class Track(id: UUID, title: String, description: String, album: UUID, artists: Set[UUID], isPlayable: Boolean, objectType: ObjectType, previewUrl: String, uri: String, genres: Set[String], lengthInSeconds: Int) {
  def toDTO(): TrackDTO = TrackDTO(Some(id), title, Some(description), album, artists.toArray, isPlayable, Some(objectType), previewUrl, uri, genres.toArray, lengthInSeconds)
}

object Track {
  def fromDTO(dto: TrackDTO) : Track = Track(dto.id.getOrElse(UUID.randomUUID()), dto.title, dto.description.getOrElse(""), dto.album, dto.artists.toSet, dto.isPlayable, dto.objectType.getOrElse(ObjectType.Track), dto.previewUrl, dto.uri, dto.genres.toSet, dto.lengthInSeconds)
}

case class TrackDTO(id: Option[UUID], title: String, description: Option[String], album: UUID, artists: Array[UUID], isPlayable: Boolean, objectType: Option[ObjectType], previewUrl: String, uri: String, genres: Array[String], lengthInSeconds: Int) {
  def toTrack(): Track = Track(id.getOrElse(UUID.randomUUID()), title, description.getOrElse(""), album, artists.toSet, isPlayable, objectType.getOrElse(ObjectType.Track), previewUrl, uri, genres.toSet, lengthInSeconds)
}

object TrackDTO {
  implicit val formatTrack: Format[TrackDTO] = Json.format[TrackDTO]
  def fromTrack(track: Track) : TrackDTO = TrackDTO(Some(track.id), track.title, Some(track.description), track.album, track.artists.toArray, track.isPlayable, Some(track.objectType), track.previewUrl, track.uri, track.genres.toArray, track.lengthInSeconds)
}
