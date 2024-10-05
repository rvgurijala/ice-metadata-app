package com.ice.metadata.persistence.entities

import ObjectType.ObjectType
import play.api.libs.json.{Format, Json}

import java.util.{Date, UUID}

case class Album(id: UUID, name: String, release_date: Date, uri: String, image: Image, artists: Set[UUID], objectType: ObjectType) {
  def toDTO() : AlbumDTO = AlbumDTO(Some(id), name, release_date, uri, image, artists.toArray, Some(objectType))
}

object Album {
  def fromDTO(dto: AlbumDTO): Album = Album(dto.id.getOrElse(UUID.randomUUID()), dto.name, dto.release_date, dto.uri, dto.image, dto.artists.toSet, dto.objectType.getOrElse(ObjectType.Album))
}

case class AlbumDTO(id: Option[UUID], name: String, release_date: Date, uri: String, image: Image, artists: Array[UUID], objectType: Option[ObjectType]) {
  def toAlbum() : Album = Album(id.getOrElse(UUID.randomUUID()), name, release_date, uri, image, artists.toSet, objectType.getOrElse(ObjectType.Album))
}

object AlbumDTO {
  implicit val formatAlbum: Format[AlbumDTO] = Json.format[AlbumDTO]
  def fromAlbum(album: Album): AlbumDTO = AlbumDTO(Some(album.id), album.name, album.release_date, album.uri, album.image, album.artists.toArray, Some(album.objectType))
}
