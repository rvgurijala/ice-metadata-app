package com.ice.metadata.repositories

import com.ice.metadata.persistence.Persistence
import com.ice.metadata.persistence.entities.Album
import com.ice.metadata.repositories.errors.RepositoryError

import java.util.UUID
import javax.inject.Inject
import scala.collection.mutable.ListBuffer
import scala.util.Try

class AlbumRepository (val db: Persistence[Album, UUID, ListBuffer]) {

  def findById(id: UUID): Either[RepositoryError, Option[Album]] = handleIfErrors(db.find(id))

  def save(album: Album): Either[RepositoryError, Album] = handleIfErrors(db.insert(album))

  def update(album: Album): Either[RepositoryError, Unit] = handleIfErrors(db.update(album))

  def delete(id: UUID): Either[RepositoryError, Unit] = handleIfErrors(db.delete(id))

  def findAll(): Either[RepositoryError, List[Album]] = handleIfErrors(db.findAll())

  private def handleIfErrors[A](f: => A) =
    Try(f).fold(e => Left(RepositoryError(e.getMessage)), v => Right(v))
}
