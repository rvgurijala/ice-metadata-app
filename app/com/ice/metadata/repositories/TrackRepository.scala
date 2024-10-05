package com.ice.metadata.repositories

import com.ice.metadata.persistence.{Persistence, TrackPersistence}
import com.ice.metadata.persistence.entities.Track
import com.ice.metadata.repositories.errors.RepositoryError

import java.util.UUID
import javax.inject.Inject
import scala.collection.mutable.ListBuffer
import scala.util.Try

class TrackRepository (val db: TrackPersistence[Track, UUID, ListBuffer]) {

  def findById(id: UUID): Either[RepositoryError, Option[Track]] = handleIfErrors(db.find(id))

  def save(track: Track): Either[RepositoryError, Track] = handleIfErrors(db.insert(track))

  def update(track: Track): Either[RepositoryError, Unit] = handleIfErrors(db.update(track))

  def delete(id: UUID): Either[RepositoryError, Unit] = handleIfErrors(db.delete(id))

  def findAll(): Either[RepositoryError, List[Track]] = handleIfErrors(db.findAll())

  def findAllByArtistId(artistId: UUID): Either[RepositoryError, List[Track]] = handleIfErrors(db.findAllByArtistId(artistId))

  private def handleIfErrors[A](f: => A) =
    Try(f).fold(e => Left(RepositoryError(e.getMessage)), v => Right(v))
}