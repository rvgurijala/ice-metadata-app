package com.ice.metadata.services

import com.ice.metadata.persistence.entities.{Album, Artist, TrackDTO}
import com.ice.metadata.repositories.errors.RepositoryError
import com.ice.metadata.repositories.{AlbumRepository, ArtistRepository, TrackRepository}
import com.ice.metadata.services.errors.ServiceError

import java.util.UUID
import javax.inject.Inject

trait TrackService {
  def get(id: UUID): Either[ServiceError, Option[TrackDTO]]

  def save(dto: TrackDTO): Either[ServiceError, TrackDTO]

  def update(dto: TrackDTO): Either[ServiceError, Unit]

  def delete(dto: TrackDTO): Either[ServiceError, Unit]

  def getAll(): Either[ServiceError, List[TrackDTO]]
}

class TrackServiceImpl @Inject()(repository: TrackRepository, albumRepository: AlbumRepository, artistRepository: ArtistRepository) extends TrackService {

  override def get(id: UUID): Either[ServiceError, Option[TrackDTO]] = repository
    .findById(id)
    .fold(
      error => Left(toServiceError(error)),
      track => Right(track.map(_.toDTO()))
    )

  override def save(dto: TrackDTO): Either[ServiceError, TrackDTO] = {
    for {
      validated_album <- validateAlbumExists(dto.album)
      (lefts, rights) = dto.artists.map(validateArtistExists).partitionMap(identity)
      validated_artist <- lefts.headOption.toLeft(rights)

      savedTrack <- repository
        .save(dto.toTrack())
        .fold(error => Left(toServiceError(error)), album => Right(album.toDTO()))

    } yield savedTrack
  }

  private def toServiceError(repositoryError: RepositoryError) =
    ServiceError(repositoryError.message)

  override def update(dto: TrackDTO): Either[ServiceError, Unit] = {
    for {
      validated_album <- validateAlbumExists(dto.album)
      (lefts, rights) = dto.artists.map(validateArtistExists).partitionMap(identity)
      validated_artist <- lefts.headOption.toLeft(rights)

      updatedTrack <- repository
        .update(dto.toTrack())
        .fold(error => Left(toServiceError(error)), _ => Right(()))

    } yield updatedTrack
  }


  override def delete(dto: TrackDTO): Either[ServiceError, Unit] = repository
    .delete(dto.id.getOrElse(UUID.randomUUID()))
    .fold(error => Left(toServiceError(error)), _ => Right(()))

  override def getAll(): Either[ServiceError, List[TrackDTO]] = repository
    .findAll()
    .fold(
      error => Left(toServiceError(error)),
      trackList => Right(trackList.map(_.toDTO()))
    )

  private def validateArtistExists(artistId: UUID): Either[ServiceError, Artist] = artistRepository.findById(artistId) match {
    case Right(Some(artist)) => Right(artist)
    case Right(None) => Left(toServiceError(RepositoryError(s"There is no artist with id ${artistId}")))
    case Left(e) => Left(toServiceError(e))
  }

  private def validateAlbumExists(albumId: UUID): Either[ServiceError, Album] = albumRepository.findById(albumId) match {
    case Right(Some(album)) => Right(album)
    case Right(None) => Left(toServiceError(RepositoryError(s"There is no album with id ${albumId}")))
    case Left(e) => Left(toServiceError(e))
  }
}
