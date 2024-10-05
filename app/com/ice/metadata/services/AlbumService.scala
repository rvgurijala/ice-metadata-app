package com.ice.metadata.services

import com.ice.metadata.persistence.entities.{Album, AlbumDTO, Artist}
import com.ice.metadata.repositories.{AlbumRepository, ArtistRepository}
import com.ice.metadata.repositories.errors.RepositoryError
import com.ice.metadata.services.errors.ServiceError

import java.util.UUID
import javax.inject.Inject

trait AlbumService {
  def get(id: UUID): Either[ServiceError, Option[AlbumDTO]]

  def save(dto: AlbumDTO): Either[ServiceError, AlbumDTO]

  def update(dto: AlbumDTO): Either[ServiceError, Unit]

  def delete(dto: AlbumDTO): Either[ServiceError, Unit]

  def getAll(): Either[ServiceError, List[AlbumDTO]]
}

class AlbumServiceImpl @Inject()(repository: AlbumRepository, artistRepository: ArtistRepository) extends AlbumService {
  override def get(id: UUID): Either[ServiceError, Option[AlbumDTO]] = repository
    .findById(id)
    .fold(
      error => Left(toServiceError(error)),
      album => Right(album.map(_.toDTO()))
    )

  override def save(dto: AlbumDTO): Either[ServiceError, AlbumDTO] = {
   val (lefts, rights) = dto.artists.map(validateArtistExists).partitionMap(identity)
    for {
      validated_artist <- lefts.headOption.toLeft(rights)
      savedAlbum <- repository
        .save(dto.toAlbum())
        .fold(error => Left(toServiceError(error)), album => Right(album.toDTO()))
    } yield savedAlbum
  }

  override def update(dto: AlbumDTO): Either[ServiceError, Unit] = {
    val (lefts, rights) = dto.artists.map(validateArtistExists).partitionMap(identity)
    for {
      validated_artist <- lefts.headOption.toLeft(rights)
      updatedAlbum <- repository
        .update(dto.toAlbum())
        .fold(error => Left(toServiceError(error)), _ => Right(()))
    } yield updatedAlbum
  }

  override def delete(dto: AlbumDTO): Either[ServiceError, Unit] = repository
    .delete(dto.id.getOrElse(UUID.randomUUID()))
    .fold(error => Left(toServiceError(error)), _ => Right(()))

  override def getAll(): Either[ServiceError, List[AlbumDTO]] = repository
    .findAll()
    .fold(
      error => Left(toServiceError(error)),
      albumList => Right(albumList.map(_.toDTO()))
    )

  private def toServiceError(repositoryError: RepositoryError) =
    ServiceError(repositoryError.message)

  private def validateArtistExists(artistId: UUID): Either[ServiceError, Artist] = artistRepository.findById(artistId) match {
    case Right(Some(artist)) => Right(artist)
    case Right(None) => Left(toServiceError(RepositoryError(s"There is no artist with id ${artistId}")))
    case Left(e) => Left(toServiceError(e))
  }
}
