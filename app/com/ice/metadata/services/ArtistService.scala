package com.ice.metadata.services

import com.ice.metadata.persistence.entities.{ArtistDTO, TrackDTO}
import com.ice.metadata.repositories.{ArtistRepository, TrackRepository}
import com.ice.metadata.repositories.errors.RepositoryError
import com.ice.metadata.services.errors.ServiceError

import java.util.UUID
import javax.inject.Inject

trait ArtistService {
  def get(id: UUID): Either[ServiceError, Option[ArtistDTO]]

  def save(dto: ArtistDTO): Either[ServiceError, ArtistDTO]

  def update(dto: ArtistDTO): Either[ServiceError, Unit]

  def delete(dto: ArtistDTO): Either[ServiceError, Unit]

  def getAll(): Either[ServiceError, List[ArtistDTO]]

  def getArtistTracks(artistId: UUID): Either[ServiceError, List[TrackDTO]]
}

class ArtistServiceImpl @Inject()(repository: ArtistRepository, trackRepository: TrackRepository) extends ArtistService {

  override def get(id: UUID): Either[ServiceError, Option[ArtistDTO]] = repository
    .findById(id)
    .fold(
      error => Left(toServiceError(error)),
      artist => Right(artist.map(_.toDTO()))
    )

  override def save(dto: ArtistDTO): Either[ServiceError, ArtistDTO] = repository
    .save(dto.toArtist())
    .fold(error => Left(toServiceError(error)), album => Right(album.toDTO()))

  override def update(dto: ArtistDTO): Either[ServiceError, Unit] = repository
    .update(dto.toArtist())
    .fold(error => Left(toServiceError(error)), _ => Right(()))

  override def delete(dto: ArtistDTO): Either[ServiceError, Unit] = repository
    .delete(dto.id.getOrElse(UUID.randomUUID()))
    .fold(error => Left(toServiceError(error)), _ => Right(()))

  override def getAll(): Either[ServiceError, List[ArtistDTO]] = repository
    .findAll()
    .fold(
      error => Left(toServiceError(error)),
      artistList => Right(artistList.map(_.toDTO()))
    )

  override def getArtistTracks(artistId: UUID): Either[ServiceError, List[TrackDTO]] = trackRepository
    .findAllByArtistId(artistId)
    .fold(
      error => Left(toServiceError(error)),
      trackList => Right(trackList.map(_.toDTO()))
    )

  private def toServiceError(repositoryError: RepositoryError) =
    ServiceError(repositoryError.message)
}
