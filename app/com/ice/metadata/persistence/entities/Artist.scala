package com.ice.metadata.persistence.entities

import ObjectType.ObjectType
import play.api.libs.json.{Format, Json, Writes}

import java.util.UUID

case class Artist(id: UUID, name: String, images: Set[Image], objectType: ObjectType, aliases: Set[String]) {
  def toDTO(): ArtistDTO = ArtistDTO(Some(id), name, images.toArray, Some(objectType), Some(aliases.toArray))
}

object Artist {
  def fromDTO(dto: ArtistDTO) : Artist = Artist(dto.id.getOrElse(UUID.randomUUID()), dto.name, dto.images.toSet, dto.objectType.getOrElse(ObjectType.Artist), dto.aliases.getOrElse(Array.empty).toSet)
}

case class ArtistDTO(id: Option[UUID], name: String, images: Array[Image], objectType: Option[ObjectType], aliases: Option[Array[String]]) {
  def toArtist(): Artist = Artist(id.getOrElse(UUID.randomUUID()), name, images.toSet, objectType.getOrElse(ObjectType.Artist), aliases.getOrElse(Array.empty).toSet)
}

object ArtistDTO {
  implicit val formatArtist: Format[ArtistDTO] = Json.format[ArtistDTO]
  def fromArtist(artist: Artist) : ArtistDTO = ArtistDTO(Some(artist.id), artist.name, artist.images.toArray, Some(artist.objectType), Some(artist.aliases.toArray))
}
